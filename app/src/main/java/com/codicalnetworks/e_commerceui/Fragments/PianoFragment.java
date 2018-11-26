package com.codicalnetworks.e_commerceui.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codicalnetworks.e_commerceui.Adapters.ProductsAdapter;
import com.codicalnetworks.e_commerceui.Models.Product;
import com.codicalnetworks.e_commerceui.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PianoFragment extends Fragment {

    private RecyclerView mPianoRecyclerView;
    private List<Product> mProductsList = new ArrayList<>();

    // Firebase
    private DatabaseReference mPianoDatabase;
    private FirebaseAuth mAuth;

    private ProgressBar mLoadingProgress;



    public PianoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_piano, container, false);

        mPianoDatabase = FirebaseDatabase.getInstance().getReference("Pianos");

        mPianoRecyclerView = view.findViewById(R.id.piano_recycler_view);
        mPianoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mPianoRecyclerView.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();

        mLoadingProgress = view.findViewById(R.id.loading_progress_piano);

        initViews();



        return view;
    }

    private void initViews() {
        mProductsList.clear();
        mPianoDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProductsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    mProductsList.add(product);
                }

                ProductsAdapter adapter = new ProductsAdapter(mProductsList, getContext());
                mPianoRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mLoadingProgress.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mPianoDatabase.keepSynced(true);
    }

}
