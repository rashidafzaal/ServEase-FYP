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
	$pid = $_POST['KEY_PID'];

	$sql="SELECT R_STATUS from request WHERE P_ID = '$pid'";

	$r = mysqli_query($conn,$sql); 
	$result = mysqli_fetch_array($r);
 
	if($result)
	{
			$mystatus= $result['R_STATUS'];
			echo $mystatus;

		}else{
			echo "Could not register";

		}
}

?>