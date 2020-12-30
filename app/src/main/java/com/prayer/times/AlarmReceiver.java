package com.prayer.times;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction() == "intent.action.SET_PRAYER_REMINDER") {
      Intent serviceIntent = new Intent(context, NotificationService.class);
      serviceIntent.putExtras(intent);
      NotificationService.enqueueWork(context, serviceIntent);
      updatePrayerAlarm(context);
    }
  }

  void updatePrayerAlarm(Context context) {
    CommonCode.Salah nextPrayer = CommonCode.getNextPrayer(context);
    CommonCode.setReminder(context, nextPrayer.getName(), nextPrayer.getTime());
  }
}
