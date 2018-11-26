package com.codicalnetworks.e_commerceui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Adapters.CartAdapter;
import com.codicalnetworks.e_commerceui.Adapters.OrderAdapter;
import com.codicalnetworks.e_commerceui.Models.Cart;
import com.codicalnetworks.e_commerceui.Models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private DatabaseReference mCartDatabase;
    private DatabaseReference mAdminCartDetails;
    private String userId;
    private String cartId;

    private List<Order> orders = new ArrayList<>();
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCartDatabase = FirebaseDatabase.getInstance().getReference("Admin").child("userDetails");
        mAdminCartDetails = FirebaseDatabase.getInstance().getReference("Admin").child("orders");

        mRecyclerView = findViewById(R.id.order_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);


        mAdminCartDetails
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        orders.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Order order = snapshot.getValue(Order.class);
                            orders.add(order);
                        }

                        OrderAdapter adapter = new OrderAdapter(orders, getApplicationContext());
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        mAdminCartDetails.keepSynced(true);


    }


}
