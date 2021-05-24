package sj_infotech.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Add_Product extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    String product_id, FS_Category, FS_Description, FS_Prize;
    FirebaseFirestore FireStoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        FireStoreDB = FirebaseFirestore.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermisssion()){
                Toast.makeText(Add_Product.this, "permission is granted!", Toast.LENGTH_LONG).show();
            }
            else {
                requestpermission();
            }
        }

    }

    private boolean checkPermisssion()
    {
        return (ContextCompat.checkSelfPermission(Add_Product.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
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
                        Toast.makeText(Add_Product.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(Add_Product.this, "Permission denied", Toast.LENGTH_LONG).show();
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
        new AlertDialog.Builder(Add_Product.this)
                .setMessage(message)
                .setPositiveButton("ok", (DialogInterface.OnClickListener) listener)
                .setNegativeButton("cancle", null)
                .create()
                .show();


    }

    @Override
    public void handleResult(Result result) {
        final String scanresult = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("scan result");
        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                product_id = scanresult.toString();

                Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/EPOS_Products/"+product_id)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    FS_Category = documentSnapshot.get("Category").toString();
                                    FS_Description = documentSnapshot.get("Description").toString();
                                    FS_Prize = documentSnapshot.get("Prize").toString();
                                    SharedPreferences sharedPreferences_product = getSharedPreferences("product", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences_product.edit();
                                    editor.putString("product_id", product_id);
                                    editor.putString("category", FS_Category);
                                    editor.putString("description", FS_Description);
                                    editor.putString("prize", FS_Prize);
                                    editor.apply();

                                    Intent intent = new Intent(Add_Product.this, Add_Stock.class);
                                    startActivity(intent);

                                } else {
                                    FS_Category = "";
                                    FS_Description = "";
                                    FS_Prize = "";
                                    SharedPreferences sharedPreferences_product = getSharedPreferences("product", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences_product.edit();
                                    editor.putString("product_id", product_id);
                                    editor.putString("category", FS_Category);
                                    editor.putString("description", FS_Description);
                                    editor.putString("prize", FS_Prize);
                                    editor.apply();

                                    Intent intent = new Intent(Add_Product.this, Add_Stock.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Add_Product.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
        builder.setMessage(scanresult);
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__product, menu);
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
                Intent intent = new Intent(Add_Product.this, login.class);
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
