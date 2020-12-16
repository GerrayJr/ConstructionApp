package com.gerray.fmsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ManagerInfo extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText facName, facAuth, facOcc;
    private Button btnContinue;
    private Spinner facType;
    private FloatingActionButton fabInfo;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_info);

        btnContinue = findViewById(R.id.fac_continue);
        btnContinue.setOnClickListener(this);

        fabInfo = findViewById(R.id.fab_info);
        fabInfo.setOnClickListener(this);

        facName = findViewById(R.id.fac_name);
        facAuth = findViewById(R.id.fac_auth);
        facOcc = findViewById(R.id.fac_occ);
        facType = findViewById(R.id.cat_spin);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    public void onClick(View v) {
        if (v == btnContinue) {
            //registerFacility();
            startActivity(new Intent(ManagerInfo.this,FacilityManager.class));
        } else if (v == fabInfo) {
            Snackbar snackbar = Snackbar
                    .make(v, "Information", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void registerFacility() {
        progressDialog.setMessage("Let us Begin");
        progressDialog.show();
        final String name = facName.getText().toString().trim();
        final String authority = facAuth.getText().toString().trim();
        final String facilityType = facType.getSelectedItem().toString().trim();
        final int occupancy = Integer.parseInt(facOcc.getText().toString().trim());
        final String facID = UUID.randomUUID().toString();

        String pNum = facOcc.getText().toString();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter Project Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(authority)) {
            Toast.makeText(this, "Enter Authority Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pNum)) {
            Toast.makeText(this, "Enter Occupancy Number", Toast.LENGTH_SHORT).show();
            return;
        }

//        FacilityCreate facilityCreate = new FacilityCreate(name, authority, facilityType, occupancy, facID);
//        databaseReference.child(facID).setValue(facilityCreate);

        progressDialog.dismiss();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}