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
	$name = $_POST['KEY_U_NAME'];
	$email = $_POST['KEY_U_EMAIL'];
	$password = $_POST['KEY_U_PASSWORD'];
	$phone = $_POST['KEY_U_PHONE'];

	$sql = "INSERT INTO user (U_NAME, U_EMAIL, U_PASSWORD, U_PHONE) VALUES ('$name' , '$email', '$password', '$phone')";

	if(mysqli_query($conn,$sql)){
			echo "Successfully Registered";
		}else{
			echo "Could not register";

		}
}

?>