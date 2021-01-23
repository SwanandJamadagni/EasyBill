<?php
require "conn.php";
$email = $_POST["email"];
$password = $_POST["password"];
$select = "SELECT pass 
           FROM userinfo
           WHERE email = '$email'";
$result = $conn->query($select);
    if ($result->rowCount() > 0) {
        while($data = $result->fetch(PDO::FETCH_ASSOC)) {
            $pass = $data["pass"];
        }
            if($pass == $password){
                echo "welcome";
            }
            else{
                echo "go_to_login";
            }
    }
    else{
        echo "go_to_login";
    }

?>