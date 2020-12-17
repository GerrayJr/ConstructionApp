package com.gerray.fmsystem.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.gerray.fmsystem.FacilityManager;
import com.gerray.fmsystem.FacilityCreate;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSelector extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selector);

        //Initializing Firebase Variables
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        final String[] userType = {null};

        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Users").child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //  Check user type and redirect accordingly
                            if (dataSnapshot.child("Category").exists()) {
                                String userType = dataSnapshot.child("Category").getValue().toString();
                                if (userType.equals("Facility Manager")) {
                                    startActivity(new Intent(UserSelector.this,
                                            FacilityManager.class));
                                }
                                if (userType.equals("Lessee")) {
                                    startActivity(new Intent(UserSelector.this,
                                            FacilityManager.class));
                                }
                                if (userType.equals("Consultant")) {
                                    startActivity(new Intent(UserSelector.this,
                                            FacilityManager.class));
                                }
                            }

                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }
}