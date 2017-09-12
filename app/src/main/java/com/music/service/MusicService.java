package com.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.music.adapter.MusicAdapter;

/**
 * Created by 雅倩宝宝 on 2017/9/11.
 */

public class MusicService extends Service{
    private int currentSongPosition;
    MusicAdapter musicAdapter;
    private PowerManager.WakeLock wakeLock = null; //电源锁
    private void acquireWakeLock(){
        if(null == wakeLock){
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,this.getClass().getCanonicalName());
            if (null != wakeLock){
                wakeLock.acquire();
                if (wakeLock.isHeld()){

                }else {
                    Toast.makeText(this, "申请电源锁失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void releseWakeLock(){
        if ((null != wakeLock)){
            wakeLock.release();
            wakeLock=null;
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();
        intent.getIntExtra("position",currentSongPosition);
        musicAdapter.playMusic(currentSongPosition);
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
