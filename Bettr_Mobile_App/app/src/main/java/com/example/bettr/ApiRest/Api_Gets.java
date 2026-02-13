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
        void onResult(boolean success, int userId);
    }

    public interface PostsCallback {
        void onResult(ArrayList<Publicaciones> posts);
    }

    public void getUser(String username, String password, ApiCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                // Asegúrate de que esta URL coincida con tu backend
                URL url = new URL(BASE_URL + "/users/" + username + "/" + password);
                Log.d(TAG, "Conectando a: " + url.toString());
                
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(5000);

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) response.append(line);
                    in.close();

                    Log.d(TAG, "Respuesta Server: " + response.toString());

                    int id = -1;
                    try {
                        JSONObject userObj = new JSONObject(response.toString());
                        id = userObj.optInt("id", -1);
                    } catch (JSONException e) {
                        Log.w(TAG, "No se pudo parsear el ID del usuario, pero el login es OK");
                    }
                    
                    callback.onResult(true, id);
                } else {
                    callback.onResult(false, -1);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error de red en login: " + e.getMessage());
                callback.onResult(false, -1);
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
                                obj.optString("username", "Usuario"),
                                obj.optString("image_url", ""),
                                obj.optString("description", ""),
                                obj.optString("habit_type", "Hábito"),
                                obj.optInt("likes_count", 0),
                                obj.optInt("streak_count", 0)
                        ));
                    }
                }
                callback.onResult(lista);
            } catch (IOException | JSONException e) {
                callback.onResult(lista);
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }
}
