package com.gerray.fmsystem.Transactions.Mpesa;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gerray.fmsystem.R;
import com.gerray.fmsystem.Transactions.Mpesa.constants.Transtype;
import com.gerray.fmsystem.Transactions.Mpesa.model.AccessToken;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMExpress;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMResult;
import com.gerray.fmsystem.Transactions.Mpesa.network.ApiClient;
import com.gerray.fmsystem.Transactions.Mpesa.network.URLs;
import com.gerray.fmsystem.Transactions.Mpesa.util.Env;
import com.gerray.fmsystem.Transactions.Mpesa.util.Settings;
import com.gerray.fmsystem.Transactions.Mpesa.util.TransactionType;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Daraja {
    private String BASE_URL;
    private String CONSUMER_KEY;
    private String CONSUMER_SECRET;

    @Nullable
    private AccessToken accessToken;

    private Daraja(Env env, String CONSUMER_KEY, String CONSUMER_SECRET) {
        this.CONSUMER_KEY = CONSUMER_KEY;
        this.CONSUMER_SECRET = CONSUMER_SECRET;
        this.BASE_URL = (env == Env.SANDBOX) ? URLs.SANDBOX_BASE_URL : URLs.PRODUCTION_BASE_URL;
    }

    //TODO :: CHECK FOR INTERNET CONNECTION
    //Generate the Auth Token
    public static Daraja with(String consumerKey, String consumerSecret, DarajaListener<AccessToken> darajaListener) {
        return with(consumerKey, consumerSecret, Env.SANDBOX, darajaListener);
    }

    public static Daraja with(String CONSUMER_KEY, String CONSUMER_SECRET, Env env, DarajaListener<AccessToken> listener) {
        Daraja daraja = new Daraja(env, CONSUMER_KEY, CONSUMER_SECRET);
        daraja.auth(listener);
        return daraja;
    }

    private void auth(final DarajaListener<AccessToken> listener) {
        ApiClient.getAuthAPI(CONSUMER_KEY, CONSUMER_SECRET, BASE_URL).getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    AccessToken accessToken = response.body();
                    if (accessToken != null) {
                        Daraja.this.accessToken = accessToken;
                        listener.onResult(accessToken);
                        return;
                    }
                }
                listener.onError(String.valueOf(R.string.authentication_failed));
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                listener.onError(String.valueOf(R.string.authentication_failed) + t.getLocalizedMessage());
            }
        });
    }
    public void requestMPESAExpress(LNMExpress lnmExpress, final DarajaListener<LNMResult> listener) {

        if (accessToken == null) {
            listener.onError(String.valueOf(R.string.not_authenticated));

            return;
        }

        String sanitizedPhoneNumber = Settings.formatPhoneNumber(lnmExpress.getPhoneNumber());
        String sanitizedPartyA = Settings.formatPhoneNumber(lnmExpress.getPartyA());
        String timeStamp = Settings.generateTimestamp();
        String generatedPassword = Settings.generatePassword(lnmExpress.getBusinessShortCode(), lnmExpress.getPassKey(), timeStamp);

        LNMExpress express = new LNMExpress(
                lnmExpress.getBusinessShortCode(),
                generatedPassword,
                timeStamp,
                lnmExpress.getAmount(),
                (lnmExpress.getType() == TransactionType.CustomerBuyGoodsOnline) ? Transtype.TRANSACTION_TYPE_CUSTOMER_BUY_GOODS : Transtype.TRANSACTION_TYPE_CUSTOMER_PAYBILL_ONLINE,
                sanitizedPartyA,
                lnmExpress.getPartyB(),
                sanitizedPhoneNumber,
                lnmExpress.getCallBackURL(),
                lnmExpress.getAccountReference(),
                lnmExpress.getTransactionDesc()
        );

        ApiClient.getAPI(BASE_URL, accessToken.getAccess_token()).getLNMPesa(express).enqueue(new Callback<LNMResult>() {
            @Override
            public void onResponse(@NonNull Call<LNMResult> call, @NonNull Response<LNMResult> response) {
                if (response.isSuccessful()) {
                    LNMResult lnmResult = response.body();
                    if (lnmResult != null) {
                        listener.onResult(lnmResult);
                        return;
                    }
                }
                listener.onError(String.valueOf(R.string.on_failure));
            }

            @Override
            public void onFailure(@NonNull Call<LNMResult> call, @NonNull Throwable t) {
                listener.onError(R.string.on_failure + t.getLocalizedMessage());
            }
        });
    }
}
