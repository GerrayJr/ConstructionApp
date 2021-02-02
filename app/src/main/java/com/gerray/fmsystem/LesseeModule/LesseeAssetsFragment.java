package com.gerray.fmsystem.LesseeModule;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.ManagerModule.Assets.AssetPopUp;
import com.gerray.fmsystem.ManagerModule.Assets.AssetViewHolder;
import com.gerray.fmsystem.ManagerModule.Assets.FacilityAssets;
import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class LesseeAssetsFragment extends Fragment {
    private FloatingActionButton addAsset;
    FirebaseRecyclerOptions<LesseeAssets> options;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<LesseeAssets, LesseeAssetViewHolder> adapter;
    DatabaseReference dbRef;

    public LesseeAssetsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_lessee_assets, container, false);

        addAsset = view.findViewById(R.id.fab_asset);
        addAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LesseeAssetPopUp.class);
                startActivityForResult(intent, 1);
            }
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        dbRef = FirebaseDatabase.getInstance().getReference().child("Lessees").child(currentUser.getUid()).child("Assets");
        dbRef.keepSynced(true);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://fm-system-a9ad4.appspot.com/Lessees");

        options = new FirebaseRecyclerOptions.Builder<LesseeAssets>().setQuery(dbRef, LesseeAssets.class).build();
        adapter = new FirebaseRecyclerAdapter<LesseeAssets, LesseeAssetViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final LesseeAssetViewHolder holder, int position, @NonNull final LesseeAssets model) {
                Picasso.with(getActivity()).load(model.getmImageUrl()).into(holder.imageView);
                holder.tvName.setText(model.getAssetName());
                holder.tvModel.setText(model.getAssetModel());
                holder.tvloc.setText(model.getLocation());
                holder.tvDate.setText(model.getPurchaseDate());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(HomeActivity.this, ProjectActivity.class);
//                        intent.putExtra("name",model.getProjectName());
//                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public LesseeAssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new LesseeAssetViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.asset_itens, parent, false));
            }
        };


//        ArrayList<FacilityAssets> arrayList = new ArrayList<FacilityAssets>();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();


        return view;
    }
}