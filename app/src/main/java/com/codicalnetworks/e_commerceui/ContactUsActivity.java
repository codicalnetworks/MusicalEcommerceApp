package com.codicalnetworks.e_commerceui;

import android.app.Dialog;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ContactUsActivity extends AppCompatActivity {

    private EditText mFullNameInput;
    private EditText mEmailInput;
    private EditText mMessageInput;

    private Button messageBtn;
    private ProgressBar mProgressBar;

    private DatabaseReference messageRef;
    private String key;

    private static String CHANNEL_ID = "id2";
    private int notificationId = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = findViewById(R.id.contact_us_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Contact us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFullNameInput = findViewById(R.id.message_name);
        mEmailInput = findViewById(R.id.message_email);
        mMessageInput = findViewById(R.id.message_message);

        messageBtn = findViewById(R.id.message_send_button);
        mProgressBar = findViewById(R.id.message_progress);

        messageRef = FirebaseDatabase.getInstance().getReference("Messages");
        key = messageRef.push().getKey();




        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("fullName", mFullNameInput.getText().toString());
                messageMap.put("email", mEmailInput.getText().toString());
                messageMap.put("message", mMessageInput.getText().toString());
                messageMap.put("key", key);


                mProgressBar.setVisibility(View.VISIBLE);
                messageRef.child(key)
                        .setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_address)
                                    .setContentTitle("C_Real global")
                                    .setContentText("Your message will be processed as received")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                            notificationManagerCompat.notify(notificationId, mBuilder.build());

                            Toast.makeText(ContactUsActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
            }
        });
    }
}
