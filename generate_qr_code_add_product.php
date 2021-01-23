<?php
require "conn.php";
?>
<?php
$category = $_POST["category"];
$description = $_POST["description"];
$prize = $_POST["prize"];
$code = crc32($category.":".$description.":".$prize);
$select = "SELECT code
           FROM products
           WHERE code = '$code'";
        $result = $conn->query($select);
        if ($result->rowCount() > 0){
            echo "product_exists";
        }
        else{
            $insert = "INSERT INTO products (code, category, description, prize)
                       VALUES ('$code', '$category', '$description', '$prize')";
           if($conn->query($insert)==TRUE){
              echo "product_added";
            }
        }