package com.baidu.duer.dcs.view;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.dcs.okhttp3.Call;
import com.baidu.dcs.okhttp3.Callback;
import com.baidu.dcs.okhttp3.OkHttpClient;
import com.baidu.dcs.okhttp3.Request;
import com.baidu.dcs.okhttp3.Response;
import com.baidu.duer.dcs.http.intercepter.LoggingInterceptor;
import com.baidu.duer.dcs.view.Iview.IDView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.baidu.duer.dcs.http.DcsHttpManager.DEFAULT_MILLISECONDS;

/**
 * Created by Huangyong on 2018/1/27.
 */

public class DownPresenter {
    private Context context;
    private IDView idView;
    private OkHttpClient mOkHttpClient;
    private String nametoken ="default";
    private String newtoken ="default";
    private File file;
    private String musicname;

    public DownPresenter(Context context, IDView idView) {
        this.context = context;
        this.idView = idView;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggingInterceptor());
        mOkHttpClient = builder.build();
    }

    /**
     * 下载文件
     * @param fileUrl 文件url
     * @param name 存储目标目录
     */
    public void downLoadFile(String fileUrl, final String name) {
        File dwonpath = new File("sdcard/DcsMusic/");
        if (!dwonpath.exists()){
            dwonpath.mkdir();
        }
        //相同则是一个歌曲，否则是下一曲
        if (newtoken.equals(fileUrl)){
            idView.downFail("此文件已在下载列表");
            return;
        }else {

            if (nametoken.equals(name)){
                //musicname = "next"+System.currentTimeMillis();
                idView.flushName();
                return;
            }else {
                musicname = name;
            }
            file = new File("sdcard/DcsMusic/", musicname+".mp3");
            if (file.exists()){
                idView.downFail("此文件已在下载列表");
                return;
            }
        }
        nametoken = name;
        newtoken = fileUrl;//目前存在的问题是，没办法获取播放队列里所有歌曲的歌名，只能暂时用个标记来判断是不是新歌

        idView.downFail("已加入下载队列");
        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    idView.startDownload();
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        Log.e(TAG, "current------>" + current);
                        idView.showprogress(total,current);
                    }
                    fos.flush();
                    idView.downSuccess(file.getName());
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    idView.downFail("IO异常");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }
}
