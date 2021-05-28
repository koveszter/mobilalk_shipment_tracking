package com.example.shipmenttracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ManageDatabaseActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 528;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managedatabase);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }
    }

    public void showMyLocation(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void searchForPackages(View view) {
        Intent intent = new Intent(this, QueryActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void startDeletePackage(View view) {
        Intent intent = new Intent(this, DeleteActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void CreatePackage(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }
}
