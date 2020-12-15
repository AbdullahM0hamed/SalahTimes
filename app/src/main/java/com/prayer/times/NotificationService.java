package com.prayer.times;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import java.io.IOException;

public class NotificationService extends JobIntentService {
  private String mMessage;
  private static int JOB_ID = 1502;

  public static void enqueueWork(Context context, Intent work) {
    enqueueWork(context, NotificationService.class, JOB_ID, work);
  }

  @Override
  protected void onHandleWork(Intent intent) {
    String salah_name = intent.getStringExtra(CommonCode.PREF_SALAH);
    salah_name = salah_name.substring(0, 1) + salah_name.substring(1).toLowerCase();
    mMessage = getResources().getString(R.string.prayer_reminder, salah_name);
    createNotification();
    App.updatePrayerAlarm();
  }

  private void createNotification() {
    NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_clock_white_24dp)
            .setContentTitle(mMessage)
            .setChannelId("salah_times")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    NotificationManager mNotificationManager =
        (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel =
          new NotificationChannel("salah_times", "adhan", NotificationManager.IMPORTANCE_LOW);
      mNotificationManager.createNotificationChannel(channel);
    }

    mNotificationManager.notify(1652, notificationBuilder.build());
  }
}
