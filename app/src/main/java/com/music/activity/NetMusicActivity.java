package com.music.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.music.R;
import com.music.activity.fragment.LocalFragment;
import com.music.activity.fragment.LrcFragment;
import com.music.adapter.FragmentAdapter;
import com.music.bean.MusicFind;
import com.music.bean.Song;
import com.music.data.LrcData;
import com.music.data.LrcDataUtil;
import com.music.lrc.DefaultLrcBulider;
import com.music.lrc.ILrcBulider;
import com.music.lrc.ILrcViewListener;
import com.music.lrc.LrcJsonUtil;
import com.music.lrc.LrcRow;
import com.music.lrc.LrcUtil;
import com.music.lrc.LrcView;
import com.music.service.MusicService;
import com.music.util.HttpUtil;
import com.music.util.MusicFindUtil;
import com.music.util.MusicUtil;
import com.music.widget.IndicatorLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import qiu.niorgai.StatusBarCompat;

import static com.music.util.MusicUtil.TYPE_SINGLE;

public class NetMusicActivity extends AppCompatActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_singer)
    TextView tvSinger;
    @Bind(R.id.iv_more)
    ImageView ivMore;
    @Bind(R.id.vp_musci_play)
    ViewPager vpMusciPlay;
    @Bind(R.id.lrc_mLrcView)
    LrcView mLrcView;
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
    @Bind(R.id.il_indicator)
    IndicatorLayout ilIndicator;
    FragmentAdapter adapter;
    MusicFind musicFind;
    private static boolean isplay = true;
    private MusicService musicService;
    private int mode = MusicUtil.TYPE_ORDER;
    private MsgReceiver msgReceiver;
    private Timer timer;
    private TimerTask timerTask;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private LrcDataUtil lrcDataUtil;
    String lrc;
    private LrcData lrcdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#5d5d5d"));
        setContentView(R.layout.activity_net_music);
        ButterKnife.bind(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LrcFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vpMusciPlay.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
        initLRC(musicFind);
        MusicFindUtil.getInstance().getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            //因为没有附着在活动中所以就算活动finish掉，还是可以有监听~喵~
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                MusicFindUtil.getInstance().comNext();
                Intent startIntent4 = new Intent(NetMusicActivity.this, MusicService.class);
                startIntent4.putExtra("action", MusicService.NCOMPLETE);
                startService(startIntent4);
                changInfo();
            }
        });
        MusicFindUtil.getInstance().getMediaPlayer().setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("d","TAGonBufferingUpdate: "+percent);
                sbProgress.setSecondaryProgress(percent);
            }
        });
        mLrcView.setListener(new ILrcViewListener() {
            @Override
            public void onLrcSeeked(int newPosition, LrcRow row) {
                if (MusicFindUtil.getInstance().getMediaPlayer() != null) {
                    MusicFindUtil.getInstance().getMediaPlayer().seekTo((int) row.time);
                }
            }
        });
    }
    public void initLRC(final MusicFind musicFind) {
        lrc = lrcDataUtil.findLrc(musicFind.getSongname()+"-"+musicFind.getSingername());
            if (lrc.equals("")) {
                HttpUtil.requstNetLrcData(musicFind.getId(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        lrc = "";
                        Log.i("fali", "fali" + lrc);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("fali", "fali" + lrc + "2");
                                ILrcBulider bulider = new DefaultLrcBulider();
                                List<LrcRow> rows = bulider.getLrcRows(lrc);
                                Log.i("fali", "fali" + rows + "3");
                                mLrcView.setLrc(rows);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        lrc = LrcUtil.getLrcFromAssets(LrcJsonUtil.parseNetJOSNWithGSON(response),2);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lrcDataUtil.addLrc(lrc, musicFind.getSongname()+"-"+musicFind.getSingername());
                                ILrcBulider bulider = new DefaultLrcBulider();
                                List<LrcRow> rows = bulider.getLrcRows(lrc);
                                mLrcView.setLrc(rows);
                            }
                        });
                    }
                });
            } else {
                ILrcBulider bulider = new DefaultLrcBulider();
                List<LrcRow> rows = bulider.getLrcRows(lrc);
                mLrcView.setLrc(rows);
            }
        }
    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_net, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_download:
                        break;
                }
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {

            }
        });
        popupMenu.show();
    }

    public void initData(){
        musicFind = (MusicFind) getIntent().getSerializableExtra("songNetInfo");
        lrcDataUtil = new LrcDataUtil(this);
        if (musicFind.getPosition() != MusicFindUtil.getInstance().getCurrentSongPosition()) {
            MusicFindUtil.getInstance().setCurrentSongPosition(musicFind.getPosition());
            Intent startIntent1 = new Intent(this, MusicService.class);
            startIntent1.putExtra("action", MusicService.NSTART);
            startService(startIntent1);
            isplay = true;
        } else {
            isplay = MusicFindUtil.getInstance().isPlaying();
        }
        MusicFindUtil.getInstance().setCurrentSongPosition(musicFind.getPosition());
        if (isplay == true) {
            ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
        } else {
            ivPlay.setImageResource(R.drawable.play_btn_play_selector);
        }
        if (mode != MusicFindUtil.getInstance().getPattern()) {
            mode = MusicFindUtil.getInstance().getPattern();
        }

        tvTitle.setText(musicFind.getSongname());
        tvSinger.setText(musicFind.getSingername());

        ilIndicator.create(vpMusciPlay.getCurrentItem());
        Intent startIntent = new Intent(this, MusicService.class);
        bindService(startIntent, connection, BIND_AUTO_CREATE);
        if (mode == MusicUtil.TYPE_ORDER) {
            ivMode.setImageResource(R.drawable.play_btn_loop_selector);
        } else if (mode == TYPE_SINGLE) {
            ivMode.setImageResource(R.drawable.play_btn_one_selector);
        } else {
            ivMode.setImageResource(R.drawable.play_btn_shuffle_selector);
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isSeekBarChanging == true) {
                    return;
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sbProgress.setMax(MusicFindUtil.getInstance().getMediaPlayer().getDuration());
                        tvTotalTime.setText(formatTime("mm:ss", MusicFindUtil.getInstance().getMediaPlayer().getDuration()));
                        final long timePassed = MusicFindUtil.getInstance().getCurrentPosition();
                        sbProgress.setProgress((int) timePassed);
                        mLrcView.seekLrcToTime(timePassed);
                        tvCurrentTime.setText(formatTime("mm:ss", MusicFindUtil.getInstance().getCurrentPosition()));
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
        sbProgress.setOnSeekBarChangeListener(new MySeekbar());
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.communication.CHANGE");
        registerReceiver(msgReceiver, intentFilter);
        lrcdata = new LrcData(this);
    }
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            changInfo();
            isplay = MusicFindUtil.getInstance().isPlaying();
            if (isplay == true) {
                ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
            } else {
                ivPlay.setImageResource(R.drawable.play_btn_play_selector);
            }
        }
    }
    @OnClick({R.id.iv_back, R.id.iv_more, R.id.iv_mode, R.id.iv_prev, R.id.iv_play, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_more:
                showPopupMenu(ivMore);
                break;
            case R.id.iv_mode:
                if (mode == MusicFindUtil.TYPE_ORDER) {
                    Toast.makeText(this, "咦，已经切换为随机模式n(*≧▽≦*)n", Toast.LENGTH_SHORT).show();
                    mode = MusicFindUtil.TYPE_RANDOM;
                    ivMode.setImageResource(R.drawable.play_btn_shuffle_selector);
                    MusicFindUtil.getInstance().setPatten(MusicFindUtil.TYPE_RANDOM);
                } else if (mode == MusicFindUtil.TYPE_RANDOM) {
                    Log.i("pat2", "" + mode);
                    Toast.makeText(this, "已经准备好单曲循环O(∩_∩)O", Toast.LENGTH_SHORT).show();
                    this.mode = MusicFindUtil.TYPE_SINGLE;
                    ivMode.setImageResource(R.drawable.play_btn_one_selector);
                    MusicFindUtil.getInstance().setPatten(MusicFindUtil.TYPE_SINGLE);
                    Log.i("pat4", "" + mode);

                } else if (mode == MusicFindUtil.TYPE_SINGLE) {
                    Log.i("pat5", "" + mode);
                    Toast.makeText(this, "那就按顺序播放吧n(*≧▽≦*)n", Toast.LENGTH_SHORT).show();
                    this.mode = MusicFindUtil.TYPE_ORDER;
                    ivMode.setImageResource(R.drawable.play_btn_loop_selector);
                    MusicFindUtil.getInstance().setPatten(MusicFindUtil.TYPE_ORDER);
                    Log.i("pat6", "" + mode);
                }
                break;
            case R.id.iv_prev:
                if (!isplay) {
                    isplay = true;
                    ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
                }
                MusicFindUtil.getInstance().pre();
                changInfo();
                Intent startIntent2 = new Intent(this, MusicService.class);
                startIntent2.putExtra("action", MusicService.NPREVIOUSMUSIC);
                startService(startIntent2);
                musicService.changNotifi(2);
                break;
            case R.id.iv_play:
                if (isplay) {
                    ivPlay.setImageResource(R.drawable.play_btn_play_selector);
                    isplay = false;
                } else {
                    isplay = true;
                    ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
                }
                Intent startIntent1 = new Intent(this, MusicService.class);
                startIntent1.putExtra("action", MusicService.NPLAYORPAUSE);
                startService(startIntent1);
                break;
            case R.id.iv_next:
                MusicFindUtil.getInstance().next();
                changInfo();
                if (!isplay) {
                    isplay = true;
                    ivPlay.setImageResource(R.drawable.play_btn_pause_selector);
                }
                Intent startIntent3 = new Intent(this, MusicService.class);
                startIntent3.putExtra("action", MusicService.NNEXTMUSIC);
                startService(startIntent3);
                break;
        }
    }
    class MySeekbar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            MusicUtil.getInstance().getMediaPlayer().seekTo(seekBar.getProgress());
            isSeekBarChanging = false;
        }

    }
    public static String formatTime(String pattern, long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service1) {
            musicService = ((MusicService.MusicBinder) service1).getService();
            musicService.changNotifi(2);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    public void changInfo() {
        MusicFind newSong = MusicFindUtil.getInstance().getNewSongInfo();
        musicFind =newSong;
        if (newSong == null)
            return;
        sbProgress.setMax((int) MusicFindUtil.getInstance().getMediaPlayer().getDuration());
        tvTotalTime.setText(formatTime("mm:ss", MusicFindUtil.getInstance().getMediaPlayer().getDuration()));
        tvTitle.setText(newSong.getSongname());
        tvSinger.setText(newSong.getSingername());
        List<LrcRow> rowList = new ArrayList<>();
        mLrcView.setLrc(rowList);
        initLRC(newSong);
    }
}
