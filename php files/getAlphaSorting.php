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
	$category = $_POST['KEY_CategoryName'];

	$sql = "SELECT * FROM provider WHERE P_SERVICE = '$category' ORDER BY P_NAME ASC";
	$r = mysqli_query($conn,$sql);
		

		$result = array();

		$row_count=mysqli_num_rows($r);

	    while ($res = mysqli_fetch_array($r))
	    {
	    	array_push($result,array(
				"id"=>$res['P_ID'],
				"name"=>$res['P_NAME'],
				"email"=>$res['P_EMAIL'],	
				"password"=>$res['P_PASSWORD'],
				"phone"=>$res['P_PHONE'],
				"pic"=>$res['P_PICTURE'],
				"service"=>$res['P_SERVICE'],
				"address"=>$res['P_ADDRESS'],
				"details"=>$res['P_DETAILS'],
				"long"=>$res['P_LONGITUDE'],
				"lat"=>$res['P_LATITUDE'],
				)		
				);
	    }

		echo json_encode(array("result"=>$result));

		mysqli_close($conn);

}

?>