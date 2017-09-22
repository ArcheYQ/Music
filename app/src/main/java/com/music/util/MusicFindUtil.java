package com.music.util;

import android.util.Log;

import com.music.bean.MusicFind;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by 雅倩宝宝 on 2017/9/21.
 */

public class MusicFindUtil {
    public static List<MusicFind> list2;
    public static int totalPage;
    public static String allPages;
    public int getPage(){
        try {
            totalPage =Integer.parseInt(allPages);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return totalPage;
    }
    public static List<MusicFind> parseJOSNWithGSON (Response response){
        list2 = new ArrayList<>();
        try{Log.i("TAG3","error");
            String responseData = response.body().string();
            Log.i("TAG3","responseData"+responseData);
            JSONObject jsonObject1 = new JSONObject(responseData);
            String showapi_res_error = jsonObject1.getString("showapi_res_error");
            if (!showapi_res_error.equals("")) {
                Log.i("TAG","error");}
            String showapi_res_body = jsonObject1.getString("showapi_res_body");
            Log.i("TAG3","showapi_res_body"+showapi_res_body);
            JSONObject jsonObject3 = new JSONObject(showapi_res_body);
            String pagebean = jsonObject3.getString("pagebean");
            Log.i("TAG3","pagebean"+pagebean);
            JSONObject jsonObject4 = new JSONObject(pagebean);
            allPages = jsonObject4.getString("allPages");
            String contentlist = jsonObject4.getString("contentlist");
            Log.i("TAG3","contentlist"+contentlist);
            JSONArray jsonArray = new JSONArray(contentlist);
            for (int i = 0; i < jsonArray.length() ; i++) {
                MusicFind musicfind = new MusicFind();
                JSONObject jsonObject5 = jsonArray.getJSONObject(i);
                musicfind.setSongname(jsonObject5.getString("songname"));
                musicfind.setSingername(jsonObject5.getString("singername"));
                musicfind.setAlbumpic_small(jsonObject5.getString("albumpic_small"));
                musicfind.setDownUrl(jsonObject5.getString("downUrl"));
                musicfind.setUrl(jsonObject5.getString("m4a"));
                list2.add(musicfind);
                Log.i("TAG3","list");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

            return list2;
    }
}
