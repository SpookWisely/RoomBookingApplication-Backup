<?php
//Variable to contain the server to details to access it.

$server_name = "localhost";
$username = "root";
$password = "";
$database_name = "loginsregister";

//create the connection with above variables
   $connection = new mysqli($server_name,$username,$password,$database_name);
    //if statement for connection failure

if($connection->connect_error) {
    exit("Failure to establish connection reason: " . $connection->connect_error);

}else {
    //echo "Conncection Success";
}
