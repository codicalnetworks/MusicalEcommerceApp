package com.codicalnetworks.e_commerceui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codicalnetworks.e_commerceui.Models.Message;
import com.codicalnetworks.e_commerceui.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADETOBA on 10/31/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public List<Message> mMessagesList = new ArrayList<>();
    public Context context;

    public MessageAdapter(List<Message> mMessagesList, Context context) {
        this.mMessagesList = mMessagesList;
        this.context = context;
    }

    public MessageAdapter() {

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessagesList.get(position);

        final String mEmail = message.getFullName();
        final String name = holder.mUserNameText.getText().toString() + " " + message.getFullName();
        final String email = holder.mUserEmailText.getText().toString() + " " + message.getEmail();
        final String messages = holder.mMessageText.getText().toString() + " " + message.getMessage();

        holder.mUserNameText.setText(name);
        holder.mUserEmailText.setText(email);
        holder.mMessageText.setText(messages);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, mEmail);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "C_real Global");

                if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(emailIntent, "Send email..."));

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView mUserNameText;
        private TextView mUserEmailText;
        private TextView mMessageText;

        private View mView;

        public MessageViewHolder(View view) {
            super(view);
            mView = view;

            mUserEmailText = mView.findViewById(R.id.message_sender_email);
            mUserNameText = mView.findViewById(R.id.message_sender_text);
            mMessageText = mView.findViewById(R.id.message_sender_message);

        }
    }

}
