<?php
echo "Testando conexão <br />";
$servername = "192.168.1.22";
$username = 'root';
$password = 'root';

$conn = new mysqli($servername, $username, $password);

if ($conn->connect_error) {
  die("conexão falhou" . $conn->connect_error);
}

echo "Conexão com sucesso";
