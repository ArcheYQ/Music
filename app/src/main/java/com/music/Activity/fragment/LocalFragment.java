package com.music.Activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.music.R;
import com.music.adapter.MusicAdapter;
import com.music.bean.Song;
import com.music.util.MusicUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 雅倩宝宝 on 2017/9/8.
 */

public class LocalFragment extends Fragment {
    @Bind(R.id.rv_fragment_local)
    RecyclerView rvFragmentLocal;
    MusicAdapter musicAdapter;
    ArrayList<Song> song;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        ButterKnife.bind(this, view);
        Log.i("LocalFragment","11111111111111");
        musicAdapter = new MusicAdapter(MusicUtil.getInstance().getLocalMusci(getActivity()));
        rvFragmentLocal.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFragmentLocal.setItemAnimator(new DefaultItemAnimator());
        rvFragmentLocal.setAdapter(musicAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
