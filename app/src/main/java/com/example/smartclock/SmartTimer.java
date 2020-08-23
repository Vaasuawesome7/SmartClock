package com.example.smartclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class SmartTimer extends AppCompatActivity {

    private TextView mCountdownText, mHText, mMText, mSText;
    private int mHour, mMinute, mSecond;
    private Button mStartStop, mReset;
    private Vibrator mVibrator;
    private NumberPicker mPickSecond, mPickMinute, mPickHour;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;
    private boolean mTimerRunning;
    private MediaPlayer player;
    private boolean isFinished;
    private SoundPool mSoundPool;
    private int mInteract;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_timer);
        fun1();

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.colorTimer));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Timer");

        mTimerRunning = false;
        isFinished = false;
        player = MediaPlayer.create(getApplicationContext(), R.raw.timer);

        mHText = findViewById(R.id.timer_hour);
        mMText = findViewById(R.id.timer_minute);
        mSText = findViewById(R.id.timer_second);

        mStartStop = findViewById(R.id.but_start_stop);
        mReset = findViewById(R.id.but_reset);

        mPickSecond = findViewById(R.id.pick_seconds);
        mPickMinute = findViewById(R.id.pick_minutes);
        mPickHour = findViewById(R.id.pick_hours);

        mCountdownText = findViewById(R.id.countdown_text);

        mPickSecond.setMinValue(0);
        mPickSecond.setMaxValue(59);

        mPickMinute.setMinValue(0);
        mPickMinute.setMaxValue(59);

        mPickHour.setMinValue(0);
        mPickHour.setMaxValue(23);

        setVibrator();

        setSoundPool();
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


    private void setVibrator() {
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }

    public void fun1() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.timer);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mSoundPool.play(mInteract, 1, 1, 0, 0, 1);
                switch (menuItem.getItemId()) {
                    case R.id.stopwatch: {
                        startActivity(new Intent(getApplicationContext(), SmartStopwatch.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;
                    }
                    case R.id.alarm: {
                        startActivity(new Intent(getApplicationContext(), SmartAlarm.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;
                    }
                    case R.id.timer:
                        return true;
                }
                return false;
            }
        });
    }

    private void getStartingTime() {
        mHour = mPickHour.getValue();
        mMinute = mPickMinute.getValue();
        mSecond = mPickSecond.getValue();
        mTimeLeftInMillis = mHour*3600 + mMinute*60 + mSecond;
        mTimeLeftInMillis *= 1000;
    }

    public void startStop(View view) {
        mSoundPool.play(mInteract, 1,1,0,0,1);
        if (mPickHour.getVisibility() == View.VISIBLE)
            getStartingTime();
        boolean check = mTimeLeftInMillis == 0 && !mTimerRunning;

        if (!check){
            if (!mTimerRunning) {
                startTimer();
            }
            else {
                pauseTimer();
                if (isFinished) {
                    player.stop();
                    mVibrator.cancel();
                    mReset.setVisibility(View.GONE);
                    isFinished = false;
                }
            }
        }
    }

    public void reset(View view) {
        mSoundPool.play(mInteract, 1,1,0,0,1);
        resetTimer();
    }

    public void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                player = MediaPlayer.create(getApplicationContext(), R.raw.timer);
                player.start();
                mVibrator.vibrate(11000);
                resetTimer();
                isFinished = true;
            }
        }.start();
        mTimerRunning = true;
        String stop = "stop";
        mStartStop.setText(stop);
        mSText.setVisibility(View.GONE);
        mReset.setVisibility(View.INVISIBLE);
        mMText.setVisibility(View.GONE);
        mHText.setVisibility(View.GONE);
        mPickHour.setVisibility(View.GONE);
        mPickMinute.setVisibility(View.GONE);
        mPickSecond.setVisibility(View.GONE);
        mCountdownText.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int secs = (int) mTimeLeftInMillis/1000;
        secs = secs%60;
        int min = ((int)mTimeLeftInMillis/1000)/60;
        min = min % 60;
        int hours = ((int)mTimeLeftInMillis/1000)/3600;

        System.out.println(hours + " " + min + " " + secs);

        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, min, secs);

        mCountdownText.setText(timeLeft);
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        String start = "start";
        mStartStop.setText(start);
        mReset.setVisibility(View.VISIBLE);
    }

    public void resetTimer() {
        isFinished = false;
        mCountDownTimer.cancel();
        mHText.setVisibility(View.VISIBLE);
        mMText.setVisibility(View.VISIBLE);
        mSText.setVisibility(View.VISIBLE);
        mPickSecond.setVisibility(View.VISIBLE);
        mPickMinute.setVisibility(View.VISIBLE);
        mPickHour.setVisibility(View.VISIBLE);
        mReset.setVisibility(View.INVISIBLE);
        mCountdownText.setVisibility(View.INVISIBLE);
        mPickSecond.setValue(0);
        mPickMinute.setValue(0);
        mPickHour.setValue(0);
    }

}