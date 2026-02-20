package com.example.bettr.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Adapters.AdapterFeed;
import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.ApiRest.Api_Inserts;
import com.example.bettr.Feed;
import com.example.bettr.Publicaciones.Habit;
import com.example.bettr.R;
import com.example.bettr.UserSession;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private RecyclerView rvFeed;
    private AdapterFeed adapter;
    private final ArrayList<Habit> listaHabitos = new ArrayList<>();
    private Api_Gets apiGets;
    private Api_Inserts apiInserts;
    private int myUserId;
    private View emptyState;
    private ShapeableImageView ivProfileTop;
    private UserSession userSession;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        apiGets = new Api_Gets();
        apiInserts = new Api_Inserts();
        rvFeed = view.findViewById(R.id.rvFeed);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyState = view.findViewById(R.id.emptyState);
        
        ivProfileTop = view.findViewById(R.id.ivProfileTop);
        userSession = new UserSession(requireContext());

        SharedPreferences prefs = requireActivity().getSharedPreferences("BettrPrefs", Context.MODE_PRIVATE);
        myUserId = prefs.getInt("userId", -1);
        
        loadUserAvatar();

        return view;
    }
    
    private void loadUserAvatar() {
        if (myUserId == -1) return;
        
        apiGets.getUserById(myUserId, user -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (user != null) {
                        String avatarBase64 = user.getAvatarUrl();
                        if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                            Bitmap avatar = decodeBase64(avatarBase64);
                            if (avatar != null) {
                                ivProfileTop.setImageBitmap(avatar);
                            } else {
                                Bitmap initialAvatar = generateInitialAvatar(user.getUsername());
                                ivProfileTop.setImageBitmap(initialAvatar);
                            }
                        } else {
                            Bitmap initialAvatar = generateInitialAvatar(user.getUsername());
                            ivProfileTop.setImageBitmap(initialAvatar);
                        }
                    } else {
                        ivProfileTop.setImageResource(R.drawable.logobettr);
                    }
                });
            }
        });
    }
    
    private Bitmap generateInitialAvatar(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        
        String initial = username.substring(0, 1).toUpperCase();
        
        int size = 200;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        
        android.graphics.Paint bgPaint = new android.graphics.Paint();
        bgPaint.setColor(android.graphics.Color.parseColor("#FACC15")); // Yellow color
        bgPaint.setStyle(android.graphics.Paint.Style.FILL);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, bgPaint);
        
        android.graphics.Paint textPaint = new android.graphics.Paint();
        textPaint.setColor(android.graphics.Color.WHITE);
        textPaint.setTextSize(100);
        textPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        
        canvas.drawText(initial, size / 2f, size / 2f + 35, textPaint);
        
        return bitmap;
    }
    
    private Bitmap decodeBase64(String input) {
        try {
            byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }

    private void loadPosts() {
        if (myUserId == -1) {
            showEmptyState();
            return;
        }

        showLoading();

        apiGets.getSocialFeed(myUserId, habits -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    hideLoading();
                    if (habits != null && !habits.isEmpty()) {
                        listaHabitos.clear();
                        listaHabitos.addAll(habits);
                        adapter = new AdapterFeed(listaHabitos, myUserId, apiInserts, () -> loadPosts());
                        rvFeed.setAdapter(adapter);
                        hideEmptyState();
                        
                        for (Habit habit : listaHabitos) {
                            final int habitPosition = listaHabitos.indexOf(habit);
                            apiGets.checkIfLiked(habit.getId(), myUserId, liked -> {
                                if (isAdded() && getActivity() != null) {
                                    getActivity().runOnUiThread(() -> {
                                        habit.setLiked(liked);
                                        if (adapter != null && habitPosition >= 0) {
                                            adapter.notifyItemChanged(habitPosition);
                                        }
                                    });
                                }
                            });
                        }
                    } else {
                        showEmptyState();
                    }
                });
            }
        });
    }

    private void showLoading() {
        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).showLoading();
        }
    }

    private void hideLoading() {
        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).hideLoading();
        }
    }

    private void showEmptyState() {
        if (rvFeed != null) rvFeed.setVisibility(View.GONE);
        if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState() {
        if (rvFeed != null) rvFeed.setVisibility(View.VISIBLE);
        if (emptyState != null) emptyState.setVisibility(View.GONE);
    }
}
