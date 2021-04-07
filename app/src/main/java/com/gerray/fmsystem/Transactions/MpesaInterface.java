package com.gerray.fmsystem.Transactions;

import com.gerray.fmsystem.Transactions.Model.AccessToken;
import com.gerray.fmsystem.Transactions.Model.STKPush;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface MPESAInterface {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    Observable<AccessToken> getAccessToken();
    @POST("mpesa/stkpush/v1/processrequest")
    Observable<STKPush> sendPush(@Body STKPush stkPush);
}
