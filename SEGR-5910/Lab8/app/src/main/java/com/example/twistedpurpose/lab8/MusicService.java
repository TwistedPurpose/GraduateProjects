package com.example.twistedpurpose.lab8;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.twistedpurpose.lab8.R;

public class MusicService extends Service {
    public MusicService() {
    }

    private MediaPlayer mPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopMediaPlayer();
    }

    public void stopMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopMediaPlayer();
        mPlayer = MediaPlayer.create(this, R.raw.son_of_chaos);
        mPlayer.setOnCompletionListener(new
                                                MediaPlayer.OnCompletionListener() {
                                                    @Override
                                                    public void onCompletion(MediaPlayer mp) {
                                                        stopMediaPlayer();
                                                        stopSelf();
                                                    }
                                                }
        );
        mPlayer.start();
        return START_NOT_STICKY;
    }
}
