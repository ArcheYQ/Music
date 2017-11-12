package com.music;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.music.activity.MainActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by 雅倩宝宝 on 2017/9/13.
 */

public class App extends Application {
    protected static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
    }

    public Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            restartApp();
        }
        };

    public void restartApp(){
        Intent intent = new Intent(instance, MainActivity.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                instance.getApplicationContext(), 0, intent,Intent.FLAG_ACTIVITY_NEW_TASK);
        //退出程序
        AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                restartIntent); // 1秒钟后重启应用
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
