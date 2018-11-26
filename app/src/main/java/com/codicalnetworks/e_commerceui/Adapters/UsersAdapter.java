package com.codicalnetworks.e_commerceui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codicalnetworks.e_commerceui.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADETOBA on 11/2/2018.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private List<Users> usersList = new ArrayList<>();
    private Context context;

    private DatabaseReference mRef;

    public UsersAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    public UsersAdapter() {

    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_layout, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        Users users = usersList.get(position);

        holder.mUserNameText.setText(users.getName());
        holder.mEmailText.setText(users.getEmail());
        holder.mTelephoneText.setText(users.getTelephone());

    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView mUserNameText;
        private TextView mEmailText;
        private TextView mTelephoneText;

        public UsersViewHolder(View view) {
            super(view);

            mView = view;

            mUserNameText = mView.findViewById(R.id.single_username);
            mEmailText = mView.findViewById(R.id.single_email);
            mTelephoneText = mView.findViewById(R.id.single_telephone);
        }
    }
}
