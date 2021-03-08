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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.CommunicationModule.ChatClass;
import com.gerray.fmsystem.CommunicationModule.ChatViewHolder;
import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LesseeRequestFragment extends Fragment {

    private DatabaseReference databaseReference, reference;
    FirebaseRecyclerAdapter<LesseeRequestClass, LesseeRequestHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<LesseeRequestClass> options;
    FirebaseUser firebaseUser;

    public LesseeRequestFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_lessee_request, container, false);


        FloatingActionButton addRequest = view.findViewById(R.id.fab_request);
        addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestPopUp.class);
                startActivity(intent);
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        reference = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants");

        options = new FirebaseRecyclerOptions.Builder<LesseeRequestClass>().setQuery(databaseReference, LesseeRequestClass.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LesseeRequestClass, LesseeRequestHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final LesseeRequestHolder holder, int position, @NonNull final LesseeRequestClass model) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String user = dataSnapshot1.child("userID").getValue().toString();
                                if (user.equals(model.getUserID())) {
                                    holder.tvRoom.setText(dataSnapshot1.getKey());
                                    holder.tvDescription.setText(model.getDescription());
                                    holder.tvDate.setText(model.getRequestDate());
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

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