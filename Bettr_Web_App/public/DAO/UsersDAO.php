<?php
error_reporting(E_ERROR | E_PARSE);

class UsersDAO {
    private $conn;

    public function __construct($conn) {
        $this->conn = $conn;
    }

    public function createUser($name, $username, $email, $password) {
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        $stmt = $this->conn->prepare("INSERT INTO usuarios (name, username, email, password_hash) VALUES (?, ?, ?, ?)");
        return $stmt->execute([$name, $username, $email, $hashedPassword]);
    }

    public function nameExists($name){
        $query = "SELECT id_usuario FROM usuarios WHERE name = '" . $name . "'";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetch() !== false;
    }

    public function usernameExists($username){
        $query = "SELECT id_usuario FROM usuarios  WHERE username = '" . $username . "'";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetch() !== false;
    }

    public function emailExists($email){
        $query = "SELECT id_usuario FROM usuarios WHERE email = '" . $email . "'";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetch() !== false;
    }

    public function getPasswordByEmail($email){
        $query = "SELECT password_hash FROM usuarios WHERE email = '" . $email . "'";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        $result = $stmt->fetch();
        return $result ? $result['password_hash'] : null;
    }
}

?>

