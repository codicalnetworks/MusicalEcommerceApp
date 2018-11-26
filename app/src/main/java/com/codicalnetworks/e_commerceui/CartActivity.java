package com.codicalnetworks.e_commerceui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Adapters.CartAdapter;
import com.codicalnetworks.e_commerceui.Models.Cart;
import com.codicalnetworks.e_commerceui.Models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private DatabaseReference mUserCartDatabase;
    private FirebaseAuth mAuth;

    private List<Cart> mProductsList = new ArrayList<>();
    private RecyclerView mCartRecycler;
    private TextView mNoItemText;

    private CartAdapter adapter;
    private int totalPrice = 0;
    private String name;
    private String quantity;
    private ProgressBar progressBarCart;

    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNoItemText = findViewById(R.id.no_text);

        mCartRecycler = findViewById(R.id.cart_recycler_view);
        mCartRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCartRecycler.setHasFixedSize(true);

        progressBarCart = findViewById(R.id.cart_loading_progress);
        progressBarCart.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                    .child(mAuth.getCurrentUser().getUid()).child("Cart");
            initViews();
        } else {
            Toast.makeText(this, "You must login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void initViews() {
        mUserCartDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProductsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cart userCart = snapshot.getValue(Cart.class);
                    mProductsList.add(0, userCart);

                }
                adapter = new CartAdapter(mProductsList, getApplicationContext());
                mCartRecycler.setAdapter(adapter);

                if (mProductsList.size() > 0) {
                    mNoItemText.setVisibility(View.GONE);
                } else {
                    mNoItemText.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                progressBarCart.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        mUserCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    name = name + ", " + snapshot.child("name").getValue();
                    quantity = quantity + " " + String.valueOf(snapshot.child("quantity").getValue().toString());
                    totalPrice = totalPrice + Integer.valueOf(snapshot.child("totalPrice").getValue().toString());


                }

//                Toast.makeText(CartActivity.this, ""+totalPrice, Toast.LENGTH_SHORT).show();
//                Toast.makeText(CartActivity.this, ""+name, Toast.LENGTH_SHORT).show();
//                Toast.makeText(CartActivity.this, ""+quantity, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_next) {
            Intent shippingIntent = new Intent(getApplicationContext(), ShippingActivity.class);
            shippingIntent.putExtra("items", name);
            shippingIntent.putExtra("quantity", quantity);
            shippingIntent.putExtra("totalPrice", totalPrice);
            startActivity(shippingIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
