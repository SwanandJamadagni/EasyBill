package sj_infotech.easybill;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Timer;
import java.util.TimerTask;

public class login extends AppCompatActivity {
    private ProgressDialog progress;
    EditText emailet, passwordet, ipet;
    private LinearLayout pdf;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pdf = (LinearLayout) findViewById(R.id.login_layout);
        emailet = (EditText)findViewById(R.id.email_id);
        passwordet = (EditText)findViewById(R.id.password_id);
        ipet = (EditText)findViewById(R.id.ipadd);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermisssion()){
                Toast.makeText(login.this, "App has permission to access external Storage ..!", Toast.LENGTH_LONG).show();

            }
            else {
                requestpermission();
            }
        }
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
        String email = emailet.getText().toString();
        String password = passwordet.getText().toString();
        String ip = ipet.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("log", "1");
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("ip", ip);
        editor.apply();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(ip, type, email, password);
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
