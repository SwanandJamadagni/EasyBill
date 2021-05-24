package sj_infotech.easybill;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Cash_Register extends AppCompatActivity {
    EditText et_quantity,et_discount,et_phonenumber,et_total;
    String product,quantity,description,discount,phonenumber,r_item,r_prize,r_quantity;
    String Products;
    String[] productlist;
    Spinner myspinner;
    List<String> itemlist = new ArrayList<String>();
    String[] items;
    ArrayAdapter<String> itemlistadapter;
    ListView listView;
    int FS_Price,FS_Quantity,Updated_FS_Quantity;
    private Bitmap bitmap;
    private LinearLayout pdf;
    FirebaseFirestore FireStoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_register);

        FireStoreDB = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences_items = getSharedPreferences("Cash_Register_Info", Context.MODE_PRIVATE);
        Products = sharedPreferences_items.getString("items", "");
        productlist = Products.split(", ");

        myspinner = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Cash_Register.this, android.R.layout.simple_list_item_1,productlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(adapter);
        myspinner.setSelection(0);

        et_quantity = (EditText)findViewById(R.id.quantity);
        et_discount = (EditText)findViewById(R.id.discount);
        et_phonenumber = (EditText)findViewById(R.id.phoneno);
        et_total = (EditText)findViewById(R.id.total);
        et_total.setText("0");

        itemlistadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemlist);
        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(itemlistadapter);
        pdf = (LinearLayout) findViewById(R.id.manualbill_layout);

        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product = parent.getItemAtPosition(position).toString();
                Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/Cash_Register_Products/"+product)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    FS_Price = Integer.parseInt(documentSnapshot.get("Prize").toString());
                                    FS_Quantity = Integer.parseInt(documentSnapshot.get("Quantity").toString());

                                } else {
                                    //Toast.makeText(Cash_Register.this, " Does Not Exist", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Cash_Register.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                            }
                        });
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                r_item = itemlist.get(i).split(":")[0];
                r_prize = itemlist.get(i).split(":")[1];
                r_quantity = itemlist.get(i).split(":")[2];
                et_total.setText(Integer.toString(Integer.parseInt(et_total.getText().toString()) - Integer.parseInt(r_prize) * Integer.parseInt(r_quantity)));
                remove_item();
                itemlist.remove(i);
                itemlistadapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public void remove_item(){

        final Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/Cash_Register_Products/"+r_item)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            FS_Quantity = Integer.parseInt(documentSnapshot.get("Quantity").toString());
                            String Updated_Quantity = Integer.toString(FS_Quantity+Integer.parseInt(r_quantity));

                            DocumentReference DocRef = FireStoreDB.document("/Billing_App/Store_Demo/Cash_Register_Products/"+r_item);
                            DocRef
                                    .update("Quantity", Updated_Quantity)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Cash_Register.this, "Quantity Updated", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Cash_Register.this, "Error Updating Quantity", Toast.LENGTH_LONG).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(Cash_Register.this, "Product Not Added To Stock", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Cash_Register.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void onadditem(View view){
        product = myspinner.getSelectedItem().toString();
        quantity = et_quantity.getText().toString();
        if (quantity.isEmpty()){
            quantity = "1";
        }
        description = product.concat(":").concat(Integer.toString(FS_Price)).concat(":").concat(quantity);
        itemlist.add(description);
        itemlistadapter.notifyDataSetChanged();
        listView.setAdapter(itemlistadapter);
        et_total.setText(Integer.toString(Integer.parseInt(et_total.getText().toString()) + FS_Price * Integer.parseInt(quantity)));
        et_quantity.setText("");

        String Updated_Quantity = Integer.toString(FS_Quantity - Integer.parseInt(quantity));

        DocumentReference DocRef = FireStoreDB.document("/Billing_App/Store_Demo/Cash_Register_Products/"+product);
        DocRef
                .update("Quantity", Updated_Quantity)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Cash_Register.this, "Quantity Updated", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Cash_Register.this, "Error Updating Quantity", Toast.LENGTH_LONG).show();
                    }
                });

        items = new String[itemlistadapter.getCount()];
        for (int i = 0; i < itemlistadapter.getCount(); i++){
            items[i] = itemlistadapter.getItem(i);
        }

    }

    public void ondonemanualbill(View view){
        discount = et_discount.getText().toString();
        if (discount.isEmpty()){
            discount = "0";
        }
        phonenumber = et_phonenumber.getText().toString();
        SharedPreferences sharedPreferences1 = getSharedPreferences("phoneinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString("phonenum", phonenumber);
        editor.apply();

        ProgressDialog progress;
        progress = new ProgressDialog(Cash_Register.this);
        progress.setMessage("Generating Bill");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        generatebill();


    }

    private void generatebill() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(Cash_Register.this, cash_register_bill.class);
                intent.putExtra("Items", (Serializable) items);
                intent.putExtra("Discount",(Serializable) discount);
                startActivity(intent);
                finish();
            }
        }, 2000);
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
