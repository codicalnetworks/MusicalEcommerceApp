package com.codicalnetworks.e_commerceui.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Models.Cart;
import com.codicalnetworks.e_commerceui.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ADETOBA on 10/27/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> mCartList;
    private Context context;


    private int value = 0;

    public CartAdapter(List<Cart> mCartList, Context context) {
        this.mCartList = mCartList;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
        final Cart cart = mCartList.get(position);

        Picasso.get().load(cart.getImageLink()).into(holder.mProductImage);
        holder.mProductName.setText(cart.getName());

        String price = holder.mPriceText.getText().toString() + String.valueOf(cart.getPrice());
        holder.mPriceText.setText(price);
        holder.mCategoryText.setText(cart.getCategory());
        holder.val.setText(String.valueOf(cart.getQuantity()));

        String totalPrice = holder.mTotalPrice.getText().toString() + String.valueOf(cart.getTotalPrice());

        holder.mTotalPrice.setText(totalPrice);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mUserCartDatabase = FirebaseDatabase.getInstance().getReference("Carts")
                .child(mAuth.getCurrentUser().getUid()).child("Cart");

        holder.mRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserCartDatabase.child(cart.getKey())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(holder.getPosition());
                    }
                });

                mUserCartDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        final FirebaseAuth finalMAuth = mAuth;


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
            Toast.makeText(context, "Quantity cannot be less than one", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView mProductName;
        private TextView mPriceText;
        private TextView mCategoryText;
        private TextView mTotalPrice;

        private ImageView mRemoveBtn;
        private ImageView mProductImage;

        private ImageButton mIncrementBtn;
        private ImageButton mDecrementBtn;

        private TextView val;


        public CartViewHolder(View view) {
            super(view);
            mView = view;

            mProductName = view.findViewById(R.id.single_cart_name);
            mPriceText = view.findViewById(R.id.single_cart_price);
            mCategoryText = view.findViewById(R.id.single_cart_category);

            mRemoveBtn = view.findViewById(R.id.single_cart_remove);
            mProductImage = view.findViewById(R.id.single_cart_image);
            mTotalPrice = view.findViewById(R.id.single_cart_total_price);


            val = view.findViewById(R.id.value);
        }
    }

}
