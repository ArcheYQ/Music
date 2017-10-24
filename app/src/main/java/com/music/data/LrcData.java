package com.music.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 雅倩宝宝 on 2017/10/23.
 */

public class LrcData extends SQLiteOpenHelper {
    private static final String DB_NAME = "LRC.DB";
    private static final int DB_VERSION = 1;
    public LrcData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public LrcData(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String NewSQL = LrcTable.getCreatSONGLRCSQL();
        sqLiteDatabase.execSQL(NewSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public static final class LrcTable {
        public static final String TBL_NAME = "SONGLRC";
        public static final String COL_ID = "ID";
        public static final String COL_SONG = "SONG";
        public static final String COL_LRC = "LRC";

        public static String getCreatSONGLRCSQL(){
            String sql = "CREATE TABLE "
                    + TBL_NAME +"(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_LRC + " TEXT,"
                    + COL_SONG + " VARCHAR(50)"
                    +")";
            return sql;
        }
    }
}

