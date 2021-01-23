<?php
require "conn.php";
?>
<?php
$request = $_POST["request"];
$products = '';
$select = "SELECT description, price
           FROM manual_stock";
$result = $conn->query($select);
if ($result->rowCount() > 0){
    while($data = $result->fetch(PDO::FETCH_ASSOC)) {
        $products = $products."&".$data["description"].":".$data["price"];
    }
} 
echo "$products";
?>