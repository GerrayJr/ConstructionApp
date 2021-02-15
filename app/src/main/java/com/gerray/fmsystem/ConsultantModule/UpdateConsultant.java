package com.gerray.fmsystem.ConsultantModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gerray.fmsystem.ManagerModule.Assets.AssetPopUp;
import com.gerray.fmsystem.ManagerModule.Assets.FacilityAssets;
import com.gerray.fmsystem.ManagerModule.Profile.ProfilePopUp;
import com.gerray.fmsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class UpdateConsultant extends AppCompatActivity {

    private ImageView imageView;
    private EditText edName, edEmail, edPhone;
    private Spinner locSpin, specSpin, catSpin;
    private Button btnUpdate, btnSelect;

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
        btnSelect = findViewById(R.id.consultant_btnImage);
        edName = findViewById(R.id.consultant_name);
        edEmail = findViewById(R.id.consultant_email);
        edPhone = findViewById(R.id.consultant_phone);

        locSpin = findViewById(R.id.consLoc_spinner);
        catSpin = findViewById(R.id.consEmp_spinner);
        specSpin = findViewById(R.id.consSpec_spinner);
        btnUpdate = findViewById(R.id.cons_update);


        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Consultants");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Consultants").child(currentUser.getUid());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateConsultant();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        if (auth.getCurrentUser() != null) {
            databaseReference.child(currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("consultantName").exists()) {
                                String name = snapshot.child("consultantName").getValue().toString().trim();
                                edName.setText(name);
                            }
                            if (snapshot.child("emailAddress").exists()) {
                                String emailAddress = snapshot.child("emailAddress").getValue().toString().trim();
                                edEmail.setText(emailAddress);
                            }
                            if (snapshot.child("phoneNumber").exists()) {
                                String phone = snapshot.child("phoneNumber").getValue().toString().trim();
                                edPhone.setText(phone);
                            }
                            if (snapshot.child("consultantImageUrl").exists()) {
                                String imageUrl = snapshot.child("consultantImageUrl").getValue().toString().trim();
                                Picasso.with(UpdateConsultant.this).load(imageUrl).into(imageView);
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
        progressDialog.setMessage("Let us Begin");
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
        final int phone = Integer.parseInt(edPhone.getText().toString().trim());

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = uri;
                                    final String downUri = downloadUri.toString().trim();

                                    CreateConsultant createConsultant = new CreateConsultant(consultantName, consCategory, consSpecs, consLocation, userID, email, phone, downUri);
                                    databaseReference.child(userID).setValue(createConsultant);
                                    progressDialog.dismiss();
                                    UpdateConsultant.this.finish();
                                }
                            });


                            progressDialog.dismiss();
                            Toast.makeText(UpdateConsultant.this, "Saved", Toast.LENGTH_SHORT).show();
                            UpdateConsultant.this.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateConsultant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}



