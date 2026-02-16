package com.example.bettr.ApiRest;

import android.util.Log;
import com.example.bettr.Dao.User;
import com.example.bettr.Publicaciones.Habit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Api_Gets {
    private static final String BASE_URL = "https://bettr-g5yv.onrender.com/rest";
    private static final String TAG = "Api_Gets";

    public interface ApiCallback {
        void onResult(boolean success, int userId);
    }

    public interface HabitsCallback {
        void onResult(ArrayList<Habit> habits);
    }

    public interface UsersCallback {
        void onResult(ArrayList<User> users);
    }

    public void getUser(String username, String passwordHash, ApiCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                String encodedUser = URLEncoder.encode(username, "UTF-8").replace("+", "%20");
                URL url = new URL(BASE_URL + "/users?search=" + encodedUser);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(10000);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    int id = -1;
                    boolean found = false;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject userObj = jsonArray.getJSONObject(i);
                        if (userObj.optString("username", "").equalsIgnoreCase(username)) {
                            id = userObj.optInt("id", -1);
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        callback.onResult(true, id);
                    } else {
                        callback.onResult(false, -1);
                    }
                } else {
                    callback.onResult(false, -1);
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error en getUser (search): " + e.getMessage());
                callback.onResult(false, -1);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void getSocialFeed(int userId, HabitsCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            ArrayList<Habit> lista = new ArrayList<>();
            try {
                URL url = new URL(BASE_URL + "/habits/feed/" + userId);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lista.add(new Habit(
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
                Log.e(TAG, "Error en feed: " + e.getMessage());
                callback.onResult(lista);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void searchUsers(String query, UsersCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            ArrayList<User> users = new ArrayList<>();
            try {
                String urlString = BASE_URL + "/users";
                if (query != null && !query.isEmpty()) {
                    urlString += "?search=" + URLEncoder.encode(query, "UTF-8");
                }
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        users.add(new User(
                                obj.optInt("id", 0),
                                obj.optString("name", ""),
                                obj.optString("username", ""),
                                obj.optString("avatar", "")
                        ));
                    }
                }
                callback.onResult(users);
            } catch (IOException | JSONException e) {
                callback.onResult(users);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void getHabits(HabitsCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            ArrayList<Habit> lista = new ArrayList<>();
            try {
                URL url = new URL(BASE_URL + "/habits");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        lista.add(new Habit(
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
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
