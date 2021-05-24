package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {
    String ip,type;
    AdView mAdView1;
    String mAppUnitId = "ca-app-pub-6634727612331345~3949039676";
    FirebaseFirestore FireStoreDB;
    List<String> itemlist = new ArrayList<>();
    String items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FireStoreDB = FirebaseFirestore.getInstance();

        mAdView1 = (AdView) findViewById(R.id.adView1);
        MobileAds.initialize(this, mAppUnitId);
        //initializeBannerAd(mAppUnitId);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);
        //loadBannerAd();

    }

    public void onadd(View view){
        Intent intent = new Intent(Admin.this, Add_Product.class);
        startActivity(intent);
    }

    public void ongen(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        type = "getcategory";
        GetCategory getCategory = new GetCategory(this);
        getCategory.execute(ip, type);
        //Intent intent = new Intent(home.this, Generate_QR_Code.class);
        //startActivity(intent);
    }

    public void onadduncoded(View view){

        Intent intent = new Intent(Admin.this, Add_Stock_Cash_Register.class);
        startActivity(intent);

    }

    public void gotofirestore(View view){
        Intent intent = new Intent(Admin.this, FireStore_Demo.class);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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
