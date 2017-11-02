package com.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.R;
import com.music.activity.NetMusicActivity;
import com.music.bean.MusicFind;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/9/20.
 */

public class MusicFenAdapter extends RecyclerView.Adapter<MusicFenAdapter.MusicFenViewHolder> {
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NetMusicActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("songNetInfo",musicFind);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
