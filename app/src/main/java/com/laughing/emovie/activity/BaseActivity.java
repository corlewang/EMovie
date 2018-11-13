package com.laughing.emovie.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;


/**
 * Created by Wy on 2018/11/9.
 */

public abstract class BaseActivity extends AppCompatActivity {


    public Context context;

    protected abstract int getLayoutId();

    protected void initView(Bundle savedInstanceState) {
    }

    protected void initData() {
    }

    protected <T extends View> T find(int id) {
        return (T) super.findViewById(id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ImmersionBar.with(this).init();
        setContentView(getLayoutId());
        initView(savedInstanceState);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }


}

