<?php
    $conection = new Conection();
    $conection->init("freedb_BettrDB", "sql.freedb.tech", "freedb_hmontes", "pMEn7Hq3e9nYb$$");
?>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Iniciar Sesión - HabitTrack</title>
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
      /* background: #000000ff; */
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

    .forgot-password {
      text-align: right;
      margin-bottom: 24px;
    }

    .forgot-password a {
      color: #FCD34D;
      text-decoration: none;
      font-size: 14px;
      font-weight: 500;
    }

    .forgot-password a:hover {
      text-decoration: underline;
    }

    .btn {
      width: 100%;
      padding: 16px;
      background: #FCD34D;
      color: #000000ff;
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

    .register-link {
      text-align: center;
      color: #94a3b8;
      font-size: 14px;
    }

    .register-link a {
      color: #FCD34D;
      text-decoration: none;
      font-weight: 600;
    }

    .register-link a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="logo">
      <div class="logo-icon">🏆</div>
    </div>
    
    <h1>Iniciar Sesión</h1>
    <p class="subtitle">Bienvenido de nuevo a Bettr</p>

    <form method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>">
      <div class="form-group">
        <label for="email">Correo Electrónico</label>
        <input type="email" id="email" name="email" placeholder="tu@email.com" required>
      </div>

      <div class="form-group">
        <label for="password">Contraseña</label>
        <input type="password" id="password" name="password" placeholder="Ingresa tu contraseña" required>
      </div>

      <div class="forgot-password">
        <a href="#">¿Olvidaste tu contraseña?</a>
      </div>

      <button type="submit" class="btn">Iniciar Sesión</button>
    </form>

    <div class="divider">o</div>

    <div class="register-link">
      ¿No tienes cuenta? <a href="registro.php">Regístrate aquí</a>
    </div>
  </div>
</body>
</html>