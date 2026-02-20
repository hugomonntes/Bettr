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

        // Obtener userId de la sesión
        myUserId = session.getUserId();

        // Primero cargar datos desde la sesión local (más rápido)
        loadUserDataFromSession();

        layoutFollowers.setOnClickListener(v -> {
            loadFragment(FollowersFragment.newInstance(myUserId, "followers"));
        });

        layoutFollowing.setOnClickListener(v -> {
            loadFragment(FollowersFragment.newInstance(myUserId, "following"));
        });

        btnLogout.setOnClickListener(v -> logout());

        if (myUserId != -1) {
            // Cargar estadísticas desde la API
            loadStatsFromAPI();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar datos al volver al fragment
        loadUserDataFromSession();
        if (myUserId != -1) {
            loadStatsFromAPI();
        }
    }

    /**
     * Cargar datos del usuario desde UserSession (datos guardados localmente)
     */
    private void loadUserDataFromSession() {
        String name = session.getUserName();
        String username = session.getUsername();
        String email = session.getUserEmail();
        String bio = session.getUserBio();
        String avatar = session.getUserAvatar();

        // Mostrar nombre
        if (name != null && !name.isEmpty()) {
            tvName.setText(name);
            tvAvatarLetter.setText(name.substring(0, 1).toUpperCase());
        } else {
            tvName.setText("Usuario");
            tvAvatarLetter.setText("U");
        }

        // Mostrar username
        if (username != null && !username.isEmpty()) {
            tvUsername.setText("@" + username);
        } else {
            tvUsername.setText("@usuario");
        }

        // Mostrar email
        if (email != null && !email.isEmpty()) {
            tvEmail.setText(email);
            tvEmail.setVisibility(View.VISIBLE);
        } else {
            tvEmail.setVisibility(View.GONE);
        }

        // Mostrar bio
        if (bio != null && !bio.isEmpty()) {
            tvBio.setText(bio);
            tvBio.setVisibility(View.VISIBLE);
        } else {
            tvBio.setVisibility(View.GONE);
        }

        // Cargar avatar desde Base64
        if (avatar != null && !avatar.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedBitmap != null) {
                    ivAvatar.setImageBitmap(decodedBitmap);
                    tvAvatarLetter.setVisibility(View.GONE);
                } else {
                    tvAvatarLetter.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                tvAvatarLetter.setVisibility(View.VISIBLE);
            }
        } else {
            tvAvatarLetter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Cargar estadísticas desde la API (followers, following, habits)
     */
    private void loadStatsFromAPI() {
        showLoading();

        apiGets.getUserStats(myUserId, (followers, following, habits) -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    updateStatsUI(followers, following, habits);
                    hideLoading();
                });
            }
        });
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
