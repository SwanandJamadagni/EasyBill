<?php
require "conn.php";
?>
<?php
$scandata = $_POST["scandata"];
//$scandata = '-1434382349:2';
$products = '';
$productdetails = explode("&",$scandata);
for($j = 0; $j < count($productdetails); $j++){
    $productdata = explode(":",$productdetails[$j]);
    $datalength = count($productdata);
    for($i = 0; $i < $datalength; $i++){
        $select = "SELECT category, description, prize
                   FROM products
                   WHERE code = '$productdata[$i]'";
        $result = $conn->query($select);
        if ($result->rowCount() > 0){
            while($data = $result->fetch(PDO::FETCH_ASSOC)) {
                $products = $products."&".$data["category"].":".$data["description"].":".$data["prize"].":".$productdata[$i+1];
            }
        }
        $i++;
    }
}
echo "$products";
?>
