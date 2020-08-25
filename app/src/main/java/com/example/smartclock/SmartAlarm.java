package com.example.smartclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class SmartAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private SoundPool mSoundPool;
    private int mInteract;
    private ArrayList<String> mAlarmList = new ArrayList<>();
    private AlarmAdapter adapter;
    private String time;
    private ArrayList<Boolean> mSwitchStates = new ArrayList<>();
    private RecyclerView mAlarmView;
    private ArrayList<Integer> mMusic;
    private ArrayList<String> mDays;

    private final String SHARED_PREFERENCES = "sharedPrefs";
    private final String SHARED_PREF_STRING = "alarm";
    private final String SHARED_PREF_SWITCH = "switch";
    private final String SHARED_PREF_MUSIC = "music";
    private final String SHARED_PREF_DAYS = "days";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_alarm);
        mAlarmView = findViewById(R.id.alarm_view);
        time = "";
        fun1();
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.colorAlarm));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Alarm");

        setSoundPool();
        mAlarmList = new ArrayList<>();
        mSwitchStates = new ArrayList<>();
        mMusic = new ArrayList<>();
        mDays = new ArrayList<>();
        loadData();
        onClickListeners();
        checkStartingAlarms();
    }

    private void checkStartingAlarms() {
        for (int i = 0; i < mSwitchStates.size(); i++) {
            if (mSwitchStates.get(i))
                startAlarm(i);
        }
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

    private void onClickListeners() {
        adapter = new AlarmAdapter(mAlarmList, mSwitchStates, mMusic);
        mAlarmView.setLayoutManager(new LinearLayoutManager(this));
        mAlarmView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int pos) {
                mSoundPool.play(mInteract, 1, 1, 0, 0, 1);
                if (mSwitchStates.get(pos))
                    stopAlarm(pos);
                mAlarmList.remove(pos);
                mSwitchStates.remove(pos);
                mMusic.remove(pos);
                mDays.remove(pos);
                adapter.notifyItemRemoved(pos);
                saveData();
            }

            @Override
            public void onSwitchClick(int pos) {
                mSoundPool.play(mInteract, 1, 1, 0, 0, 1);
                mSwitchStates.set(pos, !mSwitchStates.get(pos));
                boolean val = mSwitchStates.get(pos);
                if (val) {
                    startAlarm(pos);
                }
                else {
                    stopAlarm(pos);
                    mSwitchStates.set(pos, false);
                }
                adapter.notifyItemChanged(pos);
                saveData();
            }

            @Override
            public void onTextClick(int pos) {
                mSoundPool.play(mInteract, 1, 1, 0, 0, 1);
                int ptr = mMusic.get(pos) + 1;
                if (ptr == 6)
                    ptr = 1;
                mMusic.set(pos, ptr);
                adapter.notifyItemChanged(pos);
                if (mSwitchStates.get(pos)) {
                    stopAlarm(pos);
                    startAlarm(pos);
                }
            }

            @Override
            public void onAlarmClick(int pos) {

            }
        });
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
        mSoundPool.play(mInteract, 1, 1, 0, 0, 1);
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
        if (mAlarmList.contains(time)) {
            Toast.makeText(this, "Time already exists!", Toast.LENGTH_SHORT).show();
        }
        else {
            mAlarmList.add(time);
            mSwitchStates.add(false);
            mMusic.add(1);
            mDays.add("" + getDayChar());
            adapter.notifyDataSetChanged();
        }
        saveData();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(mAlarmList);
        String json1 = gson.toJson(mSwitchStates);
        String json2 = gson.toJson(mMusic);
        String json3 = gson.toJson(mDays);
        editor.putString(SHARED_PREF_STRING, json);
        editor.putString(SHARED_PREF_SWITCH, json1);
        editor.putString(SHARED_PREF_MUSIC, json2);
        editor.putString(SHARED_PREF_DAYS, json3);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SHARED_PREF_STRING, null);
        String json1 = sharedPreferences.getString(SHARED_PREF_SWITCH, null);
        String json2 = sharedPreferences.getString(SHARED_PREF_MUSIC, null);
        String json3 = sharedPreferences.getString(SHARED_PREF_DAYS, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Type type1 = new TypeToken<ArrayList<Boolean>>() {}.getType();
        Type type2 = new TypeToken<ArrayList<Integer>>() {}.getType();
        Type type3 = new TypeToken<ArrayList<String>>() {}.getType();
        mAlarmList = gson.fromJson(json, type);
        mSwitchStates = gson.fromJson(json1, type1);
        mMusic = gson.fromJson(json2, type2);
        mDays = gson.fromJson(json3, type3);
        if (mAlarmList == null)
            mAlarmList = new ArrayList<>();
        if (mSwitchStates == null)
            mSwitchStates = new ArrayList<>();
        if (mMusic == null)
            mMusic = new ArrayList<>();
        if (mDays == null)
            mDays = new ArrayList<>();
    }

    public void startAlarm(int pos) {

        Calendar c = Calendar.getInstance();
        String time = mAlarmList.get(pos);
        int h = Integer.parseInt(time.substring(0,2));
        int m = Integer.parseInt(time.substring(3));
        int key = h*60 + m;
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.MINUTE, m);
        c.set(Calendar.SECOND, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlertReceiver.class);
        i.putExtra("musicID", mMusic.get(pos));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), key, i, PendingIntent.FLAG_UPDATE_CURRENT);
        String alarmString = h + ":" + m;
        if (h<10)
            alarmString = "0" + h + ":" + m;
        if (m<10)
            alarmString =  h + ":0" + m;

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
            Toast.makeText(this, "Alarm will ring tomorrow at " + alarmString, Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Alarm set at " + alarmString, Toast.LENGTH_SHORT).show();
        manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void stopAlarm(int pos) {
        String time = mAlarmList.get(pos);
        int h = Integer.parseInt(time.substring(0,2));
        int m = Integer.parseInt(time.substring(3));
        int key = h*60 + m;
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlertReceiver.class);
        i.putExtra("musicID", mMusic.get(pos));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), key, i, PendingIntent.FLAG_UPDATE_CURRENT);
        String alarmString = h + ":" + m;
        if (h<10)
            alarmString = "0" + h + ":" + m;
        if (m<10)
            alarmString =  h + ":0" + m;
        Toast.makeText(this, "Alarm at " + alarmString + " is cancelled", Toast.LENGTH_SHORT).show();
        manager.cancel(pendingIntent);
    }

    private char getDayChar() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                return 's';
            case Calendar.MONDAY:
                return 'M';
            case Calendar.TUESDAY:
                return 't';
            case Calendar.WEDNESDAY:
                return 'W';
            case Calendar.THURSDAY:
                return 'T';
            case Calendar.FRIDAY:
                return 'F';
            case Calendar.SATURDAY:
                return 'S';
        }

        return 's';
    }
}