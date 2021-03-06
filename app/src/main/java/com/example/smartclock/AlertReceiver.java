package com.example.smartclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        notifyMe(context, intent);
    }
    public void notifyMe(Context context, Intent intent) {
        int idx = 1;
        MediaPlayer player = MediaPlayer.create(context, R.raw.one);

        if (intent.getExtras() != null) {
            idx = intent.getExtras().getInt("musicID");
        }
        if (idx == 1)
            player = MediaPlayer.create(context, R.raw.one);
        else if (idx == 2)
            player = MediaPlayer.create(context, R.raw.two);
        else if (idx == 3)
            player = MediaPlayer.create(context, R.raw.three);
        else if (idx == 4)
            player = MediaPlayer.create(context, R.raw.four);
        else if (idx == 5) {
            player = MediaPlayer.create(context, R.raw.five);
        }
        player.start();
        final MediaPlayer player1 = player;
        new CountDownTimer(30000, 30000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                player1.stop();
            }
        }.start();
    }
}
