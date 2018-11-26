package com.codicalnetworks.e_commerceui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
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
 * Created by ADETOBA on 10/27/2018.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private List<Product> productList;
    private Context context;
    private String key;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserCartDatabase;
    private DatabaseReference mFavoritesRef;

    public ProductsAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_layout, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, int position) {
        final Product product = productList.get(position);

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

        Picasso.get().load(product.getImageLink()).into(holder.mProductImage);

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
                    map.put("quantity", product.getQuantity());
                    map.put("price", product.getPrice());
                    map.put("stock", product.getStock());
                    map.put("likes", product.getLikes());
                    map.put("quantity", 1);
                    map.put("totalPrice", product.getPrice());
                    map.put("imageLink", product.getImageLink());
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

//        holder.mProductStock.setText(product.getStock());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("Name", product.getName());
                intent.putExtra("Category", product.getCategory());
                intent.putExtra("Description", product.getDescription());
                intent.putExtra("Quantity", 1);
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
                if (mAuth.getCurrentUser() != null) {
                    mFavoritesRef = FirebaseDatabase.getInstance().getReference("Favorites")
                            .child(mAuth.getCurrentUser().getUid());
                    key = mFavoritesRef.push().getKey();

                    final Map<String, Object> map = new HashMap<>();
                    map.put("name", product.getName());
                    map.put("description", product.getDescription());
                    map.put("category", product.getCategory());
                    map.put("price", product.getPrice());
                    map.put("stock", product.getStock());
                    map.put("likes", product.getLikes());
                    map.put("imageLink", product.getImageLink());
                    map.put("key", key);

                    mFavoritesRef.child(key)
                            .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                holder.imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thick_fav));
                                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mProductName;
        private TextView mProductPrice;
        private TextView mProductDescription;
        private TextView mProductCategory;
        private TextView mProductStock;
        private ImageView mProductImage;

        private ImageButton imageButton;

        private Button mAddToCart;

        public ProductsViewHolder(View view) {
            super(view);
            mView = view;

            mProductName = mView.findViewById(R.id.single_item_name);
            mProductPrice = mView.findViewById(R.id.single_item_price);
            mProductImage = mView.findViewById(R.id.single_item_image);

            mAddToCart = mView.findViewById(R.id.single_item_button);
            imageButton = mView.findViewById(R.id.btn);

        }
    }
}
