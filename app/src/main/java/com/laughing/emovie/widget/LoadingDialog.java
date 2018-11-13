package com.laughing.emovie.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.laughing.emovie.R;


/**
 * @version V1.0
 * @Description: 加载中Dialog
 * @date 2017-2-25
 */
public class LoadingDialog extends Dialog {

    /**
     * 上下文
     */
    private Context context;
    /**
     * 视图
     */
    private View view;
    /**
     * 显示文本
     */
    private TextView tvMessage;

    public static LoadingDialog instance;

    public static void dismissDialog() {
        if (instance != null && instance.isShowing()) {
            try {
                instance.dismiss();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public static LoadingDialog getInstance(Context context) {

        if (instance != null && instance.isShowing()) {
            instance.dismiss();
            instance = null;
        }
        instance = new LoadingDialog(context);
        return instance;
    }

    public LoadingDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.context = context;
        view = getLayoutInflater().inflate(R.layout.dialog_progress, null);
        tvMessage = (TextView) view.findViewById(R.id.textview_message);
        this.setContentView(view);
    }

    public LoadingDialog setMessage(String message) {
        tvMessage.setText(message);
        return instance;
    }

    @Override
    public void show() {
        if (instance != null && context != null) {
            super.show();
        } else {
            instance = null;

        }
    }

}
