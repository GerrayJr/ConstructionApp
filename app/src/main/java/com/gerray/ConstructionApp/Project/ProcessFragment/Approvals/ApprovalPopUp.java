package com.gerray.ConstructionApp.Project.ProcessFragment.Approvals;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.gerray.ConstructionApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApprovalPopUp extends AppCompatActivity {

    private FirebaseAuth auth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    EditText approvalName, approvalDate;
    Button btnApproval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_pop_up);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .3));
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Projects").child(currentUser.getUid()).child("Approvals");
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        approvalName = findViewById(R.id.approval_name);
        approvalDate = findViewById(R.id.approval_date);
        btnApproval = findViewById(R.id.btnApproval);

    }

    private void addApproval() {
        //progressDialog.setMessage();
        progressDialog.show();
        final String appName = approvalName.getText().toString().trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        final String creationDate = dateFormat.format(date);
    }
}