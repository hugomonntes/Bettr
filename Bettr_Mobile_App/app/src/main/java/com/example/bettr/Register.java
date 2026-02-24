package com.example.appmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bettr.ApiRest.Api_Inserts;
import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.UserSession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register extends AppCompatActivity {
    private EditText etName, etUsername, etEmail, etPassword, etConfirmPassword;
    private Api_Inserts apiInserts;
    private Api_Gets apiGets;
    private FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        apiInserts = new Api_Inserts();
        apiGets = new Api_Gets();
        loadingOverlay = findViewById(R.id.loading_overlay);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> {
                registerUser();
            });
        }

        TextView tvLogin = findViewById(R.id.tvLogin);
        if (tvLogin != null) {
            tvLogin.setOnClickListener(v -> {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validation
        if (name.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.length() < 3) {
            Toast.makeText(this, "El nombre de usuario debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        String passwordHash = hashPassword(password);

        apiInserts.insertUsuario(name, username, email, passwordHash, success -> {
            if (success) {
                // After successful registration, get the user ID by logging in
                apiGets.getUser(username, passwordHash, (loginSuccess, userId) -> {
                    runOnUiThread(() -> {
                        hideLoading();
                        if (loginSuccess && userId > 0) {
                            // Save user session
                            UserSession session = new UserSession(Register.this);
                            session.setUserId(userId);
                            session.setUsername(username);
                            session.setUserName(name);
                            session.setUserEmail(email);
                            
                            Toast.makeText(Register.this, "¡Cuenta creada!", Toast.LENGTH_SHORT).show();
                            
                            // Go to complete profile
                            Intent intent = new Intent(Register.this, CompleteProfile.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Register.this, "Cuenta creada. Por favor inicia sesión.", Toast.LENGTH_LONG).show();
                            // Go to login
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                });
            } else {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(Register.this, "Error al crear cuenta. El usuario o correo ya existe.", Toast.LENGTH_LONG).show();
                });
            }
        });
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

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

