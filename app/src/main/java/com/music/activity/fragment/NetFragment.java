package com.music.activity.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.music.R;
import com.music.activity.MusicListActivity;
import com.music.adapter.FindMusicAdapter;
import com.music.adapter.MusicGridAdapter;
import com.music.util.HttpUtil;
import com.music.util.MusicFindUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 雅倩宝宝 on 2017/9/17.
 */

public class NetFragment extends Fragment {
    @Bind(R.id.et_findlocal)
    EditText etFindlocal;
    @Bind(R.id.main_gridview)
    GridView mainGridview;
    @Bind(R.id.rv_net_song)
    RecyclerView rvNetSong;
    @Bind(R.id.iv_findNet)
    ImageView ivFindNet;
    @Bind(R.id.iv_translateAnimation)
    ImageView ivTranslateAnimation;
    @Bind(R.id.vv_xian)
    View vvXian;
    @Bind(R.id.rl_scroll)
    RelativeLayout rlScroll;
    @Bind(R.id.sr_fragment_net)
    SmartRefreshLayout srFragmentNet;
    private SparseArray<String> gridItems = new SparseArray<String>();
    private Map<String, Integer> maps = new HashMap<String, Integer>();
    MusicGridAdapter musicGridAdapter;
    FindMusicAdapter findMusicAdapter;
    private Dialog progressDialog;
    private boolean isHide;
    private int page = 1;
    private int totalPage;
    private String name;
//    Animation showAction, hideAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        initData();
        musicGridAdapter = new MusicGridAdapter(gridItems, getContext());
        mainGridview = view.findViewById(R.id.main_gridview);
        mainGridview.setAdapter(musicGridAdapter);
        mainGridview.setOnItemClickListener(new mainGridviewListener());
        ButterKnife.bind(this, view);
        initView();
        srFragmentNet.setRefreshHeader(new ClassicsHeader(getContext()));
//        hideAction = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -240.0f);
//        hideAction.setDuration(2000);
//        hideAction.setFillAfter(true);
//        showAction = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, +240.0f);
//         showAction.setDuration(2000);
//         showAction.setFillAfter(true);
        return view;
    }


    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new Dialog(getContext(), R.style.Pro);
            progressDialog.setContentView(R.layout.dialog_loading);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("(σ'・д・)σ");
            progressDialog.show();
        }
        progressDialog.show();
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
                            findMusicAdapter.addData(MusicFindUtil.parseJOSNWithGSON(response));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   findMusicAdapter.addDataChange();
                                 }
                          });
                     }});
                }else {
                    Toast.makeText(getContext(), "没有更多了~~", Toast.LENGTH_SHORT).show();
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
                        findMusicAdapter = new FindMusicAdapter(MusicFindUtil.parseJOSNWithGSON(response), getContext());
                        Log.i("TAG2", "onCreate:2 ");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("TAG2", "onCreate:3 ");
                                rvNetSong.setLayoutManager(new LinearLayoutManager(getContext()));
                                rvNetSong.setItemAnimator(new DefaultItemAnimator());
                                rvNetSong.setAdapter(findMusicAdapter);
                            }
                        });

                    }
                });
            }
        });
    }

    public void dissmiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void initData() {
        gridItems.put(0, getString(R.string.msuic_fenlei_rege));
        gridItems.put(1, getString(R.string.music_fenlei_gantai));
        gridItems.put(2, getString(R.string.music_fenlei_Kge));
        gridItems.put(3, getString(R.string.music_fenlei_liuxing));
        gridItems.put(4, getString(R.string.music_fenlei_neidi));
        gridItems.put(5, getString(R.string.music_fenlei_net));
        gridItems.put(6, getString(R.string.music_fenlei_oumei));
        gridItems.put(7, getString(R.string.music_fenlei_xingge));
        gridItems.put(8, getString(R.string.music_fenlei_yinyueren));
        getFenlei();
    }

    @OnClick({R.id.iv_findNet, R.id.iv_translateAnimation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_findNet:
                page = 1;
                name = etFindlocal.getText().toString();
                etFindlocal.setText("");
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
                        findMusicAdapter = new FindMusicAdapter(MusicFindUtil.parseJOSNWithGSON(response), getContext());
                        Log.i("TAG2", "onCreate:2 ");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("TAG2", "onCreate:3 ");
                                rvNetSong.setLayoutManager(new LinearLayoutManager(getContext()));
                                rvNetSong.setItemAnimator(new DefaultItemAnimator());
                                rvNetSong.setAdapter(findMusicAdapter);
                            }
                        });
                        dissmiss();
                        Log.i("totalPage", "totalPage3 " + totalPage);
                    }
                });
                break;
            case R.id.iv_translateAnimation:
                if (isHide) {
//                    mainGridview.startAnimation(showAction);
                    mainGridview.setVisibility(view.VISIBLE);
                    vvXian.setVisibility(view.VISIBLE);
                    ivTranslateAnimation.setImageResource(R.drawable.less);
                    isHide = false;
                } else {
//                    mainGridview.startAnimation(hideAction);
                    mainGridview.setVisibility(view.GONE);
                    vvXian.setVisibility(view.GONE);
                    ivTranslateAnimation.setImageResource(R.drawable.more_unfold);
                    isHide = true;
                }
                break;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * GirdView点击事件
     */
    private class mainGridviewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent intent = new Intent(getContext(), MusicListActivity.class);
            intent.putExtra("musictype", maps.get(gridItems.get(position)));
            startActivity(intent);
        }
    }

    public void getFenlei() {
        maps.put(getString(R.string.msuic_fenlei_rege), 26);
        maps.put(getString(R.string.music_fenlei_gantai), 6);
        maps.put(getString(R.string.music_fenlei_Kge), 36);
        maps.put(getString(R.string.music_fenlei_liuxing), 4);
        maps.put(getString(R.string.music_fenlei_neidi), 5);
        maps.put(getString(R.string.music_fenlei_net), 28);
        maps.put(getString(R.string.music_fenlei_oumei), 3);
        maps.put(getString(R.string.music_fenlei_xingge), 27);
        maps.put(getString(R.string.music_fenlei_yinyueren), 32);
        // 热歌 26
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
