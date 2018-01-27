package com.baidu.duer.dcs.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.DcsSampleOAuthActivity;
import com.baidu.duer.dcs.androidsystemimpl.PlatformFactoryImpl;
import com.baidu.duer.dcs.androidsystemimpl.webview.BaseWebView;
import com.baidu.duer.dcs.devicemodule.alerts.AlertsDeviceModule;
import com.baidu.duer.dcs.devicemodule.screen.ScreenDeviceModule;
import com.baidu.duer.dcs.devicemodule.screen.message.RenderVoiceInputTextPayload;
import com.baidu.duer.dcs.devicemodule.voiceinput.VoiceInputDeviceModule;
import com.baidu.duer.dcs.framework.DcsFramework;
import com.baidu.duer.dcs.framework.DeviceModuleFactory;
import com.baidu.duer.dcs.framework.IResponseListener;
import com.baidu.duer.dcs.http.HttpConfig;
import com.baidu.duer.dcs.oauth.api.IOauth;
import com.baidu.duer.dcs.oauth.api.OauthImpl;
import com.baidu.duer.dcs.systeminterface.IMediaPlayer;
import com.baidu.duer.dcs.systeminterface.IPlatformFactory;
import com.baidu.duer.dcs.systeminterface.IWakeUp;
import com.baidu.duer.dcs.util.BlurUtil;
import com.baidu.duer.dcs.util.CommonUtil;
import com.baidu.duer.dcs.util.LogUtil;
import com.baidu.duer.dcs.util.NetWorkUtil;
import com.baidu.duer.dcs.util.ShadeAnim;
import com.baidu.duer.dcs.util.ShareUtil;
import com.baidu.duer.dcs.view.Iview.IDView;
import com.baidu.duer.dcs.view.Iview.OnChangePageListener;
import com.baidu.duer.dcs.wakeup.WakeUp;
import com.skyfishjy.library.RippleBackground;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Huangyong on 2018/1/14.
 */

