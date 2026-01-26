package com.example.bettr.ApiRest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Api_Inserts {
	public void insertUsuario (String name, String username, String email, String password){
		new Thread(() -> {
			try {
				URL url = new URL("http://192.130.0.4:8080/api_rest/rest/users/add"); // TODO API aun por crear
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);
				JSONObject json = new JSONObject();
				json.put("name", name);
				json.put("username", username);
				json.put("email", email);
				json.put("password", password);
			} catch (IOException | JSONException e) {
				throw new RuntimeException(e);
			}
		}).start();
	}

	public void insertHabito(){

	}
}
