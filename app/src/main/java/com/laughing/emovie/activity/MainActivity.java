package com.laughing.emovie.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laughing.emovie.R;
import com.laughing.emovie.adapter.MyFragmentAdapter;
import com.laughing.emovie.fragment.NowPlayFragment;
import com.laughing.emovie.fragment.TopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wy on 2018/11/8.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private NowPlayFragment nowPlayFragment;
    private MyFragmentAdapter myFragmentAdapter;
    private TopFragment topFragment;
    private List<Fragment> mList;
    private TextView tv_line, tv_now, tv_top;
    private ImageView iv_test;

    private int lineWidth = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_now:
                tv_now.setTextColor(Color.parseColor("#ffffff"));
                tv_top.setTextColor(Color.parseColor("#cccccc"));
                break;
            case R.id.tv_top:
                tv_now.setTextColor(Color.parseColor("#cccccc"));
                tv_top.setTextColor(Color.parseColor("#ffffff"));
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        lineWidth = tv_now.getWidth();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(lineWidth - 135, 5);
        params.leftMargin = 60;
        params.rightMargin = 60;
        tv_line.setLayoutParams(params);
    }

    private void initViewPager() {
        tv_line = (TextView) findViewById(R.id.tv_line);
        tv_now = (TextView) findViewById(R.id.tv_now);
        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_now.setOnClickListener(this);
        tv_top.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.vp);
        nowPlayFragment = new NowPlayFragment();
        topFragment = new TopFragment();

        mList = new ArrayList<>();
        mList.add(nowPlayFragment);
        mList.add(topFragment);

        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(myFragmentAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                float tagerX = position * lineWidth + positionOffsetPixels / mList.size();
                tv_line.animate().translationX(tagerX)
                        .setDuration(0);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tv_now.setTextColor(Color.parseColor("#ffffff"));
                        tv_top.setTextColor(Color.parseColor("#cccccc"));
                        mViewPager.setCurrentItem(0, true);
                        break;
                    case 1:
                        tv_now.setTextColor(Color.parseColor("#cccccc"));
                        tv_top.setTextColor(Color.parseColor("#ffffff"));
                        mViewPager.setCurrentItem(1, true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        iv_test = (ImageView) findViewById(R.id.iv_test);
        Animation scaleAnimation;
        scaleAnimation = new ScaleAnimation(1, 1.3f, 1, 1.3f);
        scaleAnimation.setDuration(20000);
        scaleAnimation.setRepeatCount(Integer.MAX_VALUE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        iv_test.startAnimation(scaleAnimation);

    }


}

