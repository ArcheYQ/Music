package com.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.music.R;
import com.music.bean.MusicFind;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/9/21.
 */

public class FindMusicAdapter extends RecyclerView.Adapter<FindMusicAdapter.FindMusicViewHolder> {
    List<MusicFind> musicFinds;
    Context context;
    public FindMusicAdapter(List<MusicFind> musicFinds,Context context){
        this.musicFinds= musicFinds;
        this.context = context;

    }
    @Override
    public FindMusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FindMusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_music, parent, false));
    }

    @Override
    public void onBindViewHolder(FindMusicViewHolder holder, int position) {
    holder.load(musicFinds.get(position),context);
    }

    @Override
    public int getItemCount() {
        return musicFinds == null ? 0 : musicFinds.size();
    }

    public class FindMusicViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvFenleiSong;
        TextView tvFenleiSinger;
        public FindMusicViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvFenleiSong = itemView.findViewById(R.id.tv_fenlei_song);
            tvFenleiSinger = itemView.findViewById(R.id.tv_fenlei_singer);
        }
        public void load(final MusicFind musicFind,final Context context){
            tvFenleiSinger.setText(musicFind.getSingername());
            tvFenleiSong.setText(musicFind.getSongname());
            Glide.with(context)
                    .load(musicFind.getAlbumpic_small())
                    .crossFade()
                    .into(ivCover);
        }
    }
}


