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
	$password = $_POST["KEY_PASSWORD"];

	$sql = "SELECT * FROM provider WHERE P_PHONE = '$phone' AND P_PASSWORD = '$password'";

	$r = mysqli_query($conn,$sql) or trigger_error(mysql_error()." ".$sql);
	if(mysqli_num_rows($r) > 0)
	{
			echo "ProviderSuccess";
		}else{
			echo "failure";

		}

}

?>