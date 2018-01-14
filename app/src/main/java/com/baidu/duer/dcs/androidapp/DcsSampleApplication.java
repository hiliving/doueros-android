/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.duer.dcs.androidapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.baidu.duer.dcs.keepAlive.brodcast.MyBroadcast;
import com.baidu.duer.dcs.keepAlive.service.MyServiceOne;

/**
 * DcsSample application
 * <p>
 * Created by zhangyan42@baidu.com on 2017/5/11.
 */
public class DcsSampleApplication extends Application {
    private static volatile DcsSampleApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // LeakCanary.install(this);
        initKeepAlive();//应用保活
    }
    private void initKeepAlive() {
        //开启系统时间广播(动态注册,不能静态注册)
        //部分机型会屏蔽时间广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//系统时间，每分钟发送一次
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);//屏幕打开（解锁），
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);//屏幕关闭
        MyBroadcast myBroadcast = new MyBroadcast();
        registerReceiver(myBroadcast, intentFilter);
        startMyService();
    }
    /**
     * 开启双服务
     */
    private void startMyService() {
        Intent serviceOne = new Intent();
        serviceOne.setClass(this, MyServiceOne.class);
        startService(serviceOne);
    }
    public static DcsSampleApplication getInstance() {
        return instance;
    }

    public static Context getmContext() {
        return instance;
    }
}