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

    List<Song> list = new ArrayList<>();
    private static MusicUtil musicUtil;



    public synchronized static MusicUtil getInstance(){
        return musicUtil == null ? new MusicUtil() : musicUtil;
    }
    public List<Song> getLocalMusci(Context context) {
        int position = 0;
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.AudioColumns.IS_MUSIC);
            for (cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()) {
                Song song = new Song();
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setSong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                song.setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                if(song.getSong().contains("-")){
                    String Name[] = song.getSong().split("-");
                    String name = Name[1].contains(".mp3")?Name[1].split(".mp3")[0] : Name[1];
                    song.setSong(name);
                    song.setSinger(Name[0]);
                }
                if (song.getDuration()/(1000 * 60) >= 1) {     //只把1分钟以上的音乐添加到集合当中
                    song.setPosition(position);
                    list.add(song);
                    position++;
                }
                Log.i("getMUSIC","TTTTTTTTTTT3"+list.size());
            }
             cursor.close();
//        }

        return list;
    }


}
