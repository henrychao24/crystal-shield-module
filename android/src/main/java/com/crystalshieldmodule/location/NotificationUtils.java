package com.crystalshieldmodule.location;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.crystalshieldmodule.R;

public class NotificationUtils {

  private static NotificationManager notificationManager = null;
  private static boolean isCreatedChannel = false;

  /**
   * 创建一个通知栏
   */
  public static Notification buildNotification(Context context, String channelName, String firstLineText, String secondLineText) {
    try {
      Context mContext = context.getApplicationContext();
      Notification.Builder builder;
      Notification notification;
      if (android.os.Build.VERSION.SDK_INT >= 26) {
        //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
        if (null == notificationManager) {
          notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        String channelId = mContext.getPackageName();
        if (!isCreatedChannel) {
          NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName,
            NotificationManager.IMPORTANCE_DEFAULT);
          notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
          notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
          notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
          notificationManager.createNotificationChannel(notificationChannel);
          isCreatedChannel = true;
        }
        builder = new Notification.Builder(mContext, channelId);
      } else {
        builder = new Notification.Builder(mContext);
      }

      builder.setSmallIcon(R.drawable.ic_location)
        .setWhen(System.currentTimeMillis());
      if (!TextUtils.isEmpty(firstLineText)) {
        builder.setContentTitle(firstLineText);
      }
      if (!TextUtils.isEmpty(secondLineText)) {
        builder.setContentText(secondLineText);
      }


      notification = builder.build();
      return notification;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
