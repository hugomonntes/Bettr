package com.example.bettr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bettr.ApiRest.Api_Inserts;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompleteProfile extends AppCompatActivity {
    private TextInputEditText etBio;
    private ImageView ivAvatar;
    private Bitmap avatarBitmap;
    private Api_Inserts apiInserts;
    private FrameLayout loadingOverlay;

    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    try {
                        avatarBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        ivAvatar.setImageBitmap(avatarBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_complete_profile);

        apiInserts = new Api_Inserts();
        etBio = findViewById(R.id.etBio);
        ivAvatar = findViewById(R.id.ivAvatar);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        Button btnFinish = findViewById(R.id.btnFinishProfile);
        loadingOverlay = findViewById(R.id.loading_overlay); // Asegúrate de tener este ID en el XML

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (btnSelectImage != null) {
            btnSelectImage.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        }

        if (btnFinish != null) {
            btnFinish.setOnClickListener(v -> finishProfile());
        }
    }

    private void finishProfile() {
        String bio = etBio.getText().toString().trim();
        
        SharedPreferences prefs = getSharedPreferences("BettrPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            return;
        }

        showLoading();

        String avatarBase64 = "";
        if (avatarBitmap != null) {
            avatarBase64 = encodeImageToBase64(avatarBitmap);
        }

        apiInserts.updateUserProfile(userId, bio, avatarBase64, success -> {
            runOnUiThread(() -> {
                hideLoading();
                if (success) {
                    Intent intent = new Intent(CompleteProfile.this, Feed.class);
                    startActivity(intent);
                    finish();
                }
            });
        });
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); // Comprimimos más para el avatar
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
}
