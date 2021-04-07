package com.gerray.fmsystem.ContractorModule;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.R;
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

public class UpdateContractor extends AppCompatActivity {

    private ImageView imageView;
    private EditText edName, edEmail, edPhone;
    private Spinner locSpin, specSpin, catSpin;

    DatabaseReference databaseReference;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_consultant);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        imageView = findViewById(R.id.consultant_imageView);
        Button btnSelect = findViewById(R.id.consultant_btnImage);
        edName = findViewById(R.id.consultant_name);
        edEmail = findViewById(R.id.consultant_email);
        edPhone = findViewById(R.id.consultant_phone);

        locSpin = findViewById(R.id.consLoc_spinner);
        catSpin = findViewById(R.id.consEmp_spinner);
        specSpin = findViewById(R.id.consSpec_spinner);
        Button btnUpdate = findViewById(R.id.cons_update);


        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Contractor");
        assert currentUser != null;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Contractor").child(currentUser.getUid());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        btnUpdate.setOnClickListener(v -> updateConsultant());

        btnSelect.setOnClickListener(v -> openFileChooser());

        if (auth.getCurrentUser() != null) {
            databaseReference.child(currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("consultantName").exists()) {
                                String name = Objects.requireNonNull(snapshot.child("consultantName").getValue()).toString().trim();
                                edName.setText(name);
                            }
                            if (snapshot.child("emailAddress").exists()) {
                                String emailAddress = Objects.requireNonNull(snapshot.child("emailAddress").getValue()).toString().trim();
                                edEmail.setText(emailAddress);
                            }
                            if (snapshot.child("phoneNumber").exists()) {
                                String phone = Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString().trim();
                                edPhone.setText(phone);
                            }
                            if (snapshot.child("consultantImageUrl").exists()) {
                                String imageUrl = Objects.requireNonNull(snapshot.child("consultantImageUrl").getValue()).toString().trim();
                                Picasso.with(UpdateContractor.this).load(imageUrl).into(imageView);
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

            Picasso.with(this).load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void updateConsultant() {
        progressDialog.setMessage("Updating");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        final String consultantName = edName.getText().toString();
        final String consLocation = locSpin.getSelectedItem().toString().trim();
        final String consCategory = catSpin.getSelectedItem().toString().trim();
        final String consSpecs = specSpin.getSelectedItem().toString().trim();
        final String email = edEmail.getText().toString().trim();
        final String userID = auth.getUid();
        final String phone = edPhone.getText().toString();

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            final String downUri = uri.toString().trim();

                            CreateContractor createContractor = new CreateContractor(consultantName, consCategory, consSpecs, consLocation, userID, email, phone, downUri);
                            assert userID != null;
                            databaseReference.child(userID).setValue(createContractor);
                            progressDialog.dismiss();
                            UpdateContractor.this.finish();
                        });


                        progressDialog.dismiss();
                        Toast.makeText(UpdateContractor.this, "Saved", Toast.LENGTH_SHORT).show();
                        UpdateContractor.this.finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdateContractor.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress((int) progress);
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}



