package com.gerray.fmsystem.LesseeModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.gerray.fmsystem.ManagerModule.Profile.FacProfile;
import com.gerray.fmsystem.ManagerModule.Profile.ProfilePopUp;
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

public class LesseeProfileUpdate extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    DatabaseReference databaseReference, firebaseDatabaseReference;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;

    TextInputEditText edLesseeName, edEmail, edPhone, edContact;
    Spinner spinner;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_profile_update);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        edLesseeName = findViewById(R.id.upLessee_name);
        edEmail = findViewById(R.id.upLessee_email);
        edPhone = findViewById(R.id.upLessee_phone);
        edContact = findViewById(R.id.upLessee_contact);

        spinner = findViewById(R.id.upLessee_spinner);
        imageView = findViewById(R.id.upLessee_image);

        Button btnUpdate = findViewById(R.id.btn_update_lessee);
        btnUpdate.setOnClickListener(v -> updateProfile());

        Button selectImage = findViewById(R.id.update_image);
        selectImage.setOnClickListener(v -> openFileChooser());

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Lessees").child(currentUser.getUid());
            mStorageRef = FirebaseStorage.getInstance().getReference().child("Lessees").child(currentUser.getUid());

            databaseReference.child("Profile")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("lesseeName").exists()) {
                                String lName = Objects.requireNonNull(snapshot.child("lesseeName").getValue()).toString().trim();
                                edLesseeName.setText(lName);
                            }
                            if (snapshot.child("emailAddress").exists()) {
                                String emailAddress = Objects.requireNonNull(snapshot.child("emailAddress").getValue()).toString().trim();
                                edEmail.setText(emailAddress);
                            }
                            if (snapshot.child("phoneNumber").exists()) {
                                String occNo = Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString().trim();
                                edPhone.setText(occNo);
                            }
                            if (snapshot.child("contactName").exists()) {
                                String coName = Objects.requireNonNull(snapshot.child("contactName").getValue()).toString().trim();
                                edContact.setText(coName);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
        } else {
            Toast.makeText(this, "No Current UserID", Toast.LENGTH_SHORT).show();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


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

            Picasso.with(this).load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void updateProfile() {
        progressDialog.setMessage("Updating");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            final String contactName = Objects.requireNonNull(edContact.getText().toString().trim());
            final String lesseeName = Objects.requireNonNull(edLesseeName.getText()).toString().trim();
            final String email = Objects.requireNonNull(edEmail.getText()).toString().trim();
            final String phone = Objects.requireNonNull(edPhone.getText()).toString().trim();
            final String activity = spinner.getSelectedItem().toString().trim();

            if (TextUtils.isEmpty(lesseeName)) {
                Toast.makeText(LesseeProfileUpdate.this, "Enter Lessee Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(contactName)) {
                Toast.makeText(LesseeProfileUpdate.this, "Enter Contact Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LesseeProfileUpdate.this, "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mImageUri != null) {
                final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(mImageUri));

                mUploadTask = fileReference.putFile(mImageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                final String downUri = uri.toString().trim();
                                ProfileUpdate profileUpdate = new ProfileUpdate(contactName, lesseeName, activity, email, phone, downUri);
                                databaseReference.child("Profile").setValue(profileUpdate);

                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Lessees").child(firebaseUser.getUid());
                                databaseReference1.child("lesseeName").setValue(lesseeName);
                                databaseReference1.child("contactName").setValue(contactName);
                                databaseReference1.child("activityType").setValue(activity);
                            });


                            progressDialog.dismiss();
                            Toast.makeText(LesseeProfileUpdate.this, "Saved", Toast.LENGTH_SHORT).show();
                            LesseeProfileUpdate.this.finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(LesseeProfileUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress((int) progress);
                        });
            } else {
                Toast.makeText(LesseeProfileUpdate.this, "Select a Photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}