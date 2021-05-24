package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class cash_register_bill extends AppCompatActivity {
    String ip, email, discount;
    TableLayout tableLayout;
    String[] itemlist;
    int total = 0;
    String[] items;
    private Bitmap bitmap;
    private LinearLayout pdf;
    String toNumber, DownloadUrl;
    Task<Uri> url;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_register_bill);

        firebaseStorage = FirebaseStorage.getInstance();

        SharedPreferences sharedPreferences1 = getSharedPreferences("phoneinfo", Context.MODE_PRIVATE);
        toNumber = sharedPreferences1.getString("phonenum", "");

        Intent callingIntent= getIntent();
        itemlist = (String[]) callingIntent.getSerializableExtra("Items");
        discount = (String) callingIntent.getSerializableExtra("Discount");


        pdf = (LinearLayout) findViewById(R.id.manualprintbilllayout);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        email = sharedPreferences.getString("email", "");

        tableLayout = (TableLayout) findViewById(R.id.table);

        TableRow tr = new TableRow(this);
        TextView tv = new TextView(this);
        tv.setText("Easy Bill");
        tv.setTextSize(30);
        tv.setTextColor(Color.BLACK);
        tv.setGravity(Gravity.CENTER);
        tr.addView(tv);
        tr.setGravity(Gravity.CENTER);
        tableLayout.addView(tr);

        TableRow tr1 = new TableRow(this);
        TextView tv1 = new TextView(this);
        tv1.setText("Description");
        tv1.setGravity(Gravity.LEFT);
        tr1.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Quantity");
        tv2.setGravity(Gravity.CENTER);
        tr1.addView(tv2);
        TextView tve = new TextView(this);
        tve.setText("   ");
        tve.setGravity(Gravity.RIGHT);
        tr1.addView(tve);
        TextView tv3 = new TextView(this);
        tv3.setText("Prize");
        tv3.setGravity(Gravity.RIGHT);
        tr1.addView(tv3);
        tr1.setGravity(Gravity.CENTER);
        tableLayout.addView(tr1);

        for(int i = 0; i < itemlist.length; i++) {


            items = itemlist[i].split(":");
            TableRow tr2 = new TableRow(this);
            TextView tv4 = new TextView(this);
            tv4.setText(items[0]);
            tv4.setGravity(Gravity.LEFT);
            tr2.addView(tv4);
            TextView tv5 = new TextView(this);
            tv5.setText(items[2]);
            tv5.setGravity(Gravity.CENTER);
            tr2.addView(tv5);
            TextView tve1 = new TextView(this);
            tve1.setText("   ");
            tve1.setGravity(Gravity.RIGHT);
            tr2.addView(tve1);
            TextView tv6 = new TextView(this);
            tv6.setText(items[1]);
            tv6.setGravity(Gravity.RIGHT);
            tr2.addView(tv6);
            tr2.setGravity(Gravity.CENTER);
            tableLayout.addView(tr2);
            total = total + (Integer.parseInt(items[1]) * Integer.parseInt(items[2]));
        }


        TableRow trextra1 = new TableRow(this);
        trextra1.setGravity(Gravity.CENTER);
        tableLayout.addView(trextra1);
        TableRow trextra2 = new TableRow(this);
        trextra2.setGravity(Gravity.CENTER);
        tableLayout.addView(trextra2);

        TableRow tr3 = new TableRow(this);
        TextView tvtotal = new TextView(this);
        tvtotal.setText("Total");
        tvtotal.setGravity(Gravity.LEFT);
        tr3.addView(tvtotal);
        TextView tvextra = new TextView(this);
        tvextra.setText("---");
        tvextra.setGravity(Gravity.CENTER);
        tr3.addView(tvextra);
        TextView tvspace = new TextView(this);
        tvspace.setText("   ");
        tvspace.setGravity(Gravity.RIGHT);
        tr3.addView(tvspace);
        TextView tvamount = new TextView(this);
        tvamount.setText(Integer.toString(total));
        tvamount.setGravity(Gravity.RIGHT);
        tr3.addView(tvamount);
        tr3.setGravity(Gravity.CENTER);
        tableLayout.addView(tr3);

        TableRow tr4 = new TableRow(this);
        TextView tvdiscount = new TextView(this);
        tvdiscount.setText("Discount");
        tvdiscount.setGravity(Gravity.LEFT);
        tr4.addView(tvdiscount);
        TextView tvextra1 = new TextView(this);
        tvextra1.setText("---");
        tvextra1.setGravity(Gravity.CENTER);
        tr4.addView(tvextra1);
        TextView tvspace1 = new TextView(this);
        tvspace1.setText("   ");
        tvspace1.setGravity(Gravity.RIGHT);
        tr4.addView(tvspace1);
        TextView tvdiscountamount = new TextView(this);
        tvdiscountamount.setText(discount);
        tvdiscountamount.setGravity(Gravity.RIGHT);
        tr4.addView(tvdiscountamount);
        tr4.setGravity(Gravity.CENTER);
        tableLayout.addView(tr4);

        int payamount = total - Integer.parseInt(discount);

        TableRow tr5 = new TableRow(this);
        TextView tvpayment = new TextView(this);
        tvpayment.setText("Amoount Payable");
        tvpayment.setGravity(Gravity.LEFT);
        tr5.addView(tvpayment);
        TextView tvextra2 = new TextView(this);
        tvextra2.setText("---");
        tvextra2.setGravity(Gravity.CENTER);
        tr5.addView(tvextra2);
        TextView tvspace2 = new TextView(this);
        tvspace2.setText("   ");
        tvspace2.setGravity(Gravity.RIGHT);
        tr5.addView(tvspace2);
        TextView tvpayamount = new TextView(this);
        tvpayamount.setText(Integer.toString(payamount));
        tvpayamount.setGravity(Gravity.RIGHT);
        tr5.addView(tvpayamount);
        tr5.setGravity(Gravity.CENTER);
        tableLayout.addView(tr5);
    }


    public void onpdf(View view){
        /*String email = emailet.getText().toString();
        createPdf(email);*/
        bitmap = loadBitmapFromView(pdf, 1200, 1600);
        createPdf();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 1600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);



        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyBill/CashReg/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"bill_"+toNumber+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            UploadPdf();
        } catch (IOException e) {
            Toast.makeText(this, "Something Went Wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    public void UploadPdf(){

        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsolutePath()+"/EasyBill/CashReg");
        File pdfFile = new File(Dir+"/"+"bill_"+toNumber+".pdf");
        Uri file = Uri.fromFile(pdfFile);

        final StorageReference storageReference = firebaseStorage.getReference();// Root path for the FireBase Storage
        final StorageReference sref = storageReference.child("Store_Demo/Cash_Register/"+file.getLastPathSegment());
        sref.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        url = taskSnapshot.getStorage().getDownloadUrl();
                        url.addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                try {
                                    DownloadUrl = URLEncoder.encode(DownloadUrl = task.getResult().toString(),"UTF-8");
                                    Toast.makeText(cash_register_bill.this, "PDF Generated", Toast.LENGTH_LONG).show();
                                } catch (UnsupportedEncodingException e) {
                                    Toast.makeText(cash_register_bill.this, "Error Generating The PDF: " + e.toString(),  Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(cash_register_bill.this, "Error Uploading The File", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void onClickWhatsApp(View view) {
        try {

            PackageManager pm=getPackageManager();
            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

            String phonenumber = "+91"+toNumber;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phonenumber+"&text="+DownloadUrl));
            startActivity(intent);


        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }
}
