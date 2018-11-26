package com.codicalnetworks.e_commerceui.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Models.Order;
import com.codicalnetworks.e_commerceui.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADETOBA on 11/5/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList = new ArrayList<>();
    private Context context;

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public OrderAdapter() {

    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        final Order order = orderList.get(position);

        final DatabaseReference mAdminCartDetails = FirebaseDatabase.getInstance().getReference("Admin").child("orders");

        holder.mName.setText("Name : " +order.getName());
        holder.mAddress.setText("Address : " +order.getAddress());
        holder.mCity.setText("City : " +order.getCity());
        holder.mZone.setText("Zone : " +order.getZone());
        holder.mPostCode.setText("Post code : " +order.getPostCode());
        holder.mCountry.setText("Country : " +order.getCountry());
        holder.mId.setText("Id : " +order.getId());
        holder.mItems.setText("Items : " +order.getItems());
        holder.mQuantity.setText("Quantity : " +order.getQuantity());
        holder.mTotalPrice.setText("Price : " +order.getTotalPrice());


        holder.deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminCartDetails.child(order.getCartId())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Item successfully processed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mName, mAddress, mCity, mZone, mPostCode, mCountry, mId, mItems, mQuantity, mTotalPrice;
        private Button deliveredBtn;

        public OrderViewHolder(View view) {
            super(view);

            mView = view;

            mName = mView.findViewById(R.id.order_name);
            mAddress = mView.findViewById(R.id.order_address);
            mCity = mView.findViewById(R.id.order_city);
            mZone = mView.findViewById(R.id.order_zone);
            mPostCode = mView.findViewById(R.id.order_post_code);
            mCountry = mView.findViewById(R.id.order_country);
            mId = mView.findViewById(R.id.order_request_id);
            mItems = mView.findViewById(R.id.order_items);
            mQuantity = mView.findViewById(R.id.order_quantity);
            mTotalPrice = mView.findViewById(R.id.order_price);

            deliveredBtn = mView.findViewById(R.id.delivered_order);

        }

    }
}
