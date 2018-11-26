package com.codicalnetworks.e_commerceui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.DescriptionActivity;
import com.codicalnetworks.e_commerceui.LoginActivity;
import com.codicalnetworks.e_commerceui.Models.Favorite;
import com.codicalnetworks.e_commerceui.Models.Product;
import com.codicalnetworks.e_commerceui.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ADETOBA on 11/1/2018.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private List<Favorite> productList;
    private Context context;
    private String key;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserCartDatabase;
    private DatabaseReference mFavoritesRef;

    public FavoritesAdapter(List<Favorite> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_favorite_layout, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoritesViewHolder holder, int position) {
        final Favorite product = productList.get(position);

        mAuth = FirebaseAuth.getInstance();

//        if (mAuth.getCurrentUser() != null) {
//            mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
//                    .child(mAuth.getCurrentUser().getUid()).child("Cart");
//
//            key = mUserCartDatabase.push().getKey();
//
//        } else {
//            Toast.makeText(context, "You must login first", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, LoginActivity.class);
//            context.startActivity(intent);
//
//
//        }


        holder.mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAuth.getCurrentUser() != null) {
                    mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                            .child(mAuth.getCurrentUser().getUid()).child("Cart");

                    key = mUserCartDatabase.push().getKey();

                    final Map<String, Object> map = new HashMap<>();
                    map.put("name", product.getName());
                    map.put("description", product.getDescription());
                    map.put("category", product.getCategory());
                    map.put("price", product.getPrice());
                    map.put("stock", product.getStock());
                    map.put("likes", product.getLikes());
                    map.put("imageLink", product.getImageLink());
                    map.put("quantity", 1);
                    map.put("key", key);

                    mUserCartDatabase.child(key)
                            .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                holder.mAddToCart.setText("ADDED");
                                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Item not added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(context, "You must login first", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);


                }



            }
        });
        holder.mProductName.setText(product.getName());
//        holder.mProductCategory.setText(product.getCategory());
//        holder.mProductDescription.setText(product.getDescription());
        holder.mProductPrice.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(product.getImageLink()).into(holder.mProductImage);


//        holder.mProductStock.setText(product.getStock());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("Name", product.getName());
                intent.putExtra("Category", product.getCategory());
                intent.putExtra("Description", product.getDescription());
                intent.putExtra("Price", product.getPrice());
                intent.putExtra("Stock", product.getStock());
                intent.putExtra("Likes", product.getLikes());
                intent.putExtra("ImageLink", product.getImageLink());
                intent.putExtra("Key", product.getKey());
                context.startActivity(intent);

            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFavoritesRef = FirebaseDatabase.getInstance().getReference("Favorites")
                        .child(mAuth.getCurrentUser().getUid());

                mFavoritesRef.child(product.getKey())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(holder.getPosition());
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mProductName;
        private TextView mProductPrice;
        private TextView mProductDescription;
        private TextView mProductCategory;
        private TextView mProductStock;
        private ImageView mProductImage;

        private Button mAddToCart;
        private ImageView imageButton;

        public FavoritesViewHolder(View view) {
            super(view);
            mView = view;

            mProductName = mView.findViewById(R.id.single_fav_name);
            mProductPrice = mView.findViewById(R.id.single_fav_price);
            mProductImage = mView.findViewById(R.id.single_fav_image);

            mAddToCart = mView.findViewById(R.id.single_fav_button);
            imageButton = mView.findViewById(R.id.single_fav_remove);

        }
    }
}
