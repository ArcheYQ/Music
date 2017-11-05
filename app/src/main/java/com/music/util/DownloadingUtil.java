package com.music.util;

import com.music.bean.MusicFind;
import com.music.bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/4.
 */

public class DownloadingUtil {
    private static DownloadingUtil downloadingUtil;
    private static List<MusicFind> musicFinds;//歌曲数据
    public synchronized static DownloadingUtil getInstance(){
        if (downloadingUtil == null) {
            downloadingUtil = new DownloadingUtil();
        }
        return downloadingUtil;
    }
    public void addList(MusicFind musicFind){
        if (musicFinds == null){
            musicFinds = new ArrayList<>();
        }
        musicFinds.add(musicFind);
    }
    public List<MusicFind> getList(){
        if (musicFinds == null){
            musicFinds = new ArrayList<>();
        }
        return musicFinds;
    }
}
