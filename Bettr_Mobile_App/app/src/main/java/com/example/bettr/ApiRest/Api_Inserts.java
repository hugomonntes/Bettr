package com.example.bettr.ApiRest;

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
    private static final String BASE_URL = "https://bettr-g5yv.onrender.com/rest";

    public interface ApiInsertCallback {
        void onResult(boolean success);
    }

    public void insertUsuario(String name, String username, String email, String password, ApiInsertCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/users");
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
                if (callback != null) callback.onResult(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED);

            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error: " + e.getMessage());
                if (callback != null) callback.onResult(false);
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }

    public void addHabit(int userId, String description, String imageUrl, String habitType, ApiInsertCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/habits");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("user_id", userId);
                json.put("description", description);
                json.put("image_url", imageUrl);
                json.put("habit_type", habitType);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (callback != null) callback.onResult(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED);

            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error: " + e.getMessage());
                if (callback != null) callback.onResult(false);
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }
}
