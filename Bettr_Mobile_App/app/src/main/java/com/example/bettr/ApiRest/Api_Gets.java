package com.example.bettr.ApiRest;

import android.util.Log;
import com.example.bettr.Publicaciones.Publicaciones;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Api_Gets {
    private static final String BASE_URL = "https://bettr-g5yv.onrender.com/rest";
    private static final String TAG = "Api_Gets";

    public interface ApiCallback {
        void onResult(boolean success);
    }

    public interface PostsCallback {
        void onResult(ArrayList<Publicaciones> posts);
    }

    public void getUser(String username, String password, ApiCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {

                URL url = new URL(BASE_URL + "/users/" + username + "/" + password);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                int responseCode = connection.getResponseCode();
                callback.onResult(responseCode == HttpURLConnection.HTTP_OK);
            } catch (IOException e) {
                callback.onResult(false);
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }

    public void getHabits(PostsCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            ArrayList<Publicaciones> lista = new ArrayList<>();
            try {
                URL url = new URL(BASE_URL + "/habits"); 
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) response.append(line);
                    in.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lista.add(new Publicaciones(
                                obj.optString("username", "User"),
                                obj.optString("image_url", ""),
                                obj.optString("description", ""),
                                obj.optString("habit_type", "Habit"),
                                obj.optInt("likes_count", 0),
                                obj.optInt("streak_count", 0)
                        ));
                    }
                }
                callback.onResult(lista);
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error fetching habits: " + e.getMessage());
                callback.onResult(lista);
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }
}
