package com.taihe.eggshell.map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.taihe.eggshell.R;

import java.util.ArrayList;

/**
 * 用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 *
 */
public class LocationMapActivity extends Activity implements View.OnClickListener{

    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ImageView imgBack;
    boolean isFirstLoc = true;// 是否首次定位
    private Context mContext;
    private ArrayList<Marker> rootMarklist = new ArrayList<Marker>();
    private ArrayList<Marker> childMarkerList = new ArrayList<Marker>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mContext = this;

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.getUiSettings().setCompassEnabled(false);//去掉指南针
        mMapView.showZoomControls(false);//去掉放大缩小按钮
        mMapView.showScaleControl(false);//比例尺控件

        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        initView();
        initData();
    }

    private void initView(){

        imgBack = (ImageView)findViewById(R.id.id_back);
        imgBack.setOnClickListener(this);
        showRootMark();
    }

    private void initData(){

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for(Marker m : rootMarklist){
                    if(marker.getPosition() == m.getPosition()){
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(marker.getPosition(), 16);
                        mBaiduMap.animateMapStatus(u);
                        showChildMarker(marker.getExtraInfo());
                    }
                }
                return false;
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {}
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {}
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                   float flag = mapStatus.zoom;
                   if(flag<12){
                       initView();
                   }
                    if(flag>15){
                        showChildMarker(new Bundle());
                    }
            }
        });
    }

    private void showRootMark(){
        rootMarklist.clear();
        mBaiduMap.clear();

        for(int i=0;i<10;i++){

            LatLng llA = new LatLng(39.963175+(i*0.01), 116.400244+(i*0.01));

            Bundle bundle = new Bundle();
            bundle.putParcelable("",new RootMarker());

            BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(getItemView("海淀区",i));//fromResource(R.drawable.bg_overlay);
            OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(10).extraInfo(bundle);
            Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
            rootMarklist.add(marker);
        }
    }

    private void showChildMarker(Bundle bundle){
        childMarkerList.clear();
        mBaiduMap.clear();
        for(int i=0;i<10;i++){

            LatLng llA = new LatLng(39.963175+(i*0.001), 116.400244+(i*0.001));

            BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(getItemView("文化路",i));//fromResource(R.drawable.bg_overlay);
            OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(10);
            Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
            childMarkerList.add(marker);
        }
    }

    private View getItemView(String title,int number){

        TextView textView = new TextView(mContext);
        textView.setText(title+number+"\n"+number);
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.bg_overlay);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.addView(textView,180,180);
        linearLayout.setGravity(Gravity.CENTER);

        return  linearLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_back:
                finish();
                break;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
//            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true,BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());

                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 12);//中心，级别
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {}
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onStop() {
        // 退出时销毁定位
        mLocClient.stop();
        super.onStop();
    }
}
