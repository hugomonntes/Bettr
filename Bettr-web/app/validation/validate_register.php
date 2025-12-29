<?php

if($_SERVER['REQUEST_METHOD'] === 'POST') {
    $campos_existentes = ['name', 'username', 'email'];
    foreach($campos_existentes as $campo) {
        if ($campo ) {
            # code...
        }
    }
}
