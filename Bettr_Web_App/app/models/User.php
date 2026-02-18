<?php
error_reporting(E_ERROR | E_PARSE);

class User{
    private $id;
    private $name;
    private $username;
    private $email;
    private $password;
    private $avatar;

    public function __construct($id, $name, $username, $email, $password, $avatar){
        $this->id = $id;
        $this->name = $name;
        $this->username = $username;
        $this->email = $email;
        $this->password = $password;
        $this->avatar = $avatar;
    }

    public function getId(){
        return $this->id;
    }

    public function setName($name){
        $this->name = $name;
    }

    public function getName(){
        return $this->name;
    }

    public function setUsername($username){
        $this->username = $username;
    }
    
    public function getUsername(){
        return $this->username;
    }

    public function setEmail($email){
        $this->email = $email;
    }

    public function getEmail(){
        return $this->email;
    }

    public function setPassword($password){
        $this->password = $password;
    }

    public function getPassword(){
        return $this->password;
    }

    public function getAvatar(){
        return $this->avatar;
    }

    public function setAvatar($avatar){
        $this->avatar = $avatar;
    }

    public function __toString(){
        return "User [id=$this->id, name=$this->name, username=$this->username, email=$this->email, avatar=$this->avatar]";
    }
}

