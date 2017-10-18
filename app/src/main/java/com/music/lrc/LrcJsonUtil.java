package com.music.lrc;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Response;

/**
 * Created by 雅倩宝宝 on 2017/10/14.
 */

public class LrcJsonUtil {
    public static String parseJOSNWithGSON(Response response){
        try{
            String ResponsData = response.body().string();
            JSONObject jsonObject = new JSONObject(ResponsData);
            String count = jsonObject.getString("count");
            Log.i("TAG", "parseJOSNWithGSONCOUNT:"+count);
            if (!count.equals("0")){
                String result = jsonObject.getString("result");
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String url = jsonObject1.getString("lrc");
                Log.i("TAG", "parseJOSNWithGSON: "+url);
                return url;
            }else {
                Log.i("TAG", "parseJOSNWithGSON: "+1);
                return "";
            }
        }catch (Exception e){

        }
        return "";

    }
}
