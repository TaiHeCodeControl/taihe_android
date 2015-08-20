package com.taihe.eggshell.main;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.main.GuideActivity;
import com.taihe.eggshell.main.MainActivity;

/**
 * Created by Thinkpad on 2015/7/15.
 */
public class SplashActivity extends Activity {

    private Context mContext;
    private String Longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        View view = View.inflate(mContext, R.layout.activity_splash, null);
        setContentView(view);

        Animation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(1500);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isFirstUsing = PrefUtils.getBooleanData(mContext, PrefUtils.KEY_FIRST_USE, true);
                if (isFirstUsing) {
                    Intent intent = new Intent(mContext, GuideActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("MeFragment", "");
                    startActivity(intent);
                    finish();
                }

            }
        });

        view.setAnimation(animation);

        initLocation();


    }


    //====================定位-----------==============
    public LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public MyLocationListener mMyLocationListener;


    public Vibrator mVibrator;

    private void initLocation() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mGeofenceClient = new GeofenceClient(getApplicationContext());


        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
        int span=1000;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

        if(TextUtils.isEmpty(Longitude)){
            mLocationClient.start();

        }else{
            mLocationClient.stop();

        }
    }


    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }


    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        mLocationClient.stop();
        super.onStop();
    }
}
