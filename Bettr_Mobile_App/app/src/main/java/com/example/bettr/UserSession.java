package com.example.appmovil;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserSession {
    private static final String PREFS_NAME = "BettrPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_AVATAR = "userAvatar";
    private static final String KEY_USER_BIO = "userBio";
    private static final String KEY_FOLLOWING = "following";
    
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    
    public UserSession(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    
    public void setUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }
    
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }
    
    public void setUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }
    
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }
    
    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }
    
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }
    
    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }
    
    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }
    
    public void setUserAvatar(String avatar) {
        editor.putString(KEY_USER_AVATAR, avatar);
        editor.apply();
    }
    
    public String getUserAvatar() {
        return prefs.getString(KEY_USER_AVATAR, "");
    }
    
    public void setUserBio(String bio) {
        editor.putString(KEY_USER_BIO, bio);
        editor.apply();
    }
    
    public String getUserBio() {
        return prefs.getString(KEY_USER_BIO, "");
    }
    
    public void addFollowing(int userId) {
        Set<String> following = new HashSet<>(getFollowingListStrings());
        following.add(String.valueOf(userId));
        editor.putStringSet(KEY_FOLLOWING, following);
        editor.apply();
    }
    
    public void removeFollowing(int userId) {
        Set<String> following = new HashSet<>(getFollowingListStrings());
        following.remove(String.valueOf(userId));
        editor.putStringSet(KEY_FOLLOWING, following);
        editor.apply();
    }
    
    public boolean isFollowing(int userId) {
        return getFollowingListStrings().contains(String.valueOf(userId));
    }
    
    private Set<String> getFollowingListStrings() {
        return prefs.getStringSet(KEY_FOLLOWING, new HashSet<>());
    }

    public ArrayList<Integer> getFollowingList() {
        Set<String> following = getFollowingListStrings();
        ArrayList<Integer> result = new ArrayList<>();
        for (String id : following) {
            try {
                result.add(Integer.parseInt(id));
            } catch (NumberFormatException e) {
            }
        }
        return result;
    }
    
    public void saveUserData(int userId, String username, String name, String email, String avatar, String bio) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_AVATAR, avatar);
        editor.putString(KEY_USER_BIO, bio);
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return getUserId() > 0;
    }
    
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
