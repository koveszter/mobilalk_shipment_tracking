package com.example.shipmenttracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private static final String LOG_TAG = LocationActivity.class.getName();

    TextView actualLocationCountry;
    TextView actualLocationLocality;
    TextView actualAddress;

    FusedLocationProviderClient fusedLocationProviderClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528) {
            finish();
        }

        actualLocationCountry = findViewById(R.id.actualLocationCountryTextView);
        actualLocationLocality = findViewById(R.id.actualLocationLocalityTextView);
        actualAddress = findViewById(R.id.actualAddressTextView);

        fusedLocationProviderClient = LocationServices.
                getFusedLocationProviderClient(LocationActivity.this);

        myCheckPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void myCheckPermission() {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }
        getMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if (requestCode == 44 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
            getMyLocation();
        }
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().
                    addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                try {
                                    Geocoder geocoder = new Geocoder(LocationActivity.this,
                                            Locale.getDefault());

                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1);

                                    actualLocationCountry.setText(addresses.get(0).getCountryName());
                                    actualLocationLocality.setText(addresses.get(0).getLocality());
                                    actualAddress.setText(addresses.get(0).getAddressLine(0));

                                    Log.d(LOG_TAG, addresses.get(0).getAddressLine(0));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
            });
        }
    }
}

