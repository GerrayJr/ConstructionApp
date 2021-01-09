package com.gerray.fmsystem.ManagerModule.Assets;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class AssetPopUp extends AppCompatActivity {
    private TextInputEditText assetName, assetModel, assetSerial, assetLocation, assetPurchase, assetDescription;
    private Button btnImage, btnAdd;
    private ImageView assetImage;

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
        setContentView(R.layout.asset_pop_up);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        assetName = findViewById(R.id.asset_name);
        assetModel = findViewById(R.id.asset_model);
        assetSerial = findViewById(R.id.asset_serial);
        assetLocation = findViewById(R.id.asset_location);
        assetPurchase = findViewById(R.id.asset_pDate);
        assetDescription = findViewById(R.id.asset_description);

        assetImage = findViewById(R.id.asset_imageView);

        btnImage = findViewById(R.id.asset_image);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnAdd = findViewById(R.id.asset_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAsset();
            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid()).child("Assets");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Facilities").child(currentUser.getUid()).child("Assets");
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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

            Picasso.with(this).load(mImageUri).into(assetImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void addAsset() {
        progressDialog.setMessage("Let us Begin");
        progressDialog.show();
        final String name = assetName.getText().toString().trim();
        final String model = assetModel.getText().toString().trim();
        final String serialNo = assetSerial.getText().toString().trim();
        final String location = assetLocation.getText().toString().trim();
        final String purchaseDate = assetPurchase.getText().toString().trim();
        final String description = assetDescription.getText().toString().trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        final String addDate = dateFormat.format(date).toString();
        final String assetID = UUID.randomUUID().toString();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(model)) {
            Toast.makeText(this, "Enter Asset Model", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Add Location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(serialNo)) {
            Toast.makeText(this, "Enter Serial Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(purchaseDate)) {
            Toast.makeText(this, "Enter Purchase Date", Toast.LENGTH_SHORT).show();
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

                                    FacilityAssets facilityAssets = new FacilityAssets(name, model, serialNo, location, purchaseDate, addDate, description, assetID,downUri);
                                    databaseReference.child(assetID).setValue(facilityAssets);
                                }
                            });


                            progressDialog.dismiss();
                            Toast.makeText(AssetPopUp.this, "Saved", Toast.LENGTH_SHORT).show();
                            AssetPopUp.this.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AssetPopUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
