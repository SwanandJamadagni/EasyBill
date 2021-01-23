package sj_infotech.easybill;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class printbill extends AppCompatActivity {
        String ip, email, discount;
        AlertDialog alertdialog;
        private Bitmap bitmap;
        private LinearLayout pdf;
        TableLayout tableLayout;
        String[] scanlist;
        int total = 0;
        String[] scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printbill);


        Intent callingIntent= getIntent();
        scanlist = (String[]) callingIntent.getSerializableExtra("Scan");
        discount = (String) callingIntent.getSerializableExtra("Discount");


        pdf = (LinearLayout) findViewById(R.id.printbilllayout);



        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        email = sharedPreferences.getString("email", "");

        tableLayout = (TableLayout) findViewById(R.id.table1);

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

        for(int i = 0; i < scanlist.length; i++) {


            scan = scanlist[i].split(":");
            TableRow tr2 = new TableRow(this);
            TextView tv4 = new TextView(this);
            tv4.setText(scan[1]);
            tv4.setGravity(Gravity.LEFT);
            tr2.addView(tv4);
            TextView tv5 = new TextView(this);
            tv5.setText(scan[3]);
            tv5.setGravity(Gravity.CENTER);
            tr2.addView(tv5);
            TextView tve1 = new TextView(this);
            tve1.setText("   ");
            tve1.setGravity(Gravity.RIGHT);
            tr2.addView(tve1);
            TextView tv6 = new TextView(this);
            tv6.setText(scan[2]);
            tv6.setGravity(Gravity.RIGHT);
            tr2.addView(tv6);
            tr2.setGravity(Gravity.CENTER);
            tableLayout.addView(tr2);
            total = total + (Integer.parseInt(scan[2]) * Integer.parseInt(scan[3]));
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
        bitmap = loadBitmapFromView(pdf, pdf.getWidth(), pdf.getHeight());
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
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
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
        String directory_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyBill/Bills/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"bill.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            viewPdf("bill.pdf");
        } catch (IOException e) {
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    private void viewPdf(String file) {

        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsolutePath()+"/EasyBill/Bills");
        File pdfFile = new File(Dir+"/"+file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        startActivity(pdfIntent);
    }


}
