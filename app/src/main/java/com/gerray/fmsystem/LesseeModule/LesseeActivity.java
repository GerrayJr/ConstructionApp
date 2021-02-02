package com.gerray.fmsystem.LesseeModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gerray.fmsystem.ManagerModule.FacilityCreate;
import com.gerray.fmsystem.ManagerModule.FacilityManager;
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

public class LesseeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;
    FirebaseUser firebaseUser;

    private BottomNavigationView mNav;
    private FrameLayout mFrame;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;;

    private LesseeAssetsFragment lesseeAssetsFragment;
    private LesseeChatFragment lesseeChatFragment;
    private LesseeRequestFragment lesseeRequestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee);

        auth = FirebaseAuth.getInstance();

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
                                reference.child("Lessee").child(firebaseUser.getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

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

        mFrame = findViewById(R.id.mFrame);
        mNav = findViewById(R.id.bottom_nav);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        lesseeAssetsFragment = new LesseeAssetsFragment();
        lesseeChatFragment = new LesseeChatFragment();
        lesseeRequestFragment = new LesseeRequestFragment();

        setFragment(lesseeChatFragment);
        mNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
            }


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
}