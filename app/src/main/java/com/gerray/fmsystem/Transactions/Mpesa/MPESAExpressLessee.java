package com.gerray.fmsystem.Transactions.Mpesa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.LesseeModule.LesseeTransaction;
import com.gerray.fmsystem.ManagerModule.TransactionActivity;
import com.gerray.fmsystem.R;
import com.gerray.fmsystem.Transactions.Mpesa.model.AccessToken;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMExpress;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMResult;
import com.gerray.fmsystem.Transactions.Mpesa.util.TransactionType;
import com.gerray.fmsystem.Transactions.TransactionDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MPESAExpressLessee extends AppCompatActivity {

    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;

    EditText editTextAmount;

    @BindView(R.id.sendButton)
    Button sendButton;

    Daraja daraja;

    FirebaseAuth auth;
    String phoneNumber, amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesaexpress);
        editTextAmount = findViewById(R.id.editTextAmount);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        daraja = Daraja.with("AwPFI8FI8bMXE5S3sP63EWuFHeVyKf0S", "ZAL8boGZBpcRfhqG", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(MPESAExpressLessee.this.getClass().getSimpleName(), accessToken.getAccess_token());
            }

            @Override
            public void onError(String error) {
                Log.e(MPESAExpressLessee.this.getClass().getSimpleName(), error);
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
                            Log.i(MPESAExpressLessee.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("FacilityOccupants")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    String userID = dataSnapshot1.child("userID").getValue().toString();
                                                    String fID = dataSnapshot.getKey();
                                                    if (auth.getUid().equals(userID)) {
                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Transactions");
                                                        String transID = UUID.randomUUID().toString();
                                                        DateFormat dateFormat = DateFormat.getDateInstance();
                                                        Date date = new Date();
                                                        final String transDate = dateFormat.format(date);
                                                        TransactionDetails details = new TransactionDetails(transID, lnmResult.MerchantRequestID, auth.getUid(), fID, amount, "Lessee Payment", transDate);
                                                        reference.child(transID).setValue(details);

                                                        startActivity(new Intent(MPESAExpressLessee.this, LesseeTransaction.class));

                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }

                        @Override
                        public void onError(String error) {
                            Log.i(MPESAExpressLessee.this.getClass().getSimpleName(), error);
                        }
                    }
            );
        });
    }
}
