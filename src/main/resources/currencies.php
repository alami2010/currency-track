<?php 
include('server.php');
	
		$records = mysqli_query($db, "SELECT * FROM coin ");
          $n = mysqli_fetch_array($records);
		
		
		$coins =  array();
		while ($row = mysqli_fetch_array($records)) {
			
					$coin =  array();

    $coin["name"]= $row["name"];
    $coin["min"]= $row["min"];
    $coin["max"]= $row["max"];
	$coins[] =  $coin;
}

        $myJSON = json_encode($coins);
		echo $myJSON;

	
?>