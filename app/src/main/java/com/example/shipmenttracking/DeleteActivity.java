package com.example.shipmenttracking;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DeleteActivity extends AppCompatActivity {
    private static final String LOG_TAG = ManageDatabaseActivity.class.getName();

    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();;
    private CollectionReference mShipments = mFireStore.collection("Shipments");

    EditText deleteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        deleteId = findViewById(R.id.deleteIdEditText);
    }

    public void deletePackage(View view) {

        mShipments.whereEqualTo("trackingCode", deleteId.getText().toString()).get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());

                            mShipments.document(document.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(LOG_TAG, "Package successfully deleted!");
                                            Toast.makeText(DeleteActivity.this,
                                                    "You've deleted the package successfully.",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(LOG_TAG, "Error deleting document", e);
                                        }
                                    });
                        }
                    } else {
                        Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
