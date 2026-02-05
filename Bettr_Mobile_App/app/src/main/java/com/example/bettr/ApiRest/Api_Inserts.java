package com.example.bettr.ApiRest;

import android.media.Image;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Api_Inserts {
	private static final String TAG = "Api_Inserts"; 
	private static final String BASE_URL = "https://bettr-idlo.onrender.com/rest";

	public interface ApiInsertCallback {
		void onResult(boolean success);
	}

	public void insertUsuario (String name, String username, String email, String password, ApiInsertCallback callback){
		new Thread(() -> {
			HttpURLConnection connection = null;
			try {
				URL url = new URL(BASE_URL + "/users/add");
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);

				JSONObject json = new JSONObject();
				json.put("name", name);
				json.put("username", username);
				json.put("email", email);
				json.put("password_hash", password);

				try (OutputStream os = connection.getOutputStream()) {
					byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
					os.write(input, 0, input.length);
				}

				int responseCode = connection.getResponseCode();
				Log.d(TAG, "Insert Usuario Response Code: " + responseCode);
				
				if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
					Log.d(TAG, "Usuario insertado correctamente");
					if (callback != null){
						callback.onResult(true);
					}
				} else {
					Log.e(TAG, "Error al insertar usuario. Code: " + responseCode);
					if (callback != null){
						callback.onResult(false);
					}
				}

			} catch (IOException | JSONException e) {
				Log.e(TAG, "Error en la petición API: " + e.getMessage());
				if (callback != null){
					callback.onResult(false);
				}
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}).start();
	}

	public void insertDescription(int idUser, String descripcion, ApiInsertCallback callback){
		new Thread(() -> {
			HttpURLConnection connection = null;
			try{
				URL url = new URL(BASE_URL + "/addDescription/" + descripcion + "/" + idUser);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);

				JSONObject json = new JSONObject();
				json.put("descripcion",descripcion);
				json.put("id",idUser);

				try (OutputStream os = connection.getOutputStream()) {
					byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
					os.write(input, 0, input.length);
				}

				int responseCode = connection.getResponseCode();
				Log.d(TAG, "Insert Description Response Code: " + responseCode);

				if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
					Log.d(TAG, "Descripcion Insertada");
					if (callback != null){
						callback.onResult(true);
					}
				} else {
					Log.e(TAG, "Error al insertar descripcion. Code: " + responseCode);
					if (callback != null){
						callback.onResult(false);
					}
				}
			} catch (IOException | JSONException e) {
				Log.e(TAG, "Error en la petición API: " + e.getMessage());
				if (callback != null){
					callback.onResult(false);
				}
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		});
	}

	public void insertHabito(String nombreUsuario, String descripcion, Image post){
		new Thread(() -> {
			HttpURLConnection connection = null;
			try {
				URL url = new URL(BASE_URL + "/habits/add");
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);

				JSONObject json = new JSONObject();
				json.put("nombre", nombreUsuario);
				json.put("descripcion", descripcion);

				try (OutputStream os = connection.getOutputStream()) {
					byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
					os.write(input, 0, input.length);
				}

				int responseCode = connection.getResponseCode();
				Log.d(TAG, "Insert Habito Response Code: " + responseCode);

			} catch (IOException | JSONException e) {
				Log.e(TAG, "Error en la petición API Habito: " + e.getMessage());
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}).start();
	}
}
