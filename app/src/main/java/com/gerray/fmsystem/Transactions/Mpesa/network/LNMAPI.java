package com.gerray.fmsystem.Transactions.Mpesa.network;

import com.gerray.fmsystem.Transactions.Mpesa.model.LNMExpress;
import com.gerray.fmsystem.Transactions.Mpesa.model.LNMResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LNMAPI {

    @POST("mpesa/stkpush/v1/processrequest")
    Call<LNMResult> getLNMPesa(@Body LNMExpress lnmExpress);

}