package sj_infotech.easybill;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Manual_Bill extends AppCompatActivity {
    Button genbill;
    EditText et_quantity,et_discount,et_total;
    String description,product,quantity,discount,ip,type,email;
    int[] price;
    String[] productlist;
    Spinner myspinner;
    List<String> itemlist = new ArrayList<String>();
    String[] items;
    String[] item;
    String itemdata = "";
    ArrayAdapter<String> itemlistadapter;
    ListView listView;
    int total = 0;
    private Bitmap bitmap;
    private LinearLayout pdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual__bill);
        genbill = (Button) findViewById(R.id.genbill);
        genbill.setEnabled(false);
        myspinner = (Spinner) findViewById(R.id.spinner3);
        Intent callingIntent= getIntent();
        productlist = (String[]) callingIntent.getSerializableExtra("Productlist");
        productlist[0] = "Select Product";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Manual_Bill.this, android.R.layout.simple_list_item_1,productlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(adapter);
        myspinner.setSelection(0);
        et_quantity = (EditText)findViewById(R.id.quantity);
        et_discount = (EditText)findViewById(R.id.discount);
        et_total = (EditText)findViewById(R.id.total);
        itemlistadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemlist);
        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(itemlistadapter);
        pdf = (LinearLayout) findViewById(R.id.manualbill_layout);
    }

    public void onadditem(View view){
        product = myspinner.getSelectedItem().toString();
        quantity = et_quantity.getText().toString();
        description = product.concat(":").concat(quantity);
        itemlist.add(description);
        itemlistadapter.notifyDataSetChanged();
        listView.setAdapter(itemlistadapter);
        price = new int[itemlistadapter.getCount()];
        for (int k = 0; k < itemlistadapter.getCount(); k++) {
            item = itemlistadapter.getItem(k).split(":");
            price[k] = (Integer.parseInt(item[1])*Integer.parseInt(item[2]));
        }
        total = 0;
        for (int j = 0; j < price.length; j++) {
            total = total + price[j];
        }
        et_total.setText(Integer.toString(total));
        et_quantity.setText("");
    }

    public void ondonemanualbill(View view){
        discount = et_discount.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        email = sharedPreferences.getString("email", "");

        genbill.setEnabled(true);

        type = "manual_bill_done";
        items = new String[itemlistadapter.getCount()];
        for (int i = 0; i < itemlistadapter.getCount(); i++){
            items[i] = itemlistadapter.getItem(i);
            itemdata += items[i].concat("&");
        }

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, itemdata, discount, email);

    }

    private void generatebill() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(Manual_Bill.this, manual_printbill.class);
                intent.putExtra("Items", (Serializable) items);
                intent.putExtra("Discount",(Serializable) discount);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public void ongenmanualbill(View view){

        ProgressDialog progress;
        progress = new ProgressDialog(Manual_Bill.this);
        progress.setMessage("Generating Bill");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        generatebill();

        //Intent intent = new Intent(Manual_Bill.this, manual_printbill.class);
        //finish();
        //startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual__bill, menu);
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
