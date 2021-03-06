package com.music.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.R;
import com.music.activity.fragment.DownloadedFragment;
import com.music.activity.fragment.DownloadingFragment;
import com.music.activity.fragment.LocalFragment;
import com.music.activity.fragment.NetFragment;
import com.music.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadActivity extends AppCompatActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tb_main)
    Toolbar tbMain;
    @Bind(R.id.tv_local)
    TextView tvLocal;
    @Bind(R.id.tv_network)
    TextView tvNetwork;
    @Bind(R.id.vv_main)
    LinearLayout vvMain;
    @Bind(R.id.vp_main)
    ViewPager vpMain;
    private FragmentAdapter adapter;
    private int currentIndex;
    private int screenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DownloadingFragment());
        fragments.add(new DownloadedFragment());
        screenWidth = getWindowManager().getDefaultDisplay().getWidth() - dip2px(this, 20);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvMain.getLayoutParams();
        lp.width = screenWidth / 2;
        vvMain.setLayoutParams(lp);
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vpMain.setAdapter(adapter);
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvMain.getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * screenWidth / 2);
                } else if (currentIndex == 1 && position == 0) {
                    lp.leftMargin = (int) ((1 + positionOffset) * screenWidth / 4);
                }
                vvMain.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    tvLocal.setTextColor(Color.parseColor("#da3318"));
                    tvNetwork.setTextColor(Color.BLACK);
                    currentIndex = 0;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvMain.getLayoutParams();
                    lp.leftMargin = 0;
                    vvMain.setLayoutParams(lp);
                } else if (i == 1) {
                    tvLocal.setTextColor(Color.BLACK);
                    tvNetwork.setTextColor(Color.parseColor("#da3318"));
                    currentIndex = 1;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvMain.getLayoutParams();
                    lp.leftMargin = screenWidth / 2;
                    vvMain.setLayoutParams(lp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        vpMain.setCurrentItem(0);
        tvLocal.setTextColor(Color.parseColor("#da3318"));
        tvNetwork.setTextColor(Color.BLACK);
        currentIndex = 0;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @OnClick({R.id.iv_back, R.id.tv_local, R.id.tv_network})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_local:
                vpMain.setCurrentItem(0);
                break;
            case R.id.tv_network:
                vpMain.setCurrentItem(1);
                break;
        }
    }
        }