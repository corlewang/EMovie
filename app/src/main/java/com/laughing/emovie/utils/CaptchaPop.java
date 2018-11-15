package com.laughing.emovie.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.laughing.emovie.R;
import com.laughing.emovie.widget.SwipeCaptchaView;

import java.util.Random;


public class CaptchaPop extends PopupWindow implements OnClickListener {

    private Activity context;
    private View view;
    private int[] drawableID = {R.mipmap.login_pic_01, R.mipmap.login_pic_02,
            R.mipmap.login_pic_03, R.mipmap.login_pic_04, R.mipmap.login_pic_05};

    private SwipeCaptchaView mSwipeCaptchaView;
    private SeekBar mSeekBar;
    private int index;
    private ImageView iv_fresh;
    private TextView tv_status;

    public CaptchaPop(Activity context) {
        super();
        this.context = context;
        init();
    }

    private SwipeCaptchaView.OnCaptchaMatchCallback captchaCallBack;

    public void setCaptchaCallBack(SwipeCaptchaView.OnCaptchaMatchCallback captchaCallBack) {
        this.captchaCallBack = captchaCallBack;
    }

    private void init() {
        // TODO Auto-generated method stub
        view = LayoutInflater.from(context).inflate(R.layout.pop_caotcha, null);
//        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
//        tv_cancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        tv_ok = (TextView) view.findViewById(R.id.tv_ok);

//        if (onClickListener == null) {
//            tv_cancel.setOnClickListener(this);
//            tv_ok.setOnClickListener(this);
//        } else {
//            tv_cancel.setOnClickListener(onClickListener);
//            tv_ok.setOnClickListener(onClickListener);
//        }
        iv_fresh = (ImageView) view.findViewById(R.id.iv_fresh);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        mSwipeCaptchaView = (SwipeCaptchaView) view.findViewById(R.id.swipeCaptchaView);
        mSeekBar = (SeekBar) view.findViewById(R.id.dragBar);
        index = new Random().nextInt(drawableID.length - 1);
        mSwipeCaptchaView.setOnCaptchaMatchCallback(
                new SwipeCaptchaView.OnCaptchaMatchCallback() {
                    @Override
                    public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                        dismiss();
                        mSeekBar.setEnabled(false);
                        captchaCallBack.matchSuccess(swipeCaptchaView);
                    }

                    @Override
                    public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                        index = new Random().nextInt(drawableID.length - 1);
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID[index]);
                        mSwipeCaptchaView.setImageBitmap(bitmap);
                        mSwipeCaptchaView.createCaptcha();
                        mSeekBar.setProgress(0);
                        captchaCallBack.matchFailed(swipeCaptchaView);
                    }
                });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSwipeCaptchaView.matchCaptcha();
            }
        });
        //从网络加载图片也ok
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID[index]);
                mSwipeCaptchaView.setImageBitmap(bitmap);
                mSwipeCaptchaView.createCaptcha();
            }
        }, 300);

        iv_fresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                index = new Random().nextInt(drawableID.length - 1);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID[index]);
                mSwipeCaptchaView.setImageBitmap(bitmap);
                mSwipeCaptchaView.createCaptcha();
                mSeekBar.setProgress(0);
            }
        });

        setContentView(view);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        //        setAnimationStyle(R.style.AnimBottom);
//        setBackgroundDrawable(new ColorDrawable(0x00000000));

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dataInit();
    }

    private void dataInit() {
        // TODO Auto-generated method stub
//        if (model.getBtnEnterText() != null && !model.getBtnEnterText().isEmpty()) {
//            tv_ok.setText(model.getBtnEnterText());
//        }
//        if (model.getBtnCancelText() != null) {
//            tv_cancel.setText(model.getBtnCancelText());
//        }
//
//        updata_content.setText(model.getInfo());
//        updata_title.setText(model.getTitle());
//        if (model.getRemark() == null || model.getRemark().equals("")) {
//            updata_remark.setVisibility(View.GONE);
//        } else {
//            updata_remark.setVisibility(View.VISIBLE);
//            updata_remark.setText(model.getRemark());
//        }
//        if (model.getUpdate().equals("1")) {
//            setBackgroundDrawable(new ColorDrawable(0x4f000000));
//        } else if (model.getUpdate().equals("2")) {
//            tv_cancel.setVisibility(View.GONE);
//            setBackgroundDrawable(null);
//        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
//            case R.id.tv_cancel:
//                dismiss();
//                break;
//            case R.id.tv_ok:
//                Intent intent = new Intent(context, DmhcBourseMainService.class);
//                intent.putExtra(ConstsObject.SERVICE_KEY,
//                        ConstsObject.SERVICE_DOWNLOAD_UPDATA);
//                intent.putExtra("downloadurl", model.getUrl());
//                intent.putExtra("versionName", model.getNew_version());
//                context.startService(intent);
//                break;
            default:
                break;
        }

    }

}
