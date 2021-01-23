<?php
require "conn.php";
?>
<?php
$f = 0;
$description = $_POST["description"];
$prize = $_POST["prize"];
$quantity = $_POST["quantity"];
$select = "SELECT description
           FROM manual_stock
           WHERE description = '$description' AND prize = '$prize'";
        $result = $conn->query($select);
        if ($result->rowCount() > 0){
            $select_stock = "SELECT quantity
                             FROM manual_stock
                             WHERE description = '$description' AND prize = '$prize'";
            $stock_result = $conn->query($select_stock);
            if($stock_result->rowCount() > 0){
                            while($data = $stock_result->fetch(PDO::FETCH_ASSOC)) {
                                $old_quantity = $data["quantity"];
                            }
                $new_quantity = $quantity + $old_quantity;
                $update = "UPDATE manual_stock SET quantity = '$new_quantity' 
                           WHERE description = '$description' AND prize = '$prize'";
                if($conn->query($update)==TRUE){
                    $f = 1;
                }
            }
        }
        else{
            $insert = "INSERT INTO manual_stock (description, prize, quantity)
                       VALUES ('$description', '$prize', '$quantity')";
            if($conn->query($insert)==TRUE){
               $f = 1;
            }
         }
if($f==1){
    echo "manual_stock_updated";
}
?>