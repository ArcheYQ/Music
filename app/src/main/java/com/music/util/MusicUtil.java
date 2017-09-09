package com.music.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.music.bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/9/7.
 */

public class MusicUtil {
    private static MusicUtil musicUtil;
    public synchronized static MusicUtil getInstance(){
        return musicUtil == null ? new MusicUtil() : musicUtil;
    }
    public List<Song> getLocalMusci(Context context) {
        List<Song> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.AudioColumns.IS_MUSIC);
        Log.i("getMUSIC","222222222");
        Song song = new Song();
        Log.i("getMUSIC","时长"+song.getDuration()+cursor.getCount());
//        if(song.getDuration()/(1000*60)>=1){    //筛选出持续时间大于一分钟的音乐
            for (cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()) {
                Log.i("getMUSIC","3333333333333333");
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setSong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                song.setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                if(song.getSong().contains("-")){
                    String Name[] = song.getSong().split("-");
                    song.setSong(Name[1]);
                    song.setSinger(Name[0]);
                }
                Log.i("getMUSIC","SSSSSSSSSSS");
                list.add(song);
                Log.i("getMUSIC","DDDDDDDDDDDDD"+list.size());
            }
             cursor.close();
//        }
        Log.i("getMUSIC","TTTTTTTTTTT");
        return list;
    }
}
