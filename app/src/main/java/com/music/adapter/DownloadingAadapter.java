package com.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.R;
import com.music.bean.MusicFind;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/11/4.
 */

public class DownloadingAadapter extends RecyclerView.Adapter<DownloadingAadapter.DownloadingVierHolder>{
    List<MusicFind> musicFinds;
    public DownloadingAadapter(List<MusicFind> musicFinds){
        this.musicFinds = musicFinds;
    }

    @Override
    public DownloadingVierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownloadingVierHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_downloading,parent,false));
    }

    @Override
    public void onBindViewHolder(DownloadingVierHolder holder, int position) {
        holder.load(musicFinds.get(position));
    }

    @Override
    public int getItemCount() {
        return musicFinds == null ? 0 : musicFinds.size();
    }

    public class DownloadingVierHolder extends RecyclerView.ViewHolder {
        TextView tvfragmentingSong;
        TextView tvfragmentingSinger;
        public DownloadingVierHolder(View itemView) {
            super(itemView);
            tvfragmentingSong = itemView.findViewById(R.id.tv_fragmenting_song);
            tvfragmentingSinger = itemView.findViewById(R.id.tv_fragmenting_singer);
        }
        public void load(MusicFind musicFind){
            tvfragmentingSinger.setText(musicFind.getSingername());
            tvfragmentingSong.setText(musicFind.getSongname());
        }
    }
}
