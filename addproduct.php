<?php
require "conn.php";
?>
<?php
$product_id = $_POST["product_id"];
$category = $_POST["category"];
$description = $_POST["description"];
$prize = $_POST["prize"];
$select = "SELECT code
           FROM products
           WHERE code = '$product_id' AND prize = '$prize'";
        $result = $conn->query($select);
        if ($result->rowCount() > 0){
            echo "product_exists";
        }
        else{
            $insert = "INSERT INTO products (code, category, description, prize)
                       VALUES ('$product_id', '$category', '$description', '$prize')";
           if($conn->query($insert)==TRUE){
              echo "product_added";
            }
        }
?>