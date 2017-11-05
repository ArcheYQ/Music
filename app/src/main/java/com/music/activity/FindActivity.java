package com.music.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.music.R;
import com.music.adapter.FindMusicAdapter;
import com.music.util.HttpUtil;
import com.music.util.MusicFindUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindActivity extends AppCompatActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tb_main)
    Toolbar tbMain;
    @Bind(R.id.rv_net_song)
    RecyclerView rvNetSong;
    @Bind(R.id.sr_fragment_net)
    SmartRefreshLayout srFragmentNet;
    private int page = 1;
    private int totalPage;
    private String name;
    FindMusicAdapter findMusicAdapter;
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        page = 1;
        name = getIntent().getStringExtra("findName");
        MusicFindUtil.getInstance().deleteDate();
        showProgressDialog();
        HttpUtil.requestSongData(name, 1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onCreate:2 " + e);
                dissmiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG2", "onCreate:1 ");
                totalPage = MusicFindUtil.getInstance().getPage();
                Log.i("totalPage", "totalPage1 " + totalPage);
                findMusicAdapter = new FindMusicAdapter(MusicFindUtil.parseFindJOSNWithGSON(response), getBaseContext());
                Log.i("TAG2", "onCreate:2 ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TAG2", "onCreate:3 ");
                        rvNetSong.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        rvNetSong.setItemAnimator(new DefaultItemAnimator());
                        rvNetSong.setAdapter(findMusicAdapter);
                        initView();
                        srFragmentNet.setRefreshHeader(new ClassicsHeader(getBaseContext()));
                    }
                });
                dissmiss();
                Log.i("totalPage", "totalPage3 " + totalPage);
            }
        });

        ButterKnife.bind(this);
        initView();
//        findMusicAdapter.setPos(MusicFindUtil.getInstance().getCurrentSongPosition());
    }
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new Dialog(this, R.style.Pro);
            progressDialog.setContentView(R.layout.dialog_loading);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("(σ'・д・)σ");
            progressDialog.show();
        }
        progressDialog.show();
    }
    public void dissmiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void initView() {
        srFragmentNet.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {

                MusicFindUtil.getInstance().getPage();
                Log.i("totalPage2", "totalPage " + totalPage);
                page++;
                if (page<=totalPage){
                    HttpUtil.requestSongData(name, page, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            refreshlayout.finishLoadmore();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            refreshlayout.finishLoadmore();
                            findMusicAdapter.addData(MusicFindUtil.parseFindJOSNWithGSON(response));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findMusicAdapter.addDataChange();
                                }
                            });
                        }});
                }else {
                    Toast.makeText(getBaseContext(), "没有更多了~~", Toast.LENGTH_SHORT).show();
                    refreshlayout.finishLoadmore();
                }
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                HttpUtil.requestSongData(name, 1, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        refreshlayout.finishRefresh();
                        Log.i("TAG", "onCreate:2 " + e);
                        dissmiss();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        refreshlayout.finishRefresh();
                        findMusicAdapter = new FindMusicAdapter(MusicFindUtil.parseFindJOSNWithGSON(response), getBaseContext());
                        Log.i("TAG2", "onCreate:2 ");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("TAG2", "onCreate:3 ");
                                rvNetSong.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                                rvNetSong.setItemAnimator(new DefaultItemAnimator());
                                rvNetSong.setAdapter(findMusicAdapter);
                            }
                        });

                    }
                });
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
