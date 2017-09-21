package com.music.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.R;
import com.music.bean.MusicModle;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/9/20.
 */

public class MusicFenAdapter extends RecyclerView.Adapter<MusicFenAdapter.MusicFenViewHolder> {
    List<MusicModle> musicModles;

    public MusicFenAdapter(List<MusicModle> list) {
        this.musicModles = list;
    }

    @Override
    public MusicFenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MusicFenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_fenlei_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MusicFenViewHolder holder, int position) {
     holder.load(musicModles.get(position));
    }

    @Override
    public int getItemCount() {
        return musicModles==null?0:musicModles.size();
    }

    public class MusicFenViewHolder extends RecyclerView.ViewHolder {
        TextView tvFenleiSong;
        TextView tvFenleiSinger;

        public MusicFenViewHolder(View itemView) {
            super(itemView);
            tvFenleiSinger = itemView.findViewById(R.id.tv_fenlei_singer);
            tvFenleiSong = itemView.findViewById(R.id.tv_fenlei_song);
        }
        public void load (MusicModle musicModle){
            tvFenleiSinger.setText(musicModle.getSingername());
            tvFenleiSong.setText(musicModle.getSongname());
        }
    }
}
