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
	$u_id = $_POST['KEY_ID'];

	$sql="UPDATE request SET R_STATUS = '4' WHERE U_ID = '$u_id'";

	$r = mysqli_query($conn,$sql); 
	$result = mysqli_fetch_array($r);
 //
	if($result)
	{
		
		echo "updated";
		
		}else{
			echo "Could not register";

		}
}

?>