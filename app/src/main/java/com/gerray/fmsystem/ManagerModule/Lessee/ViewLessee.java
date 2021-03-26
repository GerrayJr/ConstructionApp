package com.gerray.fmsystem.ManagerModule.Lessee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerray.fmsystem.LesseeModule.LesseeProfileActivity;
import com.gerray.fmsystem.LesseeModule.LesseeProfileUpdate;
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

public class ViewLessee extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference, dbRef;
    FirebaseUser firebaseUser;

    TextView lesseeName, contactName, activityType, phone, email;
    ImageView lesseeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lessee);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        Intent intent = getIntent();
        String lesseeID = intent.getExtras().getString("lesseeID");

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        dbRef = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        lesseeName = findViewById(R.id.viewProf_name);
        contactName = findViewById(R.id.viewProf_contact);
        activityType = findViewById(R.id.viewProf_activity);
        phone = findViewById(R.id.viewProf_phone);
        email = findViewById(R.id.viewProf_email);

        lesseeImage = findViewById(R.id.view_imageView);

        if (lesseeID != null) {
            reference.child("Lessees").child(lesseeID).child("Profile")
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
                                Picasso.with(ViewLessee.this).load(imageUrl).into(lesseeImage);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } else {
            Toast.makeText(this, "No LesseeID", Toast.LENGTH_SHORT).show();
        }
    }
}