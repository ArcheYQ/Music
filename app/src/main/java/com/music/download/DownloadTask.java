package com.music.download;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.music.bean.MusicFind;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/6.
 */

public class DownloadTask extends AsyncTask<MusicFind,Integer,Integer> {
    public static final int TYPE_SUCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    private DownloadListener downloadListener;
    private boolean isCanceled = false;
    private boolean isPause = false;
    private int lastProgress;
    public DownloadTask(DownloadListener downloadListener){
        this.downloadListener = downloadListener;
    }
    private String name;

    @Override
    protected Integer doInBackground(MusicFind... params) {
        name = params[0].getSongname();
        InputStream is = null;
        RandomAccessFile randomAccessFile =null;
        File file = null;
        try {
        long downloadedLength = 0;
        String downloadUrl = params[0].getUrl();
        String fileName = params[0].getSongname()+"-"+params[0].getSingername();
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        file = new File(directory + fileName);
        if (file.exists()){
            downloadedLength = file.length();
        }
        long contentLength = getContentLength(downloadUrl);
        if (contentLength == 0){
            return TYPE_FAILED;
        }else if(contentLength == downloadedLength){
            return TYPE_SUCESS;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("RANGE","Bytes="+downloadUrl + "-")
                .url(downloadUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();
            if (response != null){
                is = response.body().byteStream();
                randomAccessFile = new RandomAccessFile(file,"rw");
                randomAccessFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1){
                    if (isCanceled){
                        return TYPE_CANCELED;
                    }else if (isPause){
                        return TYPE_PAUSED;
                    }else {
                        total += len;
                        randomAccessFile.write(b,0,len);
                        int progress = (int) ((total+downloadedLength)*100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null){
                    is.close();
                }
                if (randomAccessFile != null){
                    randomAccessFile.close();
                }
                if (isCanceled && file !=null){
                    file.delete();
                }
            }catch (Exception e ){
                e.printStackTrace();
            }

        }
        return  TYPE_FAILED;

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress){
            downloadListener.onProgress(progress,name);
            lastProgress = progress;
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_SUCESS:
                downloadListener.onSuccess(name);
                break;
            case TYPE_FAILED:
                downloadListener.onFailed();
                break;
            case TYPE_PAUSED:
                downloadListener.onPause();
                break;
            case TYPE_CANCELED:
                downloadListener.onCanceled(name);
                break;
            default:
                break;
        }
        super.onPostExecute(integer);
    }
    public void pauseDownload(){
        isPause = true;
    }
    public void cancelDownload(){
        isCanceled = true;
    }
    private long getContentLength (String downloadUrl) throws IOException{
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.i("", "getContentLength: "+downloadUrl);
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response != null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
