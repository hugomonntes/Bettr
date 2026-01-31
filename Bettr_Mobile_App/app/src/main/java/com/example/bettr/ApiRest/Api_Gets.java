package com.example.bettr.ApiRest;

import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api_Gets {
    private static final String BASE_URL = "http://10.0.2.2:8080/api_rest/rest";
    private static final String TAG = "Api_Gets";

    public interface ApiCallback {
        void onResult(boolean success);
    }

    public void getUser(String username, String password, ApiCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/users/get/" + username + "/" + password + "/");
                Log.d(TAG, "Attempting login at: " + url.toString());
                
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Login Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    callback.onResult(true);
                } else {
                    callback.onResult(false);
                }

            } catch (IOException e) {
                Log.e(TAG, "Error en la petición API: " + e.getMessage());
                callback.onResult(false);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
