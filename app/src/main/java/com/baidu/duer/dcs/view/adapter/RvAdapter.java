package com.baidu.duer.dcs.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.duer.dcs.R;

import java.util.ArrayList;

/**
 * Created by Huangyong on 2018/1/27.
 */

public class RvAdapter extends RecyclerView.Adapter<MusicHolder> {

    private ArrayList<String> list;

    public RvAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private Context context;

    @Override
    public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item,null);
        return new MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicHolder holder, int position) {
        holder.title.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
