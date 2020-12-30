package com.prayer.times;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import java.io.IOException;

public class App extends Application {
  private static App sIntance;
  private MediaPlayer mPlayer;

  public static void startAdhan() throws IOException {
    sIntance.mPlayer.stop();
    sIntance.mPlayer.setScreenOnWhilePlaying(true);
    AssetFileDescriptor descriptor = sIntance.getAssets().openFd("adhan.mp3");
    sIntance.mPlayer.setDataSource(
        descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
    sIntance.mPlayer.setAudioAttributes(
        new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
    sIntance.mPlayer.prepare();
    sIntance.mPlayer.start();
  }

  public static void stopAdhan() {
    sIntance.mPlayer.stop();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    sIntance = this;
    mPlayer = new MediaPlayer();
  }
}
