package com.gerray.ConstructionApp.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.ConstructionApp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    Button verifyBtn;
    String verificationSystem;
    TextInputEditText otpEdit;
    ProgressDialog progressDialog;
    PhoneAuthProvider.ForceResendingToken forceResendingToken;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        verifyBtn = findViewById(R.id.btn_verify);
        otpEdit = findViewById(R.id.ed_otp);

        String phoneNo = getIntent().getStringExtra("phone");
        sendVerificationCodeToUser(phoneNo, forceResendingToken);

        verifyBtn.setOnClickListener(view -> {
            String code = Objects.requireNonNull(otpEdit.getText()).toString();
            if (code.isEmpty() || code.length() < 6) {
                otpEdit.setError("Wrong OTP...");
                otpEdit.requestFocus();
                return;
            }
            progressDialog.show();
            progressDialog.setMessage("Please Wait");
            VerifyCode(code);
        });

    }

    private void sendVerificationCodeToUser(String phoneNo, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationSystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull @org.jetbrains.annotations.NotNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressDialog.show();
                progressDialog.setMessage("Please Wait");
                VerifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull @org.jetbrains.annotations.NotNull FirebaseException e) {
            Toast.makeText(VerifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    };

    private void VerifyCode(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationSystem, verificationCode);
        signInUserCredentials(credential);
    }

    private void signInUserCredentials(PhoneAuthCredential credential) {
        Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential).addOnSuccessListener(authResult -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            Toast.makeText(VerifyPhone.this, "Successful", Toast.LENGTH_SHORT).show();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }
}