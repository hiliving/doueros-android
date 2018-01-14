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
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidsystemimpl.PlatformFactoryImpl;
import com.baidu.duer.dcs.androidsystemimpl.webview.BaseWebView;
import com.baidu.duer.dcs.devicemodule.screen.ScreenDeviceModule;
import com.baidu.duer.dcs.devicemodule.screen.message.RenderVoiceInputTextPayload;
import com.baidu.duer.dcs.devicemodule.voiceinput.VoiceInputDeviceModule;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.BaseMultiChannelMediaPlayer;
import com.baidu.duer.dcs.framework.DcsFramework;
import com.baidu.duer.dcs.framework.DcsResponseDispatcher;
import com.baidu.duer.dcs.framework.DeviceModuleFactory;
import com.baidu.duer.dcs.framework.DialogRequestIdHandler;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.IResponseListener;
import com.baidu.duer.dcs.http.HttpConfig;
import com.baidu.duer.dcs.oauth.api.IOauth;
import com.baidu.duer.dcs.oauth.api.OauthImpl;
import com.baidu.duer.dcs.systeminterface.IMediaPlayer;
import com.baidu.duer.dcs.systeminterface.IPlatformFactory;
import com.baidu.duer.dcs.systeminterface.IWakeUp;
import com.baidu.duer.dcs.util.BlurUtil;
import com.baidu.duer.dcs.util.CommonUtil;
import com.baidu.duer.dcs.util.FastBlur;
import com.baidu.duer.dcs.util.LogUtil;
import com.baidu.duer.dcs.util.NetWorkUtil;
import com.baidu.duer.dcs.wakeup.WakeUp;
import com.skyfishjy.library.RippleBackground;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 主界面 activity
 * <p>
 * Created by zhangyan42@baidu.com on 2017/5/18.
 */
