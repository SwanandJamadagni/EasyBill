package sj_infotech.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scan_Product extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    final List<String> barcodelist = new ArrayList<String>();
    String ip,type;
    String scandata = "";
    private String quantity = "";
    AlertDialog alertdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermisssion()){
                Toast.makeText(Scan_Product.this, "permission is granted!", Toast.LENGTH_LONG).show();
            }
            else {
                requestpermission();
            }
        }
    }


    private boolean checkPermisssion()
    {
        return (ContextCompat.checkSelfPermission(Scan_Product.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestpermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionResult(int requestcode, String permission[], int grantResult[])
    {
        switch (requestcode)
        {
            case REQUEST_CAMERA:
                if (grantResult.length > 0)
                {
                    boolean cameraAccepted =grantResult[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted)
                    {
                        Toast.makeText(Scan_Product.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(Scan_Product.this, "Permission denied", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA))
                            {
                                displayAlertMessage("you need to allow access for both permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkPermisssion())
            {
                if (scannerView == null)
                {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else
            {
                requestpermission();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(Scan_Product.this)
                .setMessage(message)
                .setPositiveButton("ok", (DialogInterface.OnClickListener) listener)
                .setNegativeButton("cancle", null)
                .create()
                .show();


    }


    @Override
    public void handleResult(Result result) {
        final String scanresult = result.getText();
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("scan result");
        builder.setView(input);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                quantity = input.getText().toString();
                if (quantity.isEmpty()){
                    quantity = "1";
                }
                barcodelist.add(scanresult.concat(":").concat(quantity));
                scannerView.resumeCameraPreview(Scan_Product.this);

            }
        });
        builder.setNeutralButton("print", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                quantity = input.getText().toString();
                if (quantity.isEmpty()){
                    quantity = "1";
                }
                barcodelist.add(scanresult.concat(":").concat(quantity));
                for (int j = 0; j < barcodelist.size(); j++){
                    scandata += barcodelist.get(j).concat("&");
                }
                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                ip = sharedPreferences.getString("ip", "");
                type = "getproducts";
                DBHandler dbHandler = new DBHandler(Scan_Product.this);
                dbHandler.execute(ip, type, scandata);
                finish();
            }
        });
        builder.setMessage(scanresult);
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan__product, menu);
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
                Intent intent = new Intent(Scan_Product.this, login.class);
                finish();
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
