package com.prayer.times;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationService extends IntentService
{
	private String mMessage;
	private NotificationManager mNotificationManager;

	public NotificationService()
	{
		super("com.prayer.times");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		final String PREF_SALAH = "PREF_SALAH";
		
		mMessage = getResources().getString(R.string.prayer_reminder, intent.getStringExtra(PREF_SALAH));
		mNotificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
		NotificationChannel channel = new NotificationChannel("salah_times", "adhan", NotificationManager.IMPORTANCE_LOW);
		mNotificationManager.createNotificationChannel(channel);
		createNotification();
	}

	private void createNotification()
	{
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_clock_white_24dp)
				.setContentTitle(mMessage)
				.setChannelId("salah_times")
				.setPriority(2);
			
		mNotificationManager.notify(001, notificationBuilder.build());
	}
}