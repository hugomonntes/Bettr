package com.example.bettr;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.bettr.Fragments.FeedFragment;
import com.example.bettr.Fragments.HabitsFragment;
import com.example.bettr.Fragments.SocialFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Feed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new FeedFragment();
            } else if (itemId == R.id.nav_habits) {
                selectedFragment = new HabitsFragment();
            } else if (itemId == R.id.nav_social) {
                selectedFragment = new SocialFragment();
            } else if (itemId == R.id.nav_camera) {
                selectedFragment = new FeedFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new SocialFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FeedFragment())
                    .commit();
        }
    }
}
