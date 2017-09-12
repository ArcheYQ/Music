package com.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.music.R;
import com.music.activity.fragment.CoverFragment;
import com.music.activity.fragment.LrcFragment;
import com.music.adapter.FragmentAdapter;
import com.music.bean.Song;
import com.music.service.MusicService;
import com.music.widget.IndicatorLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MusicActivity extends AppCompatActivity implements View.OnScrollChangeListener, ViewPager.OnPageChangeListener{

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

    FragmentAdapter adapter ;
    Song song;
    private int currentSongPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LrcFragment());
        fragments.add(new CoverFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        vpMusciPlay.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
        vpMusciPlay.setOnScrollChangeListener(this);
        vpMusciPlay.setOnPageChangeListener(this);
    }

    public void initData(){
        song = (Song) getIntent().getSerializableExtra("songInfo");
        tvTitle.setText(song.getSong());
        tvSinger.setText(song.getSinger());
        ilIndicator.create(vpMusciPlay.getCurrentItem());
        currentSongPosition = song.getPosition();
        Intent startIntent = new Intent(this, MusicService.class);
        startIntent.putExtra("position", currentSongPosition);
        startService(startIntent);
    }
    @OnClick({R.id.iv_back, R.id.iv_mode, R.id.iv_prev, R.id.iv_play, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_mode:
                break;
            case R.id.iv_prev:
                break;
            case R.id.iv_play:
                break;
            case R.id.iv_next:
                break;
        }
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
}