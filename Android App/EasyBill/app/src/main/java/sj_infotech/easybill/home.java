package sj_infotech.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class home extends AppCompatActivity{
    String ip,type,admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent callingIntent= getIntent();
        admin = (String) callingIntent.getSerializableExtra("Admin");
    }

    public void onscan(View view){
        Intent intent = new Intent(home.this, Scan_Product.class);
        startActivity(intent);
    }

    public void ongenbillmanually(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        type = "getproducts_manual_stock";
        GetProducts getProducts = new GetProducts(this);
        getProducts.execute(ip, type);
        //Intent intent = new Intent(home.this, Scan_Product.class);
        //startActivity(intent);
    }

    public void onadmin(View view){
        if (admin.equals("1")) {
            Intent intent = new Intent(home.this, Admin.class);
            startActivity(intent);
            //Toast.makeText(home.this,admin,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(home.this,"User Does Not Have Admin Rights",Toast.LENGTH_LONG).show();
            //Toast.makeText(home.this,admin,Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

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
                Intent intent = new Intent(home.this, login.class);
                finish();
                startActivity(intent);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}

