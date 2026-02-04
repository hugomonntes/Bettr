package com.example.bettr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bettr.ApiRest.Api_Gets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

	private EditText etEmail, etPassword;
	private Button btnLogin;
	private Api_Gets apiGets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_login);

		apiGets = new Api_Gets();

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});

		etEmail = findViewById(R.id.etUserName);
		etPassword = findViewById(R.id.etPassword);
		btnLogin = findViewById(R.id.btnLogin);

		btnLogin.setOnClickListener(v -> loginUser());

		TextView tvRegister = findViewById(R.id.tvRegister);
		tvRegister.setOnClickListener(v -> {
			Intent intent = new Intent(Login.this, Register.class);
			startActivity(intent);
			finish();
		});
	}

	private void loginUser() {
		String username = etEmail.getText().toString().trim();
		String password = etPassword.getText().toString();

		if (username.isEmpty() || password.isEmpty()) {
			Toast.makeText(this, "Please write on all fields", Toast.LENGTH_SHORT).show();
			return;
		}

		String passwordHash = hashPassword(password);

		apiGets.getUser(username, passwordHash, success -> {
			runOnUiThread(() -> {
				if (success) {
					Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Login.this, Feed.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(Login.this, "user,password or server error", Toast.LENGTH_SHORT).show();
				}
			});
		});
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
