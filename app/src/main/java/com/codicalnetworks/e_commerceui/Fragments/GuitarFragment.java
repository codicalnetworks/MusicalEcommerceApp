package com.codicalnetworks.e_commerceui.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuitarFragment extends Fragment {

    // Widgets
    private RecyclerView mGuitarRecyclerView;
    private List<Product> mProductsList = new ArrayList<>();
    private ProgressBar mLoadingProgress;

    // Firebase
    private DatabaseReference mGuitarDatabase;
    private FirebaseAuth mAuth;


    public GuitarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_guitar, container, false);

        mGuitarDatabase = FirebaseDatabase.getInstance().getReference("Guitars");

        mGuitarRecyclerView = view.findViewById(R.id.guitar_recycler_view);
        mGuitarRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mGuitarRecyclerView.setHasFixedSize(true);

        mLoadingProgress = view.findViewById(R.id.loading_progress_guitar);

        mAuth = FirebaseAuth.getInstance();

        String key = mGuitarDatabase.push().getKey();

        final Map<String, Object> map = new HashMap<>();
        map.put("name", "Guitar");
        map.put("description", "An electronic guitar");
        map.put("category", "Guitar");
        map.put("price", "20, 000");
        map.put("stock", "In stock");
        map.put("likes", "4");
        map.put("imageLink", "image");
        map.put("key", key);

//        mGuitarDatabase.child(key).setValue(map)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getActivity(), "Successfully posted", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        initViews();



        return view;
    }

    private void initViews() {
        mProductsList.clear();
        mGuitarDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProductsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    mProductsList.add(product);
                }

                ProductsAdapter adapter = new ProductsAdapter(mProductsList, getContext());
                mGuitarRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mLoadingProgress.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mGuitarDatabase.keepSynced(true);
    }

}
