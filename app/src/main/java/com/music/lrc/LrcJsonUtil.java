package com.music.lrc;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Response;

/**
 * Created by 雅倩宝宝 on 2017/10/14.
 */

public class LrcJsonUtil {
    public static String parseJOSNWithGSON(Response response ,int c){
        try{
            String ResponsData = response.body().string();
            JSONObject jsonObject = new JSONObject(ResponsData);
            int count = Integer.parseInt(jsonObject.getString("count"));
            Log.i("TAG", "parseJOSNWithGSONCOUNT:"+count);
            if (count>=c){
                String result = jsonObject.getString("result");
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject1 = jsonArray.getJSONObject(c-1);
                String url = jsonObject1.getString("lrc");
                Log.i("TAG", "parseJOSNWithGSON:1 "+url);
                return url;
            }else {
                Log.i("TAG", "parseJOSNWithGSON: "+c);
                return "";
            }
        }catch (Exception e){

        }
        return "";

    }
    public static String parseNetJOSNWithGSON(Response response){
        try{
            String ResponsData = response.body().string();
            JSONObject jsonObject = new JSONObject(ResponsData);
            String error = jsonObject.getString("showapi_res_error");
            if (!error.equals("")) {
                Log.i("TAG","error");}
            String body = jsonObject.getString("showapi_res_body");
            JSONObject jsonObject1 = new JSONObject(body);
            String pagebean = jsonObject1.getString("lyric");
            Log.i("TAG","error1"+pagebean);
            return pagebean;
            }
        catch (Exception e){

        }
        return "";

    }
}
