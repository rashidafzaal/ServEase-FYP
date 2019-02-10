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
	$found = false;
	$p_phone = $_POST['KEY_P_PHONE'];
	$u_phone = $_POST['KEY_U_PHONE'];

	$sql = "SELECT U_ID from user WHERE U_PHONE = '$u_phone'";

	$r = mysqli_query($conn,$sql); 
	$result = mysqli_fetch_array($r);

	if($result)
	{
			$myU_ID = $result['U_ID'];

			$sql = "INSERT INTO request (U_ID) VALUES ('$myU_ID')";

			if(mysqli_query($conn,$sql))
			{
			 	$found = true;
			}
			else
			{

			}
	}
	else
	{
		echo "Could not do for USER";
	}
//===================================== Provider P_ID ===========================================

	$sql2 = "SELECT P_ID from provider WHERE P_PHONE = '$p_phone'";

	$r2 = mysqli_query($conn,$sql2); 
	$result2 = mysqli_fetch_array($r2);

	if ($result2 && $found == true)
	{
			$myP_ID = $result2['P_ID'];

			$sql = "UPDATE request SET P_ID = '$myP_ID' WHERE U_ID = '$myU_ID'";

			if(mysqli_query($conn,$sql))
			{
			 	$found = true;
			}
			else
			{

			}
	}
	else
	{
		echo "Could not do for PROVIDER";
	}

//===================================== Provider P_ID ===========================================
	if ($found)
	{
		$sql3 = "UPDATE request SET R_STATUS = '1' WHERE U_ID = '$myU_ID' AND P_ID = '$myP_ID'";
		if(mysqli_query($conn,$sql3))
			{
			 	echo "DONE";
			}
			else
			{
				"Could not dO R-STATUS";
			}
	}

}

?>