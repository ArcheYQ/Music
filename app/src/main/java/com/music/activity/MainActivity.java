package com.music.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.activity.fragment.LocalFragment;
import com.music.R;
import com.music.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

public class MainActivity extends com.music.activity.BaseActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#da3318"));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LocalFragment());
        fragments.add(new LocalFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        vpMain.setAdapter(adapter);

    }

    @OnClick({R.id.tv_local, R.id.tv_network})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_local:
                break;
            case R.id.tv_network:
                break;
        }
    }
}
