package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    String ip,type,admin;
    AdView mAdView;
    String mAppUnitId = "ca-app-pub-6634727612331345~3949039676";
    FirebaseFirestore FireStoreDB;
    List<String> itemlist = new ArrayList<>();
    String items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FireStoreDB = FirebaseFirestore.getInstance();

        mAdView = (AdView) findViewById(R.id.adView);
        MobileAds.initialize(this, mAppUnitId);
        //initializeBannerAd(mAppUnitId);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //loadBannerAd();

        //Intent callingIntent= getIntent();
        //admin = (String) callingIntent.getSerializableExtra("Admin");
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        admin = sharedPreferences.getString("admin", "");

        get_cash_register_items();
    }

    public void get_cash_register_items(){
        FireStoreDB.collection("/Billing_App/Store_Demo/Cash_Register_Products/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    itemlist = new ArrayList<>();
                    itemlist.add("Select Item");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        itemlist.add(document.getId());
                    }
                    items = itemlist.toString().replace("[","").replace("]","");
                } else {
                    items = "No Items Found";
                }
                SharedPreferences sharedPreferences = getSharedPreferences("Cash_Register_Info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("items", items);
                editor.apply();
            }
        });
    }
    public void onscan(View view){

        Intent intent = new Intent(home.this, Scan_Product.class);
        startActivity(intent);
    }

    public void ongenbillmanually(View view){

        Intent intent = new Intent(home.this, Cash_Register.class);
        startActivity(intent);


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

    public void initializeBannerAd(String appUnitId) {

        MobileAds.initialize(this, appUnitId);

    }

    public void loadBannerAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("log", "0");
                editor.apply();
                Intent intent = new Intent(home.this, login.class);
                finish();
                startActivity(intent);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}

