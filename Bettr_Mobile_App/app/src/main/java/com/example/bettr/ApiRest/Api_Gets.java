package com.example.appmovil.ApiRest;

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

    public interface UserCallback {
        void onResult(User user);
    }

    public interface StatsCallback {
        void onResult(int followers, int following, int habits);
    }

    public interface HabitsCallback {
        void onResult(ArrayList<Habit> habits);
    }

    public interface UsersCallback {
        void onResult(ArrayList<User> users);
    }

    public interface BooleanCallback {
        void onResult(boolean result);
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
                                obj.optInt("id", 0),
                                obj.optString("username", "Usuario"),
                                obj.optString("user_avatar", ""),
                                obj.optString("image_url", ""),
                                obj.optString("description", ""),
                                obj.optString("habit_type", "Otro"),
                                obj.optInt("likes_count", 0),
                                obj.optInt("streak_count", 0),
                                obj.optString("created_at", "")
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
                Log.e(TAG, "Error searching users: " + e.getMessage());
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
                                obj.optInt("id", 0),
                                obj.optString("username", "Usuario"),
                                obj.optString("user_avatar", ""),
                                obj.optString("image_url", ""),
                                obj.optString("description", ""),
                                obj.optString("habit_type", "Otro"),
                                obj.optInt("likes_count", 0),
                                obj.optInt("streak_count", 0),
                                obj.optString("created_at", "")
                        ));
                    }
                }
                callback.onResult(lista);
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error fetching habits: " + e.getMessage());
                callback.onResult(lista);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void getUserById(int userId, UserCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/users/" + userId);
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

                    JSONObject obj = new JSONObject(response.toString());
                    User user = new User(
                            obj.optInt("id", 0),
                            obj.optString("name", ""),
                            obj.optString("username", ""),
                            obj.optString("avatar", "")
                    );
                    user.setEmail(obj.optString("email", ""));
                    user.setDescription(obj.optString("description", ""));
                    callback.onResult(user);
                } else {
                    callback.onResult(null);
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error getting user by ID: " + e.getMessage());
                callback.onResult(null);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void getUserStats(int userId, StatsCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/users/" + userId + "/stats");
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

                    JSONObject obj = new JSONObject(response.toString());
                    int followers = obj.optInt("followers", 0);
                    int following = obj.optInt("following", 0);
                    int habits = obj.optInt("habits", 0);
                    callback.onResult(followers, following, habits);
                } else {
                    callback.onResult(0, 0, 0);
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error getting user stats: " + e.getMessage());
                callback.onResult(0, 0, 0);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void getHabitsByUserId(int userId, HabitsCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            ArrayList<Habit> lista = new ArrayList<>();
            try {
                URL url = new URL(BASE_URL + "/habits/user/" + userId);
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
                                obj.optInt("id", 0),
                                obj.optString("username", "Usuario"),
                                obj.optString("user_avatar", ""),
                                obj.optString("image_url", ""),
                                obj.optString("description", ""),
                                obj.optString("habit_type", "Hábito"),
                                obj.optInt("likes_count", 0),
                                obj.optInt("streak_count", 0),
                                obj.optString("created_at", "")
                        ));
                    }
                }
                callback.onResult(lista);
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error getting user habits: " + e.getMessage());
                callback.onResult(lista);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void checkIfLiked(int habitId, int userId, BooleanCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/habits/" + habitId + "/isliked/" + userId);
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

                    JSONObject obj = new JSONObject(response.toString());
                    boolean liked = obj.optBoolean("liked", false);
                    callback.onResult(liked);
                } else {
                    callback.onResult(false);
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error checking like status: " + e.getMessage());
                callback.onResult(false);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void checkIfFollowing(int followerId, int followingId, BooleanCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/users/isfollowing/" + followerId + "/" + followingId);
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

                    JSONObject obj = new JSONObject(response.toString());
                    boolean following = obj.optBoolean("following", false);
                    callback.onResult(following);
                } else {
                    callback.onResult(false);
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error checking follow status: " + e.getMessage());
                callback.onResult(false);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void getFollowers(int userId, UsersCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            ArrayList<User> users = new ArrayList<>();
            try {
                URL url = new URL(BASE_URL + "/users/" + userId + "/followers");
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
                Log.e(TAG, "Error getting followers: " + e.getMessage());
                callback.onResult(users);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void getFollowing(int userId, UsersCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            ArrayList<User> users = new ArrayList<>();
            try {
                URL url = new URL(BASE_URL + "/users/" + userId + "/following");
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
                Log.e(TAG, "Error getting following: " + e.getMessage());
                callback.onResult(users);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
