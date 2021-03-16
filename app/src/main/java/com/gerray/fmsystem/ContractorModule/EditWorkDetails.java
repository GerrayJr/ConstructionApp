package com.gerray.fmsystem.ContractorModule;

import android.annotation.SuppressLint;
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

public class EditWorkDetails extends AppCompatActivity {
    private TextView edWork, edDescription, edStatus, edCost, edFacility, edDate;
    private ImageView imageView;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_work_details);

        Intent intent = getIntent();
        final String workID = Objects.requireNonNull(intent.getExtras()).getString("workID");

        edWork = findViewById(R.id.ed_work);
        edDescription = findViewById(R.id.ed_description);
        edStatus = findViewById(R.id.ed_status);
        edCost = findViewById(R.id.ed_cost);
        edFacility = findViewById(R.id.ed_facility);
        edDate = findViewById(R.id.ed_date);

        imageView = findViewById(R.id.editWork_image);

        Button btnUpdate = findViewById(R.id.btn_update_work);
        btnUpdate.setOnClickListener(v -> editWork(workID));

        assert workID != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Work Orders").child(workID);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                edWork.setText(Objects.requireNonNull(snapshot.child("workDescription").getValue()).toString());
                edDescription.setText(Objects.requireNonNull(snapshot.child("workDescription").getValue()).toString());
                edStatus.setText(Objects.requireNonNull(snapshot.child("status").getValue()).toString());
                edDate.setText(Objects.requireNonNull(snapshot.child("workDate").getValue()).toString());
                if (snapshot.child("cost").exists()) {
                    edCost.setText(Objects.requireNonNull(snapshot.child("cost").getValue()).toString());
                } else {
                    edCost.setText("Not Set");
                }
                String imageUrl = Objects.requireNonNull(snapshot.child("imageUrl").getValue()).toString();
                Picasso.with(EditWorkDetails.this).load(imageUrl).into(imageView);

                if (snapshot.child("fManagerID").exists()) {
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Facilities").child(Objects.requireNonNull(snapshot.child("fManagerID").getValue()).toString());
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            edFacility.setText(Objects.requireNonNull(snapshot1.child("facilityManager").getValue()).toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                } else {
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Lessees").child(Objects.requireNonNull(snapshot.child("lesseeID").getValue()).toString());
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            edFacility.setText(Objects.requireNonNull(snapshot1.child("contactName").getValue()).toString());
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
    }

    public void editWork(String workID) {
        UpdateDialog updateDialog = new UpdateDialog(workID);
        updateDialog.show(getSupportFragmentManager(), "Update Tag");
    }
}