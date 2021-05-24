<?php
require "conn.php";
?>
<?php
$f = 0;
$category = $_POST["category"];
$gst = $_POST["gst"];
$select = "SELECT category
           FROM category
           WHERE category = '$category'";
        $result = $conn->query($select);
        if ($result->rowCount() > 0){
            $update = "UPDATE category SET gst = '$gst' 
                       WHERE category = '$category'";
            if($conn->query($update)==TRUE){
                $f = 1;
            }
        }
        else{
            $insert = "INSERT INTO category (category, gst)
                       VALUES ('$category', '$gst')";
           if($conn->query($insert)==TRUE){
              $f = 1;
            }
        }
        if($f==1){
            echo "category_added";
        }
?>