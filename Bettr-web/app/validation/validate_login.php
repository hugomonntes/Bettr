<?php
    require_once __DIR__ . '/../../public/DAO/Conection.php';
    require_once __DIR__ . '/../../public/DAO/UsersDAO.php';
    $conection = new Conection();
    $conection->init("freedb_BettrDB", "sql.freedb.tech", "freedb_hmontes", "pMEn7Hq3e9nYb$$");
    if($_SERVER['REQUEST_METHOD'] === 'POST') {
        $usersDAO = new UsersDAO($conection->getConection());
        $usersDAO->emailExists($_POST['email']); // Verifica si el usuario existe TODO CAMBIAR A EMAIL
        if ($usersDAO && password_verify($_POST['password'], $usersDAO->getPasswordByEmail($_POST['email']))) {
            header("Location: /Bettr-web/public/index.php");
            exit;
        } else {
            echo "Usuario o contraseña incorrectos";
        }
    }