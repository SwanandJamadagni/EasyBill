<?php
include "phpqrcode/qrlib.php";
$host= gethostname();
$ip = gethostbyname($host);
$codefile = fopen("C:/xampp/htdocs/code.txt", "r") or die("Unable to open file!");
$code =  fread($codefile,filesize("C:/xampp/htdocs/code.txt"));
$file_name="qr.png";
$qrcode = $code;
QRcode::png($qrcode, $file_name);
if(file_exists($file_name)==TRUE){
    header('location: http://'.$ip.'/qr.png');
}
?>