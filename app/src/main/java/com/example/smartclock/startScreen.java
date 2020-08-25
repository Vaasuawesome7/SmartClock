package com.example.smartclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class startScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

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
        startActivity(new Intent(this, SmartTimer.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}