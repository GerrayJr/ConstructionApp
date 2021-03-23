package com.gerray.fmsystem.LesseeModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gerray.fmsystem.ManagerModule.Profile.FacilityProfile;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class LesseeProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference, dbRef;
    FirebaseUser firebaseUser;

    TextView lesseeName, contactName, activityType, phone, email, facilityName, roomNo;
    ImageView lesseeImage;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_profile);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        dbRef = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        lesseeName = findViewById(R.id.lesseeProf_name);
        contactName = findViewById(R.id.lesseeProf_contact);
        activityType = findViewById(R.id.lesseeProf_activity);
        phone = findViewById(R.id.lesseeProf_phone);
        email = findViewById(R.id.lesseeProf_email);
        facilityName = findViewById(R.id.lesseeProf_facility);
        roomNo = findViewById(R.id.lesseeProf_room);

        lesseeImage = findViewById(R.id.lessee_imageView);
        btnUpdate = findViewById(R.id.lesseeProfile_update);
        btnUpdate.setOnClickListener(v -> startActivity(new Intent(LesseeProfileActivity.this, LesseeProfileUpdate.class)));

        if (firebaseUser != null) {
            reference.child("Lessees").child(firebaseUser.getUid()).child("Profile")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("lesseeName").exists()) {
                                lesseeName.setText(snapshot.child("lesseeName").getValue().toString());
                            }
                            if (snapshot.child("contactName").exists()) {
                                contactName.setText(snapshot.child("contactName").getValue().toString());
                            }
                            if (snapshot.child("activityType").exists()) {
                                activityType.setText(snapshot.child("activityType").getValue().toString());
                            }
                            if (snapshot.child("emailAddress").exists()) {
                                email.setText(snapshot.child("emailAddress").getValue().toString());
                            }
                            if (snapshot.child("phoneNumber").exists()) {
                                phone.setText(snapshot.child("phoneNumber").getValue().toString());
                            }
                            if (snapshot.child("uri").exists()) {
                                String imageUrl = Objects.requireNonNull(snapshot.child("uri").getValue()).toString().trim();
                                Picasso.with(LesseeProfileActivity.this).load(imageUrl).into(lesseeImage);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            firebaseDatabaseReference.child("FacilityOccupants")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String userID = dataSnapshot1.child("userID").getValue().toString();
                                    String fID = dataSnapshot.getKey();
                                    if (firebaseUser.getUid().equals(userID)) {
                                        roomNo.setText(dataSnapshot1.getKey());
                                        dbRef.child("Facilities").child(fID).child("Profile")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.child("facilityName").exists()) {
                                                            facilityName.setText(snapshot.child("facilityName").getValue().toString());
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        }

    }
}