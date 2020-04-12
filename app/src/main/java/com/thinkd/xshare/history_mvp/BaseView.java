package com.thinkd.xshare.history_mvp;

import android.view.View;

/**
 * Created by altman29 on 2017/10/18.
 * e-mial:s1yuan_chen@163.com
 */

public interface BaseView<T> {

    /**
     * set the presenter of mvp
     *
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * init the views of ui
     *
     * @param view
     */
    void initViews(View view);
}
