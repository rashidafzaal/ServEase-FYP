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
	$phone = $_POST['KEY_PHONE'];

	$sql="SELECT P_ID from provider WHERE P_PHONE = '$phone'";

	$r = mysqli_query($conn,$sql); 
	$result = mysqli_fetch_array($r);
 //
	if($result)
	{
			$myP_ID = $result['P_ID'];
			$sql2 = "UPDATE request SET R_STATUS = '0' WHERE P_ID = '$myP_ID'";
			if (mysqli_query($conn,$sql2)) 
			{
				echo "updated";
			}
			else
			{

			}

		}else{
			echo "Could not register";

		}
}

?>