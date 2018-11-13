package com.laughing.emovie.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.laughing.emovie.R;
import com.laughing.emovie.entity.MovieDetail;
import com.laughing.emovie.entity.NowPlayEntity;
import com.laughing.emovie.fragment.NowPlayFragment;
import com.laughing.emovie.widget.LoadingDialog;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Wy on 2018/11/13.
 */

public class MovieDetailActivity extends BaseActivity implements View.OnClickListener {

    private String id;
    private NiceVideoPlayer mNiceVideoPlayer;
    private TextView iv_name, tv_type, tv_date, tv_duc, tv_rating, tv_comm, tv_info;
    private RatingBar ratingBar;
    private ProgressBar progressBar;

    private MovieDetail movieDetail;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.toolbar).init();
        id = getIntent().getStringExtra("id");
        iv_name = find(R.id.iv_name);
        tv_type = find(R.id.tv_type);
        tv_date = find(R.id.tv_date);
        tv_duc = find(R.id.tv_duc);
        tv_rating = find(R.id.tv_rating);
        tv_comm = find(R.id.tv_comm);
        tv_info = find(R.id.tv_info);
        ratingBar = find(R.id.ratingBar);
        progressBar = find(R.id.progressBar);

        find(R.id.iv_back).setOnClickListener(this);
        mNiceVideoPlayer = find(R.id.nice_video_player);
        movieDetail = new MovieDetail();

        LoadingDialog.getInstance(context).setMessage("加载中").show();
        new GetDataThd().start();
    }

    //开启关闭小窗口播放按钮
    public void enterTinyWindow(View view) {
        if (mNiceVideoPlayer.isIdle()) {
            Toast.makeText(this, "要点击播放后才能进入小窗口", Toast.LENGTH_SHORT).show();
        } else {
            mNiceVideoPlayer.enterTinyWindow();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }

    private class GetDataThd extends Thread {

        @Override
        public void run() {
            super.run();
            getData();
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        try {
            Document document = Jsoup.connect
                    ("https://movie.douban.com/subject/" + id).get();

            Element element1 = document.getElementById("content");
            Element element2 = element1.getElementById("info");
            Elements elements = element2.select("span");

            StringBuilder str = new StringBuilder();
            StringBuilder str2 = new StringBuilder();
            StringBuilder str3 = new StringBuilder();
            for (Element element : elements) {
                if (element.attr("property").equals("v:genre")) {
                    String type = element.text();
                    str.append(type + "/");
                }

                if (element.attr("property").equals("v:initialReleaseDate")) {
                    String date = element.attr("content");
                    str2.append(date + "/");
                }

                if (element.attr("property").equals("v:runtime")) {
                    str3.append(element.text() + "/");
                }
            }

            str.substring(0, str.length() - 1);
            movieDetail.setType(str.toString());

            str2.substring(0, str2.length() - 1);
            movieDetail.setDate(str2.toString());

            str3.substring(0, str3.length() - 1);
            movieDetail.setDuc(str3.toString());

            Element element = element1.getElementById("interest_sectl");
            Elements element4 = element.select("strong");
            Elements element5 = element.getElementsByClass("rating_people").select("span");

            for (Element ele : element4) {
                if (ele.attr("property").equals("v:average")) {
                    movieDetail.setRating(ele.text());
                }
            }

            for (Element ele : element5) {
                if (ele.attr("property").equals("v:votes")) {
                    movieDetail.setComm(ele.text() + "人评价");
                }
            }
            Elements element3 = element1.getElementsByClass("related-info").select("span");
            for (Element ele : element3) {
                if (ele.attr("property").equals("v:summary")) {
                    movieDetail.setInfo(ele.text());
                }
            }

            Element element6 = element1.getElementById("related-pic");
            String eleImg = element6.getElementsByClass("related-pic-video").attr("style");
            movieDetail.setVideoImg(eleImg);

            String eleVideo = element6.getElementsByClass("related-pic-video").attr("href");
            movieDetail.setVideoContent(eleVideo);
            Document document2 = Jsoup.connect
                    (eleVideo).get();
            Element element7 = document2.getElementById("movie_player");
            String elements3 = element7.select("video").select("source").attr("src");

            movieDetail.setVideoUrl(elements3);
            mHandler2.sendEmptyMessage(2);

            mHandler.sendEmptyMessageDelayed(1, 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoadingDialog.dismissDialog();
        }
    };

    private Handler mHandler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressBar.setVisibility(View.GONE);
            mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
            mNiceVideoPlayer.setUp(movieDetail.getVideoUrl(), null);
            TxVideoPlayerController controller = new TxVideoPlayerController(context);
            controller.setTitle("");
            controller.setLenght(98000);
            Glide.with(context)
                    .load(movieDetail.getVideoImg())
//                    .placeholder(R.drawable.img_default)
                    .crossFade()
                    .into(controller.imageView());
            mNiceVideoPlayer.setController(controller);

        }
    };

}