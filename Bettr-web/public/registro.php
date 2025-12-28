<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Registrarse - HabitTrack</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
      background: #111;
      color: #ffffff;
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 20px;
    }

    .container {
      width: 100%;
      max-width: 400px;
      background: #111;
      border-radius: 24px;
      padding: 40px 32px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4);
    }

    .logo {
      text-align: center;
      margin-bottom: 32px;
    }

    .logo-icon {
      width: 64px;
      height: 64px;
      background: #FCD34D;
      border-radius: 50%;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      font-size: 32px;
      margin-bottom: 16px;
    }

    h1 {
      text-align: center;
      font-size: 28px;
      margin-bottom: 8px;
      font-weight: 600;
    }

    .subtitle {
      text-align: center;
      color: #94a3b8;
      font-size: 14px;
      margin-bottom: 32px;
    }

    .form-group {
      margin-bottom: 20px;
    }

    label {
      display: block;
      margin-bottom: 8px;
      font-size: 14px;
      font-weight: 500;
      color: #e2e8f0;
    }

    input {
      width: 100%;
      padding: 14px 16px;
      background: #111;
      border: 1px solid #2d3548;
      border-radius: 12px;
      color: #ffffff;
      font-size: 16px;
      transition: all 0.3s;
    }

    input:focus {
      outline: none;
      border-color: #FCD34D;
      box-shadow: 0 0 0 3px rgba(252, 211, 77, 0.1);
    }

    input::placeholder {
      color: #64748b;
    }

    .terms {
      margin-bottom: 24px;
      font-size: 13px;
      color: #94a3b8;
      line-height: 1.6;
    }

    .terms a {
      color: #FCD34D;
      text-decoration: none;
    }

    .terms a:hover {
      text-decoration: underline;
    }

    .btn {
      width: 100%;
      padding: 16px;
      background: #FCD34D;
      color: #0a0e27;
      border: none;
      border-radius: 12px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s;
      margin-bottom: 20px;
    }

    .btn:hover {
      background: #fbbf24;
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(252, 211, 77, 0.4);
    }

    .btn:active {
      transform: translateY(0);
    }

    .divider {
      display: flex;
      align-items: center;
      margin: 24px 0;
      color: #64748b;
      font-size: 14px;
    }

    .divider::before,
    .divider::after {
      content: '';
      flex: 1;
      height: 1px;
      background: #2d3548;
    }

    .divider::before {
      margin-right: 12px;
    }

    .divider::after {
      margin-left: 12px;
    }

    .login-link {
      text-align: center;
      color: #94a3b8;
      font-size: 14px;
    }

    .login-link a {
      color: #FCD34D;
      text-decoration: none;
      font-weight: 600;
    }

    .login-link a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="logo">
      <div class="logo-icon">🏆</div>
    </div>
    
    <h1>Crear Cuenta</h1>
    <p class="subtitle">Únete a Bettr y comienza tu viaje</p>

    <form>
      <div class="form-group">
        <label for="name">Nombre Completo</label>
        <input type="text" id="name" name="name" placeholder="Juan Pérez" required>
      </div>

      <div class="form-group">
        <label for="username">Nombre de Usuario</label>
        <input type="text" id="username" name="username" placeholder="@juanperez" required>
      </div>

      <div class="form-group">
        <label for="email">Correo Electrónico</label>
        <input type="email" id="email" name="email" placeholder="tu@email.com" required>
      </div>

      <div class="form-group">
        <label for="password">Contraseña</label>
        <input type="password" id="password" name="password" placeholder="Mínimo 8 caracteres" required>
      </div>

      <div class="form-group">
        <label for="confirm-password">Confirmar Contraseña</label>
        <input type="password" id="confirm-password" name="confirm-password" placeholder="Repite tu contraseña" required>
      </div>

      <div class="terms">
        Al registrarte, aceptas nuestros <a href="#">Términos de Servicio</a> y <a href="#">Política de Privacidad</a>
      </div>

      <button type="submit" class="btn">Crear Cuenta</button>
    </form>

    <div class="divider">o</div>

    <div class="login-link">
      ¿Ya tienes cuenta? <a href="login.php">Inicia sesión aquí</a>
    </div>
  </div>
</body>
</html>