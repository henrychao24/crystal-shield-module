package com.crystalshieldmodule.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.facebook.react.BuildConfig;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;

public class OnceLocationTask {

  private Promise mPromise;

  private AMapLocationListener mLocationListener = amapLocation -> {
    if (amapLocation != null) {
      if (amapLocation.getErrorCode() == 0) {
        WritableMap params = Arguments.createMap();
        params.putDouble("longitude", amapLocation.getLongitude());
        params.putDouble("latitude", amapLocation.getLatitude());
        params.putString("address", amapLocation.getAddress());
        params.putString("citycode", amapLocation.getCityCode());
        mPromise.resolve(params);
      } else {
        WritableMap params = Arguments.createMap();
        WritableMap errorMap = Arguments.createMap();
        errorMap.putInt("errorCode", amapLocation.getErrorCode());
        errorMap.putString("errorInfo", amapLocation.getErrorInfo());
        params.putMap("error", errorMap);
        mPromise.reject(new RuntimeException("单次定位异常"), params);
      }
    }
  };

  OnceLocationTask(Promise promise) {
    mPromise = promise;
  }

  public void requestOnceLocation(Context context) {
    AMapLocationClient locationClient = new AMapLocationClient(context);
    // 初始化定位参数
    AMapLocationClientOption locationOption = new AMapLocationClientOption();
    // 设置定位监听
    locationClient.setLocationListener(mLocationListener);
    // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
    locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    // 获取一次定位结果：
    // 该方法默认为false。
    locationOption.setOnceLocation(true);
    // 获取最近3s内精度最高的一次定位结果：
    // 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
    // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
    locationOption.setOnceLocationLatest(true);
    //设置定位参数
    locationClient.setLocationOption(locationOption);
    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    // 启动定位
    locationClient.startLocation();
  }

}
