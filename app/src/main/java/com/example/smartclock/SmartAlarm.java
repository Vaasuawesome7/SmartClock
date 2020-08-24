package com.example.smartclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SmartAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private SoundPool mSoundPool;
    private int mInteract;
    private ArrayList<String> mAlarmList = new ArrayList<>();
    private AlarmAdapter adapter;
    private RecyclerView mAlarmView;
    private String time;
    private ArrayList<Boolean> mSwitchStates = new ArrayList<>();

    private final String SHARED_PREFERENCES = "sharedPrefs";
    private final String SHARED_PREF_STRING = "alarm";
    private final String SHARED_PREF_SWITCH = "switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_alarm);
        mAlarmView = findViewById(R.id.alarm_view);
        time = "";
        fun1();
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.colorAlarm));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Alarm");

        setSoundPool();
        mAlarmList = new ArrayList<>();
        mSwitchStates = new ArrayList<>();
        loadData();
        Toast.makeText(this, "created", Toast.LENGTH_SHORT).show();
        adapter = new AlarmAdapter(mAlarmList, mSwitchStates);
        mAlarmView.setLayoutManager(new LinearLayoutManager(this));
        mAlarmView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int pos) {
                mAlarmList.remove(pos);
                mSwitchStates.remove(pos);
                adapter.notifyItemRemoved(pos);
                Toast.makeText(SmartAlarm.this, "setted", Toast.LENGTH_SHORT).show();
                saveData();
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
                        finish();
                        return true;
                    }
                    case R.id.timer: {
                        startActivity(new Intent(getApplicationContext(), SmartTimer.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                        return true;
                    }
                    case R.id.alarm:
                        return true;
                }
                return false;
            }
        });
    }

    public void addAlarm(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time = hourOfDay + ":" + minute;
        if (hourOfDay<10)
            time = "0" + hourOfDay + ":" + minute;
        if (minute<10)
            time = hourOfDay + ":0" + minute;
        System.out.println("first");
        System.out.println("second");
        mAlarmList.add(time);
        mSwitchStates.add(false);
        System.out.println(time);
        adapter.notifyDataSetChanged();
        saveData();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(mAlarmList);
        String json1 = gson.toJson(mSwitchStates);
        editor.putString(SHARED_PREF_STRING, json);
        editor.putString(SHARED_PREF_SWITCH, json1);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREF_STRING, null);
        String json1 = sharedPreferences.getString(SHARED_PREF_SWITCH, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Type type1 = new TypeToken<ArrayList<Boolean>>() {}.getType();
        mAlarmList = gson.fromJson(json, type);
        mSwitchStates = gson.fromJson(json1, type1);
        if (mAlarmList == null)
            mAlarmList = new ArrayList<>();
        if (mSwitchStates == null)
            mSwitchStates = new ArrayList<>();
    }
}