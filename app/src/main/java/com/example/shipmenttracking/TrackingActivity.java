package com.example.shipmenttracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class TrackingActivity extends AppCompatActivity {
    private static final String LOG_TAG = TrackingActivity.class.getName();
    private static final String PREF_KEY = TrackingActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 528;

    private FirebaseFirestore mFireStore;
    private CollectionReference mShipments;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    EditText trackingId;
    String stringTrackingId;
    TextView happy;

    static ShipmentTracking shipmentTracking;

    private NotificationHandler mNotificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mFireStore = FirebaseFirestore.getInstance();
        mShipments = mFireStore.collection("Shipments");

        trackingId = findViewById(R.id.trackIdEditText);
        happy = findViewById(R.id.happyTextView);

        Animation animation = AnimationUtils.loadAnimation(TrackingActivity.this, R.anim.bounce);
        happy.startAnimation(animation);

        mAuth = FirebaseAuth.getInstance();

        mNotificationHandler = new NotificationHandler(this);
    }

    public void tracking(View view) {
        stringTrackingId = trackingId.getText().toString();

        if (stringTrackingId.equals("")){
            Log.d(LOG_TAG, "No tracking number added.");
            Toast.makeText(TrackingActivity.this, "Please add a tracking number.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            mShipments.whereEqualTo("trackingCode", stringTrackingId).get().
                    addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                                getTrackingInfo();
                            }
                        } else {
                            Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(TrackingActivity.this,
                                    "There is no package with that tracking number.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void getTrackingInfo() {
        shipmentTracking = new ShipmentTracking(stringTrackingId);

        Intent intent = new Intent(this, TrackingInfoActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);

        mNotificationHandler.send("Yey your package is being delivered to you!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_tracking_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out_button) {
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("SECRET_KEY", SECRET_KEY);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}