package com.music.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.music.R;
import com.music.activity.MusicListActivity;
import com.music.adapter.MusicGridAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private SparseArray<String> gridItems = new SparseArray<String>();
    private Map<String, Integer> maps = new HashMap<String, Integer>();
    MusicGridAdapter musicGridAdapter;

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
        return view;
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

    @OnClick(R.id.iv_findNet)
    public void onViewClicked() {
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
