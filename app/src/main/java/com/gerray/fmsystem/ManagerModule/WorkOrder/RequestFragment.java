package com.gerray.fmsystem.ManagerModule.WorkOrder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.LesseeModule.LesseeRequestClass;
import com.gerray.fmsystem.LesseeModule.LesseeRequestHolder;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RequestFragment extends Fragment {

    private DatabaseReference databaseReference, reference;
    FirebaseRecyclerAdapter<LesseeRequestClass, LesseeRequestHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<LesseeRequestClass> options;
    FirebaseUser firebaseUser;

    public RequestFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");

        options = new FirebaseRecyclerOptions.Builder<LesseeRequestClass>().setQuery(databaseReference, LesseeRequestClass.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LesseeRequestClass, LesseeRequestHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final LesseeRequestHolder holder, int position, @NonNull final LesseeRequestClass model) {
                final String lesseeID = model.getUserID();
                reference = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (Objects.equals(dataSnapshot.getKey(), firebaseUser.getUid())) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String user = Objects.requireNonNull(dataSnapshot1.child("userID").getValue()).toString();
                                    if (user.equals(lesseeID)) {
                                        holder.itemView.setVisibility(View.VISIBLE);
//                                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        final String lesseeName = Objects.requireNonNull(dataSnapshot1.child("lesseeName").getValue()).toString();
                                        holder.tvRoom.setText(dataSnapshot1.getKey());
                                        holder.tvLessee.setText(lesseeName);
                                        holder.tvDescription.setText(model.getDescription());
                                        holder.tvDate.setText(model.getRequestDate());
                                        holder.itemView.setOnClickListener(v -> {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                                            alertDialog.setMessage("Respond to this request.")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Yes", (dialog, which) -> {
                                                        String date = model.getRequestDate();
                                                        String description = model.getDescription();
                                                        Intent intent = new Intent(getActivity(), WorkDetails.class);
                                                        intent.putExtra("lessee", lesseeName);
                                                        intent.putExtra("date", date);
                                                        intent.putExtra("imageUrl", model.getImageUrl());
                                                        intent.putExtra("description", description);
                                                        startActivity(intent);
                                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                                            @SuppressLint("LogNotTimber")
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                                for (DataSnapshot dSnapshot : snapshot1.getChildren()) {
                                                                    for (DataSnapshot ignored : dSnapshot.getChildren()) {
                                                                        String requestID = Objects.requireNonNull(dSnapshot.child("requestID").getValue()).toString();
                                                                        if (requestID.equals(model.getRequestID())) {
                                                                            String key = dSnapshot.getKey();
                                                                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                                                            assert key != null;
                                                                            dbRef.child("Requests").child(key).removeValue()
                                                                                    .addOnCompleteListener(task -> {
                                                                                        if (task.isSuccessful()) {
                                                                                            Log.d("Remove Request", "Successfully Removed");
                                                                                        } else {
                                                                                            Log.d("Remove Request", "Request couldn't be deleted");
                                                                                        }
                                                                                    });

                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    })
                                                    .setNegativeButton("Dismiss", (dialog, which) -> {
//                                                                startActivity(new Intent(SearchLessee.this, SearchLessee.class));
                                                    });
                                            AlertDialog alert = alertDialog.create();
                                            alert.setTitle(model.getDescription());
                                            alert.show();
                                        });
                                    }
                                }

                            } else {
                                holder.itemView.setVisibility(View.GONE);
                                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public LesseeRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new LesseeRequestHolder(LayoutInflater.from(getActivity()).inflate(R.layout.request_card, parent, false));
            }
        };

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


        return view;
    }
}