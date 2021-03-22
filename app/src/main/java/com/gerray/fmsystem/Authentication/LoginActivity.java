package com.gerray.fmsystem.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gerray.fmsystem.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText logEmail, logPass;
    private Button btnLogin, btnCreate, btnForgot;
    private ProgressDialog dialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = findViewById(R.id.logEmail);
        logPass = findViewById(R.id.logPass);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(this);

        btnForgot = findViewById(R.id.btn_forgot);
        btnForgot.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            loginUser();
//            startActivity(new Intent(LoginActivity.this, FacilityManager.class));
        } else if (v == btnCreate) {
            startActivity(new Intent(this, CreateActivity.class));

        } else if (v == btnForgot) {
            //To be created
            Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginUser() {
        String email = logEmail.getText().toString().trim();
        String password = logPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password!!", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.setMessage("Please Wait");
        dialog.show();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    dialog.dismiss();
                    if (task.isSuccessful()) {
                        finish();
                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, PermissionActivity.class));

                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }

                });
    }
}