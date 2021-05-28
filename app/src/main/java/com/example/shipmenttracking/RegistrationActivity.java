package com.example.shipmenttracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 528;

    EditText usernameET;
    EditText emailAddressET;
    EditText emailConfirmET;
    EditText passwordET;
    CheckBox robotET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 528){
            finish();
        }

        usernameET = findViewById(R.id.userNameEditText);
        emailAddressET = findViewById(R.id.emailAddressEditText);
        emailConfirmET = findViewById(R.id.emailConfirmEditText);
        passwordET = findViewById(R.id.passwordEditText);
        robotET = findViewById(R.id.notARobotCheckBox);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String emailAddress = preferences.getString("emailAddress", "");
        String password = preferences.getString("password", "");

        emailAddressET.setText(emailAddress);
        passwordET.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void finalRegister(View view) {
        String username = usernameET.getText().toString();
        String emailAddress = emailAddressET.getText().toString();
        String emailConfirm = emailConfirmET.getText().toString();
        String password = passwordET.getText().toString();
        boolean robot = robotET.isChecked();

        if (username.equals("")){
            Log.d(LOG_TAG, "Username is empty String.");
            Toast.makeText(RegistrationActivity.this,
                    "Please add a username.", Toast.LENGTH_LONG).show();
            return;
        }

        else if (emailAddress.equals("")) {
            Log.d(LOG_TAG, "Email address is empty String.");
            Toast.makeText(RegistrationActivity.this,
                    "Please add your email address.", Toast.LENGTH_LONG).show();
            return;
        }

        else if (!emailAddress.equals(emailConfirm)){
            Log.d(LOG_TAG, "Email and it's confirm is not equal.");
            Toast.makeText(RegistrationActivity.this,
                    "Email and it's confirm is not equal.", Toast.LENGTH_LONG).show();
            return;
        }

        else if(password.equals("")){
            Log.d(LOG_TAG, "Password is empty String.");
            Toast.makeText(RegistrationActivity.this,
                    "Please add a password.", Toast.LENGTH_LONG).show();
            return;
        }

        else if (!robot){
            Log.d(LOG_TAG, "Watch out! We've found a robot!" );
            Toast.makeText(RegistrationActivity.this,
                    "Please check the \"I'm not a robot\" checkbox.", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Log.d(LOG_TAG, "User created successfully");
                Log.i(LOG_TAG, "Registered user: " + username + ", email: " + emailAddress);
                startTracking();
            } else {
                Log.d(LOG_TAG, "User creation failed");
                Toast.makeText(RegistrationActivity.this,
                        "User creation failed: " + task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startTracking() {
        Intent intent = new Intent(this, TrackingActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }
}
