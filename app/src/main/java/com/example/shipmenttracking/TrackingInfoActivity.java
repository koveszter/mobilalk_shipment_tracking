package com.example.shipmenttracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import static com.example.shipmenttracking.TrackingActivity.shipmentTracking;

public class TrackingInfoActivity extends AppCompatActivity {
    private static final String LOG_TAG = TrackingInfoActivity.class.getName();
    private static final String PREF_KEY = TrackingInfoActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 528;

    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();;
    private CollectionReference mShipments = mFireStore.collection("Shipments");

    private SharedPreferences preferences;

    private FirebaseAuth mAuth;

    TextView actualTrackingCode;
    TextView actualCarrier;
    TextView actualLocality;
    TextView actualStreetAddress;
    TextView actualPostalCode;
    TextView actualStateOrProvince;
    TextView actualCountry;

    private NotificationHandler mNotificationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tracking_info);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        actualTrackingCode = findViewById(R.id.actualTrackingCodeTextView);
        actualCarrier = findViewById(R.id.actualCarrierTextView);
        actualLocality = findViewById(R.id.actualLocalityTextView);
        actualStreetAddress = findViewById(R.id.actualStreetAddressTextView);
        actualPostalCode = findViewById(R.id.actualPostalCodeTextView);
        actualStateOrProvince = findViewById(R.id.actualStateOrProvinceTextView);
        actualCountry = findViewById(R.id.actualCountryTextView);

        getInfo();

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        mNotificationHandler = new NotificationHandler(this);
    }

    public void getInfo() {
        mShipments.whereEqualTo("trackingCode", shipmentTracking.getTrackingCode()).get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            shipmentTracking.setCarrier(document.getString("carrier"));
                            String country = document.getString("addressTo.country");
                            String locality = document.getString("addressTo.locality");
                            String postcode = document.getString("addressTo.postcode");
                            String stateOrProvince= document
                                    .getString("addressTo.stateOrProvince");
                            String streetName = document.getString("addressTo.streetName");
                            String streetNr = document.getString("addressTo.streetNr");
                            String streetType = document.getString("addressTo.streetType");
                            String id = document.getId();

                            shipmentTracking.setAddressTo(id, country, locality, postcode,
                                    stateOrProvince, streetNr, streetName, streetType);

                            Log.d(LOG_TAG, shipmentTracking.toString());

                            actualTrackingCode.setText(shipmentTracking.getTrackingCode());
                            actualCarrier.setText(shipmentTracking.getCarrier());
                            actualLocality.setText(shipmentTracking.getAddressTo().get("locality"));
                            String streetAddress = shipmentTracking.getAddressTo().get("streetNr")
                                    + " " + shipmentTracking.getAddressTo().get("streetName")
                                    + " " + shipmentTracking.getAddressTo().get("streetType");
                            actualStreetAddress.setText(streetAddress);
                            actualPostalCode.setText(shipmentTracking.getAddressTo().get("postcode"));
                            actualStateOrProvince.setText(shipmentTracking.getAddressTo().get("stateOrProvince"));
                            actualCountry.setText(shipmentTracking.getAddressTo().get("country"));

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("shipmentId", shipmentTracking.getId());

                            editor.putString("locality", shipmentTracking.getAddressTo().get("locality"));
                            editor.putString("streetNumber", shipmentTracking.getAddressTo().get("streetNr"));
                            editor.putString("streetName", shipmentTracking.getAddressTo().get("streetName"));
                            editor.putString("streetType", shipmentTracking.getAddressTo().get("streetType"));
                            editor.putString("postcode", shipmentTracking.getAddressTo().get("postcode"));
                            editor.putString("stateOrProvince", shipmentTracking.getAddressTo().get("stateOrProvince"));
                            editor.putString("country", shipmentTracking.getAddressTo().get("country"));

                            editor.apply();
                        }
                    } else {
                        Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                    }
                });
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

    public void changeAddress(View view) {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void cancelShipping(View view) {
        mShipments.document(preferences.getString("shipmentId", "")).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(LOG_TAG, "Package successfully deleted!");
                    Toast.makeText(TrackingInfoActivity.this,
                            "You've deleted your package successfully.",
                            Toast.LENGTH_LONG).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(LOG_TAG, "Error deleting document", e);
                }
            });

        Intent intent = new Intent(this, TrackingActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);

        mNotificationHandler.cancel();
    }
}
