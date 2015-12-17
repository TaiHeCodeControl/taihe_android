package com.taihe.eggshell.main;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.taihe.eggshell.base.utils.PrefUtils;

/**
 * Created by wang on 2015/12/16.
 */
public class LocationService extends Service{

    private static final String TAG = "LOCATIONSERVICE";
    public static final String ACTION_NAME = "com.taihe.eggshell.main.LocationService";

    private Context mContext;

    //====================定位-----------==============
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private String Longitude = "";
    private String Latitude = "";
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        mContext = this;
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initLocation();
        flags = START_STICKY;//在运行onStartCommand后Service进程被kill后，将保留在开始状态，但不保留intent，不久后service将重新建立
        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
        int span = 60000;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为 1min
        option.setIsNeedAddress(true);
        mLocationClient.start();
        mLocationClient.setLocOption(option);
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            Longitude = Double.toString(location.getLongitude());
            Latitude = Double.toString(location.getLatitude());
            PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "Longitude", Longitude);
            PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, "Latitude", Latitude);
        }
    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

}
