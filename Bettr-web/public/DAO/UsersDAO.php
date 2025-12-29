<?php

class UsersDAO {
    private $conn;

    public function __construct($conn) {
        $this->conn = $conn;
    }

    public function createUser($name, $email, $password) {
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        $stmt = $this->conn->prepare("INSERT INTO usuario (username, email, password_hash) VALUES (?, ?, ?)");
        return $stmt->execute([$name, $email, $hashedPassword]);
    }

    public function nameExists($name){
        $query = "SELECT id FROM usuario WHERE name = '" . $name . "'";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetch() !== false;
    }

    public function usernameExists($username){
        $query = "SELECT id FROM usuario  WHERE username = '" . $username . "'";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetch() !== false;
    }

    public function emailExists($email){
        $query = "SELECT id FROM usuario WHERE email = '" . $email . "'";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->fetch() !== false;
    }
}

?>