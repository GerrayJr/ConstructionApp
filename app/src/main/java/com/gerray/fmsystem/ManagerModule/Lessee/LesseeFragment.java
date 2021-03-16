package com.gerray.fmsystem.ManagerModule.Lessee;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.gerray.fmsystem.CommunicationModule.ChatActivity;
import com.gerray.fmsystem.CommunicationModule.ChatClass;
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.ManagerModule.Assets.AssetViewHolder;
import com.gerray.fmsystem.ManagerModule.Assets.FacilityAssets;
import com.gerray.fmsystem.ManagerModule.Consultants.FacilityConsultant;
import com.gerray.fmsystem.ManagerModule.WorkOrder.WorkDetails;
import com.gerray.fmsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class LesseeFragment extends Fragment {
    private FloatingActionButton addLessee;
    FirebaseRecyclerOptions<LesCreate> options;
    FirebaseRecyclerAdapter<LesCreate, LesseeRecyclerViewHolder> adapter;
    DatabaseReference dbRef, databaseReference, reference, ref;
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
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setMessage(model.getLesseeName())
                                .setCancelable(false)
                                .setPositiveButton("View Profile", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })

                                .setNegativeButton("Remove Lessee", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("FacilityOccupants").child(currentUser.getUid()).child(model.getRoom()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Lessee Removed", Toast.LENGTH_SHORT).show();
                                                            ref = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid()).child("Profile");
                                                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.child("occupancyNo").exists()) {
                                                                        final int occNo = Integer.parseInt(snapshot.child("occupancyNo").getValue().toString());

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
                                                    }
                                                });
                                    }
                                });

                        AlertDialog alert = alertDialog.create();
                        alert.setTitle("Facility Lessees");
                        alert.show();
//
                    }
                });

            }

            @NonNull
            @Override
            public LesseeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                                       parent,
                                                               int viewType) {
                return new LesseeRecyclerViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.lessee_cards, parent, false));
            }
        }

        ;

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new

                LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return view;
    }
}