package com.gerray.fmsystem.ManagerModule.Profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.R;
import com.google.android.material.textfield.TextInputEditText;
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
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfilePopUp extends AppCompatActivity {
    private TextInputEditText profAuth, profAddress, profEmail, profOcc;
    private Spinner profActivity;
    private ImageView profImage;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    DatabaseReference databaseReference;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pop_up);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        Button btnUpdate = findViewById(R.id.btn_update_prof);
        btnUpdate.setOnClickListener(v -> facilityCreate());

        Button btnProfImage = findViewById(R.id.prof_image);
        btnProfImage.setOnClickListener(v -> openFileChooser());

        profImage = findViewById(R.id.prof_imageView);
        profAuth = findViewById(R.id.facility_auth);
        profAddress = findViewById(R.id.facility_postal);
        profEmail = findViewById(R.id.facility_email);
        profOcc = findViewById(R.id.facility_occ);

        profActivity = findViewById(R.id.prof_spinner);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        assert currentUser != null;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Facilities").child(currentUser.getUid());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        if (auth.getCurrentUser() != null) {
            databaseReference.child("Profile")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("authorityName").exists()) {
                                String authName = Objects.requireNonNull(snapshot.child("authorityName").getValue()).toString().trim();
                                profAuth.setText(authName);
                            }
                            if (snapshot.child("emailAddress").exists()) {
                                String emailAddress = Objects.requireNonNull(snapshot.child("emailAddress").getValue()).toString().trim();
                                profEmail.setText(emailAddress);
                            }
                            if (snapshot.child("occupancyNo").exists()) {
                                String occNo = Objects.requireNonNull(snapshot.child("occupancyNo").getValue()).toString().trim();
                                profOcc.setText(occNo);
                            }
                            if (snapshot.child("postalAddress").exists()) {
                                String postal = Objects.requireNonNull(snapshot.child("postalAddress").getValue()).toString().trim();
                                profAddress.setText(postal);
                            }
                            if (snapshot.child("facilityImageUrl").exists()) {
                                String imageUrl = Objects.requireNonNull(snapshot.child("facilityImageUrl").getValue()).toString().trim();
                                Picasso.with(ProfilePopUp.this).load(imageUrl).into(profImage);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
        }


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(profImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void facilityCreate() {
        progressDialog.setMessage("Updating");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Facilities").child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String getName = null, getFacilityID = null;
                            if (snapshot.child("name").exists()) {
                                getName = Objects.requireNonNull(snapshot.child("name").getValue()).toString().trim();
                            }
                            if (snapshot.child("facilityID").exists()) {
                                getFacilityID = Objects.requireNonNull(snapshot.child("facilityID").getValue()).toString().trim();
                            }
                            final String name = getName;
                            final String facilityID = getFacilityID;
                            final String auth = Objects.requireNonNull(profAuth.getText()).toString().trim();
                            final String postal = Objects.requireNonNull(profAddress.getText()).toString().trim();
                            final String email = Objects.requireNonNull(profEmail.getText()).toString().trim();
                            final String activity = profActivity.getSelectedItem().toString().trim();
                            final int occupancy = Integer.parseInt(Objects.requireNonNull(profOcc.getText()).toString().trim());

                            if (TextUtils.isEmpty(name)) {
                                Toast.makeText(ProfilePopUp.this, "Enter Name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(auth)) {
                                Toast.makeText(ProfilePopUp.this, "Enter Authority Name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(postal)) {
                                Toast.makeText(ProfilePopUp.this, "Add Postal Address", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(email)) {
                                Toast.makeText(ProfilePopUp.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (mImageUri != null) {
                                final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                                        + "." + getFileExtension(mImageUri));

                                mUploadTask = fileReference.putFile(mImageUri)
                                        .addOnSuccessListener(taskSnapshot -> {
                                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                                final String downUri = uri.toString().trim();

                                                FacProfile facProfile = new FacProfile(name, auth, activity, postal, email, occupancy, facilityID, downUri);
                                                databaseReference.child("Profile").setValue(facProfile);
                                            });


                                            progressDialog.dismiss();
                                            Toast.makeText(ProfilePopUp.this, "Saved", Toast.LENGTH_SHORT).show();
                                            ProfilePopUp.this.finish();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(ProfilePopUp.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                                        .addOnProgressListener(taskSnapshot -> {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                            progressDialog.setProgress((int) progress);
                                        });
                            } else {
                                Toast.makeText(ProfilePopUp.this, "No file selected", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}