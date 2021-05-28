package com.example.shipmenttracking;

import android.content.SharedPreferences;
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

public class UpdateActivity extends AppCompatActivity {
    private static final String LOG_TAG = UpdateActivity.class.getName();
    private static final String PREF_KEY = UpdateActivity.class.getPackage().toString();

    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();;
    private CollectionReference mShipments = mFireStore.collection("Shipments");

    private SharedPreferences preferences;

    EditText newCountry;
    EditText newLocality;
    EditText newStreetNumber;
    EditText newStreetName;
    EditText newStreetType;
    EditText newPostalCode;
    EditText newStateOrProvince;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        newCountry = findViewById(R.id.actualCountryEditText);
        newLocality = findViewById(R.id.actualLocalityEditText);
        newStreetNumber = findViewById(R.id.actualStreetNumberEditText);
        newStreetName = findViewById(R.id.actualStreetNameEditText);
        newStreetType = findViewById(R.id.actualStreetTypeEditText);
        newPostalCode = findViewById(R.id.actualPostalCodeEditText);
        newStateOrProvince = findViewById(R.id.actualStateOrProvinceEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String oldCountry = preferences.getString("country", "");
        String oldLocality = preferences.getString("locality", "");
        String oldStreetNumber = preferences.getString("streetNumber", "");
        String oldStreetName = preferences.getString("streetName", "");
        String oldStreetType = preferences.getString("streetType", "");
        String oldPostcode = preferences.getString("postcode", "");
        String oldStateOrProvince = preferences.getString("stateOrProvince", "");

        newCountry.setText(oldCountry);
        newLocality.setText(oldLocality);
        newStreetNumber.setText(oldStreetNumber);
        newStreetName.setText(oldStreetName);
        newStreetType.setText(oldStreetType);
        newPostalCode.setText(oldPostcode);
        newStateOrProvince.setText(oldStateOrProvince);
    }

    public void updateAddress(View view) {

        String nCountry = newCountry.getText().toString();
        String nLocality = newLocality.getText().toString();
        String nStreetNumber = newStreetNumber.getText().toString();
        String nStreetName = newStreetName.getText().toString();
        String nStreetType = newStreetType.getText().toString();
        String nPostalCode = newPostalCode.getText().toString();
        String nStateOrProvince = newStateOrProvince.getText().toString();

        mShipments.document(preferences.getString("shipmentId", ""))
                .update("addressTo.country", nCountry,
                        "addressTo.locality", nLocality,
                        "addressTo.postcode", nPostalCode,
                        "addressTo.stateOrProvince", nStateOrProvince,
                        "addressTo.streetName", nStreetName,
                        "addressTo.streetNr", nStreetNumber,
                        "addressTo.streetType", nStreetType)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LOG_TAG, "DocumentSnapshot successfully updated!");
                Toast.makeText(UpdateActivity.this,
                        "Shipping address has changed successfully.", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error updating document", e);
                    }
                });
    }

    public void backToAddress(View view) {
        finish();
    }
}
