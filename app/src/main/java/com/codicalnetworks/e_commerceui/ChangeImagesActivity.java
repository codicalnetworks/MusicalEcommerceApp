package com.codicalnetworks.e_commerceui;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ChangeImagesActivity extends AppCompatActivity {

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private Button btnImage1;
    private Button btnImage2;
    private Button btnImage3;
    private FloatingActionButton fabDone;

    private ProgressBar progress1;
    private ProgressBar progress2;
    private ProgressBar progress3;

    public static final int PICK_IMAGE_ONE = 1;
    public static final int PICK_IMAGE_TWO = 2;
    public static final int PICK_IMAGE_THREE = 3;

    private String downloadUri1;
    private String downloadUri2;
    private String downloadUri3;

    private StorageReference mStorage;
    private DatabaseReference mImageRefDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_images);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        btnImage1 = findViewById(R.id.btn_image1);
        btnImage2 = findViewById(R.id.btn_image2);
        btnImage3 = findViewById(R.id.btn_image3);

        progress1 = findViewById(R.id.progress_1);
        progress2 = findViewById(R.id.progress_2);
        progress3 = findViewById(R.id.progress_3);


        mStorage = FirebaseStorage.getInstance().getReference("startImages");
        mImageRefDatabase = FirebaseDatabase.getInstance().getReference("imageLinks");

        btnImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select Picture"), PICK_IMAGE_ONE);
            }
        });

        btnImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_TWO);
            }
        });

        btnImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent();
                intent3.setType("image/*");
                intent3.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent3, "Select Picture"), PICK_IMAGE_THREE);
            }
        });

        fabDone = findViewById(R.id.done_fab);
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("imageOne", downloadUri1);
                imageMap.put("imageTwo", downloadUri2);
                imageMap.put("imageThree", downloadUri3);

                mImageRefDatabase.child("-LQi7Li3CGIU6e0Rj7Wj").setValue(imageMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangeImagesActivity.this, "Images successfully added", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_ONE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            StorageReference mImageRef = mStorage.child(uri.getLastPathSegment());

            progress1.setVisibility(View.VISIBLE);
            mImageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            downloadUri1 = downloadUri.toString().trim();
                            Picasso.get().load(downloadUri).into(imageView1);
                            Toast.makeText(ChangeImagesActivity.this, ""+downloadUri, Toast.LENGTH_SHORT).show();
                            progress1.setVisibility(View.GONE);
                        }
                    });
        }

        if (requestCode == PICK_IMAGE_TWO && resultCode == RESULT_OK) {
            Uri uri2 = data.getData();
            StorageReference mImage2 = mStorage.child(uri2.getLastPathSegment());
            progress2.setVisibility(View.VISIBLE);

            mImage2.putFile(uri2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            downloadUri2 = downloadUri.toString().trim();
                            Picasso.get().load(downloadUri2).into(imageView2);
                            Toast.makeText(ChangeImagesActivity.this, ""+downloadUri2, Toast.LENGTH_SHORT).show();
                            progress2.setVisibility(View.GONE);
                        }
                    });
        }


        if (requestCode == PICK_IMAGE_THREE && resultCode == RESULT_OK) {
            Uri uri3 = data.getData();
            StorageReference mImage3 = mStorage.child(uri3.getLastPathSegment());
            progress3.setVisibility(View.VISIBLE);

            mImage3.putFile(uri3)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            downloadUri3 = downloadUri.toString().trim();
                            Picasso.get().load(downloadUri3).into(imageView3);
                            Toast.makeText(ChangeImagesActivity.this, ""+downloadUri2, Toast.LENGTH_SHORT).show();
                            progress3.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
