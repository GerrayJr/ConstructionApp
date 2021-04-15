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

import com.gerray.fmsystem.R;
import com.gerray.fmsystem.Transactions.Mpesa.model.AccessToken;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMExpress;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMResult;
import com.gerray.fmsystem.Transactions.Mpesa.util.TransactionType;
import com.gerray.fmsystem.ManagerModule.TransactionActivity;
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

public class MPESAExpressActivity extends AppCompatActivity {

    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;

    EditText editTextAmount;

    @BindView(R.id.sendButton)
    Button sendButton;

    Daraja daraja;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesaexpress);
        editTextAmount = findViewById(R.id.editTextAmount);
        ButterKnife.bind(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String workID = intent.getExtras().getString("workID").trim();
        String payerID = firebaseUser.getUid();


        daraja = Daraja.with("AwPFI8FI8bMXE5S3sP63EWuFHeVyKf0S", "ZAL8boGZBpcRfhqG", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(MPESAExpressActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
//                Toast.makeText(MPESAExpressActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(MPESAExpressActivity.this.getClass().getSimpleName(), error);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Work Orders").child(workID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editTextAmount.setText(snapshot.child("cost").getValue().toString());
                String consultantID = snapshot.child("consultantID").getValue().toString();
                String workDescription = snapshot.child("workDescription").getValue().toString();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(payerID);
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        editTextPhoneNumber.setText(snapshot.child("phone").getValue().toString());

                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(consultantID);
                        databaseReference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("firstName").getValue().toString() + " " + snapshot.child("secondName").getValue().toString();

                                sendButton.setOnClickListener(v -> {

                                    //Get Phone Number from User Input
                                    String phoneNumber = editTextPhoneNumber.getText().toString().trim();

                                    //Amount
                                    String amount = editTextAmount.getText().toString().trim();

                                    if (TextUtils.isEmpty(phoneNumber)) {
                                        editTextPhoneNumber.setError("Please Provide a Phone Number");
                                    }
                                    if (TextUtils.isEmpty(amount)) {
                                        editTextPhoneNumber.setError("Please Enter the Amount");
                                    }
                                    paymentAct(amount, phoneNumber, name, consultantID,workDescription);

                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void paymentAct(String amount, String phoneNumber, String name, String consID, String workDec) {
        LNMExpress lnmExpress = new LNMExpress(
                "174379",
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                TransactionType.CustomerPayBillOnline,
                amount,
                "254705499614",
                "174379",
                phoneNumber,
                "http://mpesa-requestbin.herokuapp.com/v5zzbkv6",
                name,
                "Transaction"
        );

        daraja.requestMPESAExpress(lnmExpress,
                new DarajaListener<LNMResult>() {
                    @Override
                    public void onResult(@NonNull LNMResult lnmResult) {
                        Toast.makeText(MPESAExpressActivity.this, lnmResult.CustomerMessage, Toast.LENGTH_SHORT).show();
                        Timber.i(lnmResult.ResponseDescription);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Transactions");
                        String transID = UUID.randomUUID().toString();
                        DateFormat dateFormat = DateFormat.getDateInstance();
                        Date date = new Date();
                        final String transDate = dateFormat.format(date);

                        TransactionDetails details = new TransactionDetails(transID, lnmResult.MerchantRequestID, firebaseUser.getUid(), consID, amount, workDec, transDate);
                        reference.child(transID).setValue(details);

                        startActivity(new Intent(MPESAExpressActivity.this, TransactionActivity.class));
                    }

                    @Override
                    public void onError(String error) {
                        Timber.i(error);
                    }
                }
        );
    }
}
