<?php
require_once __DIR__ . '/../../public/DAO/Conection.php';
require_once __DIR__ . '/../../public/DAO/UsersDAO.php';
$conection = new Conection();
$conection->init("freedb_BettrDB", "sql.freedb.tech", "freedb_hmontes", "pMEn7Hq3e9nYb$$");
$usersDAO = new UsersDAO($conection->getConection());
if($_SERVER['REQUEST_METHOD'] === 'POST') {
    $campos_existentes = ['name', 'username', 'email'];
    $nameExists = $usersDAO->nameExists($_POST['name']);
    $usernameExists = $usersDAO->usernameExists($_POST['username']);
    $emailExists = $usersDAO->emailExists($_POST['email']);
    if(!$nameExists && !$usernameExists && !$emailExists){
        $usersDAO -> createUser($_POST['name'], $_POST['username'], $_POST['email'], $_POST['password']);
    }
}
