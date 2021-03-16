package com.gerray.fmsystem.LesseeModule;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FacilityInformation extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;

    private TextView facilityName, facilityAuth, facilityManager, facilityPostal, facilityEmail, facilityActivity, facilityOccupancy;
    private ImageView facilityImageView;

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

        Button btnContact = findViewById(R.id.btn_contact);
        btnContact.setOnClickListener(v -> {

        });

        Button btnExit = findViewById(R.id.btn_exit_fInfo);
        btnExit.setOnClickListener(v -> startActivity(new Intent(FacilityInformation.this, FindFacility.class)));

        facilityImageView = findViewById(R.id.fInfo_imageView);
        String userID = getIntent().getStringExtra("userID");

        firebaseDatabase = FirebaseDatabase.getInstance();
        assert userID != null;
        firebaseDatabaseReference = firebaseDatabase.getReference().child("Facilities").child(userID).child("Profile");
        firebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String facilityInfoName = getIntent().getStringExtra("title");
                if (snapshot.child("facilityName").exists()) {
                    String fName = Objects.requireNonNull(snapshot.child("facilityName").getValue()).toString();
                    if (fName.equals(facilityInfoName)) {
                        String authName = Objects.requireNonNull(snapshot.child("authorityName").getValue()).toString().trim();
                        facilityAuth.setText(authName);
                        String emailAddress = Objects.requireNonNull(snapshot.child("emailAddress").getValue()).toString().trim();
                        facilityEmail.setText(emailAddress);
                        facilityName.setText(facilityInfoName);
                        String facilityType = Objects.requireNonNull(snapshot.child("facilityType").getValue()).toString().trim();
                        facilityActivity.setText(facilityType);
                        String occNo = Objects.requireNonNull(snapshot.child("occupancyNo").getValue()).toString().trim();
                        facilityOccupancy.setText(occNo);
                        String postal = Objects.requireNonNull(snapshot.child("postalAddress").getValue()).toString().trim();
                        facilityPostal.setText(postal);
                        String imageUrl = Objects.requireNonNull(snapshot.child("facilityImageUrl").getValue()).toString().trim();
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
                    String facManager = Objects.requireNonNull(snapshot.child("facilityManager").getValue()).toString().trim();
                    facilityManager.setText(facManager);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
