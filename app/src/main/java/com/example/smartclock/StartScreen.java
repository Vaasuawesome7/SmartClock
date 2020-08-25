package com.example.smartclock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {

    private SoundPool mSoundPool;
    private int mInteract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        setSoundPool();

        ActionBar a = getSupportActionBar();
        assert a != null;
        a.hide();

        TextView smart = findViewById(R.id.smart);
        TextView clock = findViewById(R.id.clock);
        TextView touch = findViewById(R.id.touch_start);
        TextView me = findViewById(R.id.me);

        Animation animationSmart = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.lefttoright);
        Animation animationClock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.righttoleft);
        Animation blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_anim);
        Animation animationMe = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        smart.startAnimation(animationSmart);
        clock.startAnimation(animationClock);
        touch.startAnimation(blink);
        me.startAnimation(animationMe);
    }

    public void start(View view) {
        mSoundPool.play(mInteract, 1, 1, 0, 0, 1);
        startActivity(new Intent(this, SmartTimer.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    private void setSoundPool() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(7)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            mSoundPool = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        }

        mInteract = mSoundPool.load(this, R.raw.button, 1);
    }

}