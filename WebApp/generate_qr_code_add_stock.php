<?php
require "conn.php";
?>
<?php
$f = 0;
$category = $_POST["category"];
$description = $_POST["description"];
$prize = $_POST["prize"];
$quantity = $_POST["quantity"];
$email = $_POST["email"];
$code = crc32($category.":".$description.":".$prize);
$select = "SELECT code
           FROM products
           WHERE code = '$code'";
        $result = $conn->query($select);
        if ($result->rowCount() > 0){
            $select_stock = "SELECT quantity
                             FROM stock
                             WHERE code = '$code'";
            $stock_result = $conn->query($select_stock);
            if($stock_result->rowCount() > 0){
                            while($data = $stock_result->fetch(PDO::FETCH_ASSOC)) {
                                $old_quantity = $data["quantity"];
                            }
                $new_quantity = $quantity + $old_quantity;
                $update = "UPDATE stock SET quantity = '$new_quantity' 
                           WHERE code = '$code'";
                if($conn->query($update)==TRUE){
                    $f = 1;
                }
            }
            else{
                $insert = "INSERT INTO stock (code, category, description, prize, quantity)
                           VALUES ('$code', '$category', '$description', '$prize', '$quantity')";
                if($conn->query($insert)==TRUE){
                    $f = 1;
                }
            }
        }
        else{
            echo "product_not_found";
        }
if($f==1){
$codefile = fopen("C:/xampp/htdocs/Users/$email/code.txt","w")or die("Unable to open file!");
if(file_put_contents("C:/xampp/htdocs/Users/$email/code.txt",$code)==TRUE){
    echo "stock_added_goto_generate_qr_code";
  }
}
?>