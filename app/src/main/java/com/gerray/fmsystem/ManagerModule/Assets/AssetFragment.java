package com.gerray.fmsystem.ManagerModule.Assets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.ManagerModule.WorkOrder.WorkDetails;
import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class AssetFragment extends Fragment {
    FirebaseRecyclerOptions<FacilityAssets> options;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<FacilityAssets, AssetViewHolder> adapter;
    DatabaseReference dbRef;

    public AssetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_asset, container, false);

        FloatingActionButton addAsset = view.findViewById(R.id.fab_asset);
        addAsset.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AssetPopUp.class);
            startActivityForResult(intent, 1);
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        assert currentUser != null;
        dbRef = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid()).child("Assets");
        dbRef.keepSynced(true);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://fm-system-a9ad4.appspot.com/Asset");

        options = new FirebaseRecyclerOptions.Builder<FacilityAssets>().setQuery(dbRef, FacilityAssets.class).build();
        adapter = new FirebaseRecyclerAdapter<FacilityAssets, AssetViewHolder>(options) {
            @SuppressLint("LogNotTimber")
            @Override
            protected void onBindViewHolder(@NonNull final AssetViewHolder holder, int position, @NonNull final FacilityAssets model) {
                Picasso.with(getActivity()).load(model.getmImageUrl()).into(holder.imageView);
                holder.tvName.setText(model.getAssetName());
                holder.tvModel.setText(model.getAssetModel());
                holder.tvloc.setText(model.getLocation());
                holder.tvDate.setText(model.getPurchaseDate());
                holder.itemView.setOnClickListener(v -> {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setMessage(model.getAssetName())
                            .setCancelable(false)
                            .setPositiveButton("Maintenance", (dialog, which) -> startActivity(new Intent(getActivity(), WorkDetails.class)
                                    .putExtra("description", "Asset Maintenance/Repair")))
                            .setNegativeButton("Remove Asset", (dialog, which) -> {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Facilities").child(currentUser.getUid()).child("Assets").child(model.getAssetID()).removeValue()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Asset Deleted", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d("Delete Asset", "Asset couldn't be deleted");
                                            }
                                        });
                            });

                    AlertDialog alert = alertDialog.create();
                    alert.setTitle("Facility Assets");
                    alert.show();
//
                });
            }

            @NonNull
            @Override
            public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AssetViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.asset_itens, parent, false));
            }
        };


        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();


        return view;
    }
}