public class DcsSampleMainActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "DcsDemoActivity";
    private static boolean ISRECORDING = false;
    private ImageView voiceButton;
  //  private TextView textViewTimeStopListen;
    private TextView textViewRenderVoiceInputText;
    private Button pauseOrPlayButton;
    private BaseWebView webView;
    private LinearLayout mTopLinearLayout;
    private DcsFramework dcsFramework;
    private DeviceModuleFactory deviceModuleFactory;
    private IPlatformFactory platformFactory;
    private boolean isPause = true;
    private long startTimeStopListen;
    private boolean isStopListenReceiving;
    private String mHtmlUrl;
    // 唤醒
    private WakeUp wakeUp;
    private volatile float vol;
    private boolean isEnablePlaying=false;
    private RippleBackground content;
    private LinearLayout control;
    private RippleBackground ripple;
    private RelativeLayout root;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dcs_sample_activity_main);
        initView();
        initOauth();
        initFramework();
    }

    private void initView() {
      /*  Button openLogBtn = (Button) findViewById(R.id.openLogBtn);
        openLogBtn.setOnClickListener(this);*/
        voiceButton = (ImageView) findViewById(R.id.voiceBtn);
        content = (RippleBackground) findViewById(R.id.content);
        voiceButton.setOnClickListener(this);

       // textViewTimeStopListen = (TextView) findViewById(R.id.id_tv_time_0);
        textViewRenderVoiceInputText = (TextView) findViewById(R.id.id_tv_RenderVoiceInputText);
        mTopLinearLayout = (LinearLayout) findViewById(R.id.topLinearLayout);

        webView = new BaseWebView(DcsSampleMainActivity.this.getApplicationContext());
        webView.setBackgroundColor(Color.BLACK);
        webView.setWebViewClientListen(new BaseWebView.WebViewClientListener() {
            @Override
            public BaseWebView.LoadingWebStatus shouldOverrideUrlLoading(WebView view, String url) {
                // 拦截处理不让其点击
                return BaseWebView.LoadingWebStatus.STATUS_TRUE;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!url.equals(mHtmlUrl) && !"about:blank".equals(mHtmlUrl)) {
                    platformFactory.getWebView().linkClicked(url);
                }

                mHtmlUrl = url;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });
//        mTopLinearLayout.addView(webView);

        Button mPreviousSongBtn = (Button) findViewById(R.id.previousSongBtn);
        root = (RelativeLayout) findViewById(R.id.root);
        ripple = (RippleBackground) findViewById(R.id.contentbottom);
        pauseOrPlayButton = (Button) findViewById(R.id.pauseOrPlayBtn);
        Button mNextSongBtn = (Button) findViewById(R.id.nextSongBtn);
        control = (LinearLayout) findViewById(R.id.songLinearLayout);
        mPreviousSongBtn.setOnClickListener(this);
        pauseOrPlayButton.setOnClickListener(this);
        mNextSongBtn.setOnClickListener(this);
        ripple.startRippleAnimation();
        blurImage();
        initWakeUp();
    }
    private void initWakeUp() {
        mediaPlayer = new MediaPlayer();
        try {
            // 打开指定音乐文件,获取assets目录下指定文件的AssetFileDescriptor对象
            AssetManager am = getAssets();
            AssetFileDescriptor afd = am.openFd("music/cmd.mp3");
            mediaPlayer.reset();
// 使用MediaPlayer加载指定的声音文件。
            mediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startRec(){
        ISRECORDING = true;
        content.startRippleAnimation();
        ripple.stopRippleAnimation();
    }
    public void finishRec(){
        ISRECORDING = false;
        content.stopRippleAnimation();
        ripple.startRippleAnimation();
    }
    private void initFramework() {
        platformFactory = new PlatformFactoryImpl(this);
        platformFactory.setWebView(webView);
        dcsFramework = new DcsFramework(platformFactory);
        deviceModuleFactory = dcsFramework.getDeviceModuleFactory();
        deviceModuleFactory.createVoiceOutputDeviceModule();
        deviceModuleFactory.createVoiceInputDeviceModule();
        deviceModuleFactory.getVoiceInputDeviceModule().addVoiceInputListener(
                new VoiceInputDeviceModule.IVoiceInputListener() {
                    @Override
                    public void onStartRecord() {
                        LogUtil.d(TAG, "onStartRecord");
                        startRecording();
                    }

                    @Override
                    public void onFinishRecord() {
                        LogUtil.d(TAG, "onFinishRecord");
                        stopRecording();
                    }

                    @Override
                    public void onSucceed(int statusCode) {
                        LogUtil.d(TAG, "onSucceed-statusCode:" + statusCode);
                        if (statusCode != 200) {
                            stopRecording();
                            Toast.makeText(DcsSampleMainActivity.this,
                                    getResources().getString(R.string.voice_err_msg),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        LogUtil.d(TAG, "onFailed-errorMessage:" + errorMessage);
                        stopRecording();
                        Toast.makeText(DcsSampleMainActivity.this,
                                getResources().getString(R.string.voice_err_msg),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        deviceModuleFactory.createAlertsDeviceModule();
        deviceModuleFactory.createAudioPlayerDeviceModule();
        deviceModuleFactory.getAudioPlayerDeviceModule().addAudioPlayListener(
                new IMediaPlayer.SimpleMediaPlayerListener() {
                    @Override
                    public void onPaused() {
                        super.onPaused();
                       // pauseOrPlayButton.setText(getResources().getString(R.string.audio_paused));
                        pauseOrPlayButton.setBackgroundResource(R.drawable.playbg);

                        isPause = true;
                    }

                    @Override
                    public void onPlaying() {
                        super.onPlaying();
                        pauseOrPlayButton.setBackgroundResource(R.drawable.pausebg);
                       // pauseOrPlayButton.setText(getResources().getString(R.string.audio_playing));
                        isPause = false;
                    }

                    @Override
                    public void onCompletion() {
                        super.onCompletion();
                       // pauseOrPlayButton.setText(getResources().getString(R.string.audio_default));
                        isPause = false;
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                       // pauseOrPlayButton.setText(getResources().getString(R.string.audio_default));
                        isPause = true;
                    }

                    @Override
                    public void onUpdateProgress(int percent) {
                        super.onUpdateProgress(percent);
                        Log.d("Progress","当前播放进度"+percent);
                    }

                    @Override
                    public void onDuration(long milliseconds) {
                        super.onDuration(milliseconds);
                        Log.d("Progress","播放总长度"+milliseconds);
                    }

                    @Override
                    public void onPrepared() {
                        super.onPrepared();
                        isEnablePlaying=true;
                        control.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onRelease() {
                        super.onRelease();
                        control.setVisibility(View.INVISIBLE);
                    }
                });

        deviceModuleFactory.createSystemDeviceModule();
        deviceModuleFactory.createSpeakControllerDeviceModule();
        deviceModuleFactory.createPlaybackControllerDeviceModule();
        deviceModuleFactory.createScreenDeviceModule();
        deviceModuleFactory.getScreenDeviceModule()
                .addRenderVoiceInputTextListener(new ScreenDeviceModule.IRenderVoiceInputTextListener() {
                    @Override
                    public void onRenderVoiceInputText(RenderVoiceInputTextPayload payload) {
                        textViewRenderVoiceInputText.setText(payload.text);
                    }

                });
        // init唤醒
        wakeUp = new WakeUp(platformFactory.getWakeUp(),
                platformFactory.getAudioRecord());
        wakeUp.addWakeUpListener(wakeUpListener);
        // 开始录音，监听是否说了唤醒词
        wakeUp.startWakeUp();
    }

    private IWakeUp.IWakeUpListener wakeUpListener = new IWakeUp.IWakeUpListener() {
        @Override
        public void onWakeUpSucceed() {
           /* Toast.makeText(DcsSampleMainActivity.this,
                    getResources().getString(R.string.wakeup_succeed),
                    Toast.LENGTH_SHORT)
                    .show();*/

             mediaPlayer.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    voiceButton.performClick();
                }
            },2000);

        }
    };
    float scaleFactor = 5;
    float blur = 15;
    public void blurImage(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.icbg);
        //创建一个长宽等比缩小的Bitmap
        int bitmap_width = bitmap.getWidth();
        int bitmap_hight = bitmap.getHeight();
        Bitmap overlay = Bitmap.createBitmap((int) (bitmap_width / scaleFactor), (int) (bitmap_hight / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        //将canvas按照bitmap等量缩放，模糊处理的图片才能显示正常
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        //对采样后的bitmap进行模糊处理，缩放采样后的图片处理比原图处理要省很多时间和内存开销
        overlay = FastBlur.doBlur(overlay, (int) blur, false);
        //模糊处理后的图片设置为头部布局背景图
        BitmapDrawable drawable = new BitmapDrawable(overlay);
        root.setBackground(drawable);
    }
    private void doUserActivity() {
        deviceModuleFactory.getSystemProvider().userActivity();
    }

    private void initOauth() {
        IOauth baiduOauth = new OauthImpl();
        HttpConfig.setAccessToken(baiduOauth.getAccessToken());
    }

    private void stopRecording() {
        wakeUp.startWakeUp();
        isStopListenReceiving = false;
      //  voiceButton.setText(getResources().getString(R.string.stop_record));
        long t = System.currentTimeMillis() - startTimeStopListen;
       // textViewTimeStopListen.setText(getResources().getString(R.string.time_record, t));
        finishRec();
    }

    private void startRecording() {
        startRec();
        wakeUp.stopWakeUp();
        isStopListenReceiving = true;
        deviceModuleFactory.getSystemProvider().userActivity();
        // voiceButton.setText(getResources().getString(R.string.start_record));
        // textViewTimeStopListen.setText("");
        textViewRenderVoiceInputText.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voiceBtn:
                if (!NetWorkUtil.isNetworkConnected(this)) {
                    Toast.makeText(this,
                            getResources().getString(R.string.err_net_msg),
                            Toast.LENGTH_SHORT).show();
                    wakeUp.startWakeUp();
                    return;
                }
                if (CommonUtil.isFastDoubleClick()) {
                    return;
                }
                if (TextUtils.isEmpty(HttpConfig.getAccessToken())) {
                    startActivity(new Intent(DcsSampleMainActivity.this, DcsSampleOAuthActivity.class));
                    finish();
                    return;
                }
                if (!dcsFramework.getDcsClient().isConnected()) {
                    dcsFramework.getDcsClient().startConnect();
                    return;
                }
                if (isStopListenReceiving) {
                    platformFactory.getVoiceInput().stopRecord();
                    isStopListenReceiving = false;
                    return;
                }
                isStopListenReceiving = true;
                startTimeStopListen = System.currentTimeMillis();
                platformFactory.getVoiceInput().startRecord();
                doUserActivity();
                break;
         /*   case R.id.openLogBtn:
               // openAssignFolder(FileUtil.getLogFilePath());
                break;*/
            case R.id.previousSongBtn:
                platformFactory.getPlayback().previous(nextPreResponseListener);
                doUserActivity();
                break;
            case R.id.nextSongBtn:
                platformFactory.getPlayback().next(nextPreResponseListener);
                doUserActivity();
                break;
            case R.id.pauseOrPlayBtn:
                if (isEnablePlaying){
                    if (isPause) {
                        pauseOrPlayButton.setBackgroundResource(R.drawable.pausebg);
                        platformFactory.getPlayback().play(playPauseResponseListener);
                    } else {
                        pauseOrPlayButton.setBackgroundResource(R.drawable.playbg);
                        platformFactory.getPlayback().pause(playPauseResponseListener);
                    }
                    doUserActivity();
                }else {
                    Toast.makeText(this, "当前播放队列为空，请先说出你想听的歌曲", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private IResponseListener playPauseResponseListener = new IResponseListener() {
        @Override
        public void onSucceed(int statusCode) {
            if (statusCode == 204) {
                Toast.makeText(DcsSampleMainActivity.this,
                        getResources().getString(R.string.no_directive),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onFailed(String errorMessage) {
            Toast.makeText(DcsSampleMainActivity.this,
                    getResources().getString(R.string.request_error),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    };

    private IResponseListener nextPreResponseListener = new IResponseListener() {
        @Override
        public void onSucceed(int statusCode) {
            if (statusCode == 204) {
                Toast.makeText(DcsSampleMainActivity.this,
                        getResources().getString(R.string.no_audio),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onFailed(String errorMessage) {
            Toast.makeText(DcsSampleMainActivity.this,
                    getResources().getString(R.string.request_error),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    };

    /**
     * 打开日志
     *
     * @param path 文件的绝对路径
     */
    private void openAssignFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(DcsSampleMainActivity.this,
                    getResources().getString(R.string.no_log),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "text/plain");
        try {
            startActivity(Intent.createChooser(intent,
                    getResources().getString(R.string.open_file_title)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 先remove listener  停止唤醒,释放资源
        wakeUp.removeWakeUpListener(wakeUpListener);
        wakeUp.stopWakeUp();
        wakeUp.releaseWakeUp();

        if (dcsFramework != null) {
            dcsFramework.release();
        }
        webView.setWebViewClientListen(null);
        mTopLinearLayout.removeView(webView);
        webView.removeAllViews();
        webView.destroy();
    }
}