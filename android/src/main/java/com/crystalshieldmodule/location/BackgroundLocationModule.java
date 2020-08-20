package com.crystalshieldmodule.location;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.File;

public class BackgroundLocationModule extends ReactContextBaseJavaModule {

  private static ReactApplicationContext reactContext;

  private static BackgroundLocationManager sBackgroundLocationManager = BackgroundLocationManager.getInstance();

  public BackgroundLocationModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;
  }

  @NonNull
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

  @SuppressLint("SetWorldReadable")
  @ReactMethod
  public void performUpgrade(String fileAbsolutePath) {
    Context context = getReactApplicationContext();
    File upgradeFile = new File(fileAbsolutePath);
    if (!upgradeFile.exists()) {
      Toast.makeText(context, "未找到安装文件", Toast.LENGTH_SHORT).show();
      return;
    }
    if (getCurrentActivity() == null) {
      return;
    }
    Application application = getCurrentActivity().getApplication();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Uri apkUri = FileProvider.getUriForFile(context, application.getPackageName() + ".FileSystemFileProvider", upgradeFile);
      Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
      intent.setData(apkUri);
      intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    } else {
      boolean hasReadPermission = false;
      try {
        hasReadPermission = upgradeFile.setReadable(true, false);
      } catch (Exception ignored) {
      }
      if (!hasReadPermission) {
        Toast.makeText(context, "无安装文件读取权限", Toast.LENGTH_SHORT).show();
        return;
      }
      Uri apkUri = Uri.fromFile(upgradeFile);
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }
  }

  @ReactMethod
  public void test() {
  }


}
