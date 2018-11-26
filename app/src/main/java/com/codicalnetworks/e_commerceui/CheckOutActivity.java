package com.codicalnetworks.e_commerceui;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Adapters.CartAdapter;
import com.codicalnetworks.e_commerceui.Models.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    private TextView mBillingName, mBillingAddress, mBillingZone, mBillingCountry;
    private TextView mShippingName, mShippingAddress, mShippingZone, mShippingCountry, mSubTotalPrice, mTotalPrice;

    private DatabaseReference mUserCartDatabase;
    private DatabaseReference mCartDatabase;
    private DatabaseReference mAdminCartDetails;
    private FirebaseAuth mAuth;

    private List<Cart> mProductsList = new ArrayList<>();
    private RecyclerView mCartRecycler;

    private Button btnOrder;
    private Button btnCancel;
    private TextView mItemsText;
    private TextView mQuantityText;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Toolbar toolbar = findViewById(R.id.check_out_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnOrder = findViewById(R.id.btn_order);
        btnCancel = findViewById(R.id.btn_cancel);

        mBillingName = findViewById(R.id.billing_name);
        mBillingAddress = findViewById(R.id.billing_address);
        mBillingCountry = findViewById(R.id.billing_country);

        mShippingName = findViewById(R.id.shipping_name);
        mShippingAddress = findViewById(R.id.shipping_address);
        mShippingCountry = findViewById(R.id.shipping_country);
        mSubTotalPrice = findViewById(R.id.sub_total_price);
        mTotalPrice = findViewById(R.id.total_price);

        mItemsText = findViewById(R.id.products_text);
        mQuantityText = findViewById(R.id.products_quantity);

        String name = getIntent().getStringExtra("name");
        String city = getIntent().getStringExtra("city");
        String address = getIntent().getStringExtra("address");
        String zone = getIntent().getStringExtra("zone");
        String country = getIntent().getStringExtra("country");
        String postCode = getIntent().getStringExtra("postCode");
        String items = getIntent().getStringExtra("items");
        String quantity = getIntent().getStringExtra("quantity");
        int totalPrice = getIntent().getIntExtra("totalPrice", 0);

        String item = items;
        mItemsText.setText(item);

        String quantityText = mQuantityText.getText().toString() + quantity;
        mQuantityText.setText(quantityText);


        mBillingName.setText(name);
        mBillingAddress.setText(address);
        mBillingCountry.setText(city + ", " + country);

        mShippingName.setText(name);
        mShippingAddress.setText(address);
        mShippingCountry.setText(city + ", " + country);

        String subTotal = mSubTotalPrice.getText().toString() + String.valueOf(totalPrice);
        String totalPriceText = mSubTotalPrice.getText().toString() + String.valueOf(totalPrice);

        mSubTotalPrice.setText(subTotal);
        mTotalPrice.setText(totalPriceText);

//        mCartRecycler = findViewById(R.id.checkout_recycler_view);
//        mCartRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        mCartRecycler.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();

            mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                    .child(mAuth.getCurrentUser().getUid()).child("Cart");
            mAdminCartDetails = FirebaseDatabase.getInstance().getReference("Admin").child("orders");

        } else {

            Toast.makeText(this, "You must login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        final String cardId = mAdminCartDetails.push().getKey();

        final Map<String, Object> userOrder = new HashMap<>();
        userOrder.put("name", name);
        userOrder.put("address", address);
        userOrder.put("city", city);
        userOrder.put("zone", zone);
        userOrder.put("postCode", postCode);
        userOrder.put("country", country);
        userOrder.put("id", mAuth.getCurrentUser().getUid());
        userOrder.put("cartId", cardId);
        userOrder.put("items", item);
        userOrder.put("quantity", quantity);
        userOrder.put("totalPrice", totalPriceText);


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminCartDetails.child(cardId).setValue(userOrder)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    final Dialog dialog = new Dialog(CheckOutActivity.this);
                                    dialog.setTitle("Order success");
                                    dialog.setContentView(R.layout.order_dialog);

                                    Button mOrderPlaced = dialog.findViewById(R.id.yes_button);
                                    mOrderPlaced.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            clearCart();
                                            finish();
                                        }
                                    });

                                    dialog.show();
                                }


                            }
                        });
            }

        });


    }

    private void clearCart() {
        if (mAuth.getCurrentUser() != null) {
            mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                    .child(mAuth.getCurrentUser().getUid()).child("Cart");
        } else {
            Toast.makeText(this, "You must login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        mUserCartDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CheckOutActivity.this, "Cleared", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
