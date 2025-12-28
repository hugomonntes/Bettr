<?php
class Conection{
    private $conection;
    private $dbName;
    private $host;
    private $user;
    private $password;

    function init($dbName, $host, $user, $password){
        $dsn = "mysql:host=$host;dbname=$dbName;charset=utf8";
        try {
            $this->conection = new PDO($dsn, $user, $password);
        } catch (PDOException $sql) {
            print("Error en la conexión con la base de datos"); 
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