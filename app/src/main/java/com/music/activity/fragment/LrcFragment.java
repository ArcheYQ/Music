package com.music.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.music.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wcy.lrcview.LrcView;

/**
 * Created by 雅倩宝宝 on 2017/9/11.
 */

public class LrcFragment extends Fragment {
    @Bind(R.id.sb_volume)
    SeekBar sbVolume;
    @Bind(R.id.lrc_view_full)
    LrcView lrcViewFull;
    private AudioManager mAudioManager;
    private ContentObserver mVoiceObserver;
    private MyVolumeReceiver mVolumeReceiver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_page_lrc, container, false);
        ButterKnife.bind(this, view);
        initVolume();
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                //系统音量和媒体音量同时更新
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, i, 0);
                audioManager.setStreamVolume(3, i, 0);// 3 代表 AudioManager.STREAM_MUSIC
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
            mVoiceObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                sbVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
            }
        };
        //屏蔽系统调节音量控件
        View.OnKeyListener keyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
                }else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                    am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
                }
                return true;
            }
        };
        return view;
    }

    private void initVolume() {
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);//获取媒体系统服务
        sbVolume.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));//设置最大音量
        sbVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));//当前的媒体音量
        myRegisterReceiver();//注册同步更新的广播
    }
    /**
     * 注册当音量发生变化时接收的广播
     */
    private void myRegisterReceiver(){
        mVolumeReceiver = new MyVolumeReceiver() ;
        IntentFilter filter = new IntentFilter() ;
        filter.addAction("android.media.VOLUME_CHANGED_ACTION") ;
        getContext().registerReceiver(mVolumeReceiver, filter) ;
    }

    /**
     * 处理音量变化时的界面显示
     * @author long
     */
    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果音量发生变化则更改seekbar的位置
            if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
                AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) ;// 当前的媒体音量
                sbVolume.setProgress(currVolume) ;
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(mVolumeReceiver);
        ButterKnife.unbind(this);
    }
}