public class ContentFragment extends Fragment implements View.OnClickListener, IDView {
    public static final String TAG = "DcsDemoActivity";
    private static final int MUSICINFO = 111;
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
    public String urls;//歌曲地址
    // 唤醒
    private WakeUp wakeUp;
    private volatile float vol;
    private boolean isEnablePlaying=false;
    private RippleBackground content;
    private LinearLayout control;
    private RippleBackground ripple;
    private RelativeLayout root;
    private MediaPlayer mediaPlayer;
    private static ContentFragment fragment;
    private View view;
    private Button user;
    private Button share;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MUSICINFO:
                    musicnames = msg.getData().getString("MUSICNAME");
                    musicname.setText(musicnames);
                    break;
            }
        }
    };
    private TextView leftTime;
    private TextView rightTime;
    private SeekBar seekBar;
    private RelativeLayout controlseek;
    private LinearLayout webroot;
    private TextView musicname;
    private RippleBackground alarmpop;
    private ImageView stoppic;
    private ObjectAnimator nope;
    private ObjectAnimator tada;
    private RotateAnimation animation;
    private ImageView downImg;
    private DownPresenter downPresenter;
    private String musicnames;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dcs_sample_activity_main,null);
        initView();
        initOauth();
        initFramework();
        return view;
    }

    public static ContentFragment getInstance(String con) {
        if (fragment!=null){
            return fragment;
        }else {
            fragment = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TYPE",con);
            fragment.setArguments(bundle);
            return fragment;
        }
    }



    private void initView() {
      /*  Button openLogBtn = (Button) findViewById(R.id.openLogBtn);
        openLogBtn.setOnClickListener(this);*/
        voiceButton = (ImageView) view.findViewById(R.id.voiceBtn);
        downImg = (ImageView) view.findViewById(R.id.down_img);
        content = (RippleBackground) view.findViewById(R.id.content);
        downPresenter = new DownPresenter(getContext(),this);
        leftTime = (TextView) view.findViewById(R.id.leftTime);
        rightTime = (TextView) view.findViewById(R.id.rightTime);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        controlseek = (RelativeLayout) view.findViewById(R.id.controlSeek);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cimage);
        seekBar.setMax(100);
        user = (Button) view.findViewById(R.id.user);
        share = (Button) view.findViewById(R.id.share);
        share.setOnClickListener(this);
        user.setOnClickListener(this);
        downImg.setOnClickListener(this);
        voiceButton.setOnClickListener(this);

        // textViewTimeStopListen = (TextView) findViewById(R.id.id_tv_time_0);
        textViewRenderVoiceInputText = (TextView) view.findViewById(R.id.id_tv_RenderVoiceInputText);
        mTopLinearLayout = (LinearLayout) view.findViewById(R.id.topLinearLayout);
        webroot = (LinearLayout) view.findViewById(R.id.webviewroot);
        webView = new BaseWebView(getContext().getApplicationContext());
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
            public void onPageFinished(WebView view, final String url) {
                webroot.setVisibility(View.VISIBLE);
                if (control.isShown()){
                    control.setVisibility(View.INVISIBLE);
                    controlseek.setVisibility(View.INVISIBLE);
                }
                if (!url.equals(mHtmlUrl) && !"about:blank".equals(mHtmlUrl)) {
                    platformFactory.getWebView().linkClicked(url);
                }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHtmlUrl = url;
                            Document doc = null;
                            try {
                                doc = Jsoup.connect(mHtmlUrl).get();
                                Elements newsHeadlines = doc.getElementsByClass("text-content text");
                                Log.d("ADADADACCCCCCC",newsHeadlines.text());
                                if (!TextUtils.isEmpty(newsHeadlines.text())){
                                    Message message = Message.obtain();
                                    Bundle bundle  = new Bundle();
                                    bundle.putString("MUSICNAME",newsHeadlines.text());
                                    message.setData(bundle);
                                    message.what = MUSICINFO;
                                    handler.sendMessage(message);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("ADADADA",doc.toString());
                        }
                    }).start();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            webroot.setVisibility(View.INVISIBLE);
                        }
                    },10000);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });
        mTopLinearLayout.addView(webView);

        Button mPreviousSongBtn = (Button) view.findViewById(R.id.previousSongBtn);
        alarmpop = (RippleBackground) view.findViewById(R.id.alarm);//闹钟动画
        stoppic = (ImageView) view.findViewById(R.id.alarmpic);//停止闹钟的按钮
        //停止闹钟的按钮
        root = (RelativeLayout) view.findViewById(R.id.root);
        ripple = (RippleBackground) view.findViewById(R.id.contentbottom);
        pauseOrPlayButton = (Button) view.findViewById(R.id.pauseOrPlayBtn);
        Button mNextSongBtn = (Button) view.findViewById(R.id.nextSongBtn);
        control = (LinearLayout) view.findViewById(R.id.songLinearLayout);
        musicname = (TextView) view.findViewById(R.id.musicname);
        mPreviousSongBtn.setOnClickListener(this);
        pauseOrPlayButton.setOnClickListener(this);
        mNextSongBtn.setOnClickListener(this);
        ripple.startRippleAnimation();
        stoppic.setOnClickListener(this);
        BlurUtil.setViewBg(getContext(),15,5,root,R.mipmap.userbg);
        initWakeUp();
        initAnimation();
    }

    private void initAnimation() {
        animation = new RotateAnimation(0,359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatMode(Animation.INFINITE);
        animation.setRepeatCount(-1);
        animation.setInterpolator(new LinearInterpolator()); // 设置插入器
        animation.setDuration(10000);
        animation.cancel();
    }

    public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 204;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }
    private void initWakeUp() {
        mediaPlayer = new MediaPlayer();
        try {
            // 打开指定音乐文件,获取assets目录下指定文件的AssetFileDescriptor对象
            AssetManager am = getActivity().getAssets();
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
        platformFactory = new PlatformFactoryImpl(getContext());
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
                            Toast.makeText(getContext(),
                                    getResources().getString(R.string.voice_err_msg),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        LogUtil.d(TAG, "onFailed-errorMessage:" + errorMessage);
                        stopRecording();
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.voice_err_msg),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        deviceModuleFactory.createAlertsDeviceModule();
        deviceModuleFactory.getAlertsDeviceModule().addAlertListener(new AlertsDeviceModule.IAlertListener() {
            @Override
            public void onAlertStarted(String alertToken) {
                alarmpop.setVisibility(View.VISIBLE);
                alarmpop.startRippleAnimation();
                if (webroot.isShown()){
                    webroot.setVisibility(View.INVISIBLE);
                }
                nope = ShadeAnim.nope(stoppic);
                nope.setRepeatCount(ValueAnimator.INFINITE);
                nope.start();
                tada = ShadeAnim.tada(stoppic);
                tada.setRepeatCount(ValueAnimator.INFINITE);
                tada.start();
            }
        });

        deviceModuleFactory.createAudioPlayerDeviceModule();
        deviceModuleFactory.getAudioPlayerDeviceModule().addAudioPlayListener(
                new IMediaPlayer.SimpleMediaPlayerListener() {

                    @Override
                    public void onPaused() {
                        super.onPaused();
                        // pauseOrPlayButton.setText(getResources().getString(R.string.audio_paused));
                        pauseOrPlayButton.setBackgroundResource(R.drawable.playbg);
                        isPause = true;
                        animation.cancel();
                    }

                    @Override
                    public void onPlaying() {
                        super.onPlaying();
                        pauseOrPlayButton.setBackgroundResource(R.drawable.pausebg);
                        // pauseOrPlayButton.setText(getResources().getString(R.string.audio_playing));
                        isPause = false;
                        if (!controlseek.isShown()){
                            controlseek.setVisibility(View.VISIBLE);
                            control.setVisibility(View.VISIBLE);
                        }
                        animation.start();
                    }

                    @Override
                    public void onCompletion() {
                        super.onCompletion();
                        // pauseOrPlayButton.setText(getResources().getString(R.string.audio_default));
                        isPause = false;
                        seekBar.setProgress(100);
                        animation.cancel();
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                        // pauseOrPlayButton.setText(getResources().getString(R.string.audio_default));
                        isPause = true;
                        animation.cancel();
                    }

                    @Override
                    public void onUpdateProgress(int percent) {
                        super.onUpdateProgress(percent);
                        Log.d("Progress","当前播放进度"+percent);
                        seekBar.setProgress(percent);
                    }

                    @Override
                    public void onDuration(long milliseconds) {
                        super.onDuration(milliseconds);
                        Log.d("Progress","播放总长度"+milliseconds);
                        SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
                        Date date = new Date(milliseconds);
                        String format = sdf.format(date);
                        rightTime.setText(format);

                    }

                    @Override
                    public void onPrepared(String url) {
                        super.onPrepared(url);
                        urls = url;
                        isEnablePlaying=true;
                        control.setVisibility(View.VISIBLE);
                        controlseek.setVisibility(View.VISIBLE);
                        if (webroot.isShown()){
                            webroot.setVisibility(View.INVISIBLE);
                        }
                        Log.d("当前下载地址是：",urls);
                    }

                    @Override
                    public void onRelease() {
                        super.onRelease();
                        control.setVisibility(View.INVISIBLE);
                        controlseek.setVisibility(View.INVISIBLE);
                        animation.cancel();
                    }

                    @Override
                    public void onUpdateMills(long millis) {
                        super.onUpdateMills(millis);
                        SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
                        Date date = new Date(millis);
                        String format = sdf.format(date);
                        leftTime.setText(format);
                    }


                    @Override
                    public void onBufferingUpdate(int percent) {
                        super.onBufferingUpdate(percent);
                        seekBar.setSecondaryProgress(percent);
                    }

                    @Override
                    public void onBufferingEnd() {
                        super.onBufferingEnd();
                        seekBar.setSecondaryProgress(100);
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
                if (!NetWorkUtil.isNetworkConnected(getContext())) {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.err_net_msg),
                            Toast.LENGTH_SHORT).show();
                    wakeUp.startWakeUp();
                    return;
                }
                if (CommonUtil.isFastDoubleClick()) {
                    return;
                }
                if (TextUtils.isEmpty(HttpConfig.getAccessToken())) {
                    startActivity(new Intent(getContext(), DcsSampleOAuthActivity.class));
                    getActivity().finish();
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
                pauseButtonClick();
                break;
            case R.id.user:
                if (listener!=null){
                    listener.switchPage(0);
                }
                break;
            case R.id.share:
                ShareUtil.share(getContext(),"发现一个很好玩的APP，下载地址：https://github.com/hiliving/doueros-android/raw/master/images/app-release.apk");
                break;
            case R.id.alarmpic:
                if (nope!=null&&nope.isRunning()){
                    nope.cancel();
                }
                if (tada!=null&&tada.isRunning()){
                    tada.cancel();
                }
                alarmpop.stopRippleAnimation();
                alarmpop.setVisibility(View.INVISIBLE);
                deviceModuleFactory.getAlertsDeviceModule().stopActiveAlert();
                break;
            case R.id.down_img:
                downPresenter.downLoadFile(urls,musicnames);
                break;
            default:
                break;
        }
    }

    private void pauseButtonClick() {
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
            Toast.makeText(getContext(), "当前播放队列为空，请先说出你想听的歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    private IResponseListener playPauseResponseListener = new IResponseListener() {
        @Override
        public void onSucceed(int statusCode) {
            if (statusCode == 204) {
                Toast.makeText(getContext(),
                        getResources().getString(R.string.no_directive),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onFailed(String errorMessage) {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.request_error),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    };

    private IResponseListener nextPreResponseListener = new IResponseListener() {
        @Override
        public void onSucceed(int statusCode) {
            if (statusCode == 204) {
                Toast.makeText(getContext(),
                        getResources().getString(R.string.no_audio),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onFailed(String errorMessage) {
            Toast.makeText(getContext(),
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
            Toast.makeText(getContext(),
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
    public void onDestroy() {
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
    private OnChangePageListener listener;
    public void setOnChangePageListener(OnChangePageListener listener){
        this.listener = listener;
    }

    @Override
    public void downSuccess(String path) {
        Log.d("当前下载完成：",path);
        Snackbar snackbar = Snackbar.make(root,path+"下载完成!",Toast.LENGTH_SHORT);
        View mView = snackbar.getView();
        mView.setBackgroundColor(getResources().getColor(R.color.alptha_snack));
        snackbar.show();
    }

    @Override
    public void downFail(String msg) {
        Log.d("当前下载失败：",msg);
        Snackbar snackbar = Snackbar.make(root,msg,Toast.LENGTH_SHORT);
        View mView = snackbar.getView();
        mView.setBackgroundColor(getResources().getColor(R.color.alptha_snack));
        snackbar.show();
    }

    @Override
    public void showprogress(long total, long current) {
        Log.d("当前下载进度：","*****"+(int)(current*1.0f/total*100.0f));
    }

    @Override
    public void flushName() {
        Snackbar snackbar = Snackbar.make(root,"歌曲名未知，请按暂停键再按播放，以获取歌曲名",Toast.LENGTH_SHORT);
        View mView = snackbar.getView();
        mView.setBackgroundColor(getResources().getColor(R.color.alptha_snack));
        snackbar.show();
    }

    @Override
    public void startDownload() {

    }
}
