package com.codicalnetworks.e_commerceui;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Adapters.CartAdapter;
import com.codicalnetworks.e_commerceui.Adapters.FavoritesAdapter;
import com.codicalnetworks.e_commerceui.Adapters.ProductsAdapter;
import com.codicalnetworks.e_commerceui.Models.Cart;
import com.codicalnetworks.e_commerceui.Models.Favorite;
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

public class FavoritesActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mFavoritesRef;
    private List<Favorite> mFavoritesList = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;
    private RecyclerView mRecyclerView;

    private DatabaseReference mUserCartDatabase;
    private int size = 0;

    private TextView textView;
    private TextView mNoItemText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = findViewById(R.id.fav_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Favorites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNoItemText = findViewById(R.id.no_item_text);


        mRecyclerView = findViewById(R.id.favorites_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.fav_loading_progress);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

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


        if (mAuth.getCurrentUser() != null) {
            mFavoritesRef = FirebaseDatabase.getInstance().getReference("Favorites")
                    .child(mAuth.getCurrentUser().getUid());
            initViews();
        } else {
            Toast.makeText(this, "You must login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void initViews() {
        mFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFavoritesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Favorite product = snapshot.getValue(Favorite.class);
                    mFavoritesList.add(product);
                }

                favoritesAdapter = new FavoritesAdapter(mFavoritesList, getApplicationContext());
                mRecyclerView.setAdapter(favoritesAdapter);

                if (mFavoritesList.size() > 0) {
                    mNoItemText.setVisibility(View.GONE);
                } else {
                    mNoItemText.setVisibility(View.VISIBLE);
                }
                favoritesAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFavoritesRef.keepSynced(true);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        textView = (TextView) notifCount.findViewById(R.id.count_text);


        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cartIntent);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {

        } else if (id == R.id.action_cart) {
            if (mAuth.getCurrentUser() != null) {
                Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cartIntent);
            } else {
                Toast.makeText(this, "You must login first", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }

        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
