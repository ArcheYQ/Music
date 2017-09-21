package com.music.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.music.R;
import com.music.adapter.MusicFenAdapter;
import com.music.bean.MusicModle;
import com.music.util.HttpUtil;
import com.music.util.MusicModleUtil;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MusicListActivity extends BaseActivity {
    @Bind(R.id.tv_toolbar)
    TextView tvToolbar;
    @Bind(R.id.tb_main)
    Toolbar tbMain;
    private int typeid = 5;
    List<MusicModle> list;
    @Bind(R.id.rv_fenlei_list)
    RecyclerView rvFenleiList;
    MusicFenAdapter musicFenAdapter;
    private SparseArray<String> maps = new SparseArray<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        ButterKnife.bind(this);
        initTypes();
        typeid = getIntent().getIntExtra("musictype", 5);
        setToolBar(R.id.tb_main);
        tvToolbar.setText(maps.get(typeid));
        initWhiteHome();
        Log.i("TAG", "onCreate: " + typeid);
        showProgressDialog();
        HttpUtil.requestStringData(typeid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onCreate:2 " + e);
                dissmiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                musicFenAdapter = new MusicFenAdapter(MusicModleUtil.parseJOSNWithGSON(response));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvFenleiList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        rvFenleiList.setItemAnimator(new DefaultItemAnimator());
                        rvFenleiList.setAdapter(musicFenAdapter);
                        dissmiss();
                    }
                });

            }
        });
    }
    private void initTypes() {
        maps.put(26,getString(R.string.msuic_fenlei_rege));
        maps.put(6,getString(R.string.music_fenlei_gantai));
        maps.put(36,getString(R.string.music_fenlei_Kge));
        maps.put(4,getString(R.string.music_fenlei_liuxing) );
        maps.put(5,getString(R.string.music_fenlei_neidi));
        maps.put(28,getString(R.string.music_fenlei_net));
        maps.put(3,getString(R.string.music_fenlei_oumei));
        maps.put(27,getString(R.string.music_fenlei_xingge));
        maps.put(32,getString(R.string.music_fenlei_yinyueren));
    }
}
