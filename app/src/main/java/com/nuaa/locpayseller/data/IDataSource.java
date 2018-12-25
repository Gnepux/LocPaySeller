package com.nuaa.locpayseller.data;

import com.nuaa.locpayseller.model.BaseResponse;
import com.nuaa.locpayseller.model.EmptyResponse;
import com.nuaa.locpayseller.model.PayRequest;
import com.nuaa.locpayseller.model.SellerRegisterRequest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IDataSource {

    @POST("/business/sellerRegister/")
    Observable<BaseResponse<EmptyResponse>> sellerRegister(@Body SellerRegisterRequest request);

    @POST("/business/pay/")
    Observable<BaseResponse<EmptyResponse>> pay(@Body PayRequest request);
}
