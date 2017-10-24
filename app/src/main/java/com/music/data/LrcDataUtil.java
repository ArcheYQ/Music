package com.music.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.music.bean.Song;

/**
 * Created by 雅倩宝宝 on 2017/10/23.
 */

public class LrcDataUtil {
    private LrcData lrcData;
    public LrcDataUtil (Context context){
        this.lrcData = new LrcData(context);
    }
    //判断数据库中是否有该歌曲歌词数据
    public String findLrc(Song song){
        SQLiteDatabase db = lrcData.getReadableDatabase();
        Cursor cursor = db.query(LrcData.LrcTable.TBL_NAME,null,null,null,null,null,null,null);
        for (cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(LrcData.LrcTable.COL_SONG)).equals(song.getALLName())){
                return cursor.getString(cursor.getColumnIndex(LrcData.LrcTable.COL_LRC));
            }
        }
        return "";
    }
    //将歌词存入数据库
    public void addLrc(String lrc,Song song){
        if (!lrc.equals("")){
            SQLiteDatabase db = lrcData.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(LrcData.LrcTable.COL_LRC,lrc);
            contentValues.put(LrcData.LrcTable.COL_SONG,song.getALLName());
            db.insert(LrcData.LrcTable.TBL_NAME,null,contentValues);
        }
    }
}
