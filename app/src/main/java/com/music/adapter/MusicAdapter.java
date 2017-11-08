package com.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.R;
import com.music.bean.Song;
import com.music.util.MusicUtil;

import java.util.List;

;import static android.content.ContentValues.TAG;

/**
 * Created by 雅倩宝宝 on 2017/9/7.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{
    List<Song> list;

    private Context context;


    public MusicAdapter (List<Song> list,Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList (List<Song> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public void setPlay(){
        notifyDataSetChanged();
    }

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
             if (song.getPath().equals(MusicUtil.getInstance().getPre())){
                 Log.i(TAG, "RED"+song.getPath()+MusicUtil.getInstance().getPre());
                 tvSize.setTextColor(Color.parseColor("#da3318"));
                 tvSinger.setTextColor(Color.parseColor("#da3318"));
                 tvSong.setTextColor(Color.parseColor("#da3318"));
             }else{
                 tvSize.setTextColor(Color.parseColor("#959595"));
                 tvSinger.setTextColor(Color.parseColor("#959595"));
                 tvSong.setTextColor(Color.parseColor("#000000"));
             }
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
//                     tvSinger.setTextColor(Color.parseColor("#da3318"));
//                     tvSong.setTextColor(Color.parseColor("#da3318"));
//                     tvSize.setTextColor(Color.parseColor("#da3318"));
                     Intent intent = new Intent(context, com.music.activity.MusicActivity.class);
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

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }
}
