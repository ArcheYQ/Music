package com.music.activity;

import android.app.usage.NetworkStats;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.music.R;
import com.music.adapter.MusicFenAdapter;
import com.music.bean.MusicFind;
import com.music.util.HttpUtil;
import com.music.util.MusicFindUtil;
import com.music.util.NetStateUtil;
import java.io.IOException;
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
    @Bind(R.id.rv_fenlei_list)
    RecyclerView rvFenleiList;
    MusicFenAdapter musicFenAdapter;
    MsgReceiver msgReceiver;
    private int netState = 0;
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
        netState = NetStateUtil.getNetWorkState(getBaseContext());
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
                musicFenAdapter = new MusicFenAdapter(MusicFindUtil.parseJOSNWithGSON(response, false), getBaseContext()) {
                    @Override
                    protected void getItemView(View itemView,final MusicFind musicFind) {

                            Log.i("netState", "onReceive: 1"+netState);
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SharedPreferences preferences = getSharedPreferences("setting",MODE_PRIVATE);
                                    if (netState == 2 || (netState == 1&&preferences.getBoolean("net",true))){
                                    Intent intent = new Intent(MusicListActivity.this, NetMusicActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("songNetInfo",musicFind);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                 }else
                                {
                                    Toast.makeText(mActivity, "当前网络状态不可以播放网络歌曲哦(；′⌒`)", Toast.LENGTH_SHORT).show();
                                }}
                            });

                        
                    }
                };
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
        if (musicFenAdapter!=null){
            musicFenAdapter.setPlay();
        }

        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.communication.CHANGE");
        intentFilter.addAction("com.example.communication.LISTCHANGE");
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(msgReceiver, intentFilter);
    }
    private void initTypes() {
        maps.put(36,getString(R.string.msuic_fenlei_rege));
        maps.put(6,getString(R.string.music_fenlei_gantai));
        maps.put(18,getString(R.string.music_fenlei_Kge));
        maps.put(4,getString(R.string.music_fenlei_liuxing) );
        maps.put(5,getString(R.string.music_fenlei_neidi));
        maps.put(28,getString(R.string.music_fenlei_net));
        maps.put(3,getString(R.string.music_fenlei_oumei));
        maps.put(27,getString(R.string.music_fenlei_xingge));
        maps.put(32,getString(R.string.music_fenlei_yinyueren));
    }
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                netState = NetStateUtil.getNetWorkState(context);}
                if (musicFenAdapter!=null){
                    musicFenAdapter.setPlay();
                }
            }
       
        }

    @Override
    protected void onResume() {
        if (musicFenAdapter!=null){
            musicFenAdapter.setPlay();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }
}
