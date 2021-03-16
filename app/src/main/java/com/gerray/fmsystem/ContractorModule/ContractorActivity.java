package com.gerray.fmsystem.ContractorModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toast;

import com.gerray.fmsystem.Authentication.LoginActivity;
import com.gerray.fmsystem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContractorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;
    FirebaseUser firebaseUser;

    Toolbar toolbar;

    ContractorProfile contractorProfile;
    ContractorChat contractorChat;
    ContractorWork contractorWork;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant);

        BottomNavigationView mNav = findViewById(R.id.bottom_nav);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Consultants")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(firebaseUser.getUid()).exists()) {
                                reference.child("Consultants").child(firebaseUser.getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            } else {
                                startActivity(new Intent(ContractorActivity.this, ContractorCreate.class));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

        contractorProfile = new ContractorProfile();
        contractorChat = new ContractorChat();
        contractorWork = new ContractorWork();

        setFragment(contractorWork);
        mNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_profile_cons:
                    setFragment(contractorProfile);
                    return true;

                case R.id.nav_work_cons:
                    setFragment(contractorWork);
                    return true;

                case R.id.nav_chat_cons:
                    setFragment(contractorChat);
                    return true;

                default:
                    return false;

            }
        });
        mNav.setSelectedItemId(R.id.nav_work_cons);
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mFrame, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cons_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cons_update:
                startActivity(new Intent(this, UpdateContractor.class));
                return true;

            case R.id.cons_settings:
//                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                return true;

            case R.id.cons_share:
                shareApp();
                return true;

            case R.id.cons_contact:
                contactUs();
                return true;

            case R.id.cons_logout:
                auth.signOut();
                startActivity(new Intent(ContractorActivity.this, LoginActivity.class));
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
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"facilityapp@gmail.com"});
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContractorActivity.this, "There are no Email Clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}