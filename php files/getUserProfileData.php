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
	$phonee = $_POST['KEY_SP_Phone'];

	$sql = "SELECT * FROM user WHERE U_PHONE = '$phonee'";
	$r = mysqli_query($conn,$sql);
		

		$result = array();

		$row_count=mysqli_num_rows($r);

	    while ($res = mysqli_fetch_array($r))
	    {
	    	array_push($result,array(
				"id"=>$res['U_ID'],
				"name"=>$res['U_NAME'],
				"email"=>$res['U_EMAIL'],	
				"password"=>$res['U_PASSWORD'],
				"phone"=>$res['U_PHONE'],
				"pic"=>$res['U_PICTURE'],
				
				)		
				);
	    }

		echo json_encode(array("result"=>$result));

		mysqli_close($conn);

}

?>