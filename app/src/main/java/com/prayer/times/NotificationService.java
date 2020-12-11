package com.prayer.times;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import java.io.IOException;

public class NotificationService extends JobIntentService {
  private String mMessage;
  private NotificationManager mNotificationManager;
  private static int JOB_ID = 1502;
  private static MediaPlayer player = new MediaPlayer();

  public static void enqueueWork(Context context, Intent work) {
    enqueueWork(context, NotificationService.class, JOB_ID, work);
  }

  @Override
  protected void onHandleWork(Intent intent) {
    String salah_name = intent.getStringExtra(CommonCode.PREF_SALAH);
    salah_name = salah_name.substring(0, 1) + salah_name.substring(1).toLowerCase();
    mMessage = getResources().getString(R.string.prayer_reminder, salah_name);
    mNotificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
    NotificationChannel channel =
        new NotificationChannel("salah_times", "adhan", NotificationManager.IMPORTANCE_LOW);
    mNotificationManager.createNotificationChannel(channel);
    try {
      createNotification();
    } catch (IOException e) {
    }

    CommonCode.Salah nextSalah = CommonCode.getNextPrayer(this);
    CommonCode.setReminder(this, nextSalah.getName(), nextSalah.getTime());
  }

  private void createNotification() throws IOException {
    NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_clock_white_24dp)
            .setContentTitle(mMessage)
            .setChannelId("salah_times")
            .setPriority(2);

    mNotificationManager.notify(001, notificationBuilder.build());
    AssetFileDescriptor afd = getAssets().openFd("adhan.mp3");
    player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
    player.setAudioAttributes(
        new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
    player.prepare();
    player.start();
  }
}
