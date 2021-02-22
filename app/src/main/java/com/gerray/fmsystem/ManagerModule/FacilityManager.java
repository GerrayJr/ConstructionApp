package com.gerray.fmsystem.ManagerModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gerray.fmsystem.Authentication.LoginActivity;
import com.gerray.fmsystem.ManagerModule.Assets.AssetFragment;
import com.gerray.fmsystem.ManagerModule.ChatRoom.ChatFragment;
import com.gerray.fmsystem.ManagerModule.Consultants.FacilityConsultant;
import com.gerray.fmsystem.ManagerModule.Lessee.LesseeFragment;
import com.gerray.fmsystem.ManagerModule.Location.FacilityLocation;
import com.gerray.fmsystem.ManagerModule.Profile.FacilityProfile;
import com.gerray.fmsystem.ManagerModule.WorkOrder.RequestFragment;
import com.gerray.fmsystem.ManagerModule.WorkOrder.WorkFragment;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FacilityManager extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mNav;
    private FrameLayout mFrame;
    private DrawerLayout drawerLayout;

    private AssetFragment assetFragment;
    private LesseeFragment lesseeFragment;
    private ChatFragment chatFragment;
    private WorkFragment workFragment;
    private RequestFragment requestFragment;
    Toolbar toolbar;

    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_facility_manager);
        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Facilities")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(firebaseUser.getUid()).exists()) {
                                reference.child("Facilities").child(firebaseUser.getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String facilityID = null, managerID = null, managerName = null, facilityName = null;
                                                if (snapshot.child("facilityID").exists()) {
                                                    facilityID = snapshot.child("facilityID").getValue().toString();
                                                }
                                                if (snapshot.child("facilityManager").exists()) {
                                                    managerName = snapshot.child("facilityManager").getValue().toString();
                                                }
                                                if (snapshot.child("name").exists()) {
                                                    facilityName = snapshot.child("name").getValue().toString();
                                                    toolbar.setTitle(facilityName);
                                                }
                                                if (snapshot.child("userID").exists()) {
                                                    managerID = snapshot.child("userID").getValue().toString();
                                                }
                                                Toast.makeText(FacilityManager.this, facilityName + " " + managerName, Toast.LENGTH_SHORT).show();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("facilityID", facilityID);
                                                LesseeFragment lesseeFragment = new LesseeFragment();
                                                lesseeFragment.setArguments(bundle);
                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                Toast.makeText(FacilityManager.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(FacilityManager.this, FacilityCreate.class));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

        mFrame = findViewById(R.id.mFrame);
        mNav = findViewById(R.id.bottom_nav);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        assetFragment = new AssetFragment();
        lesseeFragment = new LesseeFragment();
        chatFragment = new ChatFragment();
        workFragment = new WorkFragment();
        requestFragment = new RequestFragment();

        setFragment(workFragment);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.drawer_contact:
                        contactUs();
                        break;
                    case R.id.drawer_help:
                        Toast.makeText(FacilityManager.this, "Help activity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_location:
                        startActivity(new Intent(FacilityManager.this, FacilityLocation.class));
                        break;
                    case R.id.drawer_team:
                        startActivity(new Intent(FacilityManager.this, FacilityConsultant.class));
                        break;
                    case R.id.drawer_profile:
                        startActivity(new Intent(FacilityManager.this, FacilityProfile.class));
                        break;
                    case R.id.drawer_share:
                        shareApp();
                        break;
                    case R.id.drawer_payment:
                        Toast.makeText(FacilityManager.this, "Payment Activity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_logOut:
                        auth.signOut();
                        startActivity(new Intent(FacilityManager.this, LoginActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        mNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_assets:
                        setFragment(assetFragment);
                        return true;

                    case R.id.nav_chat:
                        setFragment(chatFragment);
                        return true;

                    case R.id.nav_work:
                        setFragment(workFragment);
                        return true;

                    case R.id.nav_request:
                        setFragment(requestFragment);
                        return true;

                    case R.id.nav_lessee:
                        setFragment(lesseeFragment);
                        return true;

                    default:
                        return false;

                }
            }


        });
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            Toast.makeText(FacilityManager.this, "There are no Email Clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}