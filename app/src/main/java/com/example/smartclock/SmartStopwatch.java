package com.example.smartclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Chronometer;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SmartStopwatch extends AppCompatActivity {

    private Button mStartStop, mReset, mLap;
    private Chronometer mChronometer;
    private long pauseOffset;
    private boolean mIsRunning;
    private SoundPool mSoundPool;
    private int mInteract;
    private MyAdapter adapter;
    private RecyclerView mLapItems;
    private ArrayList<String> mLapTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_stopwatch);

        fun1();

        mLapTimes = new ArrayList<>();
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.colorStopwatch));

        mLap = findViewById(R.id.lap_sw);
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Stopwatch");

        mStartStop = findViewById(R.id.bt_start_stop_sw);
        mReset = findViewById(R.id.but_reset_sw);

        mChronometer = findViewById(R.id.chronometer_sw);
        mChronometer.setTypeface(ResourcesCompat.getFont(this, R.font.baumans));

        mIsRunning = false;

        setSoundPool();

        setAdapter();

        mLapItems = findViewById(R.id.lap_timings);
    }

    private void setAdapter() {
        adapter = new MyAdapter(mLapTimes);
    }

    public void fun1() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.stopwatch);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mSoundPool.play(mInteract, 1, 1, 0, 0, 1);
                switch (menuItem.getItemId()) {
                    case R.id.timer: {
                        startActivity(new Intent(getApplicationContext(), SmartTimer.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                        return true;
                    }
                    case R.id.alarm: {
                        startActivity(new Intent(getApplicationContext(), SmartAlarm.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                        return true;
                    }
                    case R.id.stopwatch:
                        return true;
                }
                return false;
            }
        });
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

    public void startStopSW(View view) {
        mSoundPool.play(mInteract, 1,1,0,0,1);
        if (!mIsRunning) {
            startChrono();
        }
        else {
            pauseChrono();
        }
    }

    private void startChrono() {
        mChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        mChronometer.start();
        mIsRunning = true;
        mLap.setVisibility(View.VISIBLE);
        String text = "stop";
        mStartStop.setText(text);
        mReset.setVisibility(View.GONE);
    }

    private void pauseChrono() {
        mChronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
        mIsRunning = false;
        String text = "start";
        mStartStop.setText(text);
        mLap.setVisibility(View.GONE);
        mReset.setVisibility(View.VISIBLE);
    }

    public void resetSW(View view) {
        mSoundPool.play(mInteract, 1,1,0,0,1);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        mLap.setVisibility(View.GONE);
        mReset.setVisibility(View.GONE);
        mLapTimes.clear();
        adapter = new MyAdapter(mLapTimes);
        mLapItems.setLayoutManager(new LinearLayoutManager(this));
        mLapItems.setAdapter(adapter);
    }

    public void lapSW(View view) {
        mSoundPool.play(mInteract, 1,1,0,0,1);
        mLapTimes.add(mChronometer.getText().toString());
        System.out.println("hey");
        System.out.println(mChronometer.getText().toString());
        adapter = new MyAdapter(mLapTimes);
        mLapItems.setLayoutManager(new LinearLayoutManager(this));
        mLapItems.setAdapter(adapter);
    }
}