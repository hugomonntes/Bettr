package com.example.bettr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bettr.ApiRest.Api_Gets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

	private EditText etUsername, etPassword;
	private Button btnLogin;
	private Api_Gets apiGets;
	private FrameLayout loadingOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_login);

		apiGets = new Api_Gets();
		loadingOverlay = findViewById(R.id.loading_overlay);

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});

		etUsername = findViewById(R.id.etUsername);
		etPassword = findViewById(R.id.etPassword);
		btnLogin = findViewById(R.id.btnLogin);

		if (btnLogin != null) {
			btnLogin.setOnClickListener(v -> loginUser());
		}

		TextView tvRegister = findViewById(R.id.tvRegister);
		if (tvRegister != null) {
			tvRegister.setOnClickListener(v -> {
				Intent intent = new Intent(Login.this, Register.class);
				startActivity(intent);
				finish();
			});
		}
	}

	private void loginUser() {
		if (etUsername == null || etPassword == null) return;
		
		String username = etUsername.getText().toString().trim();
		String password = etPassword.getText().toString();

		if (username.isEmpty() || password.isEmpty()) {
			return;
		}

		showLoading();
		String passwordHash = hashPassword(password);

		apiGets.getUser(username, passwordHash, success -> {
			runOnUiThread(() -> {
				hideLoading();
				if (success) {
					Intent intent = new Intent(Login.this, Feed.class);
					startActivity(intent);
					finish();
				}
			});
		});
	}

	private void showLoading() {
		if (loadingOverlay != null) loadingOverlay.setVisibility(View.VISIBLE);
	}

	private void hideLoading() {
		if (loadingOverlay != null) loadingOverlay.setVisibility(View.GONE);
	}

	private String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
