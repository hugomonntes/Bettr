<?php
class Conection{
    private $conection;
    private $dbName;
    private $host;
    private $user;
    private $password;

    public function init($dbName, $host, $user, $password){
        $dsn = "mysql:host=$host;port=3306;dbname=$dbName;charset=utf8mb4";
        try {
            $this->conection = new PDO($dsn, $user, $password);
            print("Conexión establecida"); 
        } catch (PDOException $sql) {
            print("Error de conexión " . $sql->getMessage());
        }
    }

    public function getConection(){
        return $this->conection;
    }

    function stop(){
        $this->conection = null;
    }
}
?>