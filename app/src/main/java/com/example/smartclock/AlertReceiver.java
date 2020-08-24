package com.example.smartclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer.create(context, R.raw.timer).start();
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(20000);
    }
}
