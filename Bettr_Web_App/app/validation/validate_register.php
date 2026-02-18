<?php
error_reporting(E_ERROR | E_PARSE);

require_once __DIR__ . '/../../public/DAO/Conection.php';
require_once __DIR__ . '/../../public/DAO/UsersDAO.php';
$conection = new Conection();
$conection->init("freedb_BettrDB", "sql.freedb.tech", "freedb_hmontes", "pMEn7Hq3e9nYb$$");
$usersDAO = new UsersDAO($conection->getConection());
if($_SERVER['REQUEST_METHOD'] === 'POST') {
    $campos_existentes = ['name', 'username', 'email', 'password', 'confirm_password'];
    foreach($campos_existentes as $campo) {
        if(!isset($_POST[$campo]) || empty(trim($_POST[$campo]))) {
            die("El campo $campo es obligatorio.");
        } else if ($campo === 'email' && !filter_var($_POST[$campo], FILTER_VALIDATE_EMAIL)) {
            die("El correo electrónico no es válido.");
        } else if ($campo === 'confirm_password' && $_POST['password'] !== $_POST['confirm_password']) {
            die("Las contraseñas no coinciden.");
        }
    }
    $nameExists = $usersDAO->nameExists($_POST['name']);
    $usernameExists = $usersDAO->usernameExists($_POST['username']);
    $emailExists = $usersDAO->emailExists($_POST['email']);
    if(!$nameExists && !$usernameExists && !$emailExists) {
        $usersDAO -> createUser($_POST['name'], $_POST['username'], $_POST['email'], $_POST['password']);
    } else {
        $errors = [];
        if($nameExists){
            $errors[] = "<br>El nombre ya está en uso.";
        }
        if($usernameExists){
            $errors[] = "<br>El nombre de usuario ya está en uso.";
        }
        if($emailExists){
            $errors[] = "<br>El correo electrónico ya está en uso.";
        }
        print(implode("<br>", $errors));
    }
}

