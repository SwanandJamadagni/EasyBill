<?php
require "conn.php";
$fname = $_POST["fname"];
$lname = $_POST["lname"];
$email = $_POST["email"];
$password = $_POST["password"];
$selectemail = "SELECT pass, fname, lname 
                FROM userinfo
                WHERE email = '$email'";
$emailresult = $conn->query($selectemail);
if ($emailresult->rowCount() > 0) {
    echo "account already exists";
}
else{
    $dir = "C:/xampp/htdocs/Users/$email";
    mkdir($dir);
    if(is_dir($dir)){
        $billdir = "$dir/bills";
        mkdir($billdir);
        if(is_dir($billdir)){
            copy("C:/xampp/htdocs/printbill.php","$dir/printbill.php");
            copy("C:/xampp/htdocs/generate_qr_code.php","$dir/generate_qr_code.php");
        }
    $insert = "INSERT INTO userinfo (fname, lname, email, pass)
    VALUES ('$fname', '$lname', '$email', '$password')";
        if($conn->query($insert)==TRUE){
            echo "succesfull";
        }
        else{
            echo "failed";
        }
    }
}
?>
