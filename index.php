<?php
session_start();
?>
<?php
require "conn.php";
?>
<?php
if(isset($_SESSION['email'])){
    header('location: purchase.php');
    exit();
}
?>
<?php
$pass = '';
if(isset($_POST['login_btn'])){
    $email = $_POST['email'];
    $password = $_POST['password'];
    if(empty($email && $password)){
        echo "<script type='text/javascript'>alert('Email & Password cant be empty')</script>";
    }
    else{
    $select = "SELECT pass, fname, lname 
               FROM userinfo
               WHERE email = '$email'";
    $result = $conn->query($select);
    if ($result->rowCount() > 0) {
        while($data = $result->fetch(PDO::FETCH_ASSOC)) {
            $pass = $data["pass"];
            $_SESSION['f_name'] = $data["fname"];
            $_SESSION['l_name'] = $data["lname"];
        }
        if($pass == $password){
        $_SESSION['email'] = $email;
        unset($_SESSION['log']);
        header('location: purchase.php');
        exit();
        }
        else {
            echo "<script type='text/javascript'>alert('Email or Password is wrong')</script>";
        }
    }
    else{
        echo "<script type='text/javascript'>alert('User Not Found')</script>";
    }
    }
}
            elseif(isset($_POST['next_btn'])){
            }
            elseif(isset($_POST['create_btn'])){
                $fname = $_POST['fname'];
                $lname = $_POST['lname'];
                $c_email = $_POST['c_email'];
                $c_pass = $_POST['c_pass'];
                if(empty($fname && $lname && $c_email && $c_pass)){
                     echo "<script type='text/javascript'>alert('cant create account all fields are mandetory')</script>";
                }
                
                else{
                    $selectemail = "SELECT pass, fname, lname 
                    FROM userinfo
                    WHERE email = '$c_email'";
                    $emailresult = $conn->query($selectemail);
                    if ($emailresult->rowCount() > 0) {
                        echo "<script type='text/javascript'>alert('account with this email_id is already present')</script>";
                    }
                    else{
                        $dir = "C:/xampp/htdocs/Users/$c_email";
                        mkdir($dir);
                        if(is_dir($dir)){
                            $insert = "INSERT INTO userinfo (fname, lname, email, pass)
                            VALUES ('$fname', '$lname', '$c_email', '$c_pass')";
                            if($conn->query($insert)==TRUE){
                                echo "<script type='text/javascript'>alert('Account Created Succesfully')</script>";
                            }
                            else{
                                echo "<script type='text/javascript'>alert('error')</script>";
                            }
                        }
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
      <meta name="viewport" content="width=device-width, initial-scale=1.0"/> 
      <style>
            .input-field input[type=email]:focus + label,
            .input-field input[type=password]:focus + label,
            .input-field input[type=text]:focus + label{
            color: #3949ab;
            }
             .input-field input[type=email]:focus,
             .input-field input[type=password]:focus,
             .input-field input[type=text]:focus{
             border-bottom: 2px solid #3949ab;
             box-shadow: none;
            }
      </style>
    </head>
        
    <body>
        <center>
                <div class="container">
                    <div class="z-depth-1 white row" style="display: inline-block; padding: 32px 48px 0px 48px; border: 10px solid purple darken-1;">
                        
                        <img src="pc_logo.jpg">
                        <div class='row'>
                        <img src="pc_logo_1.jpg">
                        </div>
                        <h5 class="purple-text text-darken-1">Login</h5>
                        <div class='row'>
                            <div class='col s12'></div>
                        </div>
                        
                        <form action="index.php" method="post">
                        <div class='row'>
                            <div class='input-field col s12'>
                             <input class='validate' type='email' name='email' id='email'></input>
                             <label for='email'>Enter Your Email</label>
                            </div>
                        </div>
                        <div class='row'>
                            <div class='input-field col s12'>
                             <input class='validate' type='password' name='password' id='password'></input>
                             <label for='password'>Enter Your password</label>
                            </div>
                        </div>
                        <div class='row'>
                            <button type='submit' name='login_btn' class='col s12 btn btn-large waves-effect purple darken-1'>LOGIN</button>
                        </div>
                        </form>
                        <div class='row'>
                        <form action="index.php" method="post">
                            <label style='float: right;'>
                                <a class="modal-trigger" href="#modal1" style="color: red">Forget Password</a>
                            </label>
                            <div id="modal1" class="modal">
                                <div class="modal-content">
                                    <div class='row'>
                                        <div class='input-field col s12'>
                                        <input class='validate' type='email' name='f_email' id='f_email'></input>
                                        <label style='text-align: left'>Enter Email</label>
                                        </div>
                                    </div>
                                    <div class='row'>
                                        <div class='input-field col s12'>
                                        <input disabled value="Security Question" class='validate' type='text' name='ques' id='ques'></input>
                                        </div>
                                    </div>
                                    <div class='row'>
                                        <div class='input-field col s12'>
                                        <input class='validate' type='text' name='ans' id='ans'></input>
                                        <label style='text-align: left'>Answer Security Question</label>
                                        </div>
                                    </div>
                                    <div class='row'>
                                    <button type='submit' name='next_btn' class='col s12 btn btn-large waves-effect purple darken-1'>next</button>
                                    </div> 
                                </div>
                            </div>
                            </form>
                            <form action="index.php" method="post">
                            <label style='float: left;'>
                                <a class="modal-trigger" href="#modal2" style="color: green">Create Account</a>
                            </label>
                              <div id="modal2" class="modal">
                                <div class="modal-content">
                                    <div class='row'>
                                        <div class='input-field col s12'>
                                        <input class='validate' type='text' name='fname' id='fname'></input>
                                        <label style='text-align: left'>Enter First Name</label>
                                        </div>
                                    </div>
                                    <div class='row'>
                                        <div class='input-field col s12'>
                                        <input class='validate' type='text' name='lname' id='lname'></input>
                                        <label style='text-align: left'>Enter Last Name</label>
                                        </div>
                                    </div>
                                    <div class='row'>
                                        <div class='input-field col s12'>
                                        <input class='validate' type='email' name='c_email' id='c_email'></input>
                                        <label style='text-align: left'>Enter Email Id</label>
                                        </div>
                                    </div>
                                    <div class='row'>
                                        <div class='input-field col s12'>
                                        <input class='validate' type='password' name='c_pass' id='c_pass'></input>
                                        <label style='text-align: left'>Enter Password</label>
                                        </div>
                                    </div>
                                    <div class='row'>
                                    <button type='submit' name='create_btn' class='col s12 btn btn-large waves-effect purple darken-1'>Create Account</button>
                                    </div> 
                                </div>
                            </div>
                            </form>
                        </div>
                    </div>
                </div>
        </center>
        <nav class="purple darken-1">
            <div class="nav-wrapper">
            <ul id="nav-mobile" class="right hide-on-med-and-down">
            <li><i class="material-icons">copyright</i></li>
            <li><a href="#">All Rights Reserved by EasyBill</a></li>
            </ul>
            </div>
        </nav>
       
       <!--Import jQuery before materialize.js-->
      <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
      <script type="text/javascript" src="js/materialize.min.js"></script>
      <script>
          $(document).ready(function(){
            // the "href" attribute of the modal trigger must specify the modal ID that wants to be triggered
            $('.modal-trigger').leanModal();
          });
          
          $(document).ready(function() {
            $("select").material_select();
          });
      </script>
      <script type="text/javascript">
            function setTextField(ddl) {
                    document.getElementById('question').value = ddl.options[ddl.selectedIndex].text;
            }
     </script>
    </body>
  </html>