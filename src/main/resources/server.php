<?php 
	session_start();
	$db = mysqli_connect('localhost', 'id1716004_coins', '10203040abcd', 'id1716004_coins');

	// initialize variables
	$name = "";
	$min = "";
	$max = "";

	$id = 0;
	$update = false;

	if (isset($_POST['save'])) {
		$name = $_POST['name'];
		$min = $_POST['min'];
		$max = $_POST['max'];

		mysqli_query($db, "INSERT INTO coin (name, min,max) VALUES ('$name', '$min', '$max')"); 
		$_SESSION['message'] = "Coin saved"; 
		header('location: index.php');
	}


	if (isset($_POST['update'])) {
		$id = $_POST['id'];
		$name = $_POST['name'];
		$min = $_POST['min'];
		$max = $_POST['max'];


		mysqli_query($db, "UPDATE coin SET name='$name', min='$min', max='$max' WHERE id=$id");
		$_SESSION['message'] = "coin updated!"; 
		header('location: index.php');
	}

if (isset($_GET['del'])) {
	$id = $_GET['del'];
	mysqli_query($db, "DELETE FROM info WHERE id=$id");
	$_SESSION['message'] = "Coin deleted!"; 
	header('location: index.php');
}


	$results = mysqli_query($db, "SELECT * FROM coin");


?>