package com.gerray.fmsystem.LesseeModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gerray.fmsystem.Authentication.LoginActivity;
import com.gerray.fmsystem.R;
import com.gerray.fmsystem.ManagerModule.TransactionActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class LesseeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;
    FirebaseUser firebaseUser;

    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    private LesseeAssetsFragment lesseeAssetsFragment;
    private LesseeChatFragment lesseeChatFragment;
    private LesseeRequestFragment lesseeRequestFragment;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee);

        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view_lessee);
        View hView = navigationView.inflateHeaderView(R.layout.nav_header_lessee);

        TextView textView = hView.findViewById(R.id.nav_lessee_tv);
        ImageView imageView = hView.findViewById(R.id.nav_lessee_image);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Lessees")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(firebaseUser.getUid()).exists()) {
                                reference.child("Lessees").child(firebaseUser.getUid()).child("Profile")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.child("lesseeName").exists()) {
                                                    String lesseeName = Objects.requireNonNull(snapshot.child("lesseeName").getValue()).toString();
                                                    toolbar.setTitle(lesseeName);
                                                    textView.setText(lesseeName);
                                                }
                                                if (snapshot.child("uri").exists()) {
                                                    String imageUrl = Objects.requireNonNull(snapshot.child("uri").getValue()).toString().trim();
                                                    Picasso.with(LesseeActivity.this).load(imageUrl).into(imageView);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                Toast.makeText(LesseeActivity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(LesseeActivity.this, LesseeProfileCreate.class));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        BottomNavigationView mNav = findViewById(R.id.bottom_lessee_nav);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        lesseeAssetsFragment = new LesseeAssetsFragment();
        lesseeChatFragment = new LesseeChatFragment();
        lesseeRequestFragment = new LesseeRequestFragment();

        setFragment(lesseeChatFragment);
        mNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_lesseeAssets:
                    setFragment(lesseeAssetsFragment);
                    return true;

                case R.id.nav_lesseeChat:
                    setFragment(lesseeChatFragment);
                    return true;

                case R.id.nav_lesseeRequest:
                    setFragment(lesseeRequestFragment);
                    return true;

                default:
                    return false;

            }
        });
        mNav.setSelectedItemId(R.id.nav_lesseeChat);


        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.drawer_lessee_contact:
                    contactUs();
                    break;
                case R.id.drawer_lessee_help:
                    Toast.makeText(LesseeActivity.this, "Help activity", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.drawer_facilityLocate:
                    startActivity(new Intent(LesseeActivity.this, FindFacility.class));
                    //To be added
                    break;
                case R.id.drawer_consLessee:
                    startActivity(new Intent(LesseeActivity.this, LesseeConsultant.class));
                    break;
                case R.id.drawer_lessee_profile:
                    startActivity(new Intent(LesseeActivity.this, LesseeProfileActivity.class));
                    break;
                case R.id.drawer_lessee_share:
                    shareApp();
                    break;
                case R.id.drawer_lessee_pay:
                    startActivity(new Intent(LesseeActivity.this, LesseeTransaction.class));
                    break;
                case R.id.drawer_lessee_logOut:
                    auth.signOut();
                    startActivity(new Intent(LesseeActivity.this, LoginActivity.class));
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mFrame, fragment);
        fragmentTransaction.commit();
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
            Toast.makeText(LesseeActivity.this, "There are no Email Clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}