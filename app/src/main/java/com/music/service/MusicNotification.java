package com.music.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.music.R;
import com.music.bean.Song;

/**
 * Created by 雅倩宝宝 on 2017/9/24.
 */

public class MusicNotification extends Notification {
    private static MusicNotification notifyInstance = null;
    private Notification musicNotifi = null;
    private final int NOTIFICATION_ID =0;
    private Context context;
    private final int REQUEST_CODE = 30000;
    int flags= 10001;
    private NotificationManager manager = null;
    private Builder builder = null;
    private final String MUSIC_NOTIFICATION_ACTION_PLAY = "musicnotificaion.To.PLAY";
    private final String MUSIC_NOTIFICATION_ACTION_NEXT = "musicnotificaion.To.NEXT";
    private final String MUSIC_NOTIFICATION_ACTION_PRE = "musicnotificaion.To.Pre";
    private final int MUSIC_NOTIFICATION_VALUE_PLAY = 30001;
    private final int MUSIC_NOTIFICATION_VALUE_NEXT = 30002;
    private final int MUSIC_NOTIFICATION_VALUE_PRE =30003;
    private RemoteViews remoteViews;
    private Intent play=null,next=null,close = null;
    private PendingIntent musicPendIntent = null;
    public void setContext(Context context){
        this.context=context;
    }
    public void setManager(NotificationManager manager) {
        this.manager = manager;
    }
    private MusicNotification (Context context){
        this.context = context;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        builder = new Builder(context);
        play = new Intent();
        play.setAction(MUSIC_NOTIFICATION_ACTION_PLAY);
        next = new Intent();
        next.setAction(MUSIC_NOTIFICATION_ACTION_NEXT);
        close = new Intent();
        close.setAction(MUSIC_NOTIFICATION_ACTION_PRE);

    }
    public static MusicNotification getMusicNotification(Context context){
        if (notifyInstance == null) {
            notifyInstance = new MusicNotification(context);
        }
        return notifyInstance;
    }


    public void onCreateMusicNotifi() {
        // 设置点击事件

        // 1.注册控制点击事件
        play.putExtra("type",
                MUSIC_NOTIFICATION_VALUE_PLAY);
        PendingIntent pplay = PendingIntent.getBroadcast(context, REQUEST_CODE,
                play,0);
        remoteViews.setOnClickPendingIntent(R.id.iv_notigication__stopOrStart,
                pplay);

        // 2.注册下一首点击事件
        next.putExtra("type",
                MUSIC_NOTIFICATION_VALUE_NEXT);
        PendingIntent pnext = PendingIntent.getBroadcast(context, REQUEST_CODE,
                next, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_notigication__next,
                pnext);

        // 3.注册关闭点击事件
        close.putExtra("type",
                MUSIC_NOTIFICATION_VALUE_PRE);
        PendingIntent pclose = PendingIntent.getBroadcast(context, REQUEST_CODE,
                close, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_notigication__pre,
                pclose);

        builder.setContent(remoteViews).setWhen(System.currentTimeMillis())
                // 通知产生的时间，会在通知信息里显示
//				.setPriority(Notification.PRIORITY_DEFAULT)
                // 设置该通知优先级
                .setOngoing(true).setTicker("播放新的一首歌")
                .setSmallIcon(R.drawable.miao);

        // 兼容性实现
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            musicNotifi = builder.getNotification();
        } else {
            musicNotifi = builder.build();
        }
        musicNotifi.flags = Notification.FLAG_ONGOING_EVENT;
        manager.notify(0, musicNotifi);
    }

    public void onUpdataMusicNotifi(Song song, boolean isplay) {
        // 设置添加内容
        remoteViews.setTextViewText(R.id.tv_notigication_songName,
                (song.getSong()!=null?song.getSong():"什么东东") + "");
        remoteViews.setTextViewText(R.id.tv_notigication_singer,
                (song.getSinger()!=null?song.getSinger():"未知") + "");

        //判断是否播放
        if (isplay) {
            remoteViews.setImageViewResource(R.id.iv_notigication__stopOrStart,
                    R.drawable.star);
        } else {
            remoteViews.setImageViewResource(R.id.iv_notigication__stopOrStart,
                    R.drawable.stop);
        }
        onCreateMusicNotifi();
    }

    /**
     * 取消通知栏
     */
    public void onCancelMusicNotifi(){
        manager.cancel(0);
    }



}
