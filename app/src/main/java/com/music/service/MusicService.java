package com.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.music.util.MusicUtil;

/**
 * Created by 雅倩宝宝 on 2017/9/11.
 */

public class MusicService extends Service {


    /**
     * 播放音乐
     */
    public static final String COMPLETE = "4kf";

    /**
     * 暂停或者是播放音乐
     */
    public static final String PLAYORPAUSE = "2k5o";

    /**
     * 上一首音乐
     */
    public static final String PREVIOUSMUSIC = "4si3";

    /**
     * 下一首音乐
     */
    public static final String NEXTMUSIC = "2hd3";
    private PowerManager.WakeLock wakeLock = null; //电源锁


    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            if (null != wakeLock) {
                wakeLock.acquire();
                if (wakeLock.isHeld()) {

                } else {
                    Toast.makeText(this, "申请电源锁失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void releseWakeLock() {
        if ((null != wakeLock)) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releseWakeLock();
        MusicUtil.getInstance().clean();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();
        switch (intent.getStringExtra("action")) {
            case COMPLETE:
                MusicUtil.getInstance().prePlayOrNextPlay();
                break;
            case PLAYORPAUSE:
                MusicUtil.getInstance().playOrPause();
                break;
            case PREVIOUSMUSIC:
                MusicUtil.getInstance().prePlayOrNextPlay();
                break;
            case NEXTMUSIC:
                MusicUtil.getInstance().prePlayOrNextPlay();

                break;
        }


        return super.onStartCommand(intent, flags, startId);
    }
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
             return (IBinder) intent;
//        return new MusicBind();
    }
//    public class MusicBind extends Binder{
//        public MusicService getService() {
//            return MusicService.this;
//        }
//    }
}
