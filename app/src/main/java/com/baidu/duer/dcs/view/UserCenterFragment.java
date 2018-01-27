package com.baidu.duer.dcs.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.util.BlurUtil;
import com.baidu.duer.dcs.view.Iview.OnChangePageListener;
import com.baidu.duer.dcs.view.adapter.PagerAdapter;
import com.baidu.duer.dcs.view.adapter.RvAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Huangyong on 2018/1/14.
 */

public class UserCenterFragment extends Fragment{


    private static UserCenterFragment fragment;
    private RelativeLayout root;
    private View view;
    private RecyclerView rvlist;
    private ArrayList<String> list;
    private RvAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.usercenter,null);
        root = (RelativeLayout) view.findViewById(R.id.root);
        BlurUtil.setViewBg(getContext(),15,5,root,R.mipmap.icbg2);
        initView();
        initData();
        return view;
    }

    private void initData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File("sdcard/DcsMusic");
                if (file.isDirectory()){
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        list.add(files[i].getName());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }).start();

    }

    private void initView() {
        rvlist = (RecyclerView) view.findViewById(R.id.rvlist);
        list = new ArrayList<>();
        adapter = new RvAdapter(list,getContext());
        rvlist.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rvlist.setAdapter(adapter);
    }

    public static Fragment getInstance(String user) {
        if (fragment!=null){
            return fragment;
        }else {
            fragment = new UserCenterFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TYPE",user);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    private OnChangePageListener listener;
    public void setOnChangePageListener(OnChangePageListener listener) {
        this.listener = listener;
    }
}
