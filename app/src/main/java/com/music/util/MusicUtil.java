package com.music.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.music.bean.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import static android.content.ContentValues.TAG;

/**
 * Created by 雅倩宝宝 on 2017/9/7.
 */

public class MusicUtil{
    private  int preSongPosition = -1;
    private  int currentSongPosition = -1;
    private boolean isPrepare = false;
    private static MediaPlayer mediaPlayer;
    private static MusicUtil musicUtils;
    private String pre;
    private List<Song> list;//本地歌曲数据
    /**
     * 顺序播放
     */
    public static final int TYPE_ORDER = 4212;
    /**
     * 播放模式
     */

    private int pattern = TYPE_ORDER;
    /**
     * 随机播放
     */
    public static final int TYPE_RANDOM = 4313;
    /**
     * 单曲循环
     */
    private Timer timer;
    public static final int TYPE_SINGLE = 4414;
    public int getPattern(){
        return pattern;
    }
    public void setPatten(int i) {
        if (i == TYPE_SINGLE) {
            this.pattern = TYPE_SINGLE;
        }
        if (i == TYPE_ORDER) {
            this.pattern = TYPE_ORDER;
            Log.i("pat8",""+pattern);
        }
        if (i == TYPE_RANDOM) {
            this.pattern = TYPE_RANDOM;
        }
    }
   public int getCurrentSongPosition(){
        return currentSongPosition;
    }
    //获得一个MusicUtil实例
    public synchronized static MusicUtil getInstance(){
        if (musicUtils == null) {
            musicUtils = new MusicUtil();
        }
        return musicUtils;
    }
    public String getPre(){
        return pre;
    }
    public List<Song> SearchSong(String name) {
        List<Song> list2 = new ArrayList<>();
        for (Song song : list) {
            if (song.getSong().toString().contains(name) || song.getSinger().toString().contains(name)) {
                list2.add(song);
            }

        }
        return list2;
        }
    public void setPre(String p){
        pre = p;
    }
    public List<Song> getLocalMusci(Context context) {
        List<Song> list1 = new ArrayList<Song>();
        int position = 0;
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.AudioColumns.IS_MUSIC);
            for (cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()) {
                Song song = new Song();
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setALLName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                song.setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                if(song.getSong().contains("-")){
                    String Name[] = song.getSong().split("-");
                    String name = Name[1].toString().split(".mp3")[0].indexOf("[mqms2]") > 0
                            ? Name[1].toString().split(".mp3")[0].substring(0, Name[1].toString().split(".mp3")[0].indexOf("[mqms2]")) : Name[1].toString().split(".mp3")[0];
                    song.setSong(name);
                    song.setSinger(Name[0]);
                }
                if (song.getDuration()/(1000 * 60) >= 1) {     //只把1分钟以上的音乐添加到集合当中
                    song.setPosition(position);
                    list1.add(song);
                    Log.i("songsong","+"+song.getALLName()+song.getPath());
                    position++;
                }
                Log.i("getMUSIC","TTTTTTTTTTT3"+list1.size());
            }
             cursor.close();

       if (list == null) {
           list = list1;
       }
        return list1;
    }


    public int getListCount(){
        return list.size();
    }
    public void changeList(Song song){
        list.remove(song);
    }
    public List<Song> getList(){
        return list;
    }
    public void playOrPause() {
        if (mediaPlayer == null) {
            playMusic(list.get(0));
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public boolean isPlaying(){

        if (mediaPlayer == null) {
            return false;
        }
        Log.i(TAG, "Music"+mediaPlayer.isPlaying());
        if (mediaPlayer.isPlaying()){
            return true;
        }else
            return false;
    }
    public void stopMusic(){
        mediaPlayer.pause();
    }
    public void playMusic(Song song){

        if(isPrepare){
            return;
        }

            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(song.getPath());
                isPrepare = true;
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            isPrepare = false;
            currentSongPosition = song.getPosition();
            if (currentSongPosition == -1) {
                preSongPosition = currentSongPosition;
            }
            pre = list.get(currentSongPosition).getPath();
                 Log.i(TAG, "RED"+pre);
            MusicFindUtil.getInstance().setID(pre);
    }
    public void clean() {
        mediaPlayer.stop();
        mediaPlayer.release();

    }
    public int getCurrentPosition(){
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }
    public void setCurrentSongPosition(int p){
        this.currentSongPosition = p;
    }

    public void setSeekTo (int i){
        mediaPlayer.seekTo(i);
    }
    public void pre(){
        preSongPosition = currentSongPosition;
        if (pattern == TYPE_ORDER||pattern == TYPE_SINGLE) {
            preSongPosition = currentSongPosition;
            if (currentSongPosition == 0){
                currentSongPosition=MusicUtil.getInstance().getListCount()-1;
            }else{
                currentSongPosition = currentSongPosition -1;
            }
        }
        if (pattern == TYPE_RANDOM) {
            currentSongPosition = new Random().nextInt(MusicUtil.getInstance().getListCount()-1);
        }
        Log.i("song1",""+list.get(currentSongPosition).getSinger());
    }
    public void prePlayOrNextPlay(){
        if (list!=null){
        playMusic(list.get(currentSongPosition));}
    }

    public Song getNewSongInfo(){
        if(list!=null)
            return list.get(currentSongPosition);
        return null;
    }

    public void next(){
        preSongPosition = currentSongPosition;
        if (pattern == TYPE_ORDER||pattern == TYPE_SINGLE) {
            if (currentSongPosition == MusicUtil.getInstance().getListCount()-1){
                currentSongPosition = 0;
            }else{
                currentSongPosition=currentSongPosition+1;
            }
        }
        if (pattern == TYPE_RANDOM) {
            currentSongPosition = new Random().nextInt(MusicUtil.getInstance().getListCount()-1);
        }
    }

    public void comNext(){
        preSongPosition = currentSongPosition;
        if (pattern == TYPE_SINGLE){
            return;
        }else if (pattern == TYPE_ORDER) {
            if (currentSongPosition == MusicUtil.getInstance().getListCount()-1){
                currentSongPosition = 0;
            }else{
                currentSongPosition=currentSongPosition+1;
            }
        }else if (pattern == TYPE_RANDOM) {
            currentSongPosition = new Random().nextInt(MusicUtil.getInstance().getListCount()-1);
        }
    }
//    public void setPlayCompletionListener() {
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                next();
//                prePlayOrNextPlay();
//            }
//        });
//    }
    public MediaPlayer getMediaPlayer(){
        if(mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
            return mediaPlayer;

    }


}

