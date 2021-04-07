package com.gerray.fmsystem.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.gerray.fmsystem.ContractorModule.ContractorActivity;
import com.gerray.fmsystem.LesseeModule.LesseeActivity;
import com.gerray.fmsystem.ManagerModule.FacilityManager;
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

        ProgressBar pgsBar = findViewById(R.id.pBar);
        pgsBar.setVisibility(View.VISIBLE);

        //Initializing Firebase Variables
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Users").child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //  Check user type and redirect accordingly
                            if (dataSnapshot.child("category").exists()) {
                                String userType = dataSnapshot.child("category").getValue().toString();
                                switch (userType) {
                                    case "Facility Manager":
                                        startActivity(new Intent(UserSelector.this,
                                                FacilityManager.class));
                                        break;
                                    case "Lessee":
                                        startActivity(new Intent(UserSelector.this,
                                                LesseeActivity.class));
                                        break;
                                    case "Contractor":
                                        startActivity(new Intent(UserSelector.this,
                                                ContractorActivity.class));
                                        break;
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