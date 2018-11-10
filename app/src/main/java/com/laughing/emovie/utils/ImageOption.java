package com.laughing.emovie.utils;

import android.widget.ImageView;

import com.laughing.emovie.R;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

/**
 * Created by Wy on 2018/11/9.
 */

public class ImageOption {

    public static ImageOptions imageOptions() {
        ImageOptions imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
//                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                .setLoadingDrawableId(R.mipmap.ic_launcher)
//                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();
        return imageOptions;
    }

}

