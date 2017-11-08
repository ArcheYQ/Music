package com.music.activity.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.music.R;
import com.music.adapter.MusicAdapter;
import com.music.bean.Song;
import com.music.util.MusicUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 雅倩宝宝 on 2017/9/8.
 */

public class LocalFragment extends Fragment {
    @Bind(R.id.rv_fragment_local)
    RecyclerView rvFragmentLocal;
    static MusicAdapter musicAdapter;
    ArrayList<Song> song;
    @Bind(R.id.et_findlocal)
    EditText etFindlocal;
    MsgReceiver msgReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        ButterKnife.bind(this, view);
        Log.i("LocalFragment", "11111111111111");
        MPermissions.requestPermissions(LocalFragment.this, 4, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        musicAdapter = new MusicAdapter(MusicUtil.getInstance().getLocalMusci(getActivity()),getContext());
        rvFragmentLocal.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFragmentLocal.setItemAnimator(new DefaultItemAnimator());
        rvFragmentLocal.setAdapter(musicAdapter);
        etFindlocal.setHint("共"+MusicUtil.getInstance().getListCount()+"首，搜索本地歌曲");
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
                musicAdapter.setList(MusicUtil.getInstance().SearchSong(etFindlocal.getText().toString()));
            }
        });
        if (musicAdapter!=null){
            musicAdapter.setPlay();
        }
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.communication.CHANGE");
        intentFilter.addAction("com.example.communication.LISTCHANGE");
        getActivity().registerReceiver(msgReceiver, intentFilter);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @PermissionGrant(4)
    public void requestContactSuccess()
    {

    }

    @PermissionDenied(4)
    public void requestContactFailed()
    {
        Toast.makeText(getActivity(), "请给予权限~", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(msgReceiver);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    public static boolean deleteSong (Song song) {
        File file = new File(song.getPath());
        if (file.isFile() && file.exists()) {
            file.delete();
            MusicUtil.getInstance().changeList(song);
            musicAdapter.setList(MusicUtil.getInstance().getList());
            return true;
        }
          return false;
    }
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (musicAdapter!=null){
                musicAdapter.setPlay();
            }
        }
    }
    @Override
    public void onResume() {
        if (musicAdapter!=null){
            musicAdapter.setPlay();
        }
        super.onResume();
    }
}
