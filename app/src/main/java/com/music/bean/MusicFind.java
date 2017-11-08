package com.music.bean;

import java.io.Serializable;

/**
 * Created by 雅倩宝宝 on 2017/9/21.
 */

public class MusicFind implements Serializable {
    /**
     "albumid": "123298",
     "albumname": "Xposed",
     "albumpic_big": "http://imgcache.qq.com/music/photo/album_300/98/300_albumpic_123298_0.jpg",
     "albumpic_small": "http://imgcache.qq.com/music/photo/album/98/90_albumpic_123298_0.jpg",
     "downUrl": "http://stream8.qqmusic.qq.com/31530858.mp3",
     "m4a": "http://ws.stream.qqmusic.qq.com/1530858.m4a?fromtag=46",
     "singerid": "13948",
     "singername": "G.E.M. 邓紫棋",
     "songid": 1530858,
     "songname": "泡沫"
     **/
    private String albumpic_big;
    private String albumpic_small;
    private String downUrl;
    private String songname;
    private String singername;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;
    public String getAlbumpic_big() {
        return albumpic_big;
    }

    public void setAlbumpic_big(String albumpic_big) {
        this.albumpic_big = albumpic_big;
    }

    public String getAlbumpic_small() {
        return albumpic_small;
    }

    public void setAlbumpic_small(String albumpic_small) {
        this.albumpic_small = albumpic_small;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {

        if(null == obj) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }

        MusicFind musicFind = (MusicFind) obj;
        if(!musicFind.getUrl().equals(((MusicFind) obj).getUrl())) {
            return false;
        }
        return true;
    }
}
