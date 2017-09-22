package com.music.bean;

/**
 * Created by 雅倩宝宝 on 2017/9/21.
 */

public class MusicFind {
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


}
