package com.gerray.fmsystem.Transactions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.gerray.fmsystem.R;

import com.gerray.fmsystem.Transactions.Mpesa.MPESAExpressActivity;
import com.gerray.fmsystem.Transactions.Mpesa.MpesaActivity;
import com.gerray.fmsystem.Transactions.PayPal.PaypalActivity;

public class TransactionActivity extends AppCompatActivity {
    Button btnPay, btnMpesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_pay);

        btnPay = findViewById(R.id.btn_Pay);

        btnPay.setOnClickListener(v -> startActivity(new Intent(this, PaypalActivity.class)));
        btnMpesa = findViewById(R.id.btn_Mpesa);
        btnMpesa.setOnClickListener(v -> {
            startActivity(new Intent(this, MPESAExpressActivity.class));
        });
    }

}