package com.laughing.emovie.adapter;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.laughing.emovie.R;
import com.laughing.emovie.entity.NowPlayEntity;

/**
 * Created by Wy on 2018/11/8.
 */

public class MovieAdapter extends BaseQuickAdapter<NowPlayEntity, BaseViewHolder> {

    public MovieAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NowPlayEntity item) {
        ImageView iv_img = helper.getView(R.id.iv_img);
        TextView tv_name = helper.getView(R.id.tv_name);
        RatingBar ratingBar = helper.getView(R.id.ratingBar);
        TextView tv_rating = helper.getView(R.id.tv_rating);
        TextView tv_dy = helper.getView(R.id.tv_dy);
        TextView tv_alt = helper.getView(R.id.tv_alt);

        Glide.with(mContext).load(item.getImages()).into(iv_img);
        tv_name.setText(item.getTitle());
        tv_dy.setText(item.getDirectors());

        if (!TextUtils.isEmpty(item.getRating())) {
            ratingBar.setRating(Float.valueOf(item.getRating()) / 2);
            tv_rating.setText(item.getRating() + "");
        }
        tv_alt.setText(item.getCasts());
    }
}

