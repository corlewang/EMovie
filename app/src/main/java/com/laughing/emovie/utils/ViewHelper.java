package com.laughing.emovie.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 视图帮助类
 */
@SuppressLint("NewApi")
public class ViewHelper {

    public static int[] getScreenWH(Context context) {

        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        manager.getDefaultDisplay().getMetrics(displayMetrics);

        return new int[]{displayMetrics.widthPixels,
                displayMetrics.heightPixels};

    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        return result;
    }

    /**
     * 更改View的高度(按屏幕宽度比例)
     *
     * @param v           视图对象
     * @param ratioHeight 缩放比例
     */
    public static void setHeight(Context context, View v, float ratioHeight) {
        LayoutParams lp = v.getLayoutParams();
        int height = (int) (context.getResources().getDisplayMetrics().widthPixels * ratioHeight);
        lp.height = height;
        v.setLayoutParams(lp);
    }

    /**
     * 更改View的宽度(按屏幕宽度比例)
     *
     * @param v          视图对象
     * @param ratioWidth 缩放比例
     */
    public static void setWidth(Context context, View v, float ratioWidth) {
        LayoutParams lp = v.getLayoutParams();
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * ratioWidth);
        lp.width = width;
        v.setLayoutParams(lp);
    }

    /**
     * 关闭键盘
     *
     * @param context
     * @param et
     */
    public static void hideKeyboard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param et
     */
    public static void showKeyBoard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);
    }

    /**
     * 显示键盘
     *
     * @param context
     */
    public static void showKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕尺寸(像素)
     *
     * @param activity
     * @return
     */
    public static Point getWindowSize(Activity activity) {
        Display disp = activity.getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        return outP;
    }

    /**
     * 获取应用区域尺寸(像素)
     *
     * @param activity
     * @return
     */
    public static Rect getAppScreenSize(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(outRect);
        return outRect;
    }

    /**
     * 获取可绘制区域尺寸(像素)
     *
     * @param activity
     * @return
     */
    public static Rect getAppShowSize(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT)
                .getDrawingRect(outRect);
        return outRect;
    }

    /**
     * return a bitmap from service
     *
     * @param url
     * @return bitmap type
     */
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }
}
