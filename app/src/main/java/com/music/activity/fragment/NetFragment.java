package com.music.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.music.R;

import butterknife.ButterKnife;

/**
 * Created by 雅倩宝宝 on 2017/9/17.
 */

public class NetFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
