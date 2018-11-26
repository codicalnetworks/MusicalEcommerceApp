package com.codicalnetworks.e_commerceui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.codicalnetworks.e_commerceui.Adapters.Users;
import com.codicalnetworks.e_commerceui.Adapters.UsersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private List<Users> mUsersList = new ArrayList<>();

    private DatabaseReference mUserDatabase;
    private RecyclerView mUsersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toolbar toolbar = findViewById(R.id.users_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mUsersRecyclerView = findViewById(R.id.users_recycler_view);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mUsersRecyclerView.setHasFixedSize(true);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users users = snapshot.getValue(Users.class);
                    mUsersList.add(users);

                }

                UsersAdapter adapter = new UsersAdapter(mUsersList, getApplicationContext());
                mUsersRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUserDatabase.keepSynced(true);


    }
}
