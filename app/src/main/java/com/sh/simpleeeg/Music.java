package com.sh.simpleeeg;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;


public class Music extends Activity
 {


    // 程式使用的MediaPlayer物件
    private MediaPlayer mMediaPlayer = null;

    // 用來記錄是否MediaPlayer物件需要執行prepareAsync()
    public Boolean mbIsInitialised = true;


    public void play(Context context, int resource) {

        stop(context);
        mMediaPlayer = MediaPlayer.create(context, resource);
        mMediaPlayer.setLooping(true);//設定循環播放
        mMediaPlayer.start();
        if (Test.mbIsInitialised) {
            mMediaPlayer.stop();
        }

    }

    public void stop(Context context) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


 }
