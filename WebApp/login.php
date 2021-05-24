<?php
require "conn.php";
$email = $_POST["email_id"];
$password = $_POST["pass_word"];
$select = "SELECT pass 
           FROM userinfo
           WHERE email = '$email'";
$result = $conn->query($select);
    if ($result->rowCount() > 0) {
        while($data = $result->fetch(PDO::FETCH_ASSOC)) {
            $pass = $data["pass"];
        }
            if($pass == $password){
                echo "hello";
            }
        else{
            echo "error";
        }
    }
    else{
        echo "user_not_found";
    }

?>