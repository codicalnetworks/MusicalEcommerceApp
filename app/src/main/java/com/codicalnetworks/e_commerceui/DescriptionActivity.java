package com.codicalnetworks.e_commerceui;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class DescriptionActivity extends AppCompatActivity {

    private TextView mNameText;
    private TextView mDescriptionText;
    private TextView mCategoryText;
    private TextView mPriceText;
    private TextView mStockText;
    private ImageView mProductImage;

    private Button mBuyNow;
    private ImageButton mCallNow;
    private ImageButton mAddFavorites;
    private String key;
    private int value;

    private DatabaseReference mCartDatabase;
    private DatabaseReference mFavoritesRef;

    private String productName;
    private String productDescription;
    private String productCategory;
    private int productPrice;
    private String productStock;
    private String productLikes;
    private String productImageLink;
    String productQuantity;
    private TextView textView;

    private int size = 0;
    private int totalPrice = 0;
    private DatabaseReference mUserCartDatabase;
    private FirebaseAuth mAuth;

    private ImageButton mIncrementButton;
    private ImageButton mDecrementButton;
    private TextView mQuantityText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Toolbar toolbar = findViewById(R.id.description_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Product description");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        productName = getIntent().getStringExtra("Name");
        productDescription = getIntent().getStringExtra("Description");
        productCategory = getIntent().getStringExtra("Category");
        productPrice = getIntent().getIntExtra("Price", 0);
        productQuantity = getIntent().getStringExtra("Quantity");
        productStock = getIntent().getStringExtra("Stock");
        productLikes = getIntent().getStringExtra("Likes");
        productImageLink = getIntent().getStringExtra("ImageLink");

        mProductImage = findViewById(R.id.product_image);
        mAddFavorites = findViewById(R.id.btn_add_favorites);
        Picasso.get().load(productImageLink).into(mProductImage);

        mNameText = findViewById(R.id.product_name);
        mDescriptionText = findViewById(R.id.product_description);
        mPriceText = findViewById(R.id.product_price);
        mCategoryText = findViewById(R.id.product_category);
        mStockText = findViewById(R.id.product_stock);
        mBuyNow = findViewById(R.id.btn_buy_now);
        mCallNow = findViewById(R.id.btn_call);

        mIncrementButton = findViewById(R.id.btn_increment);
        mDecrementButton = findViewById(R.id.btn_decrement);
        mQuantityText = findViewById(R.id.value);
        mQuantityText.setText(String.valueOf(1));

        mIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(mQuantityText);
            }
        });

        mDecrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(mQuantityText);
            }
        });


        mNameText.setText(productName);
        mDescriptionText.setText(productDescription);
        mPriceText.setText(String.valueOf(productPrice));
        mCategoryText.setText(productCategory);
        mStockText.setText(productStock);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    mCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                            .child(mAuth.getCurrentUser().getUid());

                    initViews();
                    initView();

                } else {
                    Toast.makeText(getApplicationContext(), "No item in cart", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);


        mBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                            .child(mAuth.getCurrentUser().getUid()).child("Cart");
                    key = mUserCartDatabase.push().getKey();

                    int val = Integer.valueOf(mQuantityText.getText().toString());

                    if (val > 1) {
                        totalPrice = val * productPrice;
                    } else {
                        val = Integer.valueOf(mQuantityText.getText().toString());
                        totalPrice = val * productPrice;
                    }

                    final Map<String, Object> map = new HashMap<>();
                    map.put("name", productName);
                    map.put("description", productDescription);
                    map.put("category", productCategory);
                    map.put("price", productPrice);
                    map.put("totalPrice", totalPrice);
                    map.put("stock", productStock);
                    map.put("likes", productLikes);
                    map.put("imageLink", productImageLink);
                    map.put("quantity", val);
                    map.put("key", key);

                    mUserCartDatabase.child(key)
                            .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DescriptionActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Item not added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "You must login first", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);


                }
            }
        });

        mAddFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    mFavoritesRef = FirebaseDatabase.getInstance().getReference("Favorites")
                            .child(mAuth.getCurrentUser().getUid());
                    key = mFavoritesRef.push().getKey();

                    final Map<String, Object> map = new HashMap<>();
                    map.put("name", productName);
                    map.put("description", productDescription);
                    map.put("category", productCategory);
                    map.put("price", productPrice);
                    map.put("stock", productStock);
                    map.put("likes", productLikes);
                    map.put("imageLink", productImageLink);
                    map.put("key", key);

                    mFavoritesRef.child(key)
                            .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mAddFavorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                                Toast.makeText(DescriptionActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Item not added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "You must login first", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);


                }
            }
        });


    }

    private void initView() {
        mUserCartDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                size = (int) dataSnapshot.getChildrenCount();

                if (size == 0) {
                    textView.setText(String.valueOf(0));
                } else {
                    textView.setText(String.valueOf(size));
                }

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

    private void increment(TextView txt) {
        String _stringVal;
        value = value + 1;
        _stringVal = String.valueOf(value);
        txt.setText(_stringVal);

    }

    private void decrement(TextView textView) {
        String _stringVal;
        if (value > 1) {
            value = value - 1;
            _stringVal = String.valueOf(value);
            textView.setText(_stringVal);
        } else {
            Toast.makeText(getApplicationContext(), "Quantity cannot be less than one", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    private void initViews() {
        mCartDatabase.addChildEventListener(new ChildEventListener() {
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
        mCartDatabase.keepSynced(true);
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
