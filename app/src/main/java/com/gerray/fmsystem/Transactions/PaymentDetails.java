package com.gerray.fmsystem.Transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gerray.fmsystem.R;

import org.json.JSONObject;

import java.util.Objects;

public class PaymentDetails extends AppCompatActivity {

    TextView txtId, txtAmount, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId = findViewById(R.id.txtID);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(intent.getStringExtra("PaymentDetails")));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("status"));
            txtAmount.setText("Ksh" + paymentAmount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
