package sj_infotech.easybill;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FireStore_Demo extends AppCompatActivity {
    FirebaseFirestore FireStoreDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_store__demo);
        FireStoreDB = FirebaseFirestore.getInstance();
    }

    public void Add_Users_Firestore(View view){

        Map<String, Object> user = new HashMap<>();
        user.put("Email", "swan11196@gmail.com");
        user.put("Password", "SJ@11196");

// Add a new document with a generated ID
        try {
            FireStoreDB.collection("/Billing_App/Store_Demo/Users/").document("Swanand Jamadagni").set(user);
            Toast.makeText(FireStore_Demo.this, "User_Added_Successfully", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(FireStore_Demo.this, "Error_Adding_User", Toast.LENGTH_LONG).show();
        }

    }

    public void Read_Data_Firestore(View view){
        String user = "Swanand Jamadagni";
        Task<DocumentSnapshot> MyDoc = FireStoreDB.document("/Billing_App/Store_Demo/Users/"+user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String Email = documentSnapshot.get("Email").toString();
                            String Password = documentSnapshot.get("Password").toString();
                            Toast.makeText(FireStore_Demo.this, Email+"\n"+Password, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FireStore_Demo.this, "Error_while_reading_data", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FireStore_Demo.this, "Error while connecting to FireStore", Toast.LENGTH_LONG).show();
                    }
                });
    }
}