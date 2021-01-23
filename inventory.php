<?php
session_start();
?>
<?php
$host= gethostname();
$ip = gethostbyname($host);
?>
<?php
require "conn.php";
?>
<?php
if(isset($_SESSION['log'])){
header('location: index.php');
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
        <form action="inventory.php" method="post">
            <div class='row'>
                <div class='input-field col s2'>
                    <input class='validate' type='text' name='category' id='category'></input>
                    <label for='category'>Category</label>
                </div>
            </div>
            <div class='row'>
                <div class='col s3'>
                    <button type='submit' name='search' id="search" class='btn btn-flat waves-effect purple lighten-1'>Search</button>
                </div>
            </div>
        </form>
    
                <table border="2" style= "background-color: white; color: black; margin: 0 auto;" >
                <thead>
                <tr>
                    <th>Description</th>
                    <th>prize</th>
                    <th>quantity</th>
                </tr>
                </thead>
                <tbody>
                    <?php
                        if(isset($_POST['search'])){
                            $category = $_POST['category'];
                            $select = "SELECT description, prize, quantity 
                                       FROM stock
                                       WHERE category = '$category'";
                            $result = $conn->query($select);
                            if ($result->rowCount() > 0) {
                                while($data = $result->fetch(PDO::FETCH_ASSOC)) {
                                    echo
                                    "<tr>
                                        <td>{$data["description"]}</td>
                                        <td>{$data["prize"]}</td>
                                        <td>{$data["quantity"]}</td>
                                    </tr>\n";
                                }
                            }
                        }
                    ?>
                </tbody>
                </table>
        

        <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="js/materialize.min.js"></script>
        <script>
            $(".button-collapse").sideNav();
        </script>
    
    </body>
 </html>