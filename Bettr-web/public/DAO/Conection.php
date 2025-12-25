<?php
class Conection{
    private $dbName;
    private $host;
    private $user;
    private $password;

    function init($dbName, $host, $user, $password){
        try {
            $conexion = new PDO();
        } catch (SQLite3Exception $sql) {
            print("Error en la conexión con la base de datos");
        }
    }

    function stop(){

    }
}
?>