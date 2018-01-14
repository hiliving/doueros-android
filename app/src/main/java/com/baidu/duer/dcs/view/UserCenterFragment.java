package com.baidu.duer.dcs.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.util.BlurUtil;
import com.baidu.duer.dcs.view.Iview.OnChangePageListener;
import com.baidu.duer.dcs.view.adapter.PagerAdapter;

/**
 * Created by Huangyong on 2018/1/14.
 */

public class UserCenterFragment extends Fragment{


    private static UserCenterFragment fragment;
    private RelativeLayout root;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usercenter,null);
        root = (RelativeLayout) view.findViewById(R.id.root);
        BlurUtil.setViewBg(getContext(),15,5,root,R.mipmap.icbg2);
        return view;
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
