package com.codicalnetworks.e_commerceui;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private EditText mItemNameText;
    private EditText mItemCategoryText;
    private EditText mItemDescriptionText;
    private EditText mItemPriceText;
    private EditText mInStockText;
    private Button mAddImageBtn;
    private Button mPostItemBtn;

    private DatabaseReference mAllDatabase;
    private DatabaseReference mMicrophoneDatabase;
    private DatabaseReference mDrumsDatabase;
    private DatabaseReference mGuitarDatabase;
    private DatabaseReference mMixerDatabase;
    private DatabaseReference mMonitorDatabase;
    private DatabaseReference mPasystemDatabase;
    private DatabaseReference mPianoDatabase;
    private DatabaseReference mSpeakerDatabase;
    private DatabaseReference mWooferDatabase;

    private Spinner categorySpinner;
    private String category;
    private String downloadUrl;

    public static final int PICK_IMAGE = 1;
    private StorageReference mStorage;
    private DatabaseReference mRef;

    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStorage = FirebaseStorage.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference("All");

        mProgressBar = findViewById(R.id.uploading_progress);
        addSpinner();

        //Name
        mItemNameText = findViewById(R.id.post_item_name);

        //Category
        mItemCategoryText = findViewById(R.id.post_item_category);

        //Description
        mItemDescriptionText = findViewById(R.id.post_item_description);

        // Price
        mItemPriceText = findViewById(R.id.post_item_price);

        // inStock?
        mInStockText = findViewById(R.id.post_in_stock);

        // image
        mAddImageBtn = findViewById(R.id.post_add_image);

        mAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        mPostItemBtn = findViewById(R.id.post_item_button);
        mPostItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItem();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            mProgressBar.setVisibility(View.VISIBLE);
            StorageReference mImageRef = mStorage.child(uri.getLastPathSegment());
            mImageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setVisibility(View.GONE);
                            Uri mDownloadUri = taskSnapshot.getDownloadUrl();
                            downloadUrl = mDownloadUri.toString().trim();
                            Toast.makeText(PostActivity.this, "Image posted successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

            TextView mImageText = findViewById(R.id.image_added_text);
            mImageText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            mImageText.setText("Image selected");

        }

    }

    private void addSpinner() {
        categorySpinner = findViewById(R.id.post_type_spinner);

        final String[] arrayCategory = {
                "All", "Microphones", "Drums", "Guitars", "Mixers", "Monitors", "PaSystem", "Pianos", "Speakers", "Woofers"
        };

        final ArrayAdapter adapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, arrayCategory
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (id == 0) {
                    category = parent.getItemAtPosition(0).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);
                } else if (id == 1) {
                    category = parent.getItemAtPosition(1).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);

                } else if (id == 2) {
                    category = parent.getItemAtPosition(2).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);


                } else if (id == 3) {
                    category = parent.getItemAtPosition(3).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);


                } else if (id == 4) {
                    category = parent.getItemAtPosition(4).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);


                } else if (id == 5) {
                    category = parent.getItemAtPosition(5).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);

                } else if (id == 6) {
                    category = parent.getItemAtPosition(6).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);

                } else if (id == 7) {
                    category = parent.getItemAtPosition(7).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);

                } else if (id == 8) {
                    category = parent.getItemAtPosition(8).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);

                } else {
                    category = parent.getItemAtPosition(9).toString();
                    Toast.makeText(PostActivity.this, ""+category, Toast.LENGTH_SHORT).show();
                    mAllDatabase = FirebaseDatabase.getInstance().getReference(category);
                    mStorage = FirebaseStorage.getInstance().getReference(category);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAllDatabase = FirebaseDatabase.getInstance().getReference("All");
                mStorage = FirebaseStorage.getInstance().getReference("All");
            }
        });
    }

    private void postItem() {
        String key = mAllDatabase.push().getKey();
        String price = mItemPriceText.getText().toString();
        int priceValue = Integer.valueOf(price);

        final Map<String, Object> map = new HashMap<>();
        map.put("name", mItemNameText.getText().toString());
        map.put("description", mItemDescriptionText.getText().toString());
        map.put("category", mItemCategoryText.getText().toString());
        map.put("price", priceValue);
        map.put("stock", mInStockText.getText().toString());
        map.put("likes", "4");
        map.put("key", key);
        map.put("quantity", 1);
        map.put("imageLink", downloadUrl);


            mAllDatabase.child(key).setValue(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Successfully posted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


            mRef.child(key).setValue(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                mItemNameText.setText("");
                                mItemDescriptionText.setText("");
                                mItemCategoryText.setText("");
                                mInStockText.setText("");
                                mItemPriceText.setText("");
                            }
                        }
                    });
    }
}
