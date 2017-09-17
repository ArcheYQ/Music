package com.music.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.music.R;
import com.music.activity.fragment.LrcFragment;
import com.music.adapter.FragmentAdapter;
import com.music.bean.Song;
import com.music.service.MusicService;
import com.music.util.MusicUtil;
import com.music.widget.IndicatorLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

import static com.music.util.MusicUtil.TYPE_SINGLE;

public class MusicActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener ,ViewTreeObserver.OnScrollChangedListener{

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_singer)
    TextView tvSinger;
    @Bind(R.id.vp_musci_play)
    ViewPager vpMusciPlay;
    @Bind(R.id.il_indicator)
    IndicatorLayout ilIndicator;
    @Bind(R.id.tv_current_time)
    TextView tvCurrentTime;
    @Bind(R.id.sb_progress)
    SeekBar sbProgress;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.iv_mode)
    ImageView ivMode;
    @Bind(R.id.iv_prev)
    ImageView ivPrev;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.iv_next)
    ImageView ivNext;
//    private MusicService musicService;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    FragmentAdapter adapter ;
    Song song;
    private static boolean isplay = true;
    private int mode = MusicUtil.TYPE_ORDER ;
    private Timer timer ;
    private TimerTask timerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#5d5d5d"));
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LrcFragment());
//        fragments.add(new CoverFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vpMusciPlay.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
        vpMusciPlay.setOnPageChangeListener(this);
        MusicUtil.getInstance().getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                MusicUtil.getInstance().comNext();
                changInfo();
                Intent startIntent4 = new Intent(MusicActivity.this, MusicService.class);
                startIntent4.putExtra("action", MusicService.COMPLETE);
                startService(startIntent4);
            }
        });

    }

    public void initData() {

        song = (Song) getIntent().getSerializableExtra("songInfo");
        if (song.getPosition() != MusicUtil.getInstance().getCurrentSongPosition()) {
            MusicUtil.getInstance().playMusic(song);
            isplay = true;
        }
        if (isplay == true) {
            ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
        } else {
            ivPlay.setImageResource(R.drawable.play_btn_play_selector);
        }
        if (mode != MusicUtil.getInstance().getPattern()) {
            mode = MusicUtil.getInstance().getPattern();
        }
        sbProgress.setMax(MusicUtil.getInstance().getDuration());
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(isSeekBarChanging==true) {
                    return;
                }



                sbProgress.setProgress(MusicUtil.getInstance().getCurrentPosition());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCurrentTime.setText(formatTime("mm:ss",MusicUtil.getInstance().getCurrentPosition()));
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
        tvTitle.setText(song.getSong());
        tvSinger.setText(song.getSinger());
        tvTotalTime.setText(formatTime("mm:ss",song.getDuration()));
        ilIndicator.create(vpMusciPlay.getCurrentItem());
        Intent startIntent = new Intent(this, MusicService.class);
//        bindService(startIntent, connection, BIND_AUTO_CREATE);
        if (mode == MusicUtil.TYPE_ORDER) {
            ivMode.setImageResource(R.drawable.play_btn_loop_selector);
        } else if (mode == TYPE_SINGLE) {
            ivMode.setImageResource(R.drawable.play_btn_one_selector);
        } else {
            ivMode.setImageResource(R.drawable.play_btn_shuffle_selector);
        }
        sbProgress.setOnSeekBarChangeListener( new MySeekbar());
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ilIndicator.setCurrent(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onScrollChanged() {

    }


    @OnClick({R.id.iv_back, R.id.iv_mode, R.id.iv_prev, R.id.iv_play, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_mode:
                if (mode == MusicUtil.TYPE_ORDER){
                    Toast.makeText(this, "咦，已经切换为随机模式n(*≧▽≦*)n", Toast.LENGTH_SHORT).show();
                    mode = MusicUtil.TYPE_RANDOM;
                    ivMode.setImageResource(R.drawable.play_btn_shuffle_selector);
                    MusicUtil.getInstance().setPatten(MusicUtil.TYPE_RANDOM);
                }else if(mode == MusicUtil.TYPE_RANDOM){
                    Log.i("pat2",""+mode);
                    Toast.makeText(this, "已经准备好单曲循环O(∩_∩)O", Toast.LENGTH_SHORT).show();
                    this.mode = TYPE_SINGLE;
                    ivMode.setImageResource(R.drawable.play_btn_one_selector);
                    MusicUtil.getInstance().setPatten(TYPE_SINGLE);
                    Log.i("pat4",""+mode);
                }else if (mode == TYPE_SINGLE){
                    Log.i("pat5",""+mode);
                    Toast.makeText(this, "那就按顺序播放吧n(*≧▽≦*)n", Toast.LENGTH_SHORT).show();
                    this.mode = MusicUtil.TYPE_ORDER;
                    ivMode.setImageResource(R.drawable.play_btn_loop_selector);
                    MusicUtil.getInstance().setPatten(MusicUtil.TYPE_ORDER);
                    Log.i("pat6",""+mode);
                }
                break;
            case R.id.iv_prev:
                if (!isplay){
                    isplay=true;
                    ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
                }
                MusicUtil.getInstance().pre();
                changInfo();
                Intent startIntent2 = new Intent(this, MusicService.class);
                startIntent2.putExtra("action",MusicService.PREVIOUSMUSIC);
                startService(startIntent2);

                break;
            case R.id.iv_play:
                Intent startIntent1 = new Intent(this, MusicService.class);
                startIntent1.putExtra("action",MusicService.PLAYORPAUSE);
                startService(startIntent1);
                if (isplay){

                    ivPlay.setImageResource(R.drawable.play_btn_play_selector);
                    isplay =false;
                }else
                {
                    isplay =true;
                    ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
                }
                break;
            case R.id.iv_next:
                MusicUtil.getInstance().next();
                changInfo();
                if (!isplay){
                    isplay=true;
                    ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
                }
                Intent startIntent3 = new Intent(this, MusicService.class);
                startIntent3.putExtra("action",MusicService.NEXTMUSIC);
                startService(startIntent3);
                break;
        }
    }
     public void changInfo(){
         Song newSong = MusicUtil.getInstance().getNewSongInfo();
         if(newSong ==null)
             return;
         Log.i("song2",""+newSong.getSinger());
         tvTotalTime.setText(formatTime("mm:ss",newSong.getDuration()));
         tvTitle.setText(newSong.getSong());
         tvSinger.setText(newSong.getSinger());
         timerTask = new TimerTask() {
             @Override
             public void run() {
                 if(isSeekBarChanging==true) {
                     return;
                 }

                 sbProgress.setProgress(MusicUtil.getInstance().getCurrentPosition());
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         tvCurrentTime.setText(formatTime("mm:ss",MusicUtil.getInstance().getCurrentPosition()));
                     }
                 });
             }

         };
         timer = new Timer();

         timer.schedule(timerTask, 0, 500);
     }
//     private ServiceConnection connection = new ServiceConnection() {
//         @Override
//         public void onServiceConnected(ComponentName componentName, IBinder service1) {
//         musicService = ((MusicService.MusicBind) service1).getService();
//         }
//
//         @Override
//         public void onServiceDisconnected(ComponentName componentName) {
//
//         }
//     };
    class MySeekbar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging=true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            MusicUtil.getInstance().setSeekTo(seekBar.getProgress());
            isSeekBarChanging=false;
        }

    }
    public static String formatTime(String pattern, long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }
}