package com.codicalnetworks.e_commerceui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Models.Search;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchText;
    private DatabaseReference mRef;
    private List<Search> searchList = new ArrayList<>();


    private RecyclerView mRecyclerView;
    private ImageButton searchBtn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchText = findViewById(R.id.search_edittext);
        mRef = FirebaseDatabase.getInstance().getReference("All");

        searchBtn = findViewById(R.id.btn_search);
        mProgressBar = findViewById(R.id.loading_progress_bar);

        mRecyclerView = findViewById(R.id.search_recycler_adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseItemSearch(mSearchText.getText().toString());
            }
        });



    }

    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

        private List<Search> searches = new ArrayList<>();
        private Context context;

        public SearchAdapter(List<Search> searches, Context context) {
            this.searches = searches;
            this.context = context;
        }

        @Override
        public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_search_item, parent, false);
            return new SearchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchViewHolder holder, int position) {
            final Search model = searches.get(position);
            holder.mPriceTextView.setText(String.valueOf(model.getPrice()));
            holder.mNameTextView.setText(model.getName());
            holder.mCategoryTextView.setText(model.getCategory());
            Picasso.get().load(model.getImageLink()).into(holder.mImageView);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                    intent.putExtra("Name", model.getName());
                    intent.putExtra("Category", model.getCategory());
                    intent.putExtra("Description", model.getDescription());
                    intent.putExtra("Quantity", 1);
                    intent.putExtra("Price", model.getPrice());
                    intent.putExtra("Stock", model.getStock());
                    intent.putExtra("Likes", model.getLikes());
                    intent.putExtra("ImageLink", model.getImageLink());
                    intent.putExtra("Key", model.getKey());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.searches.size();
        }

        public class SearchViewHolder extends RecyclerView.ViewHolder {

            private View mView;

            private TextView mNameTextView;
            private TextView mCategoryTextView;
            private TextView mPriceTextView;

            private ImageView mImageView;

            public SearchViewHolder(View view) {
                super(view);
                mView = view;

                mNameTextView = mView.findViewById(R.id.search_name);
                mCategoryTextView = mView.findViewById(R.id.search_category);
                mPriceTextView = mView.findViewById(R.id.search_price);

                mImageView = mView.findViewById(R.id.search_image);

            }
        }

    }

    private void firebaseItemSearch(String searchText) {
        searchList.clear();
        Query firebaseSearchQuery = mRef.orderByChild("name").startAt(mSearchText.getText().toString()).endAt(mSearchText.getText().toString() + "\uf8ff");

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                searchList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Search search = snapshot.getValue(Search.class);
                    searchList.add(search);
                }

                SearchAdapter searchAdapter = new SearchAdapter(searchList, getApplicationContext());
                mRecyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseSearchQuery.keepSynced(true);




    }


}
