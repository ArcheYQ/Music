package com.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.R;
import com.music.activity.NetMusicActivity;
import com.music.bean.MusicFind;
import com.music.util.MusicFindUtil;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/9/20.
 */

public abstract class MusicFenAdapter extends RecyclerView.Adapter<MusicFenAdapter.MusicFenViewHolder> {
    List<MusicFind> musicFinds;
    Context context;

    public MusicFenAdapter(List<MusicFind> list,Context context) {
        this.musicFinds = list;
        this.context = context;
    }

    @Override
    public MusicFenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MusicFenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_fenlei_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MusicFenViewHolder holder, int position) {
     holder.load(musicFinds.get(position));
    }
    public void setPlay(){
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return musicFinds==null?0:musicFinds.size();
    }

    public class MusicFenViewHolder extends RecyclerView.ViewHolder {
        TextView tvFenleiSong;
        TextView tvFenleiSinger;

        public MusicFenViewHolder(View itemView) {
            super(itemView);
            tvFenleiSinger = itemView.findViewById(R.id.tv_fenlei_singer);
            tvFenleiSong = itemView.findViewById(R.id.tv_fenlei_song);
        }
        public void load (final MusicFind musicFind){
            tvFenleiSinger.setText(musicFind.getSingername());
            tvFenleiSong.setText(musicFind.getSongname());
            if (musicFind.getId().equals(MusicFindUtil.getInstance().getID())){
                tvFenleiSinger.setTextColor(Color.parseColor("#da3318"));
                tvFenleiSong.setTextColor(Color.parseColor("#da3318"));
            }else
            {
                tvFenleiSinger.setTextColor(Color.parseColor("#959595"));
                tvFenleiSong.setTextColor(Color.parseColor("#000000"));
            }
            getItemView(itemView,musicFind);

        }
    }

    protected abstract void getItemView(View itemView,final MusicFind musicFind);
}
