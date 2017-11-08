package com.music.activity.fragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.music.R;
import com.music.activity.DownloadActivity;
import com.music.adapter.DownloadingAadapter;
import com.music.bean.MusicFind;
import com.music.service.DownloadService;
import com.music.util.DownloadingUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/4.
 */

public class DownloadingFragment extends Fragment {
    @Bind(R.id.rv_fragment_downloading)
    RecyclerView rvFragmentDownloading;
    DownloadingAadapter downloadingAadapter;
    @Bind(R.id.start)
    TextView start;
    @Bind(R.id.iv_stopOrStar)
    ImageView ivStopOrStar;
    @Bind(R.id.ll_start)
    LinearLayout llStart;
    @Bind(R.id.ll_cancel)
    LinearLayout llCancel;
    @Bind(R.id.ll_deleteList)
    LinearLayout llDeleteList;
    List<MusicFind> musicFinds;
//    private DownloadService.DownloadBinder downloadBinder;
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            downloadBinder = (DownloadService.DownloadBinder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloading, container, false);
        ButterKnife.bind(this, view);
        downloadingAadapter = new DownloadingAadapter(DownloadingUtil.getInstance().getList());
        musicFinds = DownloadingUtil.getInstance().getList();
        Log.i("getLIST", "" + DownloadingUtil.getInstance().getList().size());
        rvFragmentDownloading.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFragmentDownloading.setItemAnimator(new DefaultItemAnimator());
        rvFragmentDownloading.setAdapter(downloadingAadapter);
//        Intent intent = new Intent(getActivity(),DownloadService.class);
//        getActivity().startService(intent);
//        getActivity().getApplicationContext().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//        }
        return view;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getActivity(), "拒绝权限将无法使用权限", Toast.LENGTH_SHORT).show();
//                    getActivity().finish();
//                    break;
//                }
//            default:
//        }
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
//        getActivity().getApplicationContext().unbindService(serviceConnection);
    }

    @OnClick({R.id.ll_start, R.id.ll_cancel, R.id.ll_deleteList})
    public void onViewClicked(View view) {
//        if (downloadBinder == null){
//            return;
//        }
        switch (view.getId()) {
            case R.id.ll_start:
                if (musicFinds!= null){
//                    downloadBinder.startDownload(musicFinds.get(0));
                }
                break;
            case R.id.ll_cancel:
//                downloadBinder.cancelDownload();
                break;
            case R.id.ll_deleteList:
                downloadingAadapter.deleteList();
                break;
        }
    }
}
