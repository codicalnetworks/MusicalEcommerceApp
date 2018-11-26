package com.codicalnetworks.e_commerceui.Fragments;


import android.app.MediaRouteButton;
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
public class DrumFragment extends Fragment {

    // Widgets
    private RecyclerView mDrumRecyclerView;
    private List<Product> mProductsList = new ArrayList<>();

    // Firebase
    private DatabaseReference mDrumDatabase;
    private FirebaseAuth mAuth;

    private ProgressBar mLoadingProgress;


    public DrumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_drum, container, false);

        mDrumDatabase = FirebaseDatabase.getInstance().getReference("Drums");

        mDrumRecyclerView = view.findViewById(R.id.drum_recycler_view);
        mDrumRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mDrumRecyclerView.setHasFixedSize(true);

        mLoadingProgress = view.findViewById(R.id.loading_progress_drum);


        mAuth = FirebaseAuth.getInstance();

        initViews();

        return view;
    }

    private void initViews() {
        mProductsList.clear();
        mDrumDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProductsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    mProductsList.add(product);
                }

                ProductsAdapter adapter = new ProductsAdapter(mProductsList, getContext());
                mDrumRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mLoadingProgress.setVisibility(View.GONE);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mDrumDatabase.keepSynced(true);
    }

}
