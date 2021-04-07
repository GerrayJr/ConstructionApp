package com.gerray.fmsystem.ManagerModule.WorkOrder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.ManagerModule.FacilityManager;
import com.gerray.fmsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class WorkPopUp extends AppCompatActivity {
    private TextView edWork, edStatus, edCost, edConsultant, edDate;
    private ImageView imageView;

    private DatabaseReference dbRef;
    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_pop_up);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        Intent intent = getIntent();
        final String workID = Objects.requireNonNull(intent.getExtras()).getString("workID");

        edWork = findViewById(R.id.ed_pop_work);
        edStatus = findViewById(R.id.ed_pop_status);
        edCost = findViewById(R.id.ed_pop_cost);
        edConsultant = findViewById(R.id.ed_pop_consultant);
        edDate = findViewById(R.id.ed_pop_date);

        imageView = findViewById(R.id.asset_imageView);
        assert workID != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Work Orders").child(workID);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                edWork.setText(Objects.requireNonNull(snapshot.child("workDescription").getValue()).toString());
                edStatus.setText(Objects.requireNonNull(snapshot.child("status").getValue()).toString());
                edDate.setText(Objects.requireNonNull(snapshot.child("workDate").getValue()).toString());
                if (snapshot.child("cost").exists()) {
                    edCost.setText(Objects.requireNonNull(snapshot.child("cost").getValue()).toString());
                } else {
                    edCost.setText("Not Set");
                }
                String imageUrl = Objects.requireNonNull(snapshot.child("imageUrl").getValue()).toString();
                Picasso.with(WorkPopUp.this).load(imageUrl).into(imageView);

                dbRef = FirebaseDatabase.getInstance().getReference().child("Contractor").child(Objects.requireNonNull(snapshot.child("consultantID").getValue()).toString());
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        edConsultant.setText(Objects.requireNonNull(snapshot1.child("consultantName").getValue()).toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button btnDelete = findViewById(R.id.delete_work);
        btnDelete.setOnClickListener(v -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Work Orders").child(workID).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(WorkPopUp.this, FacilityManager.class));
                            Toast.makeText(WorkPopUp.this, "Work Removed", Toast.LENGTH_SHORT).show();


                        } else {
                            Log.d("Delete Work", "Work couldn't be deleted");
                        }
                    });
        });
    }
}