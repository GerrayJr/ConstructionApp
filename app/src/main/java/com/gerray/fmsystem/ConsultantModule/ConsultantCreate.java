package com.gerray.fmsystem.ConsultantModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
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

import java.util.UUID;

public class ConsultantCreate extends AppCompatActivity {
    private Spinner locSpinner, catSpinner, specSpinner;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_create);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        Button btnContinue = findViewById(R.id.btnConsCreate);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultantCreate();
            }
        });

        locSpinner = findViewById(R.id.cons_location);
        catSpinner = findViewById(R.id.cons_category);
        specSpinner = findViewById(R.id.cons_specialization);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Consultants");
        progressDialog = new ProgressDialog(this);
    }

    @SuppressLint("SetTextI18n")
    private void consultantCreate() {
        progressDialog.setMessage("Updating");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Users").child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String firstName = null, secondName = null, email = null;
                            int phone = 123456;
                            if (snapshot.child("firstName").exists()) {
                                firstName = snapshot.child("firstName").getValue().toString().trim();
                            }
                            if (snapshot.child("secondName").exists()) {
                                secondName = snapshot.child("secondName").getValue().toString().trim();
                            }
                            if (snapshot.child("email").exists()) {
                                email = snapshot.child("email").getValue().toString().trim();
                            }
                            if (snapshot.child("phone").exists()) {
                                phone = Integer.parseInt(snapshot.child("phone").getValue().toString().trim());
                            }

                            final String consultantName = firstName + " " + secondName;
                            final String consLocation = locSpinner.getSelectedItem().toString().trim();
                            final String consCategory = catSpinner.getSelectedItem().toString().trim();
                            final String consSpecs = specSpinner.getSelectedItem().toString().trim();
                            final String userID = auth.getUid();

                            String nUrl = "null";

                            CreateConsultant createConsultant = new CreateConsultant(consultantName, consCategory, consSpecs, consLocation, userID, email, phone, nUrl);
                            databaseReference.child(userID).setValue(createConsultant);
                            progressDialog.dismiss();
                            ConsultantCreate.this.finish();
                            Toast.makeText(ConsultantCreate.this, "Welcome", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

    }
}
