<?php
require "conn.php";
require "fpdf.php";
?>
<?php
$date = date("Y/m/d");
date_default_timezone_set("Asia/Kolkata");
$time = date("h:i:sa");
$dts = $date.'-'.$time;
$email = $_POST["email"];
$scandata = $_POST["scandata"];
$discount = $_POST["discount"];
$phonenumber = $_POST["phonenumber"];
$insert = "INSERT INTO sellbills (phonenumber, dts, details, discount)
           VALUES ('$phonenumber', '$dts', '$scandata', '$discount')";
if($conn->query($insert)==TRUE){
$data = explode("&",$scandata);
$datalength = count($data);
    for($i = 0; $i < $datalength-1; $i++){
       $item = explode(":",$data[$i]);
       $itemlength = count($item);
       for($j = 0; $j <$itemlength-1; $j++){
       $category = $item[0];
       $description = $item[1];
       $prize = $item[2];
       $quantity = $item[3];
       }
       $update = "UPDATE stock SET quantity = quantity - $quantity 
           WHERE category = '$category' AND description = '$description' AND prize = '$prize'";
       $conn->query($update);
    }
}
$phoneinfo = fopen("C:/xampp/htdocs/Users/$email/phoneinfo.txt","w")or die("Unable to open file!");
if(file_put_contents("C:/xampp/htdocs/Users/$email/phoneinfo.txt",$phonenumber)==TRUE){
$emailinfo = fopen("C:/xampp/htdocs/Users/$email/email.txt","w")or die("Unable to open file!");
if( file_put_contents("C:/xampp/htdocs/Users/$email/email.txt",$email)==TRUE){
    echo "donebill";
}
}
?>

