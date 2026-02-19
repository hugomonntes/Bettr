package com.example.bettr.ApiRest;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public void addHabit(int userId, String description, String imageUrl, String habitType, ApiInsertCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/habits");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
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

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    if (callback != null) {
                        callback.onResult(true);
                    }
                } else {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line.trim());
                        }
                    }
                    if (callback != null) {
                        callback.onResult(false);
                    }
                }

            } catch (IOException | JSONException e) {
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
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
                if (callback != null) {
                    callback.onResult(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED);
                }

            } catch (IOException | JSONException e) {
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void followUser(int followerId, int followingId, ApiInsertCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/users/follow/" + followerId + "/" + followingId);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();
                if (callback != null) {
                    callback.onResult(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED);
                }

            } catch (IOException e) {
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void updateUserProfile(int userId, String description, String avatarBase64, ApiInsertCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/users/" + userId + "/profile");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("description", description);
                json.put("avatar", avatarBase64);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (callback != null) {
                    callback.onResult(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED);
                }

            } catch (IOException | JSONException e) {
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void likeHabit(int habitId, int userId, ApiInsertCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/habits/" + habitId + "/like/" + userId);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();
                if (callback != null) {
                    callback.onResult(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED);
                }

            } catch (IOException e) {
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void unlikeHabit(int habitId, int userId, ApiInsertCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/habits/" + habitId + "/like/" + userId);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();
                if (callback != null) {
                    callback.onResult(responseCode == HttpURLConnection.HTTP_OK);
                }

            } catch (IOException e) {
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void unfollowUser(int followerId, int followingId, ApiInsertCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL + "/users/unfollow/" + followerId + "/" + followingId);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();
                if (callback != null) {
                    callback.onResult(responseCode == HttpURLConnection.HTTP_OK);
                }

            } catch (IOException e) {
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
