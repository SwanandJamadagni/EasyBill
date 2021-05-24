package sj_infotech.easybill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class welcome extends AppCompatActivity {
    String log,email,password,ip,type;
    BackgroundWorker backgroundWorker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        log = sharedPreferences.getString("log", "0");
        email = sharedPreferences.getString("email", "");
        password = sharedPreferences.getString("password", "");
        ip = sharedPreferences.getString("ip", "");
        type = "welcome";


                if (log.equals("0")) {
                    setTimer1();
                } else if (log.equals("1")){
                    setTimer2();
                }
    }


    private void setTimer1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(welcome.this, login.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void setTimer2() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(welcome.this, home.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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

