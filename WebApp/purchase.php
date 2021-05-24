<?php
session_start();
?>
<?php
$host= gethostname();
$ip = gethostbyname($host);
?>
<?php
require "conn.php";
include "phpqrcode/qrlib.php";
?>
<?php
if(isset($_SESSION['log'])){
header('location: EasyBill/index.php');
exit();
}
?>
<?php
$fname=$_SESSION['f_name'];
$lname=$_SESSION['l_name'];
$email=$_SESSION['email'];
if(isset($_POST['logout'])){
$_SESSION['log'] = 1;
unset($_SESSION['email']);
header('location: index.php');
exit();
}
?>
<?php
if(isset($_POST['addstock'])){
$category = $_POST['category'];
$description = $_POST['description'];
$prize = $_POST['prize'];
$quantity = $_POST['quantity'];
$code = crc32($category.":".$description.":".$prize);
$qrno = $quantity;
$_SESSION['qrcodeno'] = $qrno;
$selectproduct = "SELECT code
                  FROM products
                  WHERE code = '$code'";
$productresult = $conn->query($selectproduct);
if ($productresult->rowCount() > 0){
    $select = "SELECT quantity
               FROM stock
               WHERE code = '$code'";
    $result = $conn->query($select);
    if ($result->rowCount() > 0) {
        while($data = $result->fetch(PDO::FETCH_ASSOC)) {
            $oquantity = $data["quantity"];
        }
        $nquantity = $quantity + $oquantity;
        $update = "UPDATE stock SET quantity = '$nquantity' 
                   WHERE category = '$category' AND description = '$description' AND prize = '$prize'";
        if($conn->query($update)==TRUE){
        $qrcode = $code;
        $_SESSION['qrcode'] = $qrcode;
        echo "<script type='text/javascript'>alert('Go to Generate-QRCode to Generate Codes')</script>";
            }
    }
    else{
            $insertproduct = "INSERT INTO stock (code, category, description, prize, quantity)
            VALUES ('$code', '$category', '$description', '$prize', '$quantity')";
            if($conn->query($insertproduct)==TRUE){
                $qrcode = $code;
                $_SESSION['qrcode'] = $qrcode;
                echo "<script type='text/javascript'>alert('Go to Generate-QRCode to Generate Codes')</script>";  
            }
        }
}
else{
        echo "<script type='text/javascript'>alert('Product not Found')</script>";
    }
}
if(isset($_POST['add'])){
$ncategory = $_POST['ncategory'];
$ndescription = $_POST['ndescription'];
$nprize = $_POST['nprize'];
$ncode = crc32($ncategory.":".$ndescription.":".$nprize);
$selectnewproduct = "SELECT code
                     FROM products
                     WHERE code = '$ncode'";
$resultnewproduct = $conn->query($selectnewproduct);
if ($resultnewproduct->rowCount() > 0){
    echo "<script type='text/javascript'>alert('Product Already Exists')</script>";
}
else{
    $insert = "INSERT INTO products (code, category, description, prize)
           VALUES ('$ncode', '$ncategory', '$ndescription', '$nprize')";
    if($conn->query($insert)==TRUE){
        echo "<script type='text/javascript'>alert('Product Added Succesfully')</script>";
    }
    else{
        echo "<script type='text/javascript'>alert('Error')</script>";
    }
}
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
      <style>
        body{
        position: fixed; 
        overflow-y: scroll;
        width: 100%;
       }
       nav{
           width: 100%
       }
       .input-field input[type=date]:focus + label
       .input-field input[type=text]:focus + label{
         color: #3949ab;
       }
       .input-field input[type=date]:focus
       .input-field input[type=text]:focus{
          border-bottom: 2px solid #3949ab;
          box-shadow: none;
       }
      </style>
    </head>
    <body>
        <ul id="slide-out" class="side-nav">
            <li><div class="user-view">
            <div class="background">
            <img>
            </div>
            <a href="#!name"><span class="black-text name">Welcome: <?php echo $fname?></span></a>
            <a href="#!email"><span class="black-text email"><?php echo $email ?></span></a>
            <a href="#!email"><span class="black-text email">IP: <?php echo $ip ?></span></a>
            </div></li>
            <li><button class="waves-effect black-text btn-flat modal-trigger" href="#modal3"> <i class="material-icons">settings</i> Setting</button></li>
            <form action="" method="post">
            <li><button class="waves-effect black-text btn-flat" type='submit' name='logout' id='logout'> <i class="material-icons">power_settings_new</i> LogOut</button></li>
            </form>
            <li><div class="divider"></div></li>
            <li><a class="waves-effect black-text btn-flat" href="http://localhost/purchase.php">purchase</a></li>
            <li><a class="waves-effect black-text btn-flat">sell</a></li>
            <li><a class="waves-effect black-text btn-flat" href="http://localhost/inventory.php">Inventory</a></li>
        </ul>
        
        <nav class="purple darken-1">
            <div class="nav-wrapper">
            <button class="button-collapse white-text btn-flat" data-activates="slide-out"><i class="material-icons">menu</i></button>
            </div>
        </nav>
        <form action="purchase.php" method="post">
            <div class='row'>
                <div class='input-field col s2'>
                    <input class='validate' type='text' name='category' id='category'></input>
                    <label for='category'>Ctegory</label>
                </div>
                <div class='input-field col s5'>
                    <input class='validate' type='text' name='description' id='description'></input>
                    <label for='brand'>Description</label>
                </div>
                <div class='input-field col s2'>
                    <input class='validate' type='text' name='prize' id='prize'></input>
                    <label for='prize'>Prize</label>
                </div>
                <div class='input-field col s1'>
                    <input class='validate' type='number' name='quantity' id='quantity'></input>
                    <label for='quantity'>Quantity</label>
                </div>
            </div>
            <div class="row">
                <div class='col s3'>
                    <button type='submit' name='addstock' id="addstock" class='btn btn-flat waves-effect purple lighten-1'>Add Stock</button>
                </div>
            </div>
            <div class="row">
                <div class='col s3'>
                    <button name='generate' id="generate" class='btn btn-flat waves-effect purple lighten-1' onclick="genqrcode()">Generate-QRCode</button>
                </div>
            </div>
            <div class="row">
                <div class='col s3'>
                    <button name='addproduct' id="addproduct" class='btn btn-flat waves-effect purple lighten-1 modal-trigger' href="#modaladdproduct">Add New Product</button>
                </div>
            </div>
            <div id="modaladdproduct" class="modal">
                <div class="modal-content">
                    <div class='row'>
                        <div class='input-field col s2'>
                            <input class='validate' type='text' name='ncategory' id='ncategory'></input>
                            <label for='ncategory'>Category</label>
                        </div>
                        <div class='input-field col s5'>
                            <input class='validate' type='text' name='ndescription' id='ndescription'></input>
                            <label for='nbrand'>Description</label>
                        </div>
                        <div class='input-field col s3'>
                            <input class='validate' type='text' name='nprize' id='nprize'></input>
                            <label for='nbrand'>Prize</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class='col s3'>
                            <button type='submit' name='add' id="add" class='btn btn-flat waves-effect green darken-1'>Add</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>

        

      <script>
        function genqrcode(){
                var myWindow = window.open("http://localhost/printQRCode.php", "", "width=1000,height=1000");
        }
      </script>
      
      <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
      <script type="text/javascript" src="js/materialize.min.js"></script>
      <script>
        $(".button-collapse").sideNav();
        $(document).ready(function(){
            // the "href" attribute of the modal trigger must specify the modal ID that wants to be triggered
            $('.modal-trigger').leanModal();
          });
      </script>
    </body>
 </html>
      