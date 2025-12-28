<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro - BETTR APP</title>
    <style>
        body {
            background-color: #111;
            color: #fff;
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .form-container {
            text-align: center;
            background-color: #1a1a1a;
            padding: 40px;
            border-radius: 10px;
            width: 300px;
        }

        h1 {
            color: #ffc107;
            margin-bottom: 20px;
            font-size: 24px;
        }

        input[type="text"], input[type="email"], input[type="password"] {
            padding: 10px;
            margin: 10px 0;
            width: 100%;
            border: none;
            border-radius: 5px;
        }

        button {
            background-color: #ffc107;
            border: none;
            padding: 10px 20px;
            color: #111;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 10px;
            width: 100%;
        }

        button:hover {
            opacity: 0.9;
        }

        a {
            color: #ffc107;
            text-decoration: none;
            font-size: 14px;
            display: block;
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>Registro</h1>
        <form>
            <input type="text" placeholder="Nombre completo" required><br>
            <input type="email" placeholder="Correo electrónico" required><br>
            <input type="password" placeholder="Contraseña" required><br>
            <button type="submit">Registrarse</button>
        </form>
        <a href="login.html">¿Ya tienes cuenta? Inicia sesión</a>
    </div>
</body>
</html>
