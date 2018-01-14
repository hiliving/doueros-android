package com.baidu.duer.dcs.androidapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.view.ContentFragment;
import com.baidu.duer.dcs.view.Iview.OnChangePageListener;
import com.baidu.duer.dcs.view.UserCenterFragment;
import com.baidu.duer.dcs.view.adapter.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huangyong on 2018/1/14.
 */

public class MainActivity extends AppCompatActivity implements OnChangePageListener {

    private ViewPager convp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initView();

    }

    private void initView() {
        convp = (ViewPager) findViewById(R.id.contentvp);
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fmts = new ArrayList<>();
        ContentFragment fm1 = ContentFragment.getInstance("CON");
        Fragment fm2 = UserCenterFragment.getInstance("USER");

        fmts.add(fm2);
        fmts.add(fm1);
        PagerAdapter adapter = new PagerAdapter(fm,fmts);
        adapter.initSwicth();
        adapter.setOnChangeListener(this);
        convp.setAdapter(adapter);
        convp.setCurrentItem(1);
    }

    @Override
    public void switchPage(int i) {
        convp.setCurrentItem(i);
    }
}
