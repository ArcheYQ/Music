package com.music.lrc;

import android.content.Context;

import com.music.util.MusicUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 雅倩宝宝 on 2017/10/10.
 */

public class LrcUtil {
    private static LrcUtil lrcUtil;
    public synchronized static LrcUtil getInstance(){
        if (lrcUtil == null) {
            lrcUtil = new LrcUtil();
        }
        return lrcUtil;
    }
    public String getLrcFromAssets(String songName,Context context){
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getResources().getAssets().open(songName+".lrc"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "" ;
            String result = "";
            while ((line = bufferedReader.readLine() )!= null){
                if (line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

