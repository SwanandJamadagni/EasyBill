package sj_infotech.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class SQLite extends AppCompatActivity {
    DatabaseHelper mydb;
    EditText fnameet, lnameet;
    AlertDialog alertdialog;
    String table_data = "&";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        mydb = new DatabaseHelper(this);
        fnameet = (EditText)findViewById(R.id.fname);
        lnameet = (EditText)findViewById(R.id.lname);
    }


    public void add_data(View view){
        String fname = fnameet.getText().toString();
        String lname = lnameet.getText().toString();
        boolean result = mydb.insertintouserinfo(fname, lname);
        if (result = true)
            Toast.makeText(SQLite.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(SQLite.this, "Error while inserting data", Toast.LENGTH_LONG).show();
    }

    public void view_data(View view){
        Cursor res = mydb.getalldata();
        if (res.getCount() == 0){
            alertdialog = new AlertDialog.Builder(SQLite.this).create();
            alertdialog.setTitle("Error");
            alertdialog.setMessage("Nothing Found!");
            alertdialog.show();
            return;
        }

        StringBuffer buffer = new StringBuffer();
        String data = "";
        while (res.moveToNext()){
            /*buffer.append(res.getString(0) + ":");
            buffer.append(res.getString(1) + ":");
            buffer.append(res.getString(2));*/
            //table_data = table_data.concat(buffer.toString()).concat("&");
            table_data = table_data.concat(res.getString(0)).concat(":").concat(res.getString(1)).concat(":").concat(res.getString(2)).concat("&");
        }
        alertdialog = new AlertDialog.Builder(SQLite.this).create();
        alertdialog.setTitle("Data");
        alertdialog.setMessage(table_data);
        alertdialog.show();
    }

    public void sync_data(View view){
        /*Cursor res = mydb.getalldata();
        if (res.getCount() == 0){
            alertdialog = new AlertDialog.Builder(login.this).create();
            alertdialog.setTitle("Error");
            alertdialog.setMessage("Nothing Found!");
            alertdialog.show();
            return;
        }

        StringBuffer buffer = new StringBuffer();
        String table_data = "&";
        while (res.moveToNext()){
            buffer.append(res.getString(0) + ":");
            buffer.append(res.getString(1) + ":");
            buffer.append(res.getString(2));
            table_data = table_data.concat(buffer.toString()).concat("&");

        }*/
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String ip = sharedPreferences.getString("ip", "");
        String type = "sync";
        DBSync dbSync = new DBSync(this);
        dbSync.execute(ip, type, table_data);

    }

    public void truncate_table(View view){
        mydb.truncate_table();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sqlite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){

            case R.id.action_logout:
                /*final String inFileName = "/data/data/sj_infotech.easybill/databases/easybill.sqlite";
                File dbFile = new File(inFileName);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(dbFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                String outFileName = Environment.getExternalStorageDirectory()+"/easybill.sqlite";

                // Open the empty db as the output stream
                OutputStream output = null;
                try {
                    output = new FileOutputStream(outFileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                try {
                    while ((length = fis.read(buffer))>0){
                        output.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Close the streams
                try {
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                try {
                    File sd = Environment.getExternalStorageDirectory();

                    if (sd.canWrite()) {
                        String currentDBPath = "/data/data/" + getPackageName() + "/databases/easybill.sqlite";
                        String backupDBPath = "easybill.sqlite";
                        File currentDB = new File(currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        }
                    }
                } catch (Exception e) {

                }
                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}
