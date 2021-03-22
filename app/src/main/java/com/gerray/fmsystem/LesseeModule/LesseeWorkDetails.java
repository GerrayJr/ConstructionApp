package com.gerray.fmsystem.LesseeModule;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class LesseeWorkDetails extends AppCompatActivity implements View.OnClickListener {
    Button imageSelect;
    Button postWork;
    TextInputEditText requester, requestDate, workDate, workDescription;
    ImageView workImage;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    String contractorID;

    StorageReference mStorageRef;
    StorageTask mUploadTask;
    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);

        imageSelect = findViewById(R.id.work_image);
        imageSelect.setOnClickListener(this);
        postWork = findViewById(R.id.btn_postWork);
        postWork.setOnClickListener(this);
        Intent intent = getIntent();
        contractorID = Objects.requireNonNull(intent.getExtras()).getString("contractorID");


        workImage = findViewById(R.id.work_imageView);

        DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        final String setDate = dateFormat.format(date);

        requester = findViewById(R.id.work_reqLessee);

        requestDate = findViewById(R.id.work_reqDate);
        requestDate.setVisibility(View.GONE);

        workDate = findViewById(R.id.work_date);
        workDate.setText(setDate);
        workDescription = findViewById(R.id.work_description);


        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid());
        assert currentUser != null;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Facilities").child(currentUser.getUid());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    @Override
    public void onClick(View v) {
        if (v == imageSelect) {
            openFileChooser();
        }
        if (v == postWork) {
            postWorkDetails();
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

            Picasso.with(this).load(mImageUri).into(workImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void postWorkDetails() {
        progressDialog.setMessage("Let us Begin");
        progressDialog.show();
        final String requestLessee = Objects.requireNonNull(requester.getText()).toString().trim();
        final String date = Objects.requireNonNull(workDate.getText()).toString().trim();
        final String description = Objects.requireNonNull(workDescription.getText()).toString().trim();
        final String status = "Requested";
        final String userID = auth.getUid();
        final String workID = UUID.randomUUID().toString();


        if (TextUtils.isEmpty(requestLessee)) {
            Toast.makeText(this, "Who Requested?", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Add Asset Description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            final String downUri = uri.toString().trim();

                            LesseeDetailsClass lesseeDetailsClass = new LesseeDetailsClass(workID, userID, requestLessee, null, date, description, status, contractorID, null, downUri);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Work Orders");
                            databaseReference.child(workID).setValue(lesseeDetailsClass);

                        });


                        progressDialog.dismiss();
                        Toast.makeText(LesseeWorkDetails.this, "Work Sent", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(LesseeWorkDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress((int) progress);
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}