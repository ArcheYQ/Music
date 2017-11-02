package com.music.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.music.activity.MusicActivity;
import com.music.bean.MusicFind;
import com.music.util.MusicFindUtil;
import com.music.util.MusicUtil;

/**
 * Created by 雅倩宝宝 on 2017/9/11.
 */

public class MusicService extends Service {
    private final String MUSIC_NOTIFICATION_ACTION_PLAY = "musicnotificaion.To.PLAY";
    private final String MUSIC_NOTIFICATION_ACTION_NEXT = "musicnotificaion.To.NEXT";
    private final String MUSIC_NOTIFICATION_ACTION_PRE = "musicnotificaion.To.Pre";
    private final String MUSIC_NOTIFICATION_ACTION_NPLAY = "musicnotificaion.To.NPLAY";
    private final String MUSIC_NOTIFICATION_ACTION_NNEXT = "musicnotificaion.To.NNEXT";
    private final String MUSIC_NOTIFICATION_ACTION_NPRE = "musicnotificaion.To.NPre";
    private MusicBroadCast musicBroadCast = null;
    private MusicNotification musicNotifi = null;
    /**
     * 播放音乐
     */
    public static final String COMPLETE = "4kf";
    public static final String NCOMPLETE = "4kf1";
    public static final String START = "4k1f";
    public static final String NSTART = "4k2f";
    /**
     * 暂停或者是播放音乐
     */
    public static final String PLAYORPAUSE = "2k5o";
    public static final String NPLAYORPAUSE = "3k5o";
    /**
     * 上一首音乐
     */
    public static final String PREVIOUSMUSIC = "4si3";
    public static final String NPREVIOUSMUSIC = "42i3";
    /**
     * 下一首音乐
     */
    public static final String NEXTMUSIC = "2hd3";
    public static final String NNEXTMUSIC = "1hd3";

    private PowerManager.WakeLock wakeLock = null; //电源锁

    private Intent intent1 = new Intent("com.example.communication.CHANGE");

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
        // 初始化通知栏
        musicNotifi = MusicNotification.getMusicNotification(getApplicationContext());
        musicNotifi.setContext(getBaseContext());
        musicNotifi
                .setManager((NotificationManager) getSystemService(NOTIFICATION_SERVICE));


