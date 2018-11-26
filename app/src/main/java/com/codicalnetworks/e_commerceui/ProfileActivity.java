package com.codicalnetworks.e_commerceui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    private Button mSaveChangesBtn;

    private EditText mProfileName;
    private EditText mProfileAddress;
    private EditText mProfileTelephone;
    private EditText mProfileDateOfBirth;
    private EditText mProfileEmail;
    private EditText mProfileNewPassword;
    private EditText mProfileSex;
    private EditText mProfileCountry;
    private EditText mProfileCity;

    private String userAddress;
    private String userTelephone;
    private String userDob;
    private String userSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My profile");
        mAuth = FirebaseAuth.getInstance();

        mProfileName = findViewById(R.id.profile_name);
        mProfileAddress = findViewById(R.id.profile_address);
        mProfileTelephone = findViewById(R.id.profile_telephone);
        mProfileDateOfBirth = findViewById(R.id.profile_date_of_birth);
        mProfileEmail = findViewById(R.id.profile_email);
        mProfileSex = findViewById(R.id.profile_sex);
        mProfileCountry = findViewById(R.id.profile_country);
        mProfileCity = findViewById(R.id.profile_city);
        mSaveChangesBtn = findViewById(R.id.btn_save_changes);



        mUserDatabase = FirebaseDatabase.getInstance().getReference("usersWithId").child(mAuth.getCurrentUser().getUid());

        final String key = mUserDatabase.push().getKey();

        mSaveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Map<String, Object> userDetailsMap = new HashMap<>();
                userDetailsMap.put("name", mProfileName.getText().toString());
                userDetailsMap.put("address", mProfileAddress.getText().toString());
                userDetailsMap.put("telephone", mProfileTelephone.getText().toString());
                userDetailsMap.put("dob", mProfileDateOfBirth.getText().toString());
                userDetailsMap.put("email", mProfileEmail.getText().toString());
                userDetailsMap.put("sex", mProfileSex.getText().toString());
                userDetailsMap.put("country", mProfileCountry.getText().toString());
                userDetailsMap.put("city", mProfileCity.getText().toString());

                mUserDatabase.setValue(userDetailsMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserDetails();

    }

    private void loadUserDetails() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue(String.class);
                mProfileName.setText(userName);
                userAddress = dataSnapshot.child("address").getValue(String.class);
                mProfileAddress.setText(userAddress);
                userTelephone = dataSnapshot.child("telephone").getValue(String.class);
                mProfileTelephone.setText(userTelephone);
                userDob = dataSnapshot.child("dob").getValue(String.class);
                mProfileDateOfBirth.setText(userDob);
                userSex = dataSnapshot.child("sex").getValue(String.class);
                mProfileSex.setText(userSex);
                String userEmail = dataSnapshot.child("email").getValue(String.class);
                mProfileEmail.setText(userEmail);
                String userCity = dataSnapshot.child("city").getValue(String.class);
                mProfileCity.setText(userCity);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mUserDatabase.addValueEventListener(eventListener);
    }
}
