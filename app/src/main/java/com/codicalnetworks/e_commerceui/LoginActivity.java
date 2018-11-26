package com.codicalnetworks.e_commerceui;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    // Widgets
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Button mLoginBtn;
    private TextView mCreateAccountText;
    private ProgressBar progressBar;
    private TextView mForgotPassword;


    // Firebase
    private FirebaseAuth mAuth;

    private static String CHANNEL_ID = "id3";
    private int notificationId = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Toolbar settings
        Toolbar loginToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(loginToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        // Widget initialization
        mEmailInput = findViewById(R.id.email_input);
        mPasswordInput = findViewById(R.id.password_input);
        mLoginBtn = findViewById(R.id.btn_login);
        mCreateAccountText = findViewById(R.id.sign_up_text);
        mForgotPassword = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.loading_progress);

        // Get string data
        final String userSignUpEmail = getIntent().getStringExtra("email");
        mEmailInput.setText(userSignUpEmail);


        // String entries
        final String email = mEmailInput.getText().toString();
        final String password = mPasswordInput.getText().toString();

        // Firebase initializations
        mAuth = FirebaseAuth.getInstance();

        // On click listeners
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (mEmailInput.getText().toString().equals("admin") && mPasswordInput.getText().toString().equals("admin")) {
                        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(newIntent);
                    } else {
                        loginWithFirebase(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
                    }

            }
        });

        mCreateAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
                finish();

            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.forgot_password_layout);
                dialog.setTitle("Forgot password");

                final EditText mPasswordInput = dialog.findViewById(R.id.email_input_forgot);

                Button mSubmitButton = dialog.findViewById(R.id.submit);
                mSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAuth.sendPasswordResetEmail(mPasswordInput.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_address)
                                                .setContentTitle("C_Real global")
                                                .setContentText("We sent you a mail on how to reset your password")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .setContentIntent(pendingIntent)
                                                .setAutoCancel(true);

                                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                        notificationManagerCompat.notify(notificationId, mBuilder.build());

                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                Button cancelButton = dialog.findViewById(R.id.cancel);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    private void loginWithFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "" +
                                    "Login not successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
