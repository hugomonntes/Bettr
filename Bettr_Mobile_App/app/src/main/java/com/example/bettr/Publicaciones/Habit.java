package com.example.bettr.Publicaciones;

public class Habit {
    private String nombreUsuario;
    private String imageUrl;
    private int likes;
    private String descripcion;
    private String info;
    private int streak;

    public Habit(String nombreUsuario, String imageUrl, String descripcion, String info, int likes, int streak) {
        this.nombreUsuario = nombreUsuario;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
        this.info = info;
        this.likes = likes;
        this.streak = streak;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public String getImageUrl() { return imageUrl; }
    public int getLikes() { return likes; }
    public String getDescripcion() { return descripcion; }
    public String getInfo() { return info; }
    public int getStreak() { return streak; }
}
