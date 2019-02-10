<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "fyp";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
} 
	
 echo "Connecte Successfully - ";

if($_SERVER['REQUEST_METHOD']=='POST')
{
	$name = $_POST['KEY_P_NAME'];
	$email = $_POST['KEY_P_EMAIL'];
	$password = $_POST['KEY_P_PASSWORD'];
	$phone = $_POST['KEY_P_PHONE'];
	$service = $_POST['KEY_P_SERVICE'];

	$sql = "INSERT INTO provider (P_NAME, P_EMAIL, P_PASSWORD, P_PHONE, P_SERVICE) VALUES ('$name' , '$email', '$password', '$phone', '$service')";

	if(mysqli_query($conn,$sql)){
			echo "success";
		}else{
			echo "Could not register";

		}
}

?>