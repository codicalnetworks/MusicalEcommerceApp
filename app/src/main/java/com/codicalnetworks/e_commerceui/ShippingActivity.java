package com.codicalnetworks.e_commerceui;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ShippingActivity extends AppCompatActivity {

    private Button btnNext;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDetails;

    private DatabaseReference mUserCartDatabase;
    private int size = 0;

    private CheckBox checkBox;
    private  String key;
    private String items;
    private String quantity;
    private int totalPrice;
    private TextView textView;

    private DatabaseReference mUserDatabase;

    private EditText mNameText, mLastName, mTelephoneText, mCityText, mAddressText, mCountryText, mZoneText, mPostCodeText, mEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        Toolbar toolbar = findViewById(R.id.shipping_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Shipping details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNameText = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        mTelephoneText = findViewById(R.id.telephone);
        mCityText = findViewById(R.id.city);
        mAddressText = findViewById(R.id.address);
        mCountryText = findViewById(R.id.country);
        mZoneText = findViewById(R.id.zone);
        mPostCodeText = findViewById(R.id.post_code);
        mEmailText = findViewById(R.id.email);

        checkBox = findViewById(R.id.default_shipping_details);
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("usersWithId").child(mAuth.getCurrentUser().getUid());

        key = mUserDatabase.push().getKey();

        totalPrice = getIntent().getIntExtra("totalPrice", 0);
        items = getIntent().getStringExtra("items");
        quantity = getIntent().getStringExtra("quantity");


        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mNameText.getText().toString()) || TextUtils.isEmpty(mLastName.getText().toString()) || TextUtils.isEmpty(mCityText.getText().toString()) || TextUtils
                        .isEmpty(mTelephoneText.getText().toString()) || TextUtils.isEmpty(mTelephoneText.getText().toString()) ||
                        TextUtils.isEmpty(mCountryText.getText().toString())) {
                    Toast.makeText(ShippingActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intent.putExtra("name", mNameText.getText().toString() + " " + mLastName.getText().toString());
                    intent.putExtra("city", mCityText.getText().toString());
                    intent.putExtra("telephone", mTelephoneText.getText().toString());
                    intent.putExtra("address", mAddressText.getText().toString());
                    intent.putExtra("country", mCountryText.getText().toString());
                    intent.putExtra("zone", mZoneText.getText().toString());
                    intent.putExtra("postCode", mPostCodeText.getText().toString());
                    intent.putExtra("items", items);
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("totalPrice", totalPrice);
                    startActivity(intent);
                }

                finish();


            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                            .child(mAuth.getCurrentUser().getUid());

                    initView();

                } else {
                    Toast.makeText(getApplicationContext(), "No item in cart", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }


    private void loadUserDetails() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue(String.class);
                String userAddress = dataSnapshot.child("address").getValue(String.class);
                mAddressText.setText(userAddress);
                String userTelephone = dataSnapshot.child("telephone").getValue(String.class);
                mTelephoneText.setText(userTelephone);
                String userCountry = dataSnapshot.child("country").getValue(String.class);
                mCountryText.setText(userCountry);
                String userEmail = dataSnapshot.child("email").getValue(String.class);
                mEmailText.setText(userEmail);
                String userCity = dataSnapshot.child("city").getValue(String.class);
                mCityText.setText(userCity);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mUserDatabase.addValueEventListener(eventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        textView = (TextView) notifCount.findViewById(R.id.count_text);
        textView.setText("1");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserDetails();
    }

    private void initView() {
        mUserCartDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                size = (int) dataSnapshot.getChildrenCount();
                textView.setText(String.valueOf(size));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(), dataSnapshot.getChildrenCount()+"");
                size = (int) dataSnapshot.getChildrenCount();
                textView.setText(String.valueOf(size));

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e(dataSnapshot.getKey(), dataSnapshot.getChildrenCount()+"");
                size = (int)dataSnapshot.getChildrenCount();
                textView.setText(String.valueOf(size));

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mUserCartDatabase.keepSynced(true);
    }

    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if (checked) {
            Map<String, Object> mUserMap = new HashMap<>();
            mUserMap.put("firstName", mNameText.getText().toString());
            mUserMap.put("lastName", mLastName.getText().toString());
            mUserMap.put("telephone", mTelephoneText.getText().toString());
            mUserMap.put("address", mNameText.getText().toString());
            mUserMap.put("country", mNameText.getText().toString());

            mUserDatabase.child(key).setValue(mUserMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ShippingActivity.this, "Default shipping details set!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void clearCart() {

    }
}
