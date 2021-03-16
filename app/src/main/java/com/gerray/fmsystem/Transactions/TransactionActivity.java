package com.gerray.fmsystem.Transactions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.R;
import com.gerray.fmsystem.Transactions.Config.Config;
import com.google.android.material.textfield.TextInputEditText;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static final PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_API);

    Button btnPay, btnMpesa;
    TextInputEditText edAmount;
    String amount = "";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_pay);

        //Start PayPal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        btnPay = findViewById(R.id.btn_Pay);
        edAmount = findViewById(R.id.edpay);

        btnPay.setOnClickListener(v -> processPayment());
        btnMpesa = findViewById(R.id.btn_Mpesa);
        btnMpesa.setOnClickListener(v -> startActivity(new Intent(TransactionActivity.this, MpesaAcitivity.class)));
    }

    private void processPayment() {
        amount = Objects.requireNonNull(edAmount.getText()).toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Payment: ", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                            startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }
}