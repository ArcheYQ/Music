package com.music.bean;

import java.io.Serializable;

/**
 * Created by 雅倩宝宝 on 2017/9/19.
 */

public class MusicModle implements Serializable {

    /**
     * "albumid": 1181789,
     * "downUrl": "http://tsmusic24.tc.qq.com/104778928.mp3",
     * "seconds": 265,
     * "singerid": 12744,
     * "singername": "小沈阳",
     * "songid": 104778928,
     * "songname": "八戒八戒 (《西游记之孙悟空三打白骨精》电影推广曲)",
     * "url": "http://ws.stream.qqmusic.qq.com/104778928.m4a?fromtag=46"
     */

    private int albumid;
    private String downUrl;
    private int seconds;
    private int singerid;
    private String singername;
    private int songid;
    private String songname;
    private String url;

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSingerid() {
        return singerid;
    }

    public void setSingerid(int singerid) {
        this.singerid = singerid;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public int getSongid() {
        return songid;
    }

    public void setSongid(int songid) {
        this.songid = songid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}