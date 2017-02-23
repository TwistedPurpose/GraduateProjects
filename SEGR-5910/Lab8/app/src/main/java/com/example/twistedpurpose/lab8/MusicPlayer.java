package com.example.twistedpurpose.lab8;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MusicPlayer extends AppCompatActivity {

    TimeZoneChangeReceiver mTimeZoneChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mTimeZoneChangeReceiver = new TimeZoneChangeReceiver();

        Button playButton = (Button) findViewById(R.id.playBtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MusicService.class);

                startService(intent);
            }
        });

        Button stopButton = (Button) findViewById(R.id.stopBtn);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MusicService.class);

                stopService(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter timeZoneFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mTimeZoneChangeReceiver, timeZoneFilter);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        unregisterReceiver(mTimeZoneChangeReceiver);
    }
}
