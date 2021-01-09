package com.gerray.fmsystem.ManagerModule.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FacilityProfile extends AppCompatActivity {
    private TextView facilityName, facilityAuth, facilityManager, facilityPostal, facilityEmail, facilityActivity, facilityOccupancy;
    private ImageView facilityImage;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_profile);

        facilityName = findViewById(R.id.tv_prof_name);
        facilityAuth = findViewById(R.id.tv_prof_auth);
        facilityManager = findViewById(R.id.tv_prof_manager);
        facilityPostal = findViewById(R.id.tv_prof_postal);
        facilityEmail = findViewById(R.id.tv_prof_email);
        facilityActivity = findViewById(R.id.tv_prof_activity);
        facilityOccupancy = findViewById(R.id.tv_prof_occupancy);

        facilityImage = findViewById(R.id.facility_imageView);

        Button btnUpdate = findViewById(R.id.profile_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacilityProfile.this, ProfilePopUp.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            reference.child("Facilities").child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("facilityManager").exists()) {
                                String facManager = snapshot.child("facilityManager").getValue().toString().trim();
                                facilityManager.setText(facManager);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            if (firebaseUser != null) {
                firebaseDatabaseReference.child("Facilities").child(firebaseUser.getUid()).child("Profile")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("authorityName").exists()) {
                                    String authName = snapshot.child("authorityName").getValue().toString().trim();
                                    facilityAuth.setText(authName);
                                }
                                if (snapshot.child("emailAddress").exists()) {
                                    String emailAddress = snapshot.child("emailAddress").getValue().toString().trim();
                                    facilityEmail.setText(emailAddress);
                                }
                                if (snapshot.child("facilityName").exists()) {
                                    String facName = snapshot.child("facilityName").getValue().toString().trim();
                                    facilityName.setText(facName);
                                }
                                if (snapshot.child("facilityType").exists()) {
                                    String facilityType = snapshot.child("facilityType").getValue().toString().trim();
                                    facilityActivity.setText(facilityType);
                                }
                                if (snapshot.child("occupancyNo").exists()) {
                                    String occNo = snapshot.child("occupancyNo").getValue().toString().trim();
                                    facilityOccupancy.setText(occNo);
                                }
                                if (snapshot.child("postalAddress").exists()) {
                                    String postal = snapshot.child("postalAddress").getValue().toString().trim();
                                    facilityPostal.setText(postal);
                                }
                                if (snapshot.child("facilityImageUrl").exists()) {
                                    String imageUrl = snapshot.child("facilityImageUrl").getValue().toString().trim();
                                    Picasso.with(FacilityProfile.this).load(imageUrl).into(facilityImage);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        }
    }
}