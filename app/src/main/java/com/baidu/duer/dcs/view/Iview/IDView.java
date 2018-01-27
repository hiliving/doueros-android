package com.baidu.duer.dcs.view.Iview;

/**
 * Created by Huangyong on 2018/1/27.
 */

public interface IDView {
    void downSuccess(String path);
    void downFail(String msg);

    void showprogress(long total, long current);

    void flushName();

    void startDownload();
}
