package com.gerray.ConstructionApp.Authentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gerray.ConstructionApp.R;
import com.gerray.ConstructionApp.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private EditText logEmail, logPass;
    private TextView create;

    ProgressDialog progressDialog;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        create = findViewById(R.id.tv_create);
        create.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();

        TextView forget = findViewById(R.id.tv_forgot);
        forget.setOnClickListener(v -> {

            final EditText resetMail = new EditText(v.getContext());
            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password ?");
            passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Yes", (dialog, which) -> {
                String mail = resetMail.getText().toString();
                auth.sendPasswordResetEmail(mail)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show());

            });

            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {
                // close the dialog
            });

            passwordResetDialog.create().show();

        });


        logEmail = findViewById(R.id.logEmail);
        logPass = findViewById(R.id.logPass);

        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            loginUser();
//            Intent intent = new Intent(LoginActivity.this, SplashScreen.class);
//            startActivity(intent);
        }
        if (v == create) {
            // if you don't have an account in the app;
            startActivity(new Intent(this, CreateActivity.class));
        }
    }

    private void loginUser() {
        String credential = logEmail.getText().toString().trim();
        String password = logPass.getText().toString().trim();

        if (TextUtils.isEmpty(credential)) {
            Toast.makeText(this, "Enter Email!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password!!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        if (credential.contains("@")) {
            auth.signInWithEmailAndPassword(credential, password)
                    .addOnCompleteListener(this, task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, SplashScreen.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }

                    });

        }
    }
}