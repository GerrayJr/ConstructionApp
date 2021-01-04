package com.gerray.fmsystem.ManagerModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gerray.fmsystem.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class FacilityLocation extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private EditText edLat, edLong;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_location);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Locations");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        Button btnCord = findViewById(R.id.btnGetCord);
        btnCord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(FacilityLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //get current location coordinates
                    client.getLastLocation().addOnSuccessListener(FacilityLocation.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                progressDialog.show();
                                final double locLatitude = location.getLatitude();
                                final double locLongitude = location.getLongitude();
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (firebaseUser != null) {
                                    firebaseDatabaseReference.child("Facilities").child(firebaseUser.getUid()).child("Profile")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String facilityName = null, facilityType = null, facilityEmail = null;
                                                    if (snapshot.child("facilityName").exists()) {
                                                        facilityName = snapshot.child("facilityName").getValue().toString();
                                                    }
                                                    if (snapshot.child("facilityType").exists()) {
                                                        facilityType = snapshot.child("facilityType").getValue().toString();
                                                    }
                                                    if (snapshot.child("emailAddress").exists()) {
                                                        facilityEmail = snapshot.child("emailAddress").getValue().toString();
                                                    }

                                                    LocationClass locationClass = new LocationClass(facilityName, facilityType, facilityEmail, locLatitude, locLongitude);
                                                    databaseReference.child(firebaseUser.getUid()).setValue(locationClass);

                                                    progressDialog.dismiss();
                                                    Toast.makeText(FacilityLocation.this, "Saved", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }
                                Toast.makeText(FacilityLocation.this, "Latitude: " + locLatitude + " Longitude: " + locLongitude, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Dexter.withActivity(FacilityLocation.this)
                            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    if (ContextCompat.checkSelfPermission(FacilityLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        client.getLastLocation().addOnSuccessListener(FacilityLocation.this, new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                if (location != null) {
                                                    double latitude = location.getLatitude();
                                                    double longitude = location.getLongitude();
                                                    Toast.makeText(FacilityLocation.this, "Latitude: " + latitude + " Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                    if (response.isPermanentlyDenied()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(FacilityLocation.this);
                                        builder.setTitle("Permission Denied")
                                                .setMessage("Permission to access Device's location is Permanently denied. Go to settings to allow permission")
                                                .setNegativeButton("Cancel", null)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent();
                                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                        intent.setData(Uri.fromParts("Package", getPackageName(), null));

                                                    }
                                                })
                                                .show();

                                    } else {
                                        Toast.makeText(FacilityLocation.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            })
                            .check();
                }
            }
        });

        edLat = findViewById(R.id.locLatitude);
        edLong = findViewById(R.id.locLongitude);

        Button btngetCord = findViewById(R.id.btnInputCord);
        btngetCord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertLocation();
            }
        });

    }

    private void insertLocation() {
        progressDialog.show();
        final double latitude = Double.parseDouble(edLat.getText().toString().trim());
        final double longitude = Double.parseDouble(edLong.getText().toString().trim());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Facilities").child(firebaseUser.getUid()).child("Profile")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String facilityName = null, facilityType = null, facilityEmail = null;
                            if (snapshot.child("facilityName").exists()) {
                                facilityName = snapshot.child("facilityName").getValue().toString();
                            }
                            if (snapshot.child("facilityType").exists()) {
                                facilityType = snapshot.child("facilityType").getValue().toString();
                            }
                            if (snapshot.child("emailAddress").exists()) {
                                facilityEmail = snapshot.child("emailAddress").getValue().toString();
                            }

                            LocationClass locationClass = new LocationClass(facilityName, facilityType, facilityEmail, latitude, longitude);
                            databaseReference.child(firebaseUser.getUid()).setValue(locationClass);

                            progressDialog.dismiss();
                            Toast.makeText(FacilityLocation.this, "Saved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }
}
