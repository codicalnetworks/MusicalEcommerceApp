package com.codicalnetworks.e_commerceui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.codicalnetworks.e_commerceui.Adapters.CartAdapter;
import com.codicalnetworks.e_commerceui.Adapters.PagerAdapter;
import com.codicalnetworks.e_commerceui.Models.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView textView;
    private TextView mName;
    private TextView mEmail;
    private ViewFlipper viewFlipper;



    private DatabaseReference mUserCartDatabase;
    private FirebaseAuth mAuth;

    private List<Cart> mProductsList = new ArrayList<>();
    private RecyclerView mCartRecycler;

    private int size = 0;

    private ImageView mImage1, mImage2, mImage3;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mImageRefDatabase;
    private TextView name, email;

    private String url1;
    private String url2;
    private String url3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Pager setup
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.sliding_tabs);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);

        viewFlipper = findViewById(R.id.view_flipper);

        mImage1 = findViewById(R.id.image1);
        mImage2 = findViewById(R.id.image2);
        mImage3 = findViewById(R.id.image3);

        mImageRefDatabase = FirebaseDatabase.getInstance().getReference("imageLinks");


        final FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewFlipper.getVisibility() == View.VISIBLE) {
                    viewFlipper.setVisibility(View.GONE);
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_short));
                } else {
                    viewFlipper.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_full));
                }

            }
        });


        if (mAuth.getCurrentUser() != null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference("usersWithId").child(mAuth.getCurrentUser().getUid());

        } else {
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
        }


        startImages();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                            .child(mAuth.getCurrentUser().getUid());


                    initViews();


                } else {
                    Toast.makeText(getApplicationContext(), "You're not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        userNameAndEmail();

    }

    private void startImages() {
        mImageRefDatabase.child("-LQi7Li3CGIU6e0Rj7Wj").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                url1 = dataSnapshot.child("imageOne").getValue(String.class);
                Picasso.get().load(url1).into(mImage1);

                url2 = dataSnapshot.child("imageTwo").getValue(String.class);
                Picasso.get().load(url2).into(mImage2);

                url3 = dataSnapshot.child("imageThree").getValue(String.class);
                Picasso.get().load(url3).into(mImage3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mImageRefDatabase.keepSynced(true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkAvailable()) {
            Log.e("Connected: ", "internet available");

        } else {
            final Dialog internetDialog = new Dialog(HomeActivity.this);
            internetDialog.setTitle("Internet connection");
            internetDialog.setContentView(R.layout.internet_dialog);

            Button btn = internetDialog.findViewById(R.id.ok_button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    internetDialog.dismiss();
                    finish();
                }
            });

            internetDialog.show();

        }


    }



    private void userNameAndEmail() {
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    mName.setText("");
                    String userName = dataSnapshot.child("name").getValue().toString();

                    mEmail.setText("");
                    String userEmail = dataSnapshot.child("email").getValue().toString();

                    if (!TextUtils.isEmpty(dataSnapshot.child("name").getValue().toString()) && !TextUtils
                            .isEmpty(dataSnapshot.child("email").getValue().toString())) {

                        mName.setText(userName);
                        mEmail.setText(userEmail);

                    } else {
                        mName.setText("No user");
                        mEmail.setText("No user" );
                    }



                } else {
                    Toast.makeText(getApplicationContext(), "No user", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initViews() {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(searchIntent);

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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_favorites) {
            Intent favIntent = new Intent(getApplicationContext(), FavoritesActivity.class);
            startActivity(favIntent);

        } else if (id == R.id.nav_account) {
            accountAction();

        } else if (id == R.id.nav_cart) {
            if (mAuth.getCurrentUser() != null) {
                Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cartIntent);
            } else {
                Toast.makeText(this, "You must login first", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }

//        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            logOut();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_contact_us) {
            Intent contactUsIntent = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(contactUsIntent);

        } else if (id == R.id.nav_about) {
            final Dialog aboutDialog = new Dialog(HomeActivity.this);
            aboutDialog.setTitle("About");
            aboutDialog.setContentView(R.layout.about_dialog);

            Button btn = aboutDialog.findViewById(R.id.yes_button_about);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aboutDialog.dismiss();
                }
            });

            aboutDialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void accountAction() {

        if (mAuth.getCurrentUser() != null) {
            Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(profileIntent);
        } else {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
        }

    }

    private void logOut() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);

        Button mYesButton = dialog.findViewById(R.id.yes);
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                dialog.dismiss();
            }
        });

        Button mNoButton = dialog.findViewById(R.id.no);
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
