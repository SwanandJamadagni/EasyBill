package sj_infotech.easybill;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Add_Uncoded_Product extends AppCompatActivity {
    EditText et_description,et_prize,et_quantity;
    String description,prize,quantity,ip,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__uncoded__product);
        et_description = (EditText)findViewById(R.id.description);
        et_prize = (EditText)findViewById(R.id.prize);
        et_quantity = (EditText)findViewById(R.id.quantity);
    }

    public void onaddstock(View view){
        description = et_description.getText().toString();
        prize = et_prize.getText().toString();
        quantity = et_quantity.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        type = "add_stock_uncoded_product";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, description, prize, quantity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__uncoded__product, menu);
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
