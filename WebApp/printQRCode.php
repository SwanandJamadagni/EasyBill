<?php
include "phpqrcode/qrlib.php";
session_start();
if(isset($_SESSION['qrcode'])){
$file_name="qr.png";
$qrcode = $_SESSION['qrcode'];
QRcode::png($qrcode, $file_name);
$qrcodeno = $_SESSION['qrcodeno'];
for($i = 0; $i < $qrcodeno ; $i++){
echo"<img src='qr.png'>";
}
}
else{
echo "<script type='text/javascript'>alert('Error')</script>";
echo "<script>window.close();</script>";
}
?>
<!DOCTYPE html>
 <html>
    <head>
      <!--Import Google Icon Font-->
      <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
      <!--Import materialize.css-->
      <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>

      <!--Let browser know website is optimized for mobile-->
      <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/> 
    </head>
    <body onload="printqrcode()">
    
        

    <script>
        function printqrcode(){
            window.print();
        }
    </script>

        
        <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="js/materialize.min.js"></script>
    
    </body>
 </html>