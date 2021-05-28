package com.example.shipmenttracking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DeliveryActivity extends AppCompatActivity {
    private static final String LOG_TAG = DeliveryActivity.class.getName();
    private static final int SECRET_KEY = 528;

    private FirebaseFirestore mFireStore;
    private CollectionReference mDeliveryManIds;

    EditText deliveryCode;
    Button deliveryLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        mFireStore = FirebaseFirestore.getInstance();
        mDeliveryManIds = mFireStore.collection("DeliveryManIds");

        deliveryCode = findViewById(R.id.deliveryCodeEditText);
        deliveryLogin = findViewById(R.id.deliveryLoginButton);
    }

    public void startDeliveryTracking(View view) {
        Animation animation = AnimationUtils.loadAnimation(DeliveryActivity.this, R.anim.fadein);
        deliveryLogin.startAnimation(animation);

        String dCode = deliveryCode.getText().toString();
        Log.d(LOG_TAG, "code: " + dCode);

        mDeliveryManIds.whereEqualTo("id", dCode).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            startManagingDatabase();
                        }
                    } else {
                        Log.d(LOG_TAG, "Invalid delivery id", task.getException());
                        Toast.makeText(DeliveryActivity.this,
                                "Invalid delivery id", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void startManagingDatabase() {
        Intent intent = new Intent(this, ManageDatabaseActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }
}
