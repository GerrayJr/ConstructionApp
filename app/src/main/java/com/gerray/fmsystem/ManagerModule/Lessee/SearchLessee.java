package com.gerray.fmsystem.ManagerModule.Lessee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SearchLessee extends AppCompatActivity {

    private EditText searchField;

    private DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<LesCreate, SearchViewHolder> firebaseRecyclerAdapter, adapter;
    FirebaseRecyclerOptions<LesCreate> options, firebaseRecyclerOptions;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lessee);

        ImageView searchButton = findViewById(R.id.ic_search);
        searchField = findViewById(R.id.input_search);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Lessees");
        auth = FirebaseAuth.getInstance();

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<LesCreate>().setQuery(databaseReference, LesCreate.class).build();
        adapter = new FirebaseRecyclerAdapter<LesCreate, SearchViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull LesCreate model) {
                holder.contactName.setText(model.getContactName());
                holder.lesseeName.setText(model.getLesseeName());
                holder.activity.setText(model.getActivityType());
                holder.itemView.setOnClickListener(v -> {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchLessee.this);
                    alertDialog.setMessage("Do You Want to Add Lessee to The Facility?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants").child(Objects.requireNonNull(auth.getUid()));
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                                if (dataSnapshot.child("userID").exists()){
                                                    String uid = dataSnapshot.child("userID").getValue().toString();
                                                    if (model.userID.equals(uid)){
                                                        Toast.makeText(SearchLessee.this, "User is already registered in a Facility", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }else{
                                                        String lesseeID = model.getLesseeID();
                                                        String userID = model.getUserID();
                                                        Intent intent = new Intent(SearchLessee.this, ConfirmAdd.class);
                                                        intent.putExtra("lesseeID", lesseeID);
                                                        intent.putExtra("userID", userID);
                                                        startActivity(intent);
                                                    }
                                                }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            })
                            .setNegativeButton("No", (dialog, which) -> startActivity(new Intent(SearchLessee.this, SearchLessee.class)));
                    AlertDialog alert = alertDialog.create();
                    alert.setTitle("Add New Lessee");
                    alert.show();
                });
            }

            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new SearchViewHolder(LayoutInflater.from(SearchLessee.this).inflate(R.layout.search_lessee_cards, parent, false));
            }
        };

        RecyclerView lesseeList = findViewById(R.id.recycler_view_search);
        lesseeList.setLayoutManager(new LinearLayoutManager(this));
        lesseeList.setAdapter(adapter);
        adapter.startListening();

        searchButton.setOnClickListener(v -> {
            String contactSearch = searchField.getText().toString();
            lesseeSearch(contactSearch);
        });
    }

    public void lesseeSearch(String searchText) {

        Query searchQuery = databaseReference.orderByChild("contactName").startAt(searchText).endAt(searchText + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<LesCreate>().setQuery(searchQuery, LesCreate.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LesCreate, SearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SearchViewHolder holder, int position, @NonNull final LesCreate model) {
                holder.contactName.setText(model.getContactName());
                holder.lesseeName.setText(model.getLesseeName());
                holder.activity.setText(model.getActivityType());
                holder.itemView.setOnClickListener(v -> {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchLessee.this);
                    alertDialog.setMessage("Do You Want to Add Lessee to The Facility?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants");
                                reference1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                if (dataSnapshot1.child("userID").exists()){
                                                    String uid = dataSnapshot1.child("userID").getValue().toString();
                                                    if (model.userID.equals(uid)){
                                                        Toast.makeText(SearchLessee.this, "User is already registered in a Facility", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }else{
                                                        String lesseeID = model.getLesseeID();
                                                        String userID = model.getUserID();
                                                        Intent intent = new Intent(SearchLessee.this, ConfirmAdd.class);
                                                        intent.putExtra("lesseeID", lesseeID);
                                                        intent.putExtra("userID", userID);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            })
                            .setNegativeButton("No", (dialog, which) -> startActivity(new Intent(SearchLessee.this, SearchLessee.class)));
                    AlertDialog alert = alertDialog.create();
                    alert.setTitle("Add New Lessee");
                    alert.show();
                });
            }

            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new SearchViewHolder(LayoutInflater.from(SearchLessee.this).inflate(R.layout.search_lessee_cards, parent, false));
            }
        };

        RecyclerView resultList = findViewById(R.id.recycler_view_search);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

}