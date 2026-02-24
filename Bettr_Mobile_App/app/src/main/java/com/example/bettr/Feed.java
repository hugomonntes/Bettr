package com.example.appmovil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView ivNavAvatar;
    private TextView tvNavAvatarLetter;
    private FrameLayout loadingOverlay;
    private int colorActive = Color.parseColor("#FACC15");
    private int colorInactive = Color.parseColor("#9CA3AF");
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);

        session = new UserSession(this);
        loadingOverlay = findViewById(R.id.loading_overlay);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initNav();
        loadUserAvatarInNav();
        
        if (savedInstanceState == null) {
            navigateToHome();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar avatar cuando vuelve a la actividad
        loadUserAvatarInNav();
    }

    /**
     * Cargar el avatar del usuario en la barra de navegación
     */
    private void loadUserAvatarInNav() {
        ivNavAvatar = findViewById(R.id.iv_nav_avatar);
        tvNavAvatarLetter = findViewById(R.id.tv_nav_avatar_letter);
        
        if (ivNavAvatar == null || tvNavAvatarLetter == null) {
            return;
        }

        String name = session.getUserName();
        String avatar = session.getUserAvatar();

        // Mostrar la primera letra del nombre
        if (name != null && !name.isEmpty()) {
            tvNavAvatarLetter.setText(name.substring(0, 1).toUpperCase());
        } else {
            tvNavAvatarLetter.setText("U");
        }

        // Cargar avatar desde Base64
        if (avatar != null && !avatar.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedBitmap != null) {
                    ivNavAvatar.setImageBitmap(decodedBitmap);
                    ivNavAvatar.setVisibility(View.VISIBLE);
                    tvNavAvatarLetter.setVisibility(View.GONE);
                } else {
                    tvNavAvatarLetter.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                tvNavAvatarLetter.setVisibility(View.VISIBLE);
            }
        } else {
            tvNavAvatarLetter.setVisibility(View.VISIBLE);
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
        // ivProfile ya no se usa, ahora usamos ivNavAvatar

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
        
        // El avatar del perfil siempre se muestra en color activo cuando se selecciona
        if (ivNavAvatar != null) {
            if (selectedId == R.id.btn_nav_profile) {
                ivNavAvatar.setColorFilter(colorActive);
            } else {
                ivNavAvatar.setColorFilter(colorInactive);
            }
        }
        
        // Actualizar el color de la letra del avatar
        if (tvNavAvatarLetter != null) {
            if (selectedId == R.id.btn_nav_profile) {
                tvNavAvatarLetter.setTextColor(colorActive);
            } else {
                tvNavAvatarLetter.setTextColor(colorInactive);
            }
        }

        if (selectedId == R.id.btn_nav_home) {
            ivHome.setColorFilter(colorActive);
        } else if (selectedId == R.id.btn_nav_habits) {
            ivHabits.setColorFilter(colorActive);
        } else if (selectedId == R.id.btn_nav_camera) {
            ivCamera.setColorFilter(colorActive);
        } else if (selectedId == R.id.btn_nav_social) {
            ivSocial.setColorFilter(colorActive);
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

