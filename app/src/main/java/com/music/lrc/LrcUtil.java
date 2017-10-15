package com.music.lrc;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 雅倩宝宝 on 2017/10/10.
 */

public class LrcUtil {
//    private static LrcUtil lrcUtil;
//    public synchronized static LrcUtil getInstance(){
//        if (lrcUtil == null) {
//            lrcUtil = new LrcUtil();
//        }
//        return lrcUtil;
//    }
    public static String getLrcFromAssets(String Url){
        if (Url.equals("")){
            return "";
        }
        try {
            URL url=new URL(Url);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            InputStream input=conn.getInputStream();
            BufferedReader in=new BufferedReader(new InputStreamReader(input));
            String line = "" ;
            String result = "";
            while ((line = in.readLine() )!= null){
                if (line.trim().equals(""))
                    continue;
                result += line + "\r\n";
                Log.i("first","getLrcFromAssets: "+result);
            }
            Log.i("total","getLrcFromAssets: "+result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}

