package com.music.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/10.
 */

public class NetStateUtil {
    private static int i = -1;

    public static int getNetWorkState(Context context){
            boolean wifi = false;
            boolean data = false;
            if (Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP){
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo dataInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (!wifiInfo.isConnected()&&dataInfo.isConnected()){
                    i = 1;
                } else if ((wifiInfo.isConnected() && dataInfo.isConnected())||(wifiInfo.isConnected() && !dataInfo.isConnected())) {
                    i = 2;
                } else if (!wifiInfo.isConnected() && !dataInfo.isConnected()){
                    i = 0;
                }
            }else {
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //用于存放网络连接信息
                StringBuilder sb = new StringBuilder();
                //通过循环将网络信息逐个取出来
                for (int j =0;  j< networks.length; j++){
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[j]);
                    Log.i("TAG", "onReceive: "+networkInfo.getTypeName());
                    if (networkInfo.getTypeName().equals("WIFI")){
                        wifi = true;
                    }else if (networkInfo.getTypeName().equals("MOBILE")){
                        data = true;
                    }
                }
                if (!wifi&&data){
                    i = 1;
                }else if ((wifi && data)||(wifi && !data)){
                    i = 2;
                }else if (!wifi && !data){
                    i = 0;
                }
            }
            return i;
        }

}
