package com.example.bettr.Publicaciones;

public class Publicaciones {
    private String nombreUsuario;
    private String imageUrl;
    private int likes;
    private String descripcion;
    private String info; // Entrenamiento, Futbol, etc.
    private int streak;

    public Publicaciones(String nombreUsuario, String imageUrl, String descripcion, String info, int likes, int streak) {
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
