<?php
error_reporting(E_ERROR | E_PARSE);
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bettr - Completa tu Perfil</title>
    <link rel="stylesheet" href="assets/css/auth.css">
    <link rel="stylesheet" href="assets/css/global.css">
    <style>
        .profile-setup-container {
            max-width: 480px;
            margin: 0 auto;
            padding: 40px 20px;
        }
        
        .avatar-upload {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-bottom: 30px;
        }
        
        .avatar-preview {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 48px;
            color: white;
            font-weight: bold;
            margin-bottom: 15px;
            overflow: hidden;
            cursor: pointer;
            transition: transform 0.2s;
            border: 4px solid white;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        
        .avatar-preview:hover {
            transform: scale(1.05);
        }
        
        .avatar-preview img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .avatar-upload-label {
            color: #667eea;
            font-weight: 500;
            cursor: pointer;
            font-size: 14px;
        }
        
        .avatar-upload-label:hover {
            text-decoration: underline;
        }
        
        #avatarInput {
            display: none;
        }
        
        .form-hint {
            font-size: 13px;
            color: #666;
            margin-top: 5px;
        }
        
        .skip-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            color: #999;
            font-size: 14px;
        }
        
        .skip-link a {
            color: #667eea;
            text-decoration: none;
        }
        
        .skip-link a:hover {
            text-decoration: underline;
        }

        #form-input {
            resize: none;
        }

    </style>
</head>
<body>
    <div class="auth-page">
        <div class="auth-container profile-setup-container">
            <div class="auth-logo-card">
                <img src="" alt="Bettr" class="auth-logo">
            </div>
            
            <h1 class="auth-title">¡Bienvenido a Bettr!</h1>
            <p class="auth-subtitle">Completa tu perfil para personalizar tu experiencia</p>
            
            <div id="errorMessage" class="auth-error"></div>
            
            <form id="profileForm" class="auth-form">
                <div class="avatar-upload">
                    <div class="avatar-preview" id="avatarPreview" onclick="document.getElementById('avatarInput').click()">
                        <span id="avatarLetter">U</span>
                    </div>
                    <label for="avatarInput" class="avatar-upload-label">Cambiar foto de perfil</label>
                    <input type="file" id="avatarInput" accept="image/*" style="display: none;">
                </div>
                
                <div class="auth-form-group">
                    <label for="description">Descripción</label>
                    <div class="input-icon-wrapper">
                        <textarea id="description" name="description" class="form-input" rows="4" placeholder="Cuéntanos sobre ti... ¿Cuáles son tus objetivos?"></textarea>
                    </div>
                </div>
                
                <button type="submit" class="auth-btn" id="saveBtn">Guardar y Continuar</button>
            </form>
            
            <p class="skip-link">
                <a href="dashboard.php">Omitir por ahora</a>
            </p>
        </div>
    </div>
    
    <div class="loading-overlay" id="loadingOverlay">
        <div class="loading-spinner"></div>
    </div>
    
    <div class="toast" id="toast"></div>    
    
    <script src="assets/js/completar_perfil.js"></script>
</body>
</html>

