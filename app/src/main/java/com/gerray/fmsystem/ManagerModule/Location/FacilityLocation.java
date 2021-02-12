package com.gerray.fmsystem.ManagerModule.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.ManagerModule.Assets.AssetViewHolder;
import com.gerray.fmsystem.ManagerModule.Assets.FacilityAssets;
import com.gerray.fmsystem.ManagerModule.Consultants.ConsultantViewHolder;
import com.gerray.fmsystem.ManagerModule.Consultants.FacilityConsultant;
import com.gerray.fmsystem.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

public class FacilityLocation extends AppCompatActivity {

    private FusedLocationProviderClient client;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser, locateAuth;
    RecyclerView recyclerView;

    FirebaseRecyclerOptions<LocationClass> options;
    FirebaseRecyclerAdapter<LocationClass, LocationViewHolder> adapter;

    int PLACE_PICKER_REQUEST = 1;

    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_location);

        auth = FirebaseAuth.getInstance();
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
                                                        facilityName = Objects.requireNonNull(snapshot.child("facilityName").getValue()).toString();
                                                    }
                                                    if (snapshot.child("facilityType").exists()) {
                                                        facilityType = Objects.requireNonNull(snapshot.child("facilityType").getValue()).toString();
                                                    }
                                                    if (snapshot.child("emailAddress").exists()) {
                                                        facilityEmail = Objects.requireNonNull(snapshot.child("emailAddress").getValue()).toString();
                                                    }

                                                    LocationClass locationClass = new LocationClass(facilityName, facilityType, facilityEmail, locLatitude, locLongitude, firebaseUser.getUid());
//                                                    databaseReference.child(firebaseUser.getUid()).setValue(locationClass);
                                                    DatabaseReference dbLoc = databaseReference.push();
                                                    dbLoc.setValue(locationClass);

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
        ;


        Button btnPickLoc = findViewById(R.id.btnPickLoc);
        btnPickLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(FacilityLocation.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        locateAuth = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Locations");
        reference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<LocationClass>().setQuery(reference, LocationClass.class).build();
        adapter = new FirebaseRecyclerAdapter<LocationClass, LocationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LocationViewHolder holder, int position, @NonNull LocationClass model) {
                String userID = model.getUserID();
                String latitude = String.valueOf(model.getLatitude());
                String longitude = String.valueOf(model.getLongitude());
                if (userID.equals(locateAuth.getUid())) {
                    holder.tvLat.setText(latitude);
                    holder.tvLong.setText(longitude);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new LocationViewHolder(LayoutInflater.from(FacilityLocation.this).inflate(R.layout.location_item, parent, false));
            }
        };

        recyclerView = findViewById(R.id.recycler_view_loc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stringBuilder = new StringBuilder();
                final double latitude = place.getLatLng().latitude;
                final double longitude = place.getLatLng().longitude;
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    firebaseDatabaseReference.child("Facilities").child(firebaseUser.getUid()).child("Profile")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String facilityName = null, facilityType = null, facilityEmail = null;
                                    if (snapshot.child("facilityName").exists()) {
                                        facilityName = Objects.requireNonNull(snapshot.child("facilityName").getValue()).toString();
                                    }
                                    if (snapshot.child("facilityType").exists()) {
                                        facilityType = Objects.requireNonNull(snapshot.child("facilityType").getValue()).toString();
                                    }
                                    if (snapshot.child("emailAddress").exists()) {
                                        facilityEmail = Objects.requireNonNull(snapshot.child("emailAddress").getValue()).toString();
                                    }

                                    LocationClass locationClass = new LocationClass(facilityName, facilityType, facilityEmail, latitude, longitude, firebaseUser.getUid());
//                                    databaseReference.child(firebaseUser.getUid()).setValue(locationClass);
                                    DatabaseReference dbLoc = databaseReference.push();
                                    dbLoc.setValue(locationClass);

                                    progressDialog.dismiss();
                                    Toast.makeText(FacilityLocation.this, "Saved", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
