package com.gerray.fmsystem.LesseeModule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.UUID;

public class LesseeProfileCreate extends AppCompatActivity {
    private TextInputEditText bznName;
    private Spinner activitySpinner;
    DatabaseReference databaseReference, reference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesse_profile_create);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Lessees");
        progressDialog = new ProgressDialog(this);

        bznName = findViewById(R.id.lessOg_name);
        activitySpinner = findViewById(R.id.spinner_lessee);

        Button btnRegister = findViewById(R.id.les_continue);
        btnRegister.setOnClickListener(v -> registerLessee());
    }

    private void registerLessee() {
        progressDialog.setMessage("Let us Begin");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Users").child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String firstName = null, secondName = null;
                            if (snapshot.child("firstName").exists()) {
                                firstName = Objects.requireNonNull(snapshot.child("firstName").getValue()).toString().trim();
                            }
                            if (snapshot.child("secondName").exists()) {
                                secondName = Objects.requireNonNull(snapshot.child("secondName").getValue()).toString().trim();
                            }

                            final String contactName = firstName + " " + secondName;
                            final String name = Objects.requireNonNull(bznName.getText()).toString().trim();
                            final String activity = activitySpinner.getSelectedItem().toString().trim();
                            final String lesseeID = UUID.randomUUID().toString();
                            final String userID = auth.getUid();

                            if (TextUtils.isEmpty(name)) {
                                Toast.makeText(LesseeProfileCreate.this, "Enter Organization Name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            LesCreate lesCreate = new LesCreate(contactName, name, activity, lesseeID, userID, null);
                            assert userID != null;
                            databaseReference.child(userID).setValue(lesCreate);
                            reference = databaseReference.child("Profile");
                            reference.setValue(lesCreate);
                            progressDialog.dismiss();
                            LesseeProfileCreate.this.finish();
                            Toast.makeText(LesseeProfileCreate.this, "Welcome", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

    }

}