package com.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.music.R;
import com.music.activity.NetMusicActivity;
import com.music.bean.MusicFind;
import com.music.util.MusicFindUtil;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/9/21.
 */

public abstract class FindMusicAdapter extends RecyclerView.Adapter<FindMusicAdapter.FindMusicViewHolder> {
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
    public void addData(List<MusicFind> list){
        for (MusicFind musicFind : list) {
            musicFinds.add(musicFind);
        }
    }
    public void addDataChange(){
        notifyItemInserted(getItemCount()-1);
    }
    @Override
    public void onBindViewHolder(FindMusicViewHolder holder, int position) {
    holder.load(musicFinds.get(position),context);
    }
    public void setPlay(){
        notifyDataSetChanged();
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
            if (musicFind.getId().equals(MusicFindUtil.getInstance().getID())){
                tvFenleiSinger.setTextColor(Color.parseColor("#da3318"));
                tvFenleiSong.setTextColor(Color.parseColor("#da3318"));
            }else
            {
                tvFenleiSinger.setTextColor(Color.parseColor("#959595"));
                tvFenleiSong.setTextColor(Color.parseColor("#000000"));
            }
            Glide.with(context)
                    .load(musicFind.getAlbumpic_small())
                    .crossFade()
                    .into(ivCover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NetMusicActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("songNetInfo",musicFind);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                    getItemView(itemView,musicFind);

                }
            });
        }
    }

    protected abstract void getItemView(View itemView, MusicFind musicFind);


}


