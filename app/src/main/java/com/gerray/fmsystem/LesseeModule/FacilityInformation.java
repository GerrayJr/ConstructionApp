package com.gerray.fmsystem.LesseeModule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gerray.fmsystem.ManagerModule.Profile.FacilityProfile;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FacilityInformation extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;
    FirebaseUser firebaseUser;

    private TextView facilityName, facilityAuth, facilityManager, facilityPostal, facilityEmail, facilityActivity, facilityOccupancy;
    private ImageView facilityImageView;
    private Button btnExit, btnContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_information);

        facilityName = findViewById(R.id.fInfo_name);
        facilityAuth = findViewById(R.id.fInfo_auth);
        facilityManager = findViewById(R.id.fInfo_manager);
        facilityPostal = findViewById(R.id.fInfo_postal);
        facilityEmail = findViewById(R.id.fInfo_email);
        facilityActivity = findViewById(R.id.fInfo_activity);
        facilityOccupancy = findViewById(R.id.fInfo_occupancy);

        btnContact = findViewById(R.id.btn_contact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnExit = findViewById(R.id.btn_exit_fInfo);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacilityInformation.this, FindFacility.class));
            }
        });

        facilityImageView = findViewById(R.id.fInfo_imageView);
        String userID = getIntent().getStringExtra("userID");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference().child("Facilities").child(userID).child("Profile");
        firebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String facilityInfoName = getIntent().getStringExtra("title");
                if (snapshot.child("facilityName").exists()) {
                    String fName = snapshot.child("facilityName").getValue().toString();
                    if (fName.equals(facilityInfoName)) {
                        String authName = snapshot.child("authorityName").getValue().toString().trim();
                        facilityAuth.setText(authName);
                        String emailAddress = snapshot.child("emailAddress").getValue().toString().trim();
                        facilityEmail.setText(emailAddress);
                        facilityName.setText(facilityInfoName);
                        String facilityType = snapshot.child("facilityType").getValue().toString().trim();
                        facilityActivity.setText(facilityType);
                        String occNo = snapshot.child("occupancyNo").getValue().toString().trim();
                        facilityOccupancy.setText(occNo);
                        String postal = snapshot.child("postalAddress").getValue().toString().trim();
                        facilityPostal.setText(postal);
                        String imageUrl = snapshot.child("facilityImageUrl").getValue().toString().trim();
                        Picasso.with(FacilityInformation.this).load(imageUrl).into(facilityImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference = firebaseDatabase.getReference().child("Facilities").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
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
    }
}
