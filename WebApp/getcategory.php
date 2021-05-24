<?php
require "conn.php";
?>
<?php
$request = $_POST["request"];
$category = '';
        $select = "SELECT category
                   FROM category";
        $result = $conn->query($select);
        if ($result->rowCount() > 0){
            while($data = $result->fetch(PDO::FETCH_ASSOC)) {
                $category = $category."&".$data["category"];
            }
        } 
echo "$category";
?>
