package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Admin extends AppCompatActivity {
    String ip,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
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

    public void onaddcategory(View view){
        Intent intent = new Intent(Admin.this, Add_Category.class);
        startActivity(intent);
    }

    public void onadduncoded(View view){
        Intent intent = new Intent(Admin.this, Add_Uncoded_Product.class);
        startActivity(intent);
    }

    public void onsqlite(View view){
        Intent intent = new Intent(Admin.this, SQLite.class);
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
