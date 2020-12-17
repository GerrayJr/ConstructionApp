package com.gerray.fmsystem.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.gerray.fmsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText sgFirst, sgSecond, sgEmail, sgPhone, sgPassword;
    private Spinner categorySpinner;
    private Button btnLog, btnCreate;

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        sgFirst = findViewById(R.id.create_firstName);
        sgSecond = findViewById(R.id.create_secondName);
        sgEmail = findViewById(R.id.create_email);
        sgPassword = findViewById(R.id.create_pass);
        sgPhone = findViewById(R.id.create_phone);
        categorySpinner = findViewById(R.id.cat_spin);

        btnCreate = findViewById(R.id.sg_create);
        btnCreate.setOnClickListener(this);

        btnLog = findViewById(R.id.sg_login);
        btnLog.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    public void onClick(View v) {
        if (v == btnCreate) {
            registerUser();
//            startActivity(new Intent(CreateActivity.this, LoginActivity.class));
        } else if (v == btnLog) {
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
    private void registerUser() {
        final String email = sgEmail.getText().toString().trim();
        final String password = sgPassword.getText().toString().trim();
        final String firstName = sgFirst.getText().toString().trim();
        final String secondName = sgSecond.getText().toString().trim();
        final int phone = Integer.parseInt(sgPhone.getText().toString().trim());
        final String category = categorySpinner.getSelectedItem().toString().trim();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User....");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            //Successful registration
                            Toast.makeText(CreateActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateActivity.this, LoginActivity.class));

                            InfoUser infoUser = new InfoUser(firstName,secondName,email,phone,category);
                            FirebaseUser user = auth.getCurrentUser();
                            databaseReference.child(user.getUid()).setValue(infoUser);
                        } else {
                            //Registration Failed
                            Toast.makeText(CreateActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}