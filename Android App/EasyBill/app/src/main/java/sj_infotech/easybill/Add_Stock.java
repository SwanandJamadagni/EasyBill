package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

public class Add_Stock extends AppCompatActivity {
    EditText et_product_id,et_description,et_prize,et_quantity;
    String product_id,category,description,prize,quantity,ip,type;
    String[] categorylist;
    Spinner myspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__stock);
        myspinner = (Spinner) findViewById(R.id.spinner2);
        Intent callingIntent= getIntent();
        categorylist = (String[]) callingIntent.getSerializableExtra("Category");
        categorylist[0] = "Select Category";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_Stock.this, android.R.layout.simple_list_item_1,categorylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(adapter);
        myspinner.setSelection(0);
        et_product_id = (EditText)findViewById(R.id.product_id);
        et_description = (EditText)findViewById(R.id.description);
        et_prize = (EditText)findViewById(R.id.prize);
        et_quantity = (EditText)findViewById(R.id.quantity);
        SharedPreferences sharedPreferences_product = getSharedPreferences("product", Context.MODE_PRIVATE);
        product_id = sharedPreferences_product.getString("product_id", "");
        et_product_id.setText(product_id);

    }

    public void onaddstock(View view){
        category = myspinner.getSelectedItem().toString();
        description = et_description.getText().toString();
        prize = et_prize.getText().toString();
        quantity = et_quantity.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        type = "add_stock";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, product_id, category, description, prize, quantity);
    }

    public void onaddproduct(View view){
        category = myspinner.getSelectedItem().toString();
        description = et_description.getText().toString();
        prize = et_prize.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        type = "add_product";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, product_id, category, description, prize);
    }

    public void onBackPressed()
    {
        //do whatever you want the 'Back' button to do
        //as an example the 'Back' button is set to start a new Activity named 'NewActivity'
        Intent intent = new Intent(Add_Stock.this, home.class);
        finish();
        startActivity(intent);

        return;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__stock, menu);
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
                Intent intent = new Intent(Add_Stock.this, login.class);
                finish();
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
