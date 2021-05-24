package sj_infotech.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    String scandata;
    private String quantity = "";
    AlertDialog alertdialog;

    FirebaseFirestore FireStoreDB;
    String FS_Category, FS_Description, FS_Prize, FS_Quantity;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        FireStoreDB = FirebaseFirestore.getInstance();

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
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("scan result");
        builder.setView(input);
        builder.setPositiveButton("Scan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scandata = scanresult.toString();
                quantity = input.getText().toString();
                if (quantity.isEmpty()){
                    quantity = "1";
                }

                final Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/EPOS_Products/"+scandata)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    FS_Category = documentSnapshot.get("Category").toString();
                                    FS_Description = documentSnapshot.get("Description").toString();
                                    FS_Prize = documentSnapshot.get("Prize").toString();
                                    FS_Quantity = documentSnapshot.get("Quantity").toString();
                                    total += Integer.parseInt(FS_Prize) * Integer.parseInt(quantity);
                                    String Updated_Quantity = Integer.toString(Integer.parseInt(FS_Quantity)-Integer.parseInt(quantity));

                                    DocumentReference DocRef = FireStoreDB.document("/Billing_App/Store_Demo/EPOS_Products/"+scandata);
                                    DocRef
                                            .update("Quantity", Updated_Quantity)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Scan_Product.this, "Quantity Updated", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Scan_Product.this, "Error Updating Quantity", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(Scan_Product.this, "Product Not Added To Stock", Toast.LENGTH_LONG).show();
                                }
                                barcodelist.add(scandata.concat(":").concat(FS_Description).concat(":").concat(FS_Prize).concat(":").concat(quantity));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Scan_Product.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                            }
                        });

                scannerView.resumeCameraPreview(Scan_Product.this);
            }
        });
        builder.setNeutralButton("Bill", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scandata = scanresult.toString();
                quantity = input.getText().toString();
                if (quantity.isEmpty()){
                    quantity = "1";
                }

                Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/EPOS_Products/"+scandata)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    FS_Category = documentSnapshot.get("Category").toString();
                                    FS_Description = documentSnapshot.get("Description").toString();
                                    FS_Prize = documentSnapshot.get("Prize").toString();
                                    FS_Quantity = documentSnapshot.get("Quantity").toString();
                                    total = total + (Integer.parseInt(FS_Prize) * Integer.parseInt(quantity));
                                    String Updated_Quantity = Integer.toString(Integer.parseInt(FS_Quantity)-Integer.parseInt(quantity));

                                    DocumentReference DocRef = FireStoreDB.document("/Billing_App/Store_Demo/EPOS_Products/"+scandata);
                                    DocRef
                                            .update("Quantity", Updated_Quantity)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Scan_Product.this, "Quantity Updated", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Scan_Product.this, "Error Updating Quantity", Toast.LENGTH_LONG).show();
                                                }
                                            });


                                } else {
                                    Toast.makeText(Scan_Product.this, "Product Not Added To Stock", Toast.LENGTH_LONG).show();
                                }
                                barcodelist.add(scandata.concat(":").concat(FS_Description).concat(":").concat(FS_Prize).concat(":").concat(quantity));

                                SharedPreferences sharedPreferences_product = getSharedPreferences("BillInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences_product.edit();
                                editor.putString("total", Integer.toString(total));
                                editor.apply();

                                Intent intent = new Intent(Scan_Product.this, bill.class);
                                intent.putExtra("Barcodes", (Serializable) barcodelist);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Scan_Product.this, "Error while connecting to DataBase", Toast.LENGTH_LONG).show();
                            }
                        });

                /*for (int j = 0; j < barcodelist.size(); j++){
                    scandata += barcodelist.get(j).concat("&");
                }
                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                ip = sharedPreferences.getString("ip", "");
                type = "getproducts";
                DBHandler dbHandler = new DBHandler(Scan_Product.this);
                dbHandler.execute(ip, type, scandata);
                finish();*/

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
