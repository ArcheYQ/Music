package com.music.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
    @Bind(R.id.et_findlocal)
    EditText etFindlocal;
    private boolean isPause; //记录当前播放器的状态
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        ButterKnife.bind(this, view);
        Log.i("LocalFragment", "11111111111111");
        musicAdapter = new MusicAdapter(MusicUtil.getInstance().getLocalMusci(getActivity()),true,getContext());
        rvFragmentLocal.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFragmentLocal.setItemAnimator(new DefaultItemAnimator());
        rvFragmentLocal.setAdapter(musicAdapter);
        etFindlocal.setHint("共"+musicAdapter.getListCount()+"首，搜索本地歌曲");
        etFindlocal.setCursorVisible(false);
        etFindlocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etFindlocal.setCursorVisible(true);
            }
        });
        etFindlocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                musicAdapter.SearchSong(etFindlocal.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
