package com.gerray.fmsystem.ManagerModule.Lessee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchLessee extends AppCompatActivity {

    private EditText searchField;

    private DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<LesCreate, SearchViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<LesCreate> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lessee);

        ImageView searchButton = findViewById(R.id.ic_search);
        searchField = findViewById(R.id.input_search);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Lessees");

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
                                String lesseeID = model.getLesseeID();
                                String userID = model.getUserID();
                                Intent intent = new Intent(SearchLessee.this, ConfirmAdd.class);
                                intent.putExtra("lesseeID", lesseeID);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
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