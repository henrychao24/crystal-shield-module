package com.crystalshieldmodule.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.facebook.react.BuildConfig;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

public class BackgroundLocationManager {

  public static final String EventLocation = "EventLocation";

  private static BackgroundLocationManager sBackgroundLocationManager = new BackgroundLocationManager();

  private BackgroundLocationManager() {
  }

  public static BackgroundLocationManager getInstance() {
    return sBackgroundLocationManager;
  }

  private AMapLocationClient mLocationClient;
  private AMapLocationListener mLocationListener = amapLocation -> {
    if (amapLocation != null) {
      if (amapLocation.getErrorCode() == 0) {
        WritableMap params = Arguments.createMap();
        WritableMap locationMap = Arguments.createMap();
        locationMap.putDouble("longitude", amapLocation.getLongitude());
        locationMap.putDouble("latitude", amapLocation.getLatitude());
        locationMap.putString("contentJson", amapLocation.toStr());
        params.putMap("location", locationMap);
        BackgroundLocationModule.sendEvent(EventLocation, params);
      } else {
        WritableMap params = Arguments.createMap();
        WritableMap errorMap = Arguments.createMap();
        errorMap.putInt("errorCode", amapLocation.getErrorCode());
        errorMap.putString("errorInfo", amapLocation.getErrorInfo());
        params.putMap("error", errorMap);
        BackgroundLocationModule.sendEvent(EventLocation, params);
      }
    }
  };

  // 必须在AmapLocationClient实例化之前调用
  public void setApiKey(String apiKey) {
    AMapLocationClient.setApiKey(apiKey);
  }

  public void startLocationUpdates(Context context, int interval, int channelId, String channelName, String firstLineText, String secondLineText) {
    if (hasStartedLocationUpdates()) {
      stopLocationUpdates();
    }

    AMapLocationClient.updatePrivacyShow(context,true,true);
    AMapLocationClient.updatePrivacyAgree(context,true);
    try {
      mLocationClient = new AMapLocationClient(context);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    //初始化定位参数
    AMapLocationClientOption locationOption = new AMapLocationClientOption();
    //设置定位监听
    mLocationClient.setLocationListener(mLocationListener);
    //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
    locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    //设置定位间隔,单位毫秒,默认为2000ms
    locationOption.setInterval(interval);
    //设置定位参数
    mLocationClient.setLocationOption(locationOption);
    // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
    // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
    // 在定位结束后，在合适的生命周期调用onDestroy()方法
    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    // 启动定位
    mLocationClient.startLocation();
    mLocationClient.enableBackgroundLocation(channelId, NotificationUtils.buildNotification(context, channelName, firstLineText, secondLineText));
  }

  public void stopLocationUpdates() {
    if (mLocationClient != null) {
      mLocationClient.disableBackgroundLocation(true);
      mLocationClient.stopLocation();
      mLocationClient.onDestroy();
      mLocationClient = null;
    }
    mLocationListener = null;
  }

  public boolean hasStartedLocationUpdates() {
    return mLocationClient != null && mLocationClient.isStarted();
  }

}
