package com.crystalshieldmodule.location;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class BackgroundLocationModule extends ReactContextBaseJavaModule {

  private static ReactApplicationContext reactContext;

  private static BackgroundLocationManager sBackgroundLocationManager = BackgroundLocationManager.getInstance();

  public BackgroundLocationModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;
  }

  @Override
  public String getName() {
    return "BackgroundLocation";
  }

  public static void sendEvent(String eventName, @Nullable WritableMap params) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }

  @ReactMethod
  public void setApiKey(String apiKey) {
    sBackgroundLocationManager.setApiKey(apiKey);
  }

  @ReactMethod
  public void startLocationUpdates(int interval, int channelId, String channelName, String firstLineText, String secondLineText) {
    sBackgroundLocationManager.startLocationUpdates(getReactApplicationContext(), interval, channelId, channelName, firstLineText, secondLineText);
  }

  @ReactMethod
  public void stopLocationUpdates() {
    sBackgroundLocationManager.stopLocationUpdates();
  }

  @ReactMethod
  public void hasStartedLocationUpdates(Promise promise) {
    boolean hasStartedLocationUpdates = sBackgroundLocationManager.hasStartedLocationUpdates();
    promise.resolve(hasStartedLocationUpdates);
  }


  @ReactMethod
  public void test() {
  }


}
