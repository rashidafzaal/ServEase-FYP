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
	$email = $_POST["KEY_EMAIL"];
	$password = $_POST["KEY_PASSWORD"];

	$sql = "SELECT * from user WHERE U_EMAIL='$email'";
	//$sql2= "UPDATE user SET U_PASSWORD ='$password' WHERE U_EMAIL='$email'";
	

	$r = mysqli_query($conn,$sql) or trigger_error(mysql_error()." ".$sql);
	if(mysqli_num_rows($r))
	{
			$sql2 = "UPDATE user SET U_PASSWORD ='$password' WHERE U_EMAIL='$email'";
			if (mysqli_query($conn,$sql2)) 
			{
				echo "UserSuccess";
			}else
			{}

		}
		else{
			$sql = "SELECT * from provider WHERE P_EMAIL='$email'";
			$r = mysqli_query($conn,$sql) or trigger_error(mysql_error()." ".$sql);
			if(mysqli_num_rows($r))
			{
				$sql2 = "UPDATE provider SET P_PASSWORD ='$password' WHERE P_EMAIL='$email'";
				
				if (mysqli_query($conn,$sql2)) 
				{
					echo "ProviderSuccess";
				}
				else
				{}
			}
			else
			{
			}



		}
	
}

?>