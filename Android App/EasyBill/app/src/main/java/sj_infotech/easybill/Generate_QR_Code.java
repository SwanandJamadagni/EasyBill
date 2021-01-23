package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class Generate_QR_Code extends AppCompatActivity {
    EditText et_description,et_prize,et_quantity;
    String category,description,prize,quantity,ip,type,email,code,crc32code;
    String[] categorylist;
    Spinner myspinner;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate__qr__code);
        myspinner = (Spinner) findViewById(R.id.spinner);
        Intent callingIntent= getIntent();
        categorylist = (String[]) callingIntent.getSerializableExtra("Category");
        categorylist[0] = "Select Category";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Generate_QR_Code.this, android.R.layout.simple_list_item_1,categorylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(adapter);
        myspinner.setSelection(0);
        et_description = (EditText)findViewById(R.id.description);
        et_prize = (EditText)findViewById(R.id.prize);
        et_quantity = (EditText)findViewById(R.id.quantity);
    }

    public void onaddstock(View view){
        category = myspinner.getSelectedItem().toString();
        category = myspinner.getSelectedItem().toString();
        description = et_description.getText().toString();
        prize = et_prize.getText().toString();
        quantity = et_quantity.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        ip = sharedPreferences.getString("ip", "");
        type = "add_stock_qr";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, category, description, prize, quantity, email);
    }

    public void onaddproduct(View view){
        category = myspinner.getSelectedItem().toString();
        category = myspinner.getSelectedItem().toString();
        description = et_description.getText().toString();
        prize = et_prize.getText().toString();
        quantity = et_quantity.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        type = "add_product_qr";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, category, description, prize, quantity);
    }

    public void ongenqrcode(View view){
        category = myspinner.getSelectedItem().toString();
        description = et_description.getText().toString();
        prize = et_prize.getText().toString();
        quantity = et_quantity.getText().toString();
        code = category.concat(":").concat(description).concat(":").concat(prize);
        byte bytes[] = code.getBytes();
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        long checksum = crc32.getValue();
        crc32code = Long.toString(checksum);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(crc32code, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            createPdf();

        }
        catch (WriterException e){
            e.printStackTrace();
        }


        /*Intent intent = new Intent(Generate_QR_Code.this, Print_QR_Code.class);
        finish();
        startActivity(intent);*/
    }

    private void createPdf(){
        /*WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
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
        document.finishPage(page);*/



        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyBill/QRCode/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"qrcode.jpg";
        File filePath = new File(targetPdf);
        try {
            //document.writeTo(new FileOutputStream(filePath));
            FileOutputStream out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            out.flush();
            out.close();
            Intent intent = new Intent(Generate_QR_Code.this, Print_QR_Code.class);
            startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        //document.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generate__qr__code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
