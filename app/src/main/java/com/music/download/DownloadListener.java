package com.music.download;

/**
 * Created by Administrator on 2017/11/6.
 */

public interface DownloadListener {
    void onProgress(int progress,String name);
    void onSuccess(String name);
    void onFailed();
    void onPause();
    void onCanceled(String name);
}
