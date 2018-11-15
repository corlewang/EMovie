package com.laughing.emovie.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.laughing.emovie.R;
import com.laughing.emovie.adapter.MyFragmentAdapter;
import com.laughing.emovie.fragment.NowPlayFragment;
import com.laughing.emovie.utils.ToastUtils;
import com.laughing.emovie.utils.ViewHelper;
import com.laughing.emovie.widget.CaptchaPop;
import com.laughing.emovie.widget.SwipeCaptchaView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wy on 2018/11/8.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private NowPlayFragment nowPlayFragment, comingFragment, topFragment;
    private MyFragmentAdapter myFragmentAdapter;
    private List<Fragment> mList;
    private TextView tv_line, tv_now, tv_coming, tv_top;
    private ImageView iv_test;
    private int stepIndex;

    private int lineWidth = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_now:
                tv_now.setTextColor(Color.parseColor("#ffffff"));
                tv_coming.setTextColor(Color.parseColor("#cccccc"));
                tv_top.setTextColor(Color.parseColor("#cccccc"));
                mViewPager.setCurrentItem(0, false);
                tv_line.animate().translationX(0).setDuration(0);
                break;
            case R.id.tv_coming:
                tv_now.setTextColor(Color.parseColor("#cccccc"));
                tv_coming.setTextColor(Color.parseColor("#ffffff"));
                tv_top.setTextColor(Color.parseColor("#cccccc"));
                mViewPager.setCurrentItem(1, false);
                tv_line.animate().translationX(stepIndex).setDuration(0);
                break;
            case R.id.tv_top:
                tv_now.setTextColor(Color.parseColor("#cccccc"));
                tv_coming.setTextColor(Color.parseColor("#cccccc"));
                tv_top.setTextColor(Color.parseColor("#ffffff"));
                mViewPager.setCurrentItem(2, false);
                tv_line.animate().translationX(stepIndex * 2).setDuration(0);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initViewPager();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        lineWidth = tv_now.getWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(lineWidth - ViewHelper.dip2px(context, 46f), 5);
        params.leftMargin = ViewHelper.dip2px(context, 25);
        params.rightMargin = ViewHelper.dip2px(context, 25);
        tv_line.setLayoutParams(params);
        stepIndex = getResources().getDisplayMetrics()
                .widthPixels / mList.size();
    }

    private void initViewPager() {
        tv_line = find(R.id.tv_line);
        tv_now = find(R.id.tv_now);
        tv_coming = find(R.id.tv_coming);
        tv_top = find(R.id.tv_top);
        tv_now.setOnClickListener(this);
        tv_coming.setOnClickListener(this);
        tv_top.setOnClickListener(this);

        iv_test = find(R.id.iv_test);

        Glide.with(this).load(R.mipmap.ad2).animate(anim()).into(iv_test);
        mViewPager = find(R.id.vp);
        nowPlayFragment = new NowPlayFragment();
        comingFragment = new NowPlayFragment();
        topFragment = new NowPlayFragment();

        Bundle bundle = new Bundle();
        bundle.putString("url", "https://movie.douban.com/cinema/nowplaying/shenzhen/");
        bundle.putString("key", "nowplaying");
        nowPlayFragment.setArguments(bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putString("url", "https://movie.douban.com/cinema/nowplaying/shenzhen/");
        bundle2.putString("key", "upcoming");
        comingFragment.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putString("url", "https://movie.douban.com/top250");
        bundle3.putBoolean("more", true);
        topFragment.setArguments(bundle3);

//        "https://api.douban.com/v2/movie/in_theaters"
//        "https://api.douban.com/v2/movie/top250"
//        topFragment = new TopFragment();

        mList = new ArrayList<>();
        mList.add(nowPlayFragment);
        mList.add(comingFragment);
        mList.add(topFragment);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CaptchaPop captchaPop = new CaptchaPop(MainActivity.this);
                captchaPop.showAtLocation(iv_test, Gravity.CENTER, 0, 0);
                captchaPop.setCaptchaCallBack(new SwipeCaptchaView.OnCaptchaMatchCallback() {
                    @Override
                    public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                        Message msg = new Message();
                        msg.obj = "验证成功！";
                        mHandler.sendMessage(msg);

                        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), mList);
                        mViewPager.setAdapter(myFragmentAdapter);
                    }

                    @Override
                    public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                        Message msg = new Message();
                        msg.obj = "验证失败！";
                        mHandler.sendMessage(msg);
                    }
                });
            }
        }, 1000);

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
                        tv_coming.setTextColor(Color.parseColor("#cccccc"));
                        tv_top.setTextColor(Color.parseColor("#cccccc"));
                        mViewPager.setCurrentItem(0, false);
                        select(0);
                        break;
                    case 1:
                        tv_now.setTextColor(Color.parseColor("#cccccc"));
                        tv_coming.setTextColor(Color.parseColor("#ffffff"));
                        tv_top.setTextColor(Color.parseColor("#cccccc"));
                        mViewPager.setCurrentItem(1, false);
                        select(1);
                        break;
                    case 2:
                        tv_now.setTextColor(Color.parseColor("#cccccc"));
                        tv_coming.setTextColor(Color.parseColor("#cccccc"));
                        tv_top.setTextColor(Color.parseColor("#ffffff"));
                        mViewPager.setCurrentItem(2, false);
                        select(2);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtils.show(context, msg.obj.toString());
        }
    };

    private void select(int index) {
        if (index == 0) {
            iv_test.setImageResource(R.mipmap.ad2);
            iv_test.setAnimation(anim());
        } else if (index == 1) {
            iv_test.setImageResource(R.mipmap.ad1);
            iv_test.setAnimation(anim());
        } else {
            iv_test.setImageResource(R.mipmap.ad3);
            iv_test.setAnimation(anim());
        }
    }

    private Animation anim() {
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(1000);
        mShowAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.3f, 1, 1.3f);
        scaleAnimation.setDuration(20000);
        scaleAnimation.setRepeatCount(Integer.MAX_VALUE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);

        animationSet.addAnimation(mShowAnimation);
        animationSet.addAnimation(scaleAnimation);
        return animationSet;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        exitApp();
    }

    /**
     * 记录返回键按下时间
     */
    private long exitTime = 0;

    /**
     * 返回键双击退出APP
     */
    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.show(context, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}

