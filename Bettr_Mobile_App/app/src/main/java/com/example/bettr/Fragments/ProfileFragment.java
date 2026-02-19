package com.example.bettr.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.Dao.User;
import com.example.bettr.Feed;
import com.example.bettr.Intro;
import com.example.bettr.R;
import com.example.bettr.UserSession;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvUsername, tvEmail, tvBio, tvFollowers, tvFollowing, tvHabitsCount, tvAvatarLetter;
    private ImageView ivAvatar;
    private View layoutFollowers, layoutFollowing;
    private FrameLayout loadingOverlay;
    private MaterialButton btnLogout;
    private Api_Gets apiGets;
    private UserSession session;
    private int myUserId;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        apiGets = new Api_Gets();
        session = new UserSession(requireContext());
        
        tvName = view.findViewById(R.id.tvName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvBio = view.findViewById(R.id.tvBio);
        tvFollowers = view.findViewById(R.id.tvFollowers);
        tvFollowing = view.findViewById(R.id.tvFollowing);
        tvHabitsCount = view.findViewById(R.id.tvHabitsCount);
        tvAvatarLetter = view.findViewById(R.id.tvAvatarLetter);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        layoutFollowers = view.findViewById(R.id.layoutFollowers);
        layoutFollowing = view.findViewById(R.id.layoutFollowing);
        loadingOverlay = view.findViewById(R.id.loading_overlay);
        btnLogout = view.findViewById(R.id.btnLogout);

        SharedPreferences prefs = requireActivity().getSharedPreferences("BettrPrefs", Context.MODE_PRIVATE);
        myUserId = prefs.getInt("userId", -1);

        layoutFollowers.setOnClickListener(v -> {
            loadFragment(FollowersFragment.newInstance(myUserId, "followers"));
        });

        layoutFollowing.setOnClickListener(v -> {
            loadFragment(FollowersFragment.newInstance(myUserId, "following"));
        });

        btnLogout.setOnClickListener(v -> logout());

        if (myUserId != -1) {
            loadProfileData();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myUserId != -1) {
            loadProfileData();
        }
    }

    private void loadProfileData() {
        showLoading();

        apiGets.getUserById(myUserId, user -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (user != null) {
                        currentUser = user;
                        updateUserUI(user);
                        
                        session.setUserName(user.getName() != null ? user.getName() : "");
                        session.setUserEmail(user.getEmail() != null ? user.getEmail() : "");
                        session.setUserAvatar(user.getAvatarUrl() != null ? user.getAvatarUrl() : "");
                        session.setUserBio(user.getDescription() != null ? user.getDescription() : "");
                    }
                });
            }
        });

        apiGets.getUserStats(myUserId, (followers, following, habits) -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    updateStatsUI(followers, following, habits);
                    hideLoading();
                });
            }
        });
    }

    private void updateUserUI(User user) {
        if (user.getName() != null && !user.getName().isEmpty()) {
            tvName.setText(user.getName());
            tvAvatarLetter.setText(user.getName().substring(0, 1).toUpperCase());
        } else {
            tvName.setText("Usuario");
            tvAvatarLetter.setText("U");
        }

        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            tvUsername.setText("@" + user.getUsername());
        } else {
            tvUsername.setText("@usuario");
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            tvEmail.setText(user.getEmail());
            tvEmail.setVisibility(View.VISIBLE);
        } else {
            tvEmail.setVisibility(View.GONE);
        }

        if (user.getDescription() != null && !user.getDescription().isEmpty()) {
            tvBio.setText(user.getDescription());
            tvBio.setVisibility(View.VISIBLE);
        } else {
            tvBio.setVisibility(View.GONE);
        }

        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(user.getAvatarUrl(), Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivAvatar.setImageBitmap(decodedBitmap);
                tvAvatarLetter.setVisibility(View.GONE);
            } catch (Exception e) {
                tvAvatarLetter.setVisibility(View.VISIBLE);
            }
        } else {
            tvAvatarLetter.setVisibility(View.VISIBLE);
        }
    }

    private void updateStatsUI(int followers, int following, int habits) {
        tvFollowers.setText(String.valueOf(followers));
        tvFollowing.setText(String.valueOf(following));
        tvHabitsCount.setText(String.valueOf(habits));
    }

    private void showLoading() {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading() {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(View.GONE);
        }
    }

    private void logout() {
        if (session != null) {
            session.logout();
        }

        Intent intent = new Intent(getActivity(), Intro.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).loadFragment(fragment);
        }
    }
}
