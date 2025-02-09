package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class Add_Stock extends AppCompatActivity {
    EditText et_product_id,et_description, et_category,et_prize,et_quantity;
    String product_id,category,description,prize,quantity,FS_Quantity;
    FirebaseFirestore FireStoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__stock);

        FireStoreDB = FirebaseFirestore.getInstance();

        et_product_id = (EditText)findViewById(R.id.product_id);
        et_category = (EditText)findViewById(R.id.category);
        et_description = (EditText)findViewById(R.id.description);
        et_prize = (EditText)findViewById(R.id.prize);
        et_quantity = (EditText)findViewById(R.id.quantity);
        SharedPreferences sharedPreferences_product = getSharedPreferences("product", Context.MODE_PRIVATE);
        product_id = sharedPreferences_product.getString("product_id", "");
        category = sharedPreferences_product.getString("category", "");
        description = sharedPreferences_product.getString("description", "");
        prize = sharedPreferences_product.getString("prize", "");
        quantity = sharedPreferences_product.getString("quantity", "");
        et_product_id.setText(product_id);
        et_category.setText(category);
        et_description.setText(description);
        et_prize.setText(prize);
        et_quantity.setText(quantity);



    }

    public void add_stock(){

        //Toast.makeText(Add_Stock_Cash_Register.this, description+"-"+prize+"-"+quantity, Toast.LENGTH_LONG).show();

        Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/EPOS_Products/"+product_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            FS_Quantity = documentSnapshot.get("Quantity").toString();
                            int Updated_Quantity = (Integer.parseInt(FS_Quantity) + Integer.parseInt(quantity));

                            Map<String, Object> product = new HashMap<>();
                            product.put("Category", category);
                            product.put("Description", description);
                            product.put("Prize", prize);
                            product.put("Quantity", Updated_Quantity);

                            try {
                                FireStoreDB.collection("/Billing_App/Store_Demo/EPOS_Products/").document(product_id).set(product);
                                Toast.makeText(Add_Stock.this, "Stock_Added_Successfully", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e){
                                Toast.makeText(Add_Stock.this, "Error_Adding_Stock", Toast.LENGTH_LONG).show();
                            }
                        }

                        else {

                            Map<String, Object> product = new HashMap<>();
                            product.put("Category", category);
                            product.put("Description", description);
                            product.put("Prize", prize);
                            product.put("Quantity", quantity);

                            try {
                                FireStoreDB.collection("/Billing_App/Store_Demo/EPOS_Products/").document(product_id).set(product);
                                Toast.makeText(Add_Stock.this, "Stock_Added_Successfully", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e){
                                Toast.makeText(Add_Stock.this, "Error_Adding_Stock", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_Stock.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void onaddstock(View view){
        category = et_category.getText().toString();
        description = et_description.getText().toString();
        prize = et_prize.getText().toString();
        quantity = et_quantity.getText().toString();
        add_stock();


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