        musicBroadCast = new MusicBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MUSIC_NOTIFICATION_ACTION_PLAY);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_NEXT);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_PRE);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_NPLAY);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_NNEXT);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_NPRE);
        registerReceiver(musicBroadCast, filter);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releseWakeLock();
        MusicUtil.getInstance().clean();
        MusicFindUtil.getInstance().clean();
        unregisterReceiver(musicBroadCast);
        musicNotifi.onCancelMusicNotifi();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();
        if (intent.getStringExtra("action")!=null){
            switch (intent.getStringExtra("action")) {
                case NSTART:
                    MusicFindUtil.getInstance().start();
                    musicNotifi.onUpdataNetMusicNotifi(MusicFindUtil.getInstance().getNewSongInfo(),MusicFindUtil.getInstance().isPlaying());
                    break;
                case NPLAYORPAUSE:
                    MusicFindUtil.getInstance().playOrPause();
                    musicNotifi.onUpdataNetMusicNotifi(MusicFindUtil.getInstance().getNewSongInfo(),MusicFindUtil.getInstance().isPlaying());
                    break;
                case NNEXTMUSIC:
                    MusicFindUtil.getInstance().start();
                    musicNotifi.onUpdataNetMusicNotifi(MusicFindUtil.getInstance().getNewSongInfo(),MusicFindUtil.getInstance().isPlaying());
                    break;
                case NPREVIOUSMUSIC:
                    MusicFindUtil.getInstance().start();
                    musicNotifi.onUpdataNetMusicNotifi(MusicFindUtil.getInstance().getNewSongInfo(),MusicFindUtil.getInstance().isPlaying());
                    break;
                case START:
                    MusicUtil.getInstance().prePlayOrNextPlay();
                    musicNotifi.onUpdataMusicNotifi(MusicUtil.getInstance().getNewSongInfo(),MusicUtil.getInstance().isPlaying());
                    break;
                case NCOMPLETE:
                    MusicUtil.getInstance().prePlayOrNextPlay();
                    musicNotifi.onUpdataNetMusicNotifi(MusicFindUtil.getInstance().getNewSongInfo(),MusicFindUtil.getInstance().isPlaying());
                    break;
                case COMPLETE:
                    MusicUtil.getInstance().prePlayOrNextPlay();
                    musicNotifi.onUpdataMusicNotifi(MusicUtil.getInstance().getNewSongInfo(),MusicUtil.getInstance().isPlaying());
                    break;
                case PLAYORPAUSE:
                    MusicUtil.getInstance().playOrPause();
                    musicNotifi.onUpdataMusicNotifi(MusicUtil.getInstance().getNewSongInfo(),MusicUtil.getInstance().isPlaying());
                    break;
                case PREVIOUSMUSIC:
                    MusicUtil.getInstance().prePlayOrNextPlay();
                    musicNotifi.onUpdataMusicNotifi(MusicUtil.getInstance().getNewSongInfo(),MusicUtil.getInstance().isPlaying());
                    break;
                case NEXTMUSIC:
                    MusicUtil.getInstance().prePlayOrNextPlay();
                    musicNotifi.onUpdataMusicNotifi(MusicUtil.getInstance().getNewSongInfo(),MusicUtil.getInstance().isPlaying());
                    break;
        }

        }


        return super.onStartCommand(intent, flags, startId);
    }
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void changNotifi(int i){
        if (i == 1){
            musicNotifi.onUpdataMusicNotifi(MusicUtil.getInstance().getNewSongInfo(),MusicUtil.getInstance().isPlaying());

        }else
        {
            musicNotifi.onUpdataNetMusicNotifi(MusicFindUtil.getInstance().getNewSongInfo(),MusicFindUtil.getInstance().isPlaying());
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

//        musicNotifi.onUpdataMusicNotifi(MusicUtil.getInstance().getNewSongInfo(),MusicUtil.getInstance().isPlaying());

             return new MusicBinder();
    }

//    public class MusicBind extends Binder{
//        public MusicService getService() {
//            return MusicService.this;
//        }
//    }
    public class MusicBroadCast extends BroadcastReceiver {
    private final String MUSIC_NOTIFICATION_ACTION_PLAY = "musicnotificaion.To.PLAY";
    private final String MUSIC_NOTIFICATION_ACTION_NEXT = "musicnotificaion.To.NEXT";
    private final String MUSIC_NOTIFICATION_ACTION_PRE = "musicnotificaion.To.PRE";
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case MUSIC_NOTIFICATION_ACTION_PLAY :
                Intent startIntent1 = new Intent(getApplicationContext(), MusicService.class);
                startIntent1.putExtra("action",MusicService.PLAYORPAUSE);
                startService(startIntent1);
                sendBroadcast(intent1);
                break;
            case MUSIC_NOTIFICATION_ACTION_NEXT:
                MusicUtil.getInstance().next();
                Intent startIntent3 = new Intent(getApplicationContext(), MusicService.class);
                startIntent3.putExtra("action",MusicService.NEXTMUSIC);
                startService(startIntent3);
                sendBroadcast(intent1);
                break;
            case MUSIC_NOTIFICATION_ACTION_PRE:
                MusicUtil.getInstance().pre();
                Intent startIntent2 = new Intent(getApplicationContext(), MusicService.class);
                startIntent2.putExtra("action",MusicService.PREVIOUSMUSIC);
                startService(startIntent2);
                sendBroadcast(intent1);
                break;
            case MUSIC_NOTIFICATION_ACTION_NPLAY :
                Intent startIntent4 = new Intent(getApplicationContext(), MusicService.class);
                startIntent4.putExtra("action",MusicService.NPLAYORPAUSE);
                startService(startIntent4);
                sendBroadcast(intent1);
                break;
            case MUSIC_NOTIFICATION_ACTION_NNEXT:
                MusicFindUtil.getInstance().next();
                Intent startIntent5 = new Intent(getApplicationContext(), MusicService.class);
                startIntent5.putExtra("action",MusicService.NNEXTMUSIC);
                startService(startIntent5);
                sendBroadcast(intent1);
                break;
            case MUSIC_NOTIFICATION_ACTION_NPRE:
                MusicFindUtil.getInstance().pre();
                Intent startIntent6 = new Intent(getApplicationContext(), MusicService.class);
                startIntent6.putExtra("action",MusicService.NPREVIOUSMUSIC);
                startService(startIntent6);
                sendBroadcast(intent1);
                break;
        }

    }
}
}
