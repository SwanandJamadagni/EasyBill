package sj_infotech.easybill;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class login extends AppCompatActivity {
    FirebaseFirestore FireStoreDB;
    EditText emailet, passwordet;
    String FS_password, FS_admin, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FireStoreDB = FirebaseFirestore.getInstance();
        emailet = (EditText)findViewById(R.id.email_id);
        passwordet = (EditText)findViewById(R.id.password_id);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermisssion()){
                //Toast.makeText(login.this, "App has permission to access external Storage ..!", Toast.LENGTH_LONG).show();

            }
            else {
                requestpermission();
            }
        }

        emailet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus){
                    String user = emailet.getText().toString();
                    Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/Users/"+user)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        FS_password = documentSnapshot.get("Password").toString();
                                        FS_admin = documentSnapshot.get("AdminAccess").toString();
                                    } else {
                                        Toast.makeText(login.this, "Account Does Not Exist", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(login.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    private boolean checkPermisssion()
    {
        return (ContextCompat.checkSelfPermission(login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestpermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }



    public void onlogin(View view) {

        email = emailet.getText().toString();
        password = passwordet.getText().toString();

        if (FS_password.equals(password)){
            SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("log", "1");
            editor.putString("email", email);
            editor.putString("admin", FS_admin);
            editor.apply();


            ProgressDialog progress;
            progress = new ProgressDialog(login.this);
            progress.setMessage("Signing In....!");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    Intent intent = new Intent(login.this, home.class);
                    //intent.putExtra("Admin", (Serializable) FS_admin);
                    login.this.startActivity(intent);
                }
            }, 2000);
        }
        else {
            Toast.makeText(login.this, "Email or password is InCorrect....!", Toast.LENGTH_LONG).show();
        }

        /*String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, email, password);*/
    }

    public void create_account(View view){
        Intent intent = new Intent(login.this, users.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){

                //return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
