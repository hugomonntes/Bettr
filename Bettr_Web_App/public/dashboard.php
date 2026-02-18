<?php
// Check if user is logged in
$user = isset($_SESSION['user']) ? $_SESSION['user'] : null;

// Report only serious errors
error_reporting(E_ERROR | E_PARSE);
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bettr - Inicio</title>
    <link rel="stylesheet" href="assets/css/global.css">
    <link rel="stylesheet" href="assets/css/dashboard.css">
</head>
<body>
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-logo">
            <img src="https://i.imgur.com/YcR0J8h.png" alt="Bettr" style="width:40px;height:40px;border-radius:12px;">
            <span>Bettr</span>
        </div>
        
        <div class="sidebar-user" id="sidebarUser">
            <div class="sidebar-user-avatar" id="userAvatar">U</div>
            <div class="sidebar-user-info">
                <div class="sidebar-user-name" id="userName">Usuario</div>
                <div class="sidebar-user-username" id="userUsername">@username</div>
            </div>
        </div>
        
        <nav class="sidebar-menu">
            <a href="#" class="nav-item active" data-page="feed">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
                Inicio
            </a>
            <a href="#" class="nav-item" data-page="habits">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                Mis Hábitos
            </a>
            <a href="#" class="nav-item" data-page="discover">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
                Descubrir
            </a>
            <a href="#" class="nav-item" data-page="profile">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
                Perfil
            </a>
        </nav>
        
        <button class="sidebar-upload" onclick="openHabitModal()">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" width="20" height="20" style="display:inline;vertical-align:middle;margin-right:8px;">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            Nuevo Hábito
        </button>
        
        <div class="bottom">
            <a href="#" class="nav-item" onclick="logout()">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
                Cerrar Sesión
            </a>
        </div>
    </aside>
    
    <!-- Main Content -->
    <main class="main-content">
        <!-- Topbar -->
        <header class="topbar">
            <h1 class="topbar-title" id="pageTitle">Inicio</h1>
        </header>
        
        <!-- Content Area -->
        <div class="content" id="mainContent">
            <!-- Feed will be loaded here -->
        </div>
    </main>
    
    <!-- Habit Modal -->
    <div class="modal-overlay" id="habitModal">
        <div class="modal">
            <div class="modal-header">
                <h2 class="modal-title">Compartir Hábito</h2>
                <button class="modal-close" onclick="closeHabitModal()">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" width="24" height="24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </button>
            </div>
            <form id="habitForm">
                <div class="form-group">
                    <label class="form-label">Tipo de Hábito</label>
                    <select class="form-input" id="habitType">
                        <option value="Ejercicio">💪 Ejercicio</option>
                        <option value="Lectura">📚 Lectura</option>
                        <option value="Meditación">🧘 Meditación</option>
                        <option value="Estudio">📖 Estudio</option>
                        <option value="Saludable">🥗 Comida Saludable</option>
                        <option value="Otro">⭐ Otro</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">Descripción</label>
                    <textarea class="form-input" id="habitDescription" rows="4" placeholder="¿Qué hábito completaste hoy?"></textarea>
                </div>
                <button type="submit" class="btn btn-primary btn-full">Compartir</button>
            </form>
        </div>
    </div>
    
    <!-- Loading Overlay -->
    <div class="loading-overlay" id="loadingOverlay">
        <div class="loading-spinner"></div>
    </div>
    
    <!-- Toast -->
    <div class="toast" id="toast"></div>
    
    <script src="assets/js/dashboard.js"></script>
</body>
</html>

