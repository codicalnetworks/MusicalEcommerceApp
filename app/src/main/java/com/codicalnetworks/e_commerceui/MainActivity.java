package com.codicalnetworks.e_commerceui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Adapters.CartAdapter;
import com.codicalnetworks.e_commerceui.Models.Cart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardView mAllUsers;
    private CardView mAllOrders;
    private CardView mPostItems;
    private CardView mMessages;
    private CardView mStartImages;

    private RecyclerView mRecyclerView;
    private List<Cart> mCartsList = new ArrayList<>();

    private DatabaseReference mAdminCartDetails;
    private String cartKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Admin panel");
//        mRecyclerView = findViewById(R.id.main_recycler);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        mRecyclerView.setHasFixedSize(true);

        mAllUsers = findViewById(R.id.all_users);
        mAllOrders = findViewById(R.id.all_orders);
        mPostItems = findViewById(R.id.post_items);
        mMessages = findViewById(R.id.user_messages);
        mStartImages = findViewById(R.id.start_images);

        mAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent usersIntent = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(usersIntent);
            }
        });

        mMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messagesIntent = new Intent(getApplicationContext(), MessagesActivity.class);
                startActivity(messagesIntent);
            }
        });

        mPostItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(intent);
            }
        });

        mAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderIntent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(orderIntent);
            }
        });

        mStartImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startImageIntent = new Intent(getApplicationContext(), ChangeImagesActivity.class);
                startActivity(startImageIntent);
            }
        });

        mAdminCartDetails = FirebaseDatabase.getInstance().getReference("Admin").child("userOrders").child("userDetails");

        mAdminCartDetails.child("cartId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    cartKey = (String) snapshot.getValue();
                    mAdminCartDetails.child("userCartItems").child(cartKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Cart cart = dataSnapshot1.getValue(Cart.class);
                                mCartsList.add(cart);
                            }

                            CartAdapter adapter = new CartAdapter(mCartsList, getApplicationContext());
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



}
