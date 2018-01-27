package com.baidu.duer.dcs.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.duer.dcs.R;

/**
 * Created by Huangyong on 2018/1/27.
 */

public class MusicHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView play;

    public MusicHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.musictitle);
        play = (ImageView) itemView.findViewById(R.id.playbt);
    }
}
