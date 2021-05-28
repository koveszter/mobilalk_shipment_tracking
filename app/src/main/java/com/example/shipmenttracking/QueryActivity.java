package com.example.shipmenttracking;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {
    private static final String LOG_TAG = QueryActivity.class.getName();

    EditText searchCountry;
    EditText searchLocality;
    EditText searchCarrier;

    private FirebaseFirestore mFireStore;
    private CollectionReference mShipments;

    private RecyclerView mRecyclerView;
    private ArrayList<ShipmentTracking> mItemsData;
    private PackagesAdapter mAdapter;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        searchCountry = findViewById(R.id.searchCountryEditText);
        searchLocality = findViewById(R.id.searchLocalityEditText);
        searchCarrier = findViewById(R.id.searchCarrierEditText);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemsData = new ArrayList<>();

        mAdapter = new PackagesAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);

        mFireStore = FirebaseFirestore.getInstance();
        mShipments = mFireStore.collection("Shipments");
    }

    public void searchPackages(View view) {
        mItemsData.clear();

        String sCountry = searchCountry.getText().toString().trim();
        String sLocality = searchLocality.getText().toString().trim();
        String sCarrier = searchCarrier.getText().toString().trim();

        if (sCountry.equals("") && sLocality.equals("") && sCarrier.equals("")){
            Log.d(LOG_TAG, "No parameters added.");
            Toast.makeText(QueryActivity.this,
                    "Please add a searching parameter.", Toast.LENGTH_LONG).show();
        }

        else if (sCountry.equals("") && !sLocality.equals("") && !sCarrier.equals("")){
            // if the user adds the locality, he should add the country too
            Log.d(LOG_TAG, "No country name added, locality "
                    + sLocality+ ", carrier: " + sCarrier);
            Toast.makeText(QueryActivity.this,
                    "Please add the country name.", Toast.LENGTH_LONG).show();
        }

        else if (sCountry.equals("") && !sLocality.equals("") && sCarrier.equals("")){
            // if the user adds the locality, he should add the country too
            Log.d(LOG_TAG, "No country and carrier name added, locality: " + sLocality);
            Toast.makeText(QueryActivity.this,
                    "Please add the country name.", Toast.LENGTH_LONG).show();
        }

        else if (!sCountry.equals("") && !sLocality.equals("") && sCarrier.equals("")){
            Log.d(LOG_TAG, "No carrier name added, country: "
                    + sCountry + ", locality: " + sLocality);

            mShipments.whereEqualTo("addressTo.country", sCountry)
                    .whereEqualTo("addressTo.locality", sLocality)
                    .orderBy("trackingCode").limit(5).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            ShipmentTracking item = document.toObject(ShipmentTracking.class);
                            mItemsData.add(item);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else if (!sCountry.equals("") && sLocality.equals("") && !sCarrier.equals("")){
            Log.d(LOG_TAG, "No locality name added, country: "
                    + sCountry + ", carrier: " + sCarrier);

            mShipments.whereEqualTo("addressTo.country", sCountry)
                    .whereEqualTo("carrier", sCarrier)
                    .orderBy("trackingCode").limit(5).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            ShipmentTracking item = document.toObject(ShipmentTracking.class);
                            mItemsData.add(item);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else if (!sCountry.equals("") && sLocality.equals("") && sCarrier.equals("")){
            Log.d(LOG_TAG, "No locality and carrier name added, country: " + sCountry);

            mShipments.whereEqualTo("addressTo.country", sCountry)
                    .orderBy("trackingCode").limit(5).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            ShipmentTracking item = document.toObject(ShipmentTracking.class);
                            mItemsData.add(item);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else if (sCountry.equals("") && sLocality.equals("") && !sCarrier.equals("")) {
            Log.d(LOG_TAG, "No country and locality name added, carrier: " + sCarrier);

            mShipments.whereEqualTo("carrier", sCarrier)
                    .orderBy("trackingCode").limit(5).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            ShipmentTracking item = document.toObject(ShipmentTracking.class);
                            mItemsData.add(item);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        else if (!sCountry.equals("") && !sLocality.equals("") && !sCarrier.equals("")) {
            Log.d(LOG_TAG, "All params were added, country: "
                    + sCountry + ", locality: " + sLocality + ", carrier: " + sCarrier);

            mShipments.whereEqualTo("addressTo.country", sCountry)
                    .whereEqualTo("addressTo.locality", sLocality)
                    .whereEqualTo("carrier", sCarrier)
                    .orderBy("trackingCode").limit(5).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                                    ShipmentTracking item = document.toObject(ShipmentTracking.class);
                                    mItemsData.add(item);
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                            }
                        }
            });
        }
    }
}
