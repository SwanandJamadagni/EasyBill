package sj_infotech.easybill;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class bill extends AppCompatActivity {
    List<String> barcodes;
    ArrayAdapter<String> adapter;
    String[] scan, product;
    String item, quantity, prize;
    int FS_Quantity;
    String scandata = "";
    EditText discountet, totalet, phoneno;
    String total;
    String phonenumber, ip, type, email, discount;
    AlertDialog alertdialog;
    FirebaseFirestore FireStoreDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        FireStoreDB = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences_product = getSharedPreferences("BillInfo", Context.MODE_PRIVATE);
        total = sharedPreferences_product.getString("total", "");
        
        discountet = (EditText)findViewById(R.id.discount);
        phoneno = (EditText)findViewById(R.id.phoneno);
        totalet = (EditText)findViewById(R.id.total);
        totalet.setText(total);
        Intent callingIntent= getIntent();
        barcodes = (List<String>) callingIntent.getSerializableExtra("Barcodes");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, barcodes);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                product = barcodes.get(i).split(":");
                item = product[0];
                prize = product[2];
                quantity = product[3];
                total = Integer.toString(Integer.parseInt(total) - (Integer.parseInt(prize) * Integer.parseInt(quantity)));
                totalet.setText(total);
                remove_item();
                barcodes.remove(i);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public void remove_item(){

        final Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/EPOS_Products/"+item)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            FS_Quantity = Integer.parseInt(documentSnapshot.get("Quantity").toString());
                            String Updated_Quantity = Integer.toString(FS_Quantity+Integer.parseInt(quantity));

                            DocumentReference DocRef = FireStoreDB.document("/Billing_App/Store_Demo/EPOS_Products/"+item);
                            DocRef
                                    .update("Quantity", Updated_Quantity)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(bill.this, "Quantity Updated", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(bill.this, "Error Updating Quantity", Toast.LENGTH_LONG).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(bill.this, "Product Not Added To Stock", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(bill.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                    }
                });
    }



    public void ondone(View view){
        discount = discountet.getText().toString();
        if (discount.isEmpty()){
            discount = "0";
        }
        phonenumber = phoneno.getText().toString();
        /*SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip", "");
        email = sharedPreferences.getString("email", "");*/

        SharedPreferences sharedPreferences1 = getSharedPreferences("phoneinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString("phonenum", phonenumber);
        editor.apply();
        //genbill.setEnabled(true);
        //type = "done";
        scan = new String[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++){
            scan[i] = adapter.getItem(i);
            //scandata += scan[i].concat("&");
        }
        /*BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, scandata, discount, phonenumber, email);*/

        ProgressDialog progress;
        progress = new ProgressDialog(bill.this);
        progress.setMessage("Generating Bill");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        generatebill();

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
