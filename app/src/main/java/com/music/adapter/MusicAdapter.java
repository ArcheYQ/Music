package com.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.R;
import com.music.activity.MusicActivity;
import com.music.bean.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/9/7.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{
    List<Song> list;
    List<Song> allList;
    Boolean isfirst;
    private boolean isPrepare = false;
    private MediaPlayer mediaPlayer;
    private Context context;

    public MusicAdapter (List<Song> list,Boolean isfirst,Context context){
        this.list = list;
        this.context = context;
        if(isfirst){
        allList = list;
        this.isfirst = false;}
    }
    public void SearchSong(String name) {
        List<Song> list1 = new ArrayList<>();
        Log.i("getMUSIC","TTTTTTTTTTT1"+allList.size());
        for (Song song : allList) {
            if (song.getSong().toString().contains(name) || song.getSinger().toString().contains(name)) {
                list1.add(song);
            }
        }
        this.list = list1;
        notifyDataSetChanged();
    };
    public int getListCount(){
        return allList.size();
    };

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false));
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
    holder.load(list.get(position),context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
         TextView tvSong;
         TextView tvSinger;
         TextView tvSize;
         public MusicViewHolder(View itemView) {
             super(itemView);
             tvSinger = itemView.findViewById(R.id.tv_singer);
             tvSong = itemView.findViewById(R.id.tv_song);
             tvSize = itemView.findViewById(R.id.tv_size);
         }
         public void load (final Song song, final Context context){
             tvSinger.setText(song.getSinger());
             tvSize.setText(convertFileSize(song.getSize()));
             tvSong.setText(song.getSong());
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent = new Intent(context, MusicActivity.class);
                     Bundle bundle = new Bundle();
                     bundle.putSerializable("songInfo",song);
                     intent.putExtras(bundle);
                     context.startActivity(intent);
                 }
             });
         }
     }
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
    public void playMusic(int position){
        if(isPrepare){
            return;
        }
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(allList.get(position).getPath());
            isPrepare = true;
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        isPrepare = false;
    }
}
