<?php
ini_set('mysql.connect_timeout', 300);
ini_set('default_socket_timeout', 300); 

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


  
if($_SERVER['REQUEST_METHOD']=='POST')
{
	$img = $_POST['KEY_IMAGE'];
	$location = $_POST['KEY_LOCATION'];
	$details = $_POST['KEY_DETAILS'];
	$phone = $_POST['KEY_PHONE'];
	$longitude = $_POST['KEY_LONGITUDE'];
	$latitude = $_POST['KEY_LATITUDE'];

	$sql="UPDATE provider SET P_PICTURE='$img',P_ADDRESS='$location',P_DETAILS='$details',P_LONGITUDE='$longitude',P_LATITUDE='$latitude' WHERE P_PHONE='$phone'";

 //
	if(mysqli_query($conn,$sql)){
			echo "Success";
		}else{
			echo "Could not register";

		}
}

?>