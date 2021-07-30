package com.gerray.ConstructionApp.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.ConstructionApp.Authentication.LoginActivity;
import com.gerray.ConstructionApp.Home.Profile.ProfileActivity;
import com.gerray.ConstructionApp.Project.ProjectActivity;
import com.gerray.ConstructionApp.R;
import com.gerray.ConstructionApp.Settings.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseAuth auth;
    FirebaseRecyclerOptions<ProjectCreate> options;
    FirebaseRecyclerAdapter<ProjectCreate, ProjectViewHolder> adapter;
    DatabaseReference dbRef, reference;
    FloatingActionButton fab;

    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.startListening();
        adapter.notifyDataSetChanged();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String userCategory = snapshot.child("category").getValue().toString();
                if (!userCategory.equals("Construction Manager")) {
                    fab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        dbRef = FirebaseDatabase.getInstance().getReference().child("Projects").child(currentUser.getUid());
        dbRef.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<ProjectCreate>().setQuery(dbRef, ProjectCreate.class).build();
        adapter = new FirebaseRecyclerAdapter<ProjectCreate, ProjectViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProjectViewHolder holder, int position, @NonNull final ProjectCreate model) {
                holder.tvName.setText(model.getProjectName());
                holder.tvDate.setText(model.getCreationDate());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, ProjectActivity.class);
                        intent.putExtra("name", model.getProjectName());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ProjectViewHolder(LayoutInflater.from(HomeActivity.this).inflate(R.layout.project_recycler_items, parent, false));
            }
        };

        ArrayList<ProjectCreate> arrayList = new ArrayList<ProjectCreate>();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,PopUpWindow.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;

            case R.id.home_help:
                return true;

            case R.id.home_settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                return true;

            case R.id.home_share:
                shareApp();
                return true;

            case R.id.home_switch:

            case R.id.home_contact:
                contactUs();
                return true;

            case R.id.home_logout:
                auth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getTitle());
        startActivity(Intent.createChooser(intent, "Choose One"));
    }

    private void contactUs() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"gerrykyalo@gmail.com"});
//         i.putExtra(Intent.EXTRA_SUBJECT, "Subject of the Email");
//         i.putExtra(Intent.EXTRA_TEXT   , "Body");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(HomeActivity.this, "There are no Email Clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}