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
  
 if($_SERVER['REQUEST_METHOD']=='POST')
 {
	$phone = $_POST["KEY_PHONE"];

	$sql = "SELECT U_PICTURE from user WHERE U_PHONE = '$phone'";

	
	$r = mysqli_query($conn,$sql); 
	$result = mysqli_fetch_array($r);

 	echo ($result['U_PICTURE']);


}

?>