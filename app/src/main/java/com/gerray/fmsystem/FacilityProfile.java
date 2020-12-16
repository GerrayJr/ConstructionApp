package com.gerray.fmsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FacilityProfile extends AppCompatActivity {
    private TextView facilityName, facilityAuth, facilityManager, facilityPostal, facilityEmail, facilityActivity, facilityOccupancy;
    private ImageView facilityImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_profile);

        Button btnUpdate = findViewById(R.id.profile_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacilityProfile.this, ProfilePopUp.class));
            }
        });
    }
}