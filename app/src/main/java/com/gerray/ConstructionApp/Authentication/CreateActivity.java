package com.gerray.ConstructionApp.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gerray.ConstructionApp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnCreate, tvLogin;
    private TextInputEditText sgFirst, sgSecond, sgEmail, sgPhone, sgPassword, conPass;
    private Spinner catSpinner;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        btnCreate = findViewById(R.id.sg_create);
        btnCreate.setOnClickListener(this);

        tvLogin = findViewById(R.id.sg_login);
        tvLogin.setOnClickListener(this);

        sgFirst = findViewById(R.id.create_firstName);
        sgSecond = findViewById(R.id.create_secondName);
        sgEmail = findViewById(R.id.create_email);
        sgPhone = findViewById(R.id.create_phone);
        sgPassword = findViewById(R.id.create_pass);
        conPass = findViewById(R.id.confirm_pas);

        catSpinner = findViewById(R.id.cat_spin);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnCreate) {
            //After creation
            createAccount();
        }
        if (v == tvLogin) {
            //Already have an account
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    private void createAccount() {
        final String firstName = Objects.requireNonNull(sgFirst.getText()).toString().trim();
        final String secondName = Objects.requireNonNull(sgSecond.getText()).toString().trim();
        final String email = Objects.requireNonNull(sgEmail.getText()).toString().trim();
        final String phoneNo = Objects.requireNonNull(sgPhone.getText()).toString().trim();
        final String password = Objects.requireNonNull(sgPassword.getText()).toString().trim();
        final String confirmPass = Objects.requireNonNull(conPass.getText()).toString().trim();
        final String category = catSpinner.getSelectedItem().toString();

        String pNum = sgPhone.getText().toString(); //for int Validation

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(secondName)) {
            Toast.makeText(this, "Enter Second Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(pNum)) {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!confirmPass.equals(password)) {
            conPass.setError("Password Do Not Match");
        } else {
            progressDialog.setMessage("Registering User....");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            //Successful registration
                            Toast.makeText(CreateActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            String phoneNumber = Settings.formatPhoneNumber(phoneNo);
                            Intent intent = new Intent(CreateActivity.this, VerifyPhone.class);
                            intent.putExtra("phone", phoneNumber);
                            startActivity(intent);

                            InfoUser infoUser = new InfoUser(firstName, secondName, email, phoneNumber, firebaseAuth.getUid(),category);
                            databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(infoUser);

                        } else {
                            //Registration Failed
                            Toast.makeText(CreateActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }

                    });
        }


    }
}