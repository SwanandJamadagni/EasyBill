package sj_infotech.easybill;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class users extends AppCompatActivity {
    EditText emailet, passwordet, admiinpasset;
    CheckBox admin;
    String email, password, adminpass, adminaccess;
    FirebaseFirestore FireStoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        FireStoreDB = FirebaseFirestore.getInstance();

        emailet = (EditText)findViewById(R.id.email_id);
        passwordet = (EditText)findViewById(R.id.password_id);
        admin = (CheckBox) findViewById(R.id.admin);
        admiinpasset = (EditText)findViewById(R.id.admin_pass);

        admiinpasset.setEnabled(false);

        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (admin.isChecked()) {
                    admiinpasset.setEnabled(true);
                } else {
                    admiinpasset.setText("");
                    admiinpasset.setEnabled(false);
                }
            }
        });
    }

     public void add_user(){

        Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/Users/"+email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            Toast.makeText(users.this, "Already Existing Account", Toast.LENGTH_LONG).show();
                        }
                        else {

                            Map<String, Object> user = new HashMap<>();
                            user.put("Password", password);
                            user.put("AdminAccess",adminaccess);

                            try {
                                FireStoreDB.collection("/Billing_App/Store_Demo/Users/").document(email).set(user);
                                Toast.makeText(users.this, "User_Added_Successfully", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e){
                                Toast.makeText(users.this, "Error_Adding_User", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(users.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void create_user(View view) {

        email = emailet.getText().toString();
        password = passwordet.getText().toString();
        adminpass = admiinpasset.getText().toString();



        //String type = "create_user";

        String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)){
                File Root = Environment.getExternalStorageDirectory();
                File Dir = new File(Root.getAbsolutePath()+"/EasyBill");
                if (!Dir.exists()){
                    Dir.mkdir();
                }
                if(Dir.exists()){
                    File BillDir = Environment.getExternalStoragePublicDirectory("/EasyBill/EPos");
                    if (!BillDir.exists()){
                        BillDir.mkdir();
                    }
                    if(BillDir.exists()){
                        File ManualBillDir = Environment.getExternalStoragePublicDirectory("/EasyBill/CashReg");
                        if (!ManualBillDir.exists()){
                            ManualBillDir.mkdir();
                        }
                        if (ManualBillDir.exists()){
                            File QRCodeDir = Environment.getExternalStoragePublicDirectory("/EasyBill/QRCode");
                            if (!QRCodeDir.exists()){
                                QRCodeDir.mkdir();
                            }
                        }
                    }
                }
            }
            else{
                Toast.makeText(users.this, "Storage not available", Toast.LENGTH_LONG).show();
            }

        if(adminpass.equals("EasyBillAdmin")){

            adminaccess = "1";
            add_user();

            /*BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(ip, type, fname, lname, email, password, adminaccess);*/
        }
        else {
            if (adminpass.equals("")){

                adminaccess = "0";
                add_user();


                /*BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(ip, type, fname, lname, email, password, adminaccess);*/
            }
            else {
                Toast.makeText(users.this, "wrong admin password try creating account without admin rights.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void go_back_to_login(View view)
    {
        //do whatever you want the 'Back' button to do
        //as an example the 'Back' button is set to start a new Activity named 'NewActivity'
        Intent intent = new Intent(users.this, login.class);
        finish();
        startActivity(intent);

        return;
    }

}
