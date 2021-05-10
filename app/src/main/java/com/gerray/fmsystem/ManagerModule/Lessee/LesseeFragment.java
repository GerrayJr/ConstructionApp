package com.gerray.fmsystem.ManagerModule.Lessee;

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
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LesseeFragment extends Fragment {
    FirebaseRecyclerOptions<LesCreate> options;
    FirebaseRecyclerAdapter<LesCreate, LesseeRecyclerViewHolder> adapter;
    DatabaseReference dbRef;
    DatabaseReference ref;
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

        FloatingActionButton addLessee = view.findViewById(R.id.fab_lessee);
        addLessee.setOnClickListener(v -> startActivity(new Intent(getActivity(), SearchLessee.class)));
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        assert currentUser != null;
        dbRef = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants").child(currentUser.getUid());
        options = new FirebaseRecyclerOptions.Builder<LesCreate>().setQuery(dbRef, LesCreate.class).build();
        adapter = new FirebaseRecyclerAdapter<LesCreate, LesseeRecyclerViewHolder>(options) {
            @SuppressLint("LogNotTimber")
            @Override
            protected void onBindViewHolder(@NonNull LesseeRecyclerViewHolder holder, int position, @NonNull LesCreate model) {
                holder.room.setText(model.getRoom());
                holder.activity.setText(model.getActivityType());
                holder.contactName.setText(model.getContactName());
                holder.lesseeName.setText(model.getLesseeName());
                holder.itemView.setOnClickListener(v -> {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setMessage(model.getLesseeName())
                            .setCancelable(false)
                            .setPositiveButton("View Profile", (dialog, which) -> {
//                                startActivity(new Intent(getContext(), ViewLessee.class)
//                                        .putExtra("lesseeID", model.getLesseeID()));
                                Intent intent = new Intent(getActivity(),ViewLessee.class);
                                intent.putExtra("lesseeID",model.getUserID());
                                startActivity(intent);
                            })

                            .setNegativeButton("Remove Lessee", (dialog, which) -> {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants").child(currentUser.getUid());
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.child(model.room).exists())
                                        {
                                            snapshot.getRef().removeValue()
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Lessee Removed", Toast.LENGTH_SHORT).show();
                                                            ref = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid()).child("Profile");
                                                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.child("occupancyNo").exists()) {
                                                                        final int occNo = Integer.parseInt(Objects.requireNonNull(snapshot.child("occupancyNo").getValue()).toString());

                                                                        int newCode = occNo + 1;
                                                                        ref.child("occupancyNo").setValue(newCode);

                                                                    }
                                                                }


                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }

                                                            });
                                                        } else {
                                                            Log.d("Delete Asset", "Asset couldn't be deleted");
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });                                 
                            });

                    AlertDialog alert = alertDialog.create();
                    alert.setTitle("Facility Lessees");
                    alert.show();
//
                });

            }

            @NonNull
            @Override
            public LesseeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new LesseeRecyclerViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.lessee_cards, parent, false));
            }
        }

        ;

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return view;
    }
}