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
}

?>