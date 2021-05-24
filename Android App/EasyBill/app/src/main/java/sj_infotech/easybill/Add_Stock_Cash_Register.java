package sj_infotech.easybill;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Add_Stock_Cash_Register extends AppCompatActivity {
    EditText et_description, et_prize,et_quantity;
    String description,prize,quantity,FS_Quantity;
    FirebaseFirestore FireStoreDB;
    String items;
    String [] itemlist;
    Spinner myspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__stock__cash_register);

        FireStoreDB = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences_items = getSharedPreferences("Cash_Register_Info", Context.MODE_PRIVATE);
        items = sharedPreferences_items.getString("items", "");
        itemlist = items.split(", ");

        myspinner = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_Stock_Cash_Register.this, android.R.layout.simple_list_item_1,itemlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(adapter);
        myspinner.setSelection(0);

        et_description = (EditText) findViewById(R.id.description);
        et_prize = (EditText)findViewById(R.id.prize);
        et_quantity = (EditText)findViewById(R.id.quantity);

        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    et_description.setText("");
                }
                else {
                    et_description.setText(parent.getItemAtPosition(position).toString());
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void add_stock(){

        //Toast.makeText(Add_Stock_Cash_Register.this, description+"-"+prize+"-"+quantity, Toast.LENGTH_LONG).show();

        Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/Cash_Register_Products/"+description)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            FS_Quantity = documentSnapshot.get("Quantity").toString();
                            int Updated_Quantity = (Integer.parseInt(FS_Quantity) + Integer.parseInt(quantity));

                            Map<String, Object> product = new HashMap<>();
                            product.put("Prize", prize);
                            product.put("Quantity", Updated_Quantity);

                            try {
                                FireStoreDB.collection("/Billing_App/Store_Demo/Cash_Register_Products/").document(description).set(product);
                                Toast.makeText(Add_Stock_Cash_Register.this, "Stock_Added_Successfully", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e){
                                Toast.makeText(Add_Stock_Cash_Register.this, "Error_Adding_Stock", Toast.LENGTH_LONG).show();
                            }
                        }

                        else {

                            Map<String, Object> product = new HashMap<>();
                            product.put("Prize", prize);
                            product.put("Quantity", quantity);

                            try {
                                FireStoreDB.collection("/Billing_App/Store_Demo/Cash_Register_Products/").document(description).set(product);
                                Toast.makeText(Add_Stock_Cash_Register.this, "Stock_Added_Successfully", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e){
                                Toast.makeText(Add_Stock_Cash_Register.this, "Error_Adding_Stock", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_Stock_Cash_Register.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void onaddstock(View view){
        description = et_description.getText().toString();
        prize = et_prize.getText().toString();
        quantity = et_quantity.getText().toString();
        add_stock();


        /*SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        type = "add_stock_uncoded_product";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, description, prize, quantity);*/
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
