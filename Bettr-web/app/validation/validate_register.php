<?php
require_once __DIR__ . '/../../public/dao/Connection.php';
require_once __DIR__ . '/../../public/dao/UsersDAO.php';
$conection = new Conection();
$conection->init("freedb_BettrDB", "sql.freedb.tech", "freedb_hmontes", "pMEn7Hq3e9nYb$$");
$usersDAO = new UsersDAO($conection->getConection());
if($_SERVER['REQUEST_METHOD'] === 'POST') {
    $campos_existentes = ['name', 'username', 'email'];
    $nameExists = $usersDAO->nameExists($_POST['name']);
    $usernameExists = $usersDAO->usernameExists($_POST['username']);
    $emailExists = $usersDAO->emailExists($_POST['email']);
    $usersDAO -> createUser($_POST['name'], $_POST['email'], $_POST['password']);
}
