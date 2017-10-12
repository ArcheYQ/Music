package com.music.lrc;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/10/11.
 */

public class LrcRow implements Comparable<LrcRow>{
    public final static String TAG = "LrcRow";
    public String strTime;
    public long time;
    public String content;
    public LrcRow(){}
    public LrcRow(String strTime,long time,String content){
        this.strTime = strTime;
        this.time = time;
        this.content = content;
    }

    @Override
    public String toString() {
        return "[" + strTime + " ]"  + content;
    }
    public static List createRows(String standardLrcLine){
        try {
            if (standardLrcLine.indexOf("[")!=0||standardLrcLine.indexOf("]")!=9){
                return null;
            }
            int lastIndexOfRightBacket = standardLrcLine.lastIndexOf("]");
            String content = standardLrcLine.substring(lastIndexOfRightBacket + 1,standardLrcLine.length());
            String times = standardLrcLine.substring(0,lastIndexOfRightBacket + 1).replace("[","-").replace("]","-");
            String arrTimes[] = times.split("-");
            List listTimes = new ArrayList();
            for (String arrTime : arrTimes) {
                if (arrTime.trim().length()==0){
                    continue;
                }
                LrcRow lrcRow = new LrcRow(arrTime,timeConvert(arrTime),content);
                listTimes.add(lrcRow);
            }
            return listTimes;
        }catch (Exception e){
            Log.e(TAG, "createRows: "+ e.getMessage() );
            return null;
        }

    }

    private static long timeConvert(String time) {
        time = time.replace(".",":");
        String[] times = time.split(":");
        return Integer.valueOf(times[0])*60*1000+Integer.valueOf(times[1])*1000+Integer.valueOf(times[2]);
    }

    public int compareTo(LrcRow another) {
        return  (int)(this.time = another.time);
    }

}
