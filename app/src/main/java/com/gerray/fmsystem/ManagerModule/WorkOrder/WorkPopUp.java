package com.gerray.fmsystem.ManagerModule.WorkOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gerray.fmsystem.ContractorModule.EditWorkDetails;
import com.gerray.fmsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class WorkPopUp extends AppCompatActivity {
    private TextView edWork, edDescription, edStatus, edCost, edConsultant, edDate;
    private ImageView imageView;
    private Button btnDelete;

    private DatabaseReference reference, dbref;

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

        edWork = findViewById(R.id.ed_work);
        edDescription = findViewById(R.id.ed_description);
        edStatus = findViewById(R.id.ed_status);
        edCost = findViewById(R.id.ed_cost);
        edConsultant = findViewById(R.id.ed_consultant);
        edDate = findViewById(R.id.ed_date);

        imageView = findViewById(R.id.editWork_image);
        reference = FirebaseDatabase.getInstance().getReference().child("Work Orders").child(workID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                edWork.setText(snapshot.child("workDescription").getValue().toString());
                edDescription.setText(snapshot.child("workDescription").getValue().toString());
                edStatus.setText(snapshot.child("status").getValue().toString());
                edDate.setText(snapshot.child("workDate").getValue().toString());
                if (snapshot.child("cost").exists())
                {
                    edCost.setText(snapshot.child("cost").getValue().toString());
                }else {
                    edCost.setText("Not Set");
                }
                String imageUrl = snapshot.child("imageUrl").getValue().toString();
                Picasso.with(WorkPopUp.this).load(imageUrl).into(imageView);

                dbref = FirebaseDatabase.getInstance().getReference().child("Consultants").child(snapshot.child("consultantID").getValue().toString());
                dbref.addValueEventListener(new ValueEventListener() {
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
    }
}