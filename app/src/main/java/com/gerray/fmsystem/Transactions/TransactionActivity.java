package com.gerray.fmsystem.Transactions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.gerray.fmsystem.R;
import com.gerray.fmsystem.Transactions.Config.Config;
import com.gerray.fmsystem.Transactions.Model.AccessToken;
import com.gerray.fmsystem.Transactions.Model.STKPush;
import com.google.android.material.textfield.TextInputEditText;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TransactionActivity extends AppCompatActivity implements PhoneNumberInputDialog.PhoneInputInterface {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static final PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_API);

    Button btnPay, btnMpesa;
    TextInputEditText edAmount;
    String amount = "";

    private CompositeDisposable compositeDisposable;
    private AccessToken newAccessToken;
    private STKPush stkPush;
    private int itemPrice;
    private String phone;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();

        if (!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_pay);
        compositeDisposable = new CompositeDisposable();
        //Start PayPal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        btnPay = findViewById(R.id.btn_Pay);
        edAmount = findViewById(R.id.edpay);

        btnPay.setOnClickListener(v -> processPayment());
        btnMpesa = findViewById(R.id.btn_Mpesa);
        btnMpesa.setOnClickListener(v -> {
            showDialog();
        });
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
    private void showDialog() {
        DialogFragment dialog = new PhoneNumberInputDialog();
        dialog.show(getSupportFragmentManager(), "PhoneNumberInputDialog");
    }
    /*
     * Phone Dialog Interface implementation
     * */

    public void onDialogPositiveClick(String number) {
        phone = number;
        /*
         * Get item price from items list
         * */
//        itemPrice =items.get(itemPosition).getAmount();
        itemPrice = 6500;
        /*
         * Implement authentication progress loader
         * */

        /*
         * Using Rxjava to perform an asynchronous call to the mpesa ouath endpoint
         * */
        compositeDisposable.add(
                NetworkUtil.getRetrofit()
                        .getAccessToken()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleAccessTokenResponse, this::handleAccessTokenException));


    }


    private void handleAccessTokenResponse(AccessToken accessToken) {

        newAccessToken = new AccessToken(accessToken.getAccessToken(),accessToken.getExpiresIn());
        /*
         * Implement payment progress loader
         * */

        /*
         * Implement STK push body
         * */
        stkPush = new STKPush(
                Constants.BUSINESS_SHORT_CODE,
                Constants.getPassword(Constants.BUSINESS_SHORT_CODE,Constants.PASSKEY,Constants.getTimestamp()),
                Constants.getTimestamp(),
                Constants.TRANSACTION_TYPE,
                String.valueOf(itemPrice),
                phone,
                Constants.PARTYB,
                phone,
                Constants.CALLBACK_URL,
                "test",
                "test"
        );

        if (newAccessToken.getAccessToken() != null){
            /*
             * Using Rxjava to perform an asynchronous call to the mpesa stkpush endpoint
             * */
            compositeDisposable.add(
                    NetworkUtil.getRetrofit(newAccessToken.getAccessToken())
                            .sendPush(stkPush)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<STKPush>() {
                                @Override
                                public void accept(STKPush stkPush) throws Exception {
                                    handleSTKResponse(stkPush);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    handleSTKException(throwable);
                                }
                            }));
        }
    }

    private void handleAccessTokenException(Throwable throwable) {

    }

    private void handleSTKResponse(STKPush stkPush) {
        /*
         *
         * */
    }

    private void handleSTKException(Throwable throwable) {
        /*
         * Log the STK request exception
         * */
    }

}