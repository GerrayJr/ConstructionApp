package com.gerray.ConstructionApp.Authentication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gerray.ConstructionApp.R;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EditText emailAddress = findViewById(R.id.resetEmail);
        Button reset = findViewById(R.id.btn_reset);

        String email = emailAddress.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        reset.setOnClickListener(task ->{
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Email sent.");
                        }
                    });
        });


    }
}