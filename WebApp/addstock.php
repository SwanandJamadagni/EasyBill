<?php
require "conn.php";
?>
<?php
$f = 0;
$product_id = $_POST["product_id"];
$category = $_POST["category"];
$description = $_POST["description"];
$prize = $_POST["prize"];
$quantity = $_POST["quantity"];
$select = "SELECT code
           FROM products
           WHERE code = '$product_id' AND prize = '$prize'";
        $result = $conn->query($select);
        if ($result->rowCount() > 0){
            $select_stock = "SELECT quantity
                             FROM stock
                             WHERE code = '$product_id' AND prize = '$prize'";
            $stock_result = $conn->query($select_stock);
            if($stock_result->rowCount() > 0){
                            while($data = $stock_result->fetch(PDO::FETCH_ASSOC)) {
                                $old_quantity = $data["quantity"];
                            }
                $new_quantity = $quantity + $old_quantity;
                $update = "UPDATE stock SET quantity = '$new_quantity' 
                           WHERE code = '$product_id' AND prize = '$prize'";
                if($conn->query($update)==TRUE){
                    $f = 1;
                }
            }
            else{
                $insert = "INSERT INTO stock (code, category, description, prize, quantity)
                           VALUES ('$product_id', '$category', '$description', '$prize', '$quantity')";
                if($conn->query($insert)==TRUE){
                    $f = 1;
                }
            }
        }
        else{
            echo "product_not_found";
        }
        if($f==1){
            echo "stock_added";
        }
?>