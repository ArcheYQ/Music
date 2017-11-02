package com.music.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by 雅倩宝宝 on 2017/9/20.
 */

public class HttpUtil {
    public static void requestStringData(final int topid ,okhttp3.Callback callback) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://route.showapi.com/213-4?showapi_appid=46426&topid="+topid+"&showapi_sign=f908983676e94a34a11a56eb71400f79")
                            .build();
                    client.newCall(request).enqueue(callback);
                }
    public static void requestSongData(final String keyword ,final int page,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://route.showapi.com/213-1?showapi_appid=46426&keyword="+keyword+"&page="+page+"&showapi_sign=f908983676e94a34a11a56eb71400f79")
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void requstLrcData(final String name,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Log.i(TAG, "requstLrcData: "+name);
        String name1 = name.trim();
        Log.i(TAG, "requstLrcData:"+name1);
        Request request = new Request.Builder()
                .url("http://gecimi.com/api/lyric/"+name1)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void requstNetLrcData(final String id,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://route.showapi.com/213-2?showapi_appid=46426&musicid="+id+"&showapi_sign=f908983676e94a34a11a56eb71400f79")
                .build();
        client.newCall(request).enqueue(callback);
    }
}

