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
	$pic = $_POST["KEY_PIC"];

	$sql = "UPDATE user SET U_PASSWORD ='$password', U_NAME = '$name', U_PICTURE = '$pic' WHERE U_PHONE='$phone'";

	if(mysqli_query($conn,$sql)){
			echo "UserSucess";
		}else{
			echo "Failed";

		}
}

?>