package com.gerray.fmsystem.ManagerModule.Lessee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.ManagerModule.Assets.AssetPopUp;
import com.gerray.fmsystem.ManagerModule.FacilityManager;
import com.gerray.fmsystem.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class ConfirmAdd extends AppCompatActivity {

    private TextInputEditText roomNumber;
    private Button btnAdd, btnExit;

    DatabaseReference databaseReference, reference, ref, refLessee;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_add);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLessee();
            }
        });

        btnExit = findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmAdd.this, SearchLessee.class));
            }
        });

        roomNumber = findViewById(R.id.add_roomNo);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


    }

    public void addLessee() {
        progressDialog.setMessage("Adding Lessee");
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        final String roomNo = roomNumber.getText().toString().trim();
        final FirebaseUser currentUser = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("facilityID").exists()) {
                    final String facilityID = snapshot.child("facilityID").getValue().toString();
                    Intent intent = getIntent();
                    final String lesseeID = intent.getExtras().getString("lesseeID");
                    final String userID = intent.getExtras().getString("userID");
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants").child(currentUser.getUid()).child(roomNo);
                    refLessee = FirebaseDatabase.getInstance().getReference().child("Lessees").child(userID);
                    refLessee.addValueEventListener(new ValueEventListener() {
                        String lesseeName, contactName, activityType;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("lesseeName").exists()) {
                                lesseeName = snapshot.child("lesseeName").getValue().toString();
                            }
                            if (snapshot.child("contactName").exists()) {
                                contactName = snapshot.child("contactName").getValue().toString();
                            }
                            if (snapshot.child("activityType").exists()) {
                                activityType = snapshot.child("activityType").getValue().toString();
                            }

                            LesCreate lesCreate = new LesCreate(contactName, lesseeName, activityType, lesseeID, userID, roomNo);
                            databaseReference.setValue(lesCreate);

//                            for (int counter = 1; counter > 0; counter--) {
//                                int newOcc = occNo - 1;
//                                ref.child("occupancyNo").setValue(String.valueOf(newOcc));
//                            }


                            progressDialog.dismiss();
                            Toast.makeText(ConfirmAdd.this, "Lessee Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ConfirmAdd.this, SearchLessee.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref = reference.child("Profile");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("occupancyNo").exists()) {
                    final int occNo = Integer.parseInt(snapshot.child("occupancyNo").getValue().toString());

                    int newCode = occNo - 1;
                    ref.child("occupancyNo").setValue(newCode);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
}