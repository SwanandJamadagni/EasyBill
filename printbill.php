<?php
require "C:/xampp/htdocs/conn.php";
require "C:/xampp/htdocs/fpdf.php";
?>
<?php
$host= gethostname();
$ip = gethostbyname($host);
?>
<?php
$phoneinfo = fopen("phoneinfo.txt", "r") or die("Unable to open file!");
$phoneno =  fread($phoneinfo,filesize("phoneinfo.txt"));
$emailinfo = fopen("email.txt", "r") or die("Unable to open file!");
$email =  fread($emailinfo,filesize("email.txt"));
$select = "SELECT billno, details, discount
           FROM sellbills
           WHERE phonenumber = '$phoneno'
           ORDER BY billno DESC
           LIMIT 1";
           $result = $conn->query($select);
           if ($result->rowCount() > 0) {
           while($data = $result->fetch(PDO::FETCH_ASSOC)) {
                $scandata = $data["details"];
                $billno = $data["billno"];
                $discount = $data["discount"];
            }
           }
$data = explode("&",$scandata);
$datalength = count($data);
?>
<?php
date_default_timezone_set("Asia/Kolkata");
$date = date("Y/m/d");
$time = date("h:i:sa");
$dts = date("Ymd").date("His");
$bill = 'C:/xampp/htdocs/Users/'.$email.'/bills/bill.pdf';
$pdf_file = 'C:/xampp/htdocs/bills/'.$phoneno.'_'.$dts.'.pdf';
$pdf = new FPDF('P','mm','A4');
$pdf->AliasNbPages();
$pdf ->AddPage();
$pdf ->SetFont('Arial','B','20');
$pdf ->Cell(188, 10, ' Easy Bill ', 1, 1, 'C');
$pdf ->SetFont('Arial','','14');
$pdf ->Cell(188, 10, 'Address: SF-2 Vitthal complex Rajarampuri 9th lane Kolhapur Ph. 0231-2525200', 1, 1, 'C');
$pdf ->Cell(47, 8, $date, 1, 0, 'C');
$pdf ->Cell(47, 8, '', 0, 0, 'C');
$pdf ->Cell(47, 8, 'Contact No: ', 1, 0, 'C');
$pdf ->Cell(47, 8, '9421217690', 1, 1, 'C');
$pdf ->Cell(47, 8, $time, 1, 0, 'C');
$pdf ->Cell(47, 8, '', 0, 0, 'C');
$pdf ->Cell(47, 8, 'Invoce No: ', 1, 0, 'C');
$pdf ->Cell(47, 8, $billno, 1, 1, 'C');
$pdf ->Cell(100, 8, 'Description                                        Quantity', 1, 0, 'L');
//$pdf ->Cell(20, 8, 'Quantity', 1, 0, 'L');
$pdf ->Cell(88, 8, 'Amount', 1, 1, 'C');

$sum = 0;
for($i = 0; $i < $datalength-1; $i++){
    $item = explode(":",$data[$i]);
            $description = $item[0].'-'.$item[1].'                              '.$item[3];
            $prize = $item[2];
            //$quantity = $item[3];
            $pdf ->Cell(100, 8, $description, 1, 0, 'L');
            //$pdf ->Cell(20, 8, $quantity, 1, 0, 'C');
            $pdf ->Cell(88, 8, $prize, 1, 1, 'C');
            $sum = $sum + $item[2]*$item[3];
}
$pdf ->Cell(100, 8, 'Total', 1, 0, 'C');
$pdf ->Cell(88, 8, $sum, 1, 1, 'C');
$pdf ->Cell(100, 8, 'Discount', 1, 0, 'C');
$pdf ->Cell(88, 8, $discount, 1, 1, 'C');
$amount_payable = $sum - $discount;
$pdf ->Cell(100, 8, 'Amount Payable', 1, 0, 'C');
$pdf ->Cell(88, 8, $amount_payable, 1, 1, 'C');
$pdf ->Output($bill, 'F');
$pdf ->Output($pdf_file, 'F');
if(file_exists($pdf_file)==TRUE){
    if(file_exists($bill)==TRUE){
        header('location: http://'.$ip.'/Users/'.$email.'/bills/bill.pdf');
    }
}
else{
    echo "error";
}
//echo "bill generated";
//$pdf ->Output();
?>
