package com.example.bettr.Publicaciones;

public class Habit {
    private int id;
    private String nombreUsuario;
    private String userAvatar; // Base64 del avatar del usuario
    private String imageUrl;   // Base64 de la imagen del post
    private int likes;
    private String descripcion;
    private String info;
    private int streak;
    private String habitType;
    private String createdAt;
    private boolean isLiked;

    // Constructor original para compatibilidad
    public Habit(String nombreUsuario, String userAvatar, String imageUrl, String descripcion, String info, int likes, int streak) {
        this.nombreUsuario = nombreUsuario;
        this.userAvatar = userAvatar;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
        this.info = info;
        this.likes = likes;
        this.streak = streak;
    }

    // Nuevo constructor con todos los campos
    public Habit(int id, String nombreUsuario, String userAvatar, String imageUrl, String descripcion, String habitType, int likes, int streak, String createdAt) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.userAvatar = userAvatar;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
        this.habitType = habitType;
        this.info = habitType + " • ";
        this.likes = likes;
        this.streak = streak;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getUserAvatar() { return userAvatar; }
    public String getImageUrl() { return imageUrl; }
    public int getLikes() { return likes; }
    public String getDescripcion() { return descripcion; }
    public String getInfo() { return info; }
    public int getStreak() { return streak; }
    public String getHabitType() { return habitType; }
    public String getCreatedAt() { return createdAt; }
    
    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; }
    public void setLikes(int likes) { this.likes = likes; }
}
