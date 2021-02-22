package com.gerray.fmsystem.ManagerModule.Lessee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.ManagerModule.Assets.AssetViewHolder;
import com.gerray.fmsystem.ManagerModule.Assets.FacilityAssets;
import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


public class LesseeFragment extends Fragment {
    private FloatingActionButton addLessee;
    FirebaseRecyclerOptions<LesCreate> options;
    FirebaseRecyclerAdapter<LesCreate, LesseeRecyclerViewHolder> adapter;
    DatabaseReference dbRef, reference;
    FirebaseAuth auth;

    public LesseeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_lessee, container, false);

        addLessee = view.findViewById(R.id.fab_lessee);
        addLessee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchLessee.class));
            }
        });
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        dbRef = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants").child(currentUser.getUid());
        options = new FirebaseRecyclerOptions.Builder<LesCreate>().setQuery(dbRef, LesCreate.class).build();
        adapter = new FirebaseRecyclerAdapter<LesCreate, LesseeRecyclerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LesseeRecyclerViewHolder holder, int position, @NonNull LesCreate model) {
                holder.room.setText(model.getRoom());
                holder.activity.setText(model.getActivityType());
                holder.contactName.setText(model.getContactName());
                holder.lesseeName.setText(model.getLesseeName());
            }

            @NonNull
            @Override
            public LesseeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new LesseeRecyclerViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.lessee_cards, parent, false));
            }
        };

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return view;
    }
}