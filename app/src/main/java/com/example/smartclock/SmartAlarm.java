package com.example.smartclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SmartAlarm extends AppCompatActivity implements AlarmAdapter.OnAlarmListener, TimePickerDialog.OnTimeSetListener {

    private SoundPool mSoundPool;
    private int mInteract;
    private ArrayList<String> mAlarmList;
    private AlarmAdapter adapter;
    private RecyclerView mAlarmView;
    private TextView decoy;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_alarm);
        mAlarmView = findViewById(R.id.alarm_view);
        time = "";
        fun1();

        decoy = findViewById(R.id.decoy);
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.colorAlarm));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Alarm");

        setSoundPool();
        mAlarmList = new ArrayList<>();
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


    public void fun1() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.alarm);
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
                    case R.id.timer: {
                        startActivity(new Intent(getApplicationContext(), SmartTimer.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;
                    }
                    case R.id.alarm:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onAlarmClick(int pos) {

    }

    public void addAlarm(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
        System.out.println("second");
        mAlarmList.add(time);
        System.out.println(time);
        adapter = new AlarmAdapter(mAlarmList);
        adapter.notifyDataSetChanged();
        mAlarmView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAlarmView.setAdapter(adapter);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time = hourOfDay + ":" + minute;
        if (hourOfDay<10)
            time = "0" + hourOfDay + ":" + minute;
        if (minute<10)
            time = hourOfDay + ":0" + minute;
        System.out.println("first");
    }
}