package com.nuaa.locpayseller.data;

import com.nuaa.locpayseller.model.BaseResponse;
import com.nuaa.locpayseller.model.EmptyResponse;
import com.nuaa.locpayseller.model.PayRequest;
import com.nuaa.locpayseller.model.SellerRegisterRequest;

import io.reactivex.Observable;

public class DataManager {

    private static DataManager sDataManager = null;

    private IDataSource mDataSource;

    public DataManager(IDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    public static DataManager getInstance() {
        if (sDataManager == null) {
            synchronized (DataManager.class) {
                if (sDataManager == null) {
                    sDataManager = new DataManager(RetrofitServiceUtil.create());
                }
            }
        }
        return sDataManager;
    }

    public Observable<BaseResponse<EmptyResponse>> sellerRegister(String sid, String account, String loc, String publicKey) {
        return mDataSource.sellerRegister(new SellerRegisterRequest(sid, account, loc, publicKey));
    }

    public Observable<BaseResponse<EmptyResponse>> pay(PayRequest request) {
        return mDataSource.pay(request);
    }
}
