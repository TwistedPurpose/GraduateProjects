package com.example.twistedpurpose.lab8;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main activity for app, has two buttons play and stop
 * When play is pressed, song is played.
 * When stop is pressed, song is stopped.
 * When system changes time zones, song is played with notification
 */
public class MusicPlayer extends AppCompatActivity {

    // Receiver for changes in time zone, will play music when triggered
    private TimeZoneChangeReceiver mTimeZoneChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // New receiver for time zone changes
        mTimeZoneChangeReceiver = new TimeZoneChangeReceiver();

        Button playButton = (Button) findViewById(R.id.playBtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When play is hit, start up the music player
                Intent intent = new Intent(v.getContext(), MusicService.class);

                startService(intent);
            }
        });

        Button stopButton = (Button) findViewById(R.id.stopBtn);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Conversely, stop the music player
                Intent intent = new Intent(v.getContext(), MusicService.class);

                stopService(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Listen for time zone change
        IntentFilter timeZoneFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);

        // Register the receiver
        registerReceiver(mTimeZoneChangeReceiver, timeZoneFilter);
    }

    @Override
    protected  void onPause(){
        super.onPause();

        // Pause receiver when activity is paused
        unregisterReceiver(mTimeZoneChangeReceiver);
    }
}
