package com.music.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.R;

/**
 * Created by 雅倩宝宝 on 2017/9/19.
 */

public class MusicGridAdapter extends BaseAdapter{
    private SparseArray<String> gridItems;
    private ViewHolder holder = null;
    private Context context;
    private int[] ids = { R.drawable.net28, R.drawable.yinyueren32,R.drawable.neidi5,
            R.drawable.rege26, R.drawable.gaotai6, R.drawable.oumei3,
            R.drawable.liuxing4, R.drawable.xingge26, R.drawable.kge36 };
    @Override
    public int getCount() {
        return gridItems.size() > 0 ? gridItems.size() : 0;
    }
    public MusicGridAdapter (SparseArray<String>  gridItems, Context context) {
        this.gridItems = gridItems;
        this.context = context;
    }
    @Override
    public Object getItem(int i) {
        return gridItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_gridview_song,viewGroup,false);
            holder = new ViewHolder(view);
        }
        holder.tvItemMainGrid.setText(gridItems.get(i)+"");
        holder.ivItemMainGird.setImageResource(ids[i]);
        return view;
    }
    private class ViewHolder {
        public ImageView ivItemMainGird;
        public TextView tvItemMainGrid;
        public ViewHolder(View convertView) {
            ivItemMainGird =  convertView.findViewById(R.id.iv_item_main_gird);
            tvItemMainGrid = convertView.findViewById(R.id.tv_item_main_grid);
        }
    }


}
