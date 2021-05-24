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
$insert = "INSERT INTO manual_sellbills (dts, details, discount, email)
           VALUES ('$dts', '$scandata', '$discount', '$email')";
if($conn->query($insert)==TRUE){
$data = explode("&",$scandata);
$datalength = count($data);
    for($i = 0; $i < $datalength-1; $i++){
       $item = explode(":",$data[$i]);
       $itemlength = count($item);
       for($j = 0; $j <$itemlength-1; $j++){
       $description = $item[0];
       $price = $item[1];
       $quantity = $item[2];
       }
       $update = "UPDATE manual_stock SET quantity = quantity - $quantity 
           WHERE description = '$description' AND price = '$price'";
       $conn->query($update);
    }
}
$emailinfo = fopen("C:/xampp/htdocs/Users/$email/email.txt","w")or die("Unable to open file!");
if( file_put_contents("C:/xampp/htdocs/Users/$email/email.txt",$email)==TRUE){
    echo "done_manual_bill";
}
?>

