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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.oauth.api.BaiduDialog;
import com.baidu.duer.dcs.oauth.api.BaiduDialogError;
import com.baidu.duer.dcs.oauth.api.BaiduException;
import com.baidu.duer.dcs.oauth.api.BaiduOauthImplicitGrant;
import com.baidu.duer.dcs.util.LogUtil;

import me.wangyuwei.particleview.ParticleView;

/**
 * 用户认证界面
 * <p>
 * Created by zhangyan42@baidu.com on 2017/5/18.
 */
public class DcsSampleOAuthActivity extends Activity{
    // 需要开发者自己申请client_id
    // client_id，就是oauth的client_id
    private static final String CLIENT_ID = "YVmVUz3TA0M8GDZVb1b2WeXLOwIcdXeq";
    // 是否每次授权都强制登陆
    private boolean isForceLogin = false;
    // 是否每次都确认登陆
    private boolean isConfirmLogin = false;
    private EditText editTextClientId;
    private Button oauthLoginButton;
    private BaiduOauthImplicitGrant baiduOauthImplicitGrant;
    private ParticleView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.dcs_sample_activity_oauth);
        start = (ParticleView) findViewById(R.id.startAnim);
        initView();
    }


    private void initView() {
        start.startAnim();
        start.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                init();
            }
        });
    }

    private void init() {
        String clientId = CLIENT_ID;
        if (!TextUtils.isEmpty(clientId)) {
            baiduOauthImplicitGrant = new BaiduOauthImplicitGrant(clientId, DcsSampleOAuthActivity.this.getApplication());
            baiduOauthImplicitGrant.authorize(DcsSampleOAuthActivity.this, isForceLogin, isConfirmLogin, new BaiduDialog
                    .BaiduDialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    startMainActivity();
                }

                @Override
                public void onBaiduException(BaiduException e) {

                }

                @Override
                public void onError(BaiduDialogError e) {
                    if (null != e) {
                        String toastString = TextUtils.isEmpty(e.getMessage())
                                ? DcsSampleOAuthActivity.this.getResources()
                                .getString(R.string.err_net_msg) : e.getMessage();
                        Toast.makeText(DcsSampleOAuthActivity.this.getApplicationContext(), toastString,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancel() {
                    LogUtil.d("cancle", "I am back");
                }
            });
        } else {
            Toast.makeText(DcsSampleOAuthActivity.this.getApplicationContext(),
                    getResources().getString(R.string.client_id_empty),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(DcsSampleOAuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}