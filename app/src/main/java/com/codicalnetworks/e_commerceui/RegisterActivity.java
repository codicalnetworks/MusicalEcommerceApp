package com.codicalnetworks.e_commerceui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // Widgets
    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mTelephoneInput;
    private Button mRegisterButton;
    private TextView mLoginText;
    private ProgressBar mProgressBar;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    // Variables
    private String userId;
    private String key;

    // Constants
    private static String NAME = "name";
    private static String PASSWORD = "password";
    private static String EMAIL = "email";
    private static String TELEPHONE = "telephone";
    private static String CHANNEL_ID = "id";

    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Widget initializations
        mNameInput = findViewById(R.id.name_input);
        mEmailInput = findViewById(R.id.email_input);
        mPasswordInput = findViewById(R.id.password_input);
        mTelephoneInput = findViewById(R.id.telephone_input);
        mRegisterButton = findViewById(R.id.btn_sign_up);
        mLoginText = findViewById(R.id.sign_in_text);
        mProgressBar = findViewById(R.id.register_progress);

        // Firebase initializations
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // On click listeners
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                registerWithFirebase(mEmailInput.getText().toString(), mPasswordInput.getText().toString(),
                        mNameInput.getText().toString(), mTelephoneInput.getText().toString());
            }
        });


        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void registerWithFirebase(final String email, String password, String name, String telephone) {
        final Map<String, Object> registerMap = new HashMap<>();
        registerMap.put(NAME, name);
        registerMap.put(PASSWORD, password);
        registerMap.put(EMAIL, email);
        registerMap.put(TELEPHONE, telephone);

        key = mUserDatabase.push().getKey();
        mUserDatabase.child(key).setValue(registerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(RegisterActivity.this, "Database successfully updated", Toast.LENGTH_SHORT).show();
            }
        });

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Created successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_address)
                                    .setContentTitle("C_Real global")
                                    .setContentText("Thank you for registering...")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                            notificationManagerCompat.notify(notificationId, mBuilder.build());
                            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            loginIntent.putExtra(EMAIL, email);
                            startActivity(loginIntent);
                            finish();
                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Account not created", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
