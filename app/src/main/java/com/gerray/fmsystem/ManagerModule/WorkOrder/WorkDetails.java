package com.gerray.fmsystem.ManagerModule.WorkOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.gerray.fmsystem.ManagerModule.Assets.AssetPopUp;
import com.gerray.fmsystem.ManagerModule.Assets.FacilityAssets;
import com.gerray.fmsystem.ManagerModule.FacilityManager;
import com.gerray.fmsystem.ManagerModule.Profile.FacProfile;
import com.gerray.fmsystem.ManagerModule.Profile.ProfilePopUp;
import com.gerray.fmsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class WorkDetails extends AppCompatActivity implements View.OnClickListener {
    Button conSelect, imageSelect, postWork;
    TextInputEditText requester, requestDate, workDate, workDescription;
    ImageView workImage;

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
        setContentView(R.layout.activity_work_details);


        conSelect = findViewById(R.id.btn_conSelect);
        conSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imageSelect = findViewById(R.id.work_image);
        imageSelect.setOnClickListener(this);
        postWork = findViewById(R.id.btn_postWork);
        postWork.setOnClickListener(this);

        Intent intent = getIntent();
        final String lessee = intent.getExtras().getString("lessee");
        final String reqDate = intent.getExtras().getString("date");
        final String description = intent.getExtras().getString("description");
        final String imageUrl = intent.getExtras().getString("imageUrl");

//        Bundle i = new Bundle();
//        i.putString("lessee", lessee);
//        i.putString("reqDate", reqDate);

        workImage = findViewById(R.id.work_imageView);
        Picasso.with(WorkDetails.this).load(imageUrl).into(workImage);

        DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        final String setDate = dateFormat.format(date);

        requester = findViewById(R.id.work_reqLessee);
        requester.setText(lessee);
        requestDate = findViewById(R.id.work_reqDate);
        requestDate.setText(reqDate);
        workDate = findViewById(R.id.work_date);
        workDate.setText(setDate);
        workDescription = findViewById(R.id.work_description);
        workDescription.setText(description);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid());
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
        final String requestLessee = requester.getText().toString().trim();
        final String reqDate = requestDate.getText().toString().trim();
        final String date = workDate.getText().toString().trim();
        final String description = workDescription.getText().toString().trim();


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
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = uri;
                                    final String downUri = downloadUri.toString().trim();

                                    Intent select = new Intent(WorkDetails.this, SelectConsultant.class);
                                    select.putExtra("lessee", requestLessee);
                                    select.putExtra("date", reqDate);
                                    select.putExtra("imageUrl", downUri);
                                    select.putExtra("description", description);
                                    select.putExtra("uri", downUri);
                                    select.putExtra("workDate", date);
                                    startActivity(select);
                                }
                            });


                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(WorkDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
