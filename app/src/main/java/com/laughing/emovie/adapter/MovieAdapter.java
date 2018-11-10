package com.laughing.emovie.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.laughing.emovie.R;
import com.laughing.emovie.entity.NowPlayEntity;
import com.laughing.emovie.utils.ImageOption;

import org.xutils.x;

/**
 * Created by Wy on 2018/11/8.
 */

public class MovieAdapter extends BaseQuickAdapter<NowPlayEntity.SubjectsBean, BaseViewHolder> {

    public MovieAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NowPlayEntity.SubjectsBean item) {
        ImageView iv_img = helper.getView(R.id.iv_img);
        TextView tv_name = helper.getView(R.id.tv_name);
        RatingBar ratingBar = helper.getView(R.id.ratingBar);
        TextView tv_rating = helper.getView(R.id.tv_rating);
        TextView tv_dy = helper.getView(R.id.tv_dy);
        TextView tv_alt = helper.getView(R.id.tv_alt);

        x.image().bind(iv_img, item.getImages().getLarge(), ImageOption.imageOptions());
        tv_name.setText(item.getTitle());

        StringBuilder director = new StringBuilder();
        for (int i = 0; i < item.getDirectors().size(); i++) {
            if (i < item.getDirectors().size() - 1)
                director.append(item.getDirectors().get(i).getName() + "/");
            else
                director.append(item.getDirectors().get(i).getName());
        }
        tv_dy.setText(director);

        StringBuilder avatars = new StringBuilder();
        for (int i = 0; i < item.getCasts().size(); i++) {
            if (i < item.getCasts().size() - 1)
                avatars.append(item.getCasts().get(i).getName() + "/");
            else {
                avatars.append(item.getCasts().get(i).getName());
            }
        }
        ratingBar.setRating(item.getRating().getAverage() / 2);
        tv_rating.setText(item.getRating().getAverage() + "");

        tv_alt.setText(avatars.toString());
    }
}

