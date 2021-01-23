package sj_infotech.easybill;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class bill extends AppCompatActivity {
    Button genbill;
    List<String> barcodes;
    ArrayAdapter<String> adapter;
    String[] scan;
    String[] item;
    int[] prize;
    String scandata = "";
    EditText discountet, totalet, phoneno;
    int total = 0;
    String phonenumber, ip, type, email, discount;
    AlertDialog alertdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        genbill = (Button) findViewById(R.id.gen_bill);
        genbill.setEnabled(false);
        discountet = (EditText)findViewById(R.id.discount);
        phoneno = (EditText)findViewById(R.id.phoneno);
        totalet = (EditText)findViewById(R.id.total);
        Intent callingIntent= getIntent();
        barcodes = (List<String>) callingIntent.getSerializableExtra("Barcodes");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, barcodes);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                barcodes.remove(i);
                adapter.notifyDataSetChanged();
                prize = new int[adapter.getCount()];
                for (int k = 0; k < adapter.getCount(); k++) {
                    item = adapter.getItem(k).split(":");
                    prize[k] = (Integer.parseInt(item[2])*Integer.parseInt(item[3]));
                }
                total = 0;
                for (int j = 0; j < prize.length; j++) {
                    total = total + prize[j];
                }

                totalet.setText(Integer.toString(total));
                return false;
            }
        });
        prize = new int[adapter.getCount()];
        for (int k = 0; k < adapter.getCount(); k++) {
            item = adapter.getItem(k).split(":");
            prize[k] = (Integer.parseInt(item[2])*Integer.parseInt(item[3]));
        }
        for (int j = 0; j < prize.length; j++) {
            total = total + prize[j];
        }
        totalet.setText(Integer.toString(total));
    }



    public void ondone(View view){
        discount = discountet.getText().toString();
        phonenumber = phoneno.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        email = sharedPreferences.getString("email", "");

        SharedPreferences sharedPreferences1 = getSharedPreferences("phoneinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString("phonenum", phonenumber);
        editor.apply();
        genbill.setEnabled(true);
        type = "done";
        scan = new String[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++){
            scan[i] = adapter.getItem(i);
            scandata += scan[i].concat("&");
        }
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, scandata, discount, phonenumber, email);

    }

    private void generatebill() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(bill.this, printbill.class);
                intent.putExtra("Scan", (Serializable) scan);
                intent.putExtra("Discount", (Serializable) discount);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public void ongenbill(View view){
        /*SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        String user_email = sharedPreferences.getString("email", "");
        String action = "print";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, action, user_email);*/

        ProgressDialog progress;
        progress = new ProgressDialog(bill.this);
        progress.setMessage("Generating Bill");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        generatebill();

        //Intent intent = new Intent(bill.this, printbill.class);
        //startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){

            case R.id.action_logout:
                String log = "0";
                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password", "0");
                editor.apply();
                Intent intent = new Intent(bill.this, login.class);
                finish();
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
