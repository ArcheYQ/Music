package com.music.util;

import android.util.Log;

import com.music.bean.MusicModle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by 雅倩宝宝 on 2017/9/20.
 */

public class MusicModleUtil {
    public static List<MusicModle> list1;
    public static List<MusicModle> parseJOSNWithGSON (Response response){
        Log.i("TAG", "onCreate:5"+"");
        list1 = new ArrayList<>();
        try{
            String ResponsData = response.body().string();
            JSONObject jsonObject = new JSONObject(ResponsData);
            String error = jsonObject.getString("showapi_res_error");
            if (!error.equals("")) {
                Log.i("TAG","error");}
            String body = jsonObject.getString("showapi_res_body");
            JSONObject jsonObject1 = new JSONObject(body);
            String pagebean = jsonObject1.getString("pagebean");
            JSONObject jsonObject2 = new JSONObject(pagebean);
            String songlist = jsonObject2.getString("songlist");
            JSONArray jsonArray = new JSONArray(songlist);

            for (int i = 0; i < jsonArray.length() ; i++) {
                MusicModle musicModle = new MusicModle();
                JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                musicModle.setDownUrl(jsonObject3.getString("downUrl"));
                musicModle.setSingername(jsonObject3.getString("singername"));
                musicModle.setSongname(jsonObject3.getString("songname"));
                musicModle.setUrl(jsonObject3.getString("url"));
                list1.add(musicModle);
                Log.i("TAG", "onCreate:4 "+musicModle.getDownUrl());
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
        return list1;
    }
}
