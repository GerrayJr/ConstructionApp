package com.gerray.fmsystem.ManagerModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gerray.fmsystem.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class FacilityCreate extends AppCompatActivity {
    private TextInputEditText facName;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_create);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        Button btnContinue = findViewById(R.id.fac_continue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFacility();
            }
        });

        facName = findViewById(R.id.fac_name);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Facilities");
        progressDialog = new ProgressDialog(this);

    }

    private void registerFacility() {
        progressDialog.setMessage("Let us Begin");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Users").child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        String fManager;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String firstName = null, secondName = null;
                            if (snapshot.child("firstName").exists()) {
                                firstName = snapshot.child("firstName").getValue().toString().trim();
                            } else if (snapshot.child("secondName").exists()) {
                                secondName = snapshot.child("secondName").getValue().toString().trim();
                            }

                            final String fManager = firstName + " " + secondName;
                            final String name = facName.getText().toString().trim();
                            final String facID = UUID.randomUUID().toString();
                            final String userID = auth.getUid();

                            if (TextUtils.isEmpty(name)) {
                                Toast.makeText(FacilityCreate.this, "Enter Facility Name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            FacRegister facRegister = new FacRegister(facID, userID, name, fManager);
                            databaseReference.child(userID).setValue(facRegister);
                            progressDialog.dismiss();
                            FacilityCreate.this.finish();
                            Toast.makeText(FacilityCreate.this, "Welcome", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

    }
}
