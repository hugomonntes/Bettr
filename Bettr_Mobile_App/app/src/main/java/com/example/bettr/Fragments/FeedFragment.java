package com.example.bettr.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private RecyclerView rvFeed;
    private AdapterFeed adapter;
    private final ArrayList<Habit> listaHabitos = new ArrayList<>();
    private Api_Gets apiGets;
    private Api_Inserts apiInserts;
    private int myUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        apiGets = new Api_Gets();
        apiInserts = new Api_Inserts();
        rvFeed = view.findViewById(R.id.rvFeed);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefs = requireActivity().getSharedPreferences("BettrPrefs", Context.MODE_PRIVATE);
        myUserId = prefs.getInt("userId", -1);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }

    private void loadPosts() {
        if (myUserId == -1) {
            setupDummyData();
            return;
        }

        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).showLoading();
        }

        apiGets.getSocialFeed(myUserId, habits -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (getActivity() instanceof Feed) {
                        ((Feed) getActivity()).hideLoading();
                    }
                    if (habits != null && !habits.isEmpty()) {
                        listaHabitos.clear();
                        listaHabitos.addAll(habits);
                        adapter = new AdapterFeed(listaHabitos, myUserId, apiInserts, () -> loadPosts());
                        rvFeed.setAdapter(adapter);
                        
                        // Check like status for each habit
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
                        setupDummyData();
                    }
                });
            }
        });
    }

    private void setupDummyData() {
    }
}
