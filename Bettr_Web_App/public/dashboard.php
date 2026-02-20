<?php
$user = isset($_SESSION['user']) ? $_SESSION['user'] : null;

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
    
    <main class="main-content">
        <header class="topbar">
            <h1 class="topbar-title" id="pageTitle">Inicio</h1>
        </header>
        
        <div class="content" id="mainContent">
        </div>
    </main>
    
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
                    <label class="form-label">Foto del Hábito</label>
                    <div class="image-upload-container">
                        <div class="image-preview" id="habitImagePreview">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" width="40" height="40">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                            </svg>
                            <span>Sin imagen</span>
                        </div>
                        <div class="image-upload-buttons">
                            <label for="habitImageInput" class="btn btn-secondary">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" width="18" height="18">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                </svg>
                                Subir imagen
                            </label>
                            <input type="file" id="habitImageInput" accept="image/*" style="display: none;">
                            <button type="button" class="btn btn-secondary" onclick="openCamera()">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" width="18" height="18">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                                </svg>
                                Cámara
                            </button>
                        </div>
                        <button type="button" class="btn btn-link" id="removeImageBtn" onclick="removeHabitImage()" style="display: none; color: #EF4444;">
                            Eliminar imagen
                        </button>
                    </div>
                    <input type="hidden" id="habitImageBase64" value="">
                </div>
                
                <div class="modal-overlay" id="cameraModal" style="display: none;">
                    <div class="modal">
                        <div class="modal-header">
                            <h2 class="modal-title">Tomar Foto</h2>
                            <button class="modal-close" onclick="closeCamera()">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" width="24" height="24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>
                        <div class="camera-container">
                            <video id="cameraVideo" autoplay playsinline style="width: 100%; border-radius: 8px;"></video>
                            <canvas id="cameraCanvas" style="display: none;"></canvas>
                        </div>
                        <button type="button" class="btn btn-primary btn-full" onclick="takePhoto()">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" width="20" height="20">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                            </svg>
                            Capturar
                        </button>
                    </div>
                </div>
                
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
    
    <div class="loading-overlay" id="loadingOverlay">
        <div class="loading-spinner"></div>
    </div>
    
    <div class="toast" id="toast"></div>
    
    <script src="assets/js/dashboard.js"></script>
</body>
</html>

