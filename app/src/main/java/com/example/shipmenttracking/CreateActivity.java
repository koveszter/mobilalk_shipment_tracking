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

public class CreateActivity extends AppCompatActivity {
    private static final String LOG_TAG = CreateActivity.class.getName();

    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();;
    private CollectionReference mShipments = mFireStore.collection("Shipments");

    EditText createTrackingCode;
    EditText createCarrier;
    EditText createLocality;
    EditText createStreetNumber;
    EditText createStreetName;
    EditText createStreetType;
    EditText createPostalCode;
    EditText createStateOrProvince;
    EditText createCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        createTrackingCode = findViewById(R.id.createTrackingCodeEditText);
        createCarrier = findViewById(R.id.createCarrierEditText);
        createLocality = findViewById(R.id.createLocalityEditText);
        createStreetNumber = findViewById(R.id.createStreetNumberEditText);
        createStreetName = findViewById(R.id.createStreetNameEditText);
        createStreetType = findViewById(R.id.createStreetTypeEditText);
        createPostalCode = findViewById(R.id.createPostalCodeEditText);
        createStateOrProvince = findViewById(R.id.createStateOrProvinceEditText);
        createCountry = findViewById(R.id.createCountryEditText);
    }

    public void backToManage(View view) {
        finish();
    }

    public void createPackage(View view) {

        String nTrackingCode = createTrackingCode.getText().toString();
        String nCreateCarrier = createCarrier.getText().toString();
        String nCountry = createCountry.getText().toString();
        String nLocality = createLocality.getText().toString();
        String nStreetNumber = createStreetNumber.getText().toString();
        String nStreetName = createStreetName.getText().toString();
        String nStreetType = createStreetType.getText().toString();
        String nPostalCode = createPostalCode.getText().toString();
        String nStateOrProvince = createStateOrProvince.getText().toString();

        if (nTrackingCode.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the tracking code.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No tracking code added.");
            return;
        }
        if (nCreateCarrier.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the carrier name.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No carrier name added.");
            return;
        }
        if (nCountry.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the country name.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No country name added.");
            return;
        }
        if (nLocality.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the locality name.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No locality name added.");
            return;
        }
        if (nStreetNumber.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the street number.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No street number added.");
            return;
        }
        if (nStreetName.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the street name.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No street name added.");
            return;
        }
        if (nStreetType.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the street type.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No street type added.");
            return;
        }
        if (nPostalCode.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the postal code.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No postal code added.");
            return;
        }
        if (nStateOrProvince.equals("")){
            Toast.makeText(CreateActivity.this, "Please add the name of the state or province.", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "No state or province name added.");
            return;
        }

        ShipmentTracking createShipmentTracking = new ShipmentTracking(nTrackingCode,
                nCreateCarrier, nCountry, nLocality,
                nPostalCode, nStateOrProvince, nStreetNumber,
                nStreetName, nStreetType);

        mShipments.document().set(createShipmentTracking).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LOG_TAG, "DocumentSnapshot successfully written!");
                Toast.makeText(CreateActivity.this, "Package created successfully.", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error writing document", e);
                }
        });
    }
}
