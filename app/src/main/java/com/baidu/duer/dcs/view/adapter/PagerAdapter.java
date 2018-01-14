package com.baidu.duer.dcs.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.baidu.duer.dcs.view.ContentFragment;
import com.baidu.duer.dcs.view.Iview.OnChangePageListener;
import com.baidu.duer.dcs.view.UserCenterFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huangyong on 2018/1/14.
 */

public class PagerAdapter extends FragmentPagerAdapter implements OnChangePageListener {
    private Context context;
    private List<Fragment> list;
    private int position;

    public PagerAdapter(FragmentManager fm, List<Fragment> fmts) {
        super(fm);
        this.list = fmts;
    }

    @Override
    public Fragment getItem(int position) {
        this.position = position;
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public void initSwicth() {
        ((ContentFragment)list.get(1)).setOnChangePageListener(this);
        ((UserCenterFragment)list.get(0)).setOnChangePageListener(this);
    }

    @Override
    public void switchPage(int i) {
        if (changePageListener!=null){
            changePageListener.switchPage(i);
        }
    }
    private OnChangePageListener changePageListener;
    public void setOnChangeListener(OnChangePageListener listener){
        this.changePageListener = listener;
    }
}
