package com.nuaa.locpayseller.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.nuaa.locpayseller.R;
import com.nuaa.locpayseller.data.DataManager;
import com.nuaa.locpayseller.model.BaseResponse;
import com.nuaa.locpayseller.model.EmptyResponse;
import com.nuaa.locpayseller.model.HeaderModel;
import com.nuaa.locpayseller.model.PayRequest;
import com.nuaa.locpayseller.model.QRCodeBean;
import com.nuaa.locpayseller.model.UserInfoBean;
import com.nuaa.locpayseller.permission.PermissionManager;
import com.nuaa.locpayseller.permission.RequestPermCallback;
import com.nuaa.locpayseller.util.KeyUtils;
import com.nuaa.locpayseller.util.RSAUtil;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GetCashActivity extends AppCompatActivity {

    private EditText mDistanceUserSellerEt;

    private EditText mDistanceSellerRegisterPayEt;

    private EditText mSidEt;

    private EditText mAmountEt;

    private TextView mScanTimeTv;

    private String mSid;

    private String mAmount;

    private String mDistanceUserSeller;

    private String mDistanceSellerRegisterPay;

    private BDLocation mLocation = null;

    public LocationClient mLocationClient = null;

    private MyLocationListener myListener = new MyLocationListener();

    private long mTime1;
    private long mTime2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cash);

        mSidEt = findViewById(R.id.et_sid);
        mAmountEt = findViewById(R.id.et_amount);
        mDistanceUserSellerEt = findViewById(R.id.et_distance_user_seller);
        mDistanceSellerRegisterPayEt = findViewById(R.id.et_distance_seller_register_pay);
        mScanTimeTv = findViewById(R.id.tv_scan_time);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

//        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求
    }

    private void startScan() {
        Intent intent = new Intent(GetCashActivity.this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, 1);
    }

    public void scan(View view) {
        mSid = mSidEt.getText().toString();
        if (TextUtils.isEmpty(mSid)) {
            mSidEt.requestFocus();
            mSidEt.setError("请输入sid");
            return;
        }

        mAmount = mAmountEt.getText().toString();
        if (TextUtils.isEmpty(mAmount)) {
            mAmountEt.requestFocus();
            mAmountEt.setError("请输入金额");
            return;
        }

        mDistanceUserSeller = mDistanceUserSellerEt.getText().toString();
        if (TextUtils.isEmpty(mDistanceUserSeller)) {
            mDistanceUserSellerEt.requestFocus();
            mDistanceUserSellerEt.setError("请输入距离");
            return;
        }

        mDistanceSellerRegisterPay = mDistanceSellerRegisterPayEt.getText().toString();
        if (TextUtils.isEmpty(mDistanceSellerRegisterPay)) {
            mDistanceSellerRegisterPayEt.requestFocus();
            mDistanceSellerRegisterPayEt.setError("请输入距离");
            return;
        }

        if (mLocation == null) {
            Toast.makeText(this, "未能获取到当前位置, 请稍候再试", Toast.LENGTH_SHORT).show();
            return;
        }

        PermissionManager.getInstance().requestPermission(this, "需要开启摄像头权限才能扫码", new RequestPermCallback() {
            @Override
            protected void onGrant() {
                mTime1 = System.currentTimeMillis();
                startScan();
            }

            @Override
            protected void onDeny() {
                Toast.makeText(GetCashActivity.this, "开启摄像头权限失败", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Log.e("xupeng", content);
                mTime2 = System.currentTimeMillis();
                Gson gson = new Gson();
                QRCodeBean qrCodeBean = gson.fromJson(content, QRCodeBean.class);
                pay(qrCodeBean);
            }
        }
    }

    private void pay(QRCodeBean qrCodeBean) {
        UserInfoBean userInfoBean = new UserInfoBean(qrCodeBean.getAid(), qrCodeBean.getLon() + "," + qrCodeBean.getLat(), qrCodeBean.getTime(), qrCodeBean.getSign());
        long sellerTime = System.currentTimeMillis();

        long timeDis = Math.abs(sellerTime - qrCodeBean.getTime());
        if (timeDis / 1000 > 60) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GetCashActivity.this);
            builder.setCancelable(false).setMessage("用户二维码已超过60秒有效期, 请重新生成")
                    .setPositiveButton("知道了", null);
            builder.show();
            return;
        }

        if (mLocation == null) {
            Toast.makeText(this, "未能获取到当前位置, 请稍候再试", Toast.LENGTH_SHORT).show();
            return;
        }

        String sellerLon = String.valueOf(mLocation.getLongitude());
        String sellerLat = String.valueOf(mLocation.getLatitude());
        String userLon = String.valueOf(qrCodeBean.getLon());
        String userLat = String.valueOf(qrCodeBean.getLat());

        double distance = getDistance(sellerLat, sellerLon, userLat, userLon);
        if (distance > Double.valueOf(mDistanceUserSeller)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GetCashActivity.this);
            builder.setCancelable(false).setMessage("商户和用户距离为" + distance + "米,超过" + mDistanceUserSeller + "米, 无法支付。" + "扫码用时:" + (mTime2 - mTime1) + "毫秒")
                    .setPositiveButton("知道了", null);
            builder.show();
            return;
        }

        String loc = sellerLon + "," + sellerLat;

        try {
            String msg = mSid + sellerTime + loc + mAmount;
            String msgSign = RSAUtil.sign(KeyUtils.getPrivateKey(mSid), msg);

            PayRequest payRequest = new PayRequest(mSid, sellerTime, loc, mAmount, msgSign, Integer.valueOf(mDistanceSellerRegisterPay), userInfoBean);

            DataManager.getInstance().pay(payRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<BaseResponse<EmptyResponse>>() {
                        @Override
                        public void accept(BaseResponse<EmptyResponse> response) {
                            HeaderModel headerModel = response.getHeader();
                            if (com.nuaa.locpayseller.constant.Constant.OK.equals(headerModel.getErrorCode())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(GetCashActivity.this);
                                builder.setCancelable(false).setMessage("支付成功, 扫码用时:" + (mTime2 - mTime1) + "毫秒")
                                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                builder.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(GetCashActivity.this);
                                builder.setCancelable(false).setMessage(headerModel.getErrorMsg() + ", 扫码用时:" + (mTime2 - mTime1) + "毫秒")
                                        .setPositiveButton("知道了", null);
                                builder.show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Toast.makeText(GetCashActivity.this, "网络错误:" + throwable.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {

        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            mLocation = location;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }

    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离
     */
    public static double getDistance(String lat1, String lng1, String lat2,
                                     String lng2) {

        double radLat1 = rad(Double.parseDouble(lat1));
        double radLat2 = rad(Double.parseDouble(lat2));
        double a = radLat1 - radLat2;
        double b = rad(Double.parseDouble(lng1)) - rad(Double.parseDouble(lng2));
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
