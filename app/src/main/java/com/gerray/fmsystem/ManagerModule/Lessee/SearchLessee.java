package com.gerray.fmsystem.ManagerModule.Lessee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchLessee extends AppCompatActivity {

    private EditText searchField;
    private ImageView searchButton;

    private RecyclerView resultList;
    private DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<LesCreate, SearchViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<LesCreate> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lessee);

        searchButton = findViewById(R.id.ic_search);
        searchField = findViewById(R.id.input_search);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Lessees");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactSearch = searchField.getText().toString();
                lesseeSearch(contactSearch);
            }
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
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchLessee.this);
                        alertDialog.setMessage("Do You Want to Add Lessee to The Facility?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String lesseeID = model.getLesseeID();
                                        String userID = model.getUserID();
                                        Intent intent = new Intent(SearchLessee.this, ConfirmAdd.class);
                                        intent.putExtra("lesseeID", lesseeID);
                                        intent.putExtra("userID", userID);
                                        startActivity(intent);
                                        return;
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(SearchLessee.this, SearchLessee.class));
                                    }
                                });
                        AlertDialog alert = alertDialog.create();
                        alert.setTitle("Add New Lessee");
                        alert.show();
                    }
                });
            }

            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new SearchViewHolder(LayoutInflater.from(SearchLessee.this).inflate(R.layout.search_lessee_cards, parent, false));
            }
        };

        resultList = findViewById(R.id.recycler_view_search);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

}