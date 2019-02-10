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
	$phone = $_POST['KEY_PHONE'];
	$name = $_POST['KEY_NAME'];
	$password = $_POST["KEY_PASSWORD"];
	$pic= $_POST["KEY_PIC"];
	$address= $_POST["KEY_ADDRESS"];

	$sql = "UPDATE provider SET P_PASSWORD ='$password', P_NAME = '$name', P_PICTURE = '$pic', P_ADDRESS = '$address' WHERE P_PHONE='$phone'";

	if(mysqli_query($conn,$sql)){
			echo "ProviderSucess";
		}else{
			echo "Failed";

		}
}

?>