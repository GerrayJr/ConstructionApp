package com.gerray.fmsystem.Transactions.Mpesa;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.R;
import com.gerray.fmsystem.Transactions.Mpesa.model.AccessToken;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMExpress;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMResult;
import com.gerray.fmsystem.Transactions.Mpesa.util.TransactionType;



import butterknife.BindView;
import butterknife.ButterKnife;

public class MPESAExpressActivity extends AppCompatActivity {

    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;

    EditText editTextAmount;

    @BindView(R.id.sendButton)
    Button sendButton;

    //Declare Daraja :: Global Variable
    Daraja daraja;

    String phoneNumber, amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesaexpress);
        editTextAmount = findViewById(R.id.editTextAmount);
        ButterKnife.bind(this);


        //Init Daraja
        //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
        daraja = Daraja.with("AwPFI8FI8bMXE5S3sP63EWuFHeVyKf0S", "ZAL8boGZBpcRfhqG", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(MPESAExpressActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(MPESAExpressActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(MPESAExpressActivity.this.getClass().getSimpleName(), error);
            }
        });

        //TODO :: THIS IS A SIMPLE WAY TO DO ALL THINGS AT ONCE!!! DON'T DO THIS :)
        sendButton.setOnClickListener(v -> {

            //Get Phone Number from User Input
            phoneNumber = editTextPhoneNumber.getText().toString().trim();

            //Amount
            amount = editTextAmount.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber)) {
                editTextPhoneNumber.setError("Please Provide a Phone Number");
                return;
            }

            //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
            LNMExpress lnmExpress = new LNMExpress(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    TransactionType.CustomerPayBillOnline, // TransactionType.CustomerPayBillOnline  <- Apply any of these two
                    amount,
                    "254705499614",
                    "174379",
                    phoneNumber,
                    "http://mpesa-requestbin.herokuapp.com/v5zzbkv6",
                    "MPESA Android Test ",
                    "Payment"
            );

            //This is the
            daraja.requestMPESAExpress(lnmExpress,
                    new DarajaListener<LNMResult>() {
                        @Override
                        public void onResult(@NonNull LNMResult lnmResult) {
                            Log.i(MPESAExpressActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i(MPESAExpressActivity.this.getClass().getSimpleName(), error);
                        }
                    }
            );
        });
    }
}
