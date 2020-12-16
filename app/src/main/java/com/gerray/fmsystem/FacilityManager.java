package com.gerray.fmsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gerray.fmsystem.Authentication.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class FacilityManager extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mNav;
    private FrameLayout mFrame;
    private DrawerLayout drawerLayout;

    private AssetFragment assetFragment;
    private LesseeFragment lesseeFragment;
    private ChatFragment chatFragment;
    private WorkFragment workFragment;
    private RequestFragment requestFragment;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_manager);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        mFrame = findViewById(R.id.mFrame);
        mNav = findViewById(R.id.bottom_nav);

        Toolbar toolbar = findViewById(R.id.toolbar);
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
                        Toast.makeText(FacilityManager.this, "Location Activity", Toast.LENGTH_SHORT).show();
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
                        //auth.signOut();
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