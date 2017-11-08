package com.music.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.music.R;
import com.music.activity.DownloadActivity;
import com.music.bean.MusicFind;
import com.music.download.DownloadListener;
import com.music.download.DownloadTask;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask downloadTask ;
    private MusicFind musicFind;
    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onProgress(int progress,String name) {
            getNoNotificationManager().notify(1,getNotification("正在下载"+name+"...",progress));
        }

        @Override
        public void onSuccess(String name) {
            downloadTask = null;
            stopForeground(true);
            getNoNotificationManager().notify(1,getNotification(name+"下载成功",-1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNoNotificationManager().notify(1,getNotification("Download Failed",-1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPause() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled(String name) {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Cancelde", Toast.LENGTH_SHORT).show();
            if (musicFind!=null){
                String fileName = musicFind.getSongname()+"-"+musicFind.getSingername();
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory+fileName);
                if (file.exists()){
                    file.delete();
                }
                getNoNotificationManager().cancel(1);
                stopForeground(true);
            }
        }
    };
    public DownloadService() {
    }
    private NotificationManager getNoNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNotification (String title,int progress){
        Intent intent = new Intent(this, DownloadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.miao);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.miao));
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        if (progress>= 0 ){
            builder.setContentText(progress +"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }
    private DownloadBinder mbinder = new DownloadBinder();
    public class DownloadBinder extends Binder{
        public void startDownload (MusicFind music){
            if (downloadTask == null){
                musicFind = music;
                downloadTask = new DownloadTask(downloadListener);
                downloadTask.execute(musicFind);
                startForeground(1,getNotification("Downloading...",0));
                Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }
        public void pauseDownload(){
            if (downloadTask !=null){
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){
            if (downloadTask!=null){
                downloadTask.cancelDownload();
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mbinder;
    }
}
