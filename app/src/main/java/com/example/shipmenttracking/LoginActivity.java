package com.example.shipmenttracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity{
    private static final String LOG_TAG = LoginActivity.class.getName();
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 528;

    EditText emailAddressET;
    EditText passwordET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        emailAddressET = findViewById(R.id.editTextEmailAddress);
        passwordET = findViewById(R.id.editTextPassword);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }

    public void finalLogin(View view) {
        String emailAddress = emailAddressET.getText().toString();
        String password = passwordET.getText().toString();

        if (emailAddress.equals("") && password.equals("")){
            Toast.makeText(LoginActivity.this,
                    "Please add your email address and password.", Toast.LENGTH_LONG).show();
            return;
        }

        else if (emailAddress.equals("")){
            Toast.makeText(LoginActivity.this,
                    "Please add your email address.", Toast.LENGTH_LONG).show();
            return;
        }

        else if (password.equals("")){
            Toast.makeText(LoginActivity.this,
                    "Please add your password.", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Log.d(LOG_TAG, "User logged in successfully");
                startTracking();
            } else{
                Log.d(LOG_TAG, "User login fail");
                Toast.makeText(LoginActivity.this,
                        "Invalid email address or password.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startTracking() {
        Intent intent = new Intent(this, TrackingActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void registration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String emailAddress = preferences.getString("emailAddress", "");

        emailAddressET.setText(emailAddress);

        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("emailAddress", emailAddressET.getText().toString());
        editor.putString("password", passwordET.getText().toString());
        editor.apply();

        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("emailAddress");
        editor.remove("password");
        editor.apply();

        Log.i(LOG_TAG, "onDestroy");
    }
}