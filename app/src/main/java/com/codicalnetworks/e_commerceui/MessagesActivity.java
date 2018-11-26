package com.codicalnetworks.e_commerceui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.codicalnetworks.e_commerceui.Adapters.MessageAdapter;
import com.codicalnetworks.e_commerceui.Models.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private DatabaseReference mMessagesRef;
    private List<Message> mMessagesList = new ArrayList<>();
    private Context mcontext;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Toolbar toolbar = findViewById(R.id.messages_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Messages");

        mMessagesRef = FirebaseDatabase.getInstance().getReference("Messages");

        mRecyclerView = findViewById(R.id.messages_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);


        initViews();

    }


    private void initViews() {
        mMessagesList.clear();
        mMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    mMessagesList.add(message);
                }

                MessageAdapter adapter = new MessageAdapter(mMessagesList, getApplicationContext());
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMessagesRef.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
