package com.music.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.music.R;
import com.music.adapter.DownloadingAadapter;
import com.music.util.DownloadingUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/4.
 */

public class DownloadingFragment extends Fragment {
    @Bind(R.id.rv_fragment_downloading)
    RecyclerView rvFragmentDownloading;
    DownloadingAadapter downloadingAadapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloading, container, false);
        ButterKnife.bind(this, view);
        downloadingAadapter = new DownloadingAadapter(DownloadingUtil.getInstance().getList());
        Log.i("getLIST",""+DownloadingUtil.getInstance().getList().size());
        rvFragmentDownloading.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFragmentDownloading.setItemAnimator(new DefaultItemAnimator());
        rvFragmentDownloading.setAdapter(downloadingAadapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
