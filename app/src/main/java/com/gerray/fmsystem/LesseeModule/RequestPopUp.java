package com.gerray.fmsystem.LesseeModule;

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

public class RequestPopUp extends AppCompatActivity {

    private TextInputEditText requestDescription;
    private ImageView req_image;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    DatabaseReference databaseReference;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_pop_up);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        requestDescription = findViewById(R.id.req_desc);

        req_image = findViewById(R.id.request_imageView);
        Button btnImage = findViewById(R.id.req_imageBtn);
        btnImage.setOnClickListener(v -> openFileChooser());

        Button btnAdd = findViewById(R.id.requestAdd);
        btnAdd.setOnClickListener(v -> postRequest());


        auth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        assert currentUser != null;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Facilities").child(currentUser.getUid());
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

            Picasso.with(this).load(mImageUri).into(req_image);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void postRequest() {
        progressDialog.setMessage("Sending Request");
        progressDialog.show();
        final String description = Objects.requireNonNull(requestDescription.getText()).toString().trim();
        DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        final String requestDate = dateFormat.format(date);
        final String requestID = UUID.randomUUID().toString();

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Add Description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            final String downUri = uri.toString().trim();

                            LesseeRequestClass requestClass = new LesseeRequestClass(requestID, auth.getUid(), description, requestDate, downUri);
                            DatabaseReference dbLoc = databaseReference.push();
                            dbLoc.setValue(requestClass);
                        });


                        progressDialog.dismiss();
                        Toast.makeText(RequestPopUp.this, "Sent", Toast.LENGTH_SHORT).show();
                        RequestPopUp.this.finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(RequestPopUp.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress((int) progress);
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}