<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Bettr</title>Bettr
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="assets/css/global.css">
</head>
<body>

<aside class="sidebar">
    <div class="logo">BETTR</div>

    <div class="user">
        <img src="assets/images/user.jpg" alt="Usuario">
        <div>
             <!-- Inyectar nombre de usuario + imagen de perfil -->
        </div>
    </div>

    <nav class="menu">
        <!-- Me falta añadirle los icons -->
        <a class="active" href="#">Inicio</a>
        <a href="#">Hábitos</a>
        <a href="#">Social</a>
        <a href="#">Perfil</a>
    </nav>

    <button class="upload-btn">Subir imagen</button>

    <div class="bottom">
        <a href="#">Mensajes</a>
        <a href="#">Notificaciones</a>
    </div>
</aside>

<div class="app">

    <header class="topbar">
        <h1>BETTR</h1>

        <!-- Inyectar de base de datos peticion si cumplio X día para ponerle la class done -->
        <div class="week">
            <span>S</span><span>S</span><span>M</span>
            <span class="done">T</span>
            <span class="done">W</span>
            <span class="done">T</span>
            <span class="done">F</span>
        </div>
    </header>

    <main class="content">
