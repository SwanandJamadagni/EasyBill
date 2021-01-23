package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class users extends AppCompatActivity {
    EditText emailet, passwordet, fnamet, lnamet, ipet, admiinpasset;
    CheckBox admin;
    String adminaccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        emailet = (EditText)findViewById(R.id.email_id);
        passwordet = (EditText)findViewById(R.id.password_id);
        admin = (CheckBox) findViewById(R.id.admin);
        admiinpasset = (EditText)findViewById(R.id.admin_pass);
        fnamet = (EditText)findViewById(R.id.fname_id);
        lnamet = (EditText)findViewById(R.id.lname_id);
        ipet = (EditText)findViewById(R.id.ip);
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

    public void create_user(View view) {
        String email = emailet.getText().toString();
        String password = passwordet.getText().toString();
        String adminpass = admiinpasset.getText().toString();
        String fname = fnamet.getText().toString();
        String lname = lnamet.getText().toString();
        String ip = ipet.getText().toString();
        String type = "create_user";
        if(adminpass.equals("EasyBillAdmin")){

            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)){
                File Root = Environment.getExternalStorageDirectory();
                File Dir = new File(Root.getAbsolutePath()+"/EasyBill");
                if (!Dir.exists()){
                    Dir.mkdir();
                }
                if(Dir.exists()){
                    File BillDir = Environment.getExternalStoragePublicDirectory("/EasyBill/Bills");
                    if (!BillDir.exists()){
                        BillDir.mkdir();
                    }
                    if(BillDir.exists()){
                        File ManualBillDir = Environment.getExternalStoragePublicDirectory("/EasyBill/ManualBills");
                        if (!ManualBillDir.exists()){
                            ManualBillDir.mkdir();
                        }
                        if (ManualBillDir.exists()){
                            File QRCodeDir = Environment.getExternalStoragePublicDirectory("/EasyBill/QRCode");
                            if (!QRCodeDir.exists()){
                                QRCodeDir.mkdir();
                            }
                            if (QRCodeDir.exists()){
                                Toast.makeText(users.this, "External Storage Enabled.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
            else{
                Toast.makeText(users.this, "Storage not available", Toast.LENGTH_LONG).show();
            }

            adminaccess = "1";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(ip, type, fname, lname, email, password, adminaccess);
        }
        else {
            if (adminpass.equals("")){
                adminaccess = "0";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(ip, type, fname, lname, email, password, adminaccess);
            }
            else {
                Toast.makeText(users.this, "wrong admin password try creating account without admin rights.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onBackPressed()
    {
        //do whatever you want the 'Back' button to do
        //as an example the 'Back' button is set to start a new Activity named 'NewActivity'
        Intent intent = new Intent(users.this, login.class);
        finish();
        startActivity(intent);

        return;
    }

}
