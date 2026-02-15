package com.example.bettr;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.bettr.Fragments.CameraFragment;
import com.example.bettr.Fragments.FeedFragment;
import com.example.bettr.Fragments.HabitsFragment;
import com.example.bettr.Fragments.ProfileFragment;
import com.example.bettr.Fragments.SocialFragment;

public class Feed extends AppCompatActivity {

    private FrameLayout btnHome, btnHabits, btnCamera, btnSocial, btnProfile;
    private ImageView ivHome, ivHabits, ivCamera, ivSocial, ivProfile;
    private FrameLayout loadingOverlay;
    private int colorActive = Color.parseColor("#FACC15");
    private int colorInactive = Color.parseColor("#9CA3AF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);

        loadingOverlay = findViewById(R.id.loading_overlay);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initNav();
        
        if (savedInstanceState == null) {
            navigateToHome();
        }
    }

    public void showLoading() {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading() {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(View.GONE);
        }
    }

    public void navigateToHome() {
        updateNavUI(R.id.btn_nav_home);
        loadFragment(new FeedFragment());
    }

    private void initNav() {
        btnHome = findViewById(R.id.btn_nav_home);
        btnHabits = findViewById(R.id.btn_nav_habits);
        btnCamera = findViewById(R.id.btn_nav_camera);
        btnSocial = findViewById(R.id.btn_nav_social);
        btnProfile = findViewById(R.id.btn_nav_profile);

        ivHome = findViewById(R.id.iv_home_icon);
        ivHabits = findViewById(R.id.iv_habits_icon);
        ivCamera = findViewById(R.id.iv_camera_icon);
        ivSocial = findViewById(R.id.iv_social_icon);
        ivProfile = findViewById(R.id.iv_profile_icon);

        btnHome.setOnClickListener(v -> { updateNavUI(R.id.btn_nav_home); loadFragment(new FeedFragment()); });
        btnHabits.setOnClickListener(v -> { updateNavUI(R.id.btn_nav_habits); loadFragment(new HabitsFragment()); });
        btnCamera.setOnClickListener(v -> { updateNavUI(R.id.btn_nav_camera); loadFragment(new CameraFragment()); });
        btnSocial.setOnClickListener(v -> { updateNavUI(R.id.btn_nav_social); loadFragment(new SocialFragment()); });
        btnProfile.setOnClickListener(v -> { updateNavUI(R.id.btn_nav_profile); loadFragment(new ProfileFragment()); });
    }

    private void updateNavUI(int selectedId) {
        ivHome.setColorFilter(colorInactive);
        ivHabits.setColorFilter(colorInactive);
        ivCamera.setColorFilter(colorInactive);
        ivSocial.setColorFilter(colorInactive);
        ivProfile.setColorFilter(colorInactive);

        if (selectedId == R.id.btn_nav_home) {
            ivHome.setColorFilter(colorActive);
        } else if (selectedId == R.id.btn_nav_habits) {
            ivHabits.setColorFilter(colorActive);
        } else if (selectedId == R.id.btn_nav_camera) {
            ivCamera.setColorFilter(colorActive);
        } else if (selectedId == R.id.btn_nav_social) {
            ivSocial.setColorFilter(colorActive);
        } else if (selectedId == R.id.btn_nav_profile) {
            ivProfile.setColorFilter(colorActive);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
