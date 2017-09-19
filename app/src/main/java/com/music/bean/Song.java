package com.music.bean;

import java.io.Serializable;

/**
 * Created by 雅倩宝宝 on 2017/9/7.
 */

public class Song implements Serializable {


    /**
     * 歌手
     */
    private String singer;
    /**
     * 歌曲名称
     */
    private String song;
    /**
     * 地址
     */
    private String path;
    /**
     * 长度
     */
    private long duration;
    /**
     * 大小
     */
    private long size;
    /**
     * 顺序
     */
    private int position;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
