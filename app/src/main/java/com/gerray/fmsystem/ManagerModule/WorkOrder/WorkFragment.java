package com.gerray.fmsystem.ManagerModule.WorkOrder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.LesseeModule.LesseeRequestClass;
import com.gerray.fmsystem.LesseeModule.LesseeRequestHolder;
import com.gerray.fmsystem.ManagerModule.Lessee.SearchLessee;
import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkFragment extends Fragment {
    private DatabaseReference databaseReference, reference;
    FirebaseRecyclerAdapter<DetailsClass, WorkViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<DetailsClass> options;
    FirebaseUser firebaseUser;

    public WorkFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_work, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Work Orders");

        options = new FirebaseRecyclerOptions.Builder<DetailsClass>().setQuery(databaseReference, DetailsClass.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DetailsClass, WorkViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final WorkViewHolder holder, int position, @NonNull final DetailsClass model) {
                if (model.getfManagerID() != null) {
                    if (model.getfManagerID().equals(firebaseUser.getUid())) {
                        holder.tvStatus.setText(model.getStatus());
                        holder.tvWork.setText(model.getWorkDescription());
                        holder.tvWorkDate.setText(model.getWorkDate());
                        reference = FirebaseDatabase.getInstance().getReference().child("Consultants").child(model.getConsultantID());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                holder.tvConsultant.setText(snapshot.child("consultantName").getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), WorkPopUp.class);
                                intent.putExtra("workID", model.getWorkID());
                                startActivity(intent);
                            }
                        });
                    }
                }else {
                    holder.itemView.setVisibility(View.GONE);
                }


            }

            @NonNull
            @Override
            public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new WorkViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.work_card, parent, false));
            }
        };

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_work);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


        return view;
    }
}