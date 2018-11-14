package com.laughing.emovie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.laughing.emovie.R;
import com.laughing.emovie.activity.MovieDetailActivity;
import com.laughing.emovie.adapter.MovieAdapter;
import com.laughing.emovie.entity.NowPlayEntity;
import com.laughing.emovie.widget.LoadingDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by florentchampigny on 24/04/15.
 */
public class NowPlayFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private String key, requestUrl;
    private List<NowPlayEntity> mList;
    private SwipeRefreshLayout srl_content;
    private boolean loadMore;
    //是否是刷新操作
    private boolean fresh;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mRecyclerView = find(R.id.recyclerView);
        srl_content = find(R.id.srl_content);
        mList = new ArrayList<>();
        movieAdapter = new MovieAdapter(R.layout.list_item_card_small);
        final LinearLayoutManager manager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(movieAdapter);

        loadMore = getArguments().getBoolean("more");

        movieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent().setClass(getActivity(), MovieDetailActivity.class)
                        .putExtra("id", movieAdapter.getData().get(position).getId())
                        .putExtra("type", loadMore));
            }
        });
        if (loadMore) {
            srl_content.setEnabled(true);
            srl_content.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fresh = true;
                    new GetDataThd().start();
                }
            });

            movieAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    new GetDataThd().start();
                }
            });

//            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        int lastVisiblePosition = manager.findLastVisibleItemPosition();
//                        if (lastVisiblePosition >= manager.getItemCount() - 1) {
//
//                        }
//                    }
//                }
//            });
        } else {
            srl_content.setEnabled(false);
        }


        if (!loadMore) {
            LoadingDialog.getInstance(getActivity()).setMessage("加载中").show();
        } else {
            srl_content.setRefreshing(true);
        }
        new GetDataThd().start();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private class GetDataThd extends Thread {
        @Override
        public void run() {
            super.run();
            if (!loadMore) {
                getData();
            } else {
                getTopData();
            }
        }

    }

    /**
     * 获取数据
     */
    private void getData() {
        try {
            Bundle bundle = getArguments();
            requestUrl = bundle.getString("url");
            key = bundle.getString("key");
            if (TextUtils.isEmpty(key))
                return;

            Document document = Jsoup.connect(requestUrl).get();
            Element element1 = document.getElementById(key);
            Elements elements = element1.getElementsByClass
                    ("mod-bd");
            for (Element e : elements) {
                Elements titles = e.getElementsByClass("lists");
                for (Element title : titles) {
                    //查找 h4 标签下的 a 标签 中的 href 属性
                    Elements list = title.select("li");
                    for (Element element : list) {
                        NowPlayEntity nowPlayEntity = new NowPlayEntity();
                        String titletxt = element.attr("data-title");
                        String score = element.attr("data-score");
                        String id = element.attr("data-subject");
                        String director = element.attr("data-director");
                        String actors = element.attr("data-actors");
                        String image =
                                element.select("ul")
                                        .select("li")
                                        .select("a")
                                        .select("img")
                                        .attr("src");

                        nowPlayEntity.setTitle(titletxt);
                        nowPlayEntity.setRating(score);
                        nowPlayEntity.setImages(image);
                        nowPlayEntity.setId(id);
                        nowPlayEntity.setDirectors(director);
                        nowPlayEntity.setCasts(actors);

                        if (!TextUtils.isEmpty(nowPlayEntity.getTitle()) || !TextUtils.isEmpty(nowPlayEntity.getId()))
                            mList.add(nowPlayEntity);
                    }
                }
            }

            mHandler.sendEmptyMessageDelayed(1, 2000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //针对top250 电影解析
    private void getTopData() {
        try {
            Bundle bundle = getArguments();
            requestUrl = bundle.getString("url");
            loadMore = getArguments().getBoolean("more");

            if (loadMore) {
                requestUrl = requestUrl + "?start=" + mList.size();

                Log.e("requestUrl=== ", requestUrl);
            }

            Document document = Jsoup.connect(requestUrl).get();
            Element element4 = document.getElementById("content");
            Elements element5 = element4.getElementsByClass("grid_view").select("li");

            for (Element element : element5) {
                NowPlayEntity nowPlayEntity = new NowPlayEntity();
                String id = element.getElementsByClass("hd").select("a").attr("href");
                nowPlayEntity.setId(id);
                Elements ele2 = element.getElementsByClass("pic");

                String name = ele2.select("a").select("img").attr("alt");
                String img = ele2.select("a").select("img").attr("src");

                nowPlayEntity.setTitle(name);
                nowPlayEntity.setImages(img);

                Elements elements = element.getElementsByClass("info");
                for (Element element2 : elements) {
                    Elements elements1 = element2.select("div").select("p");
                    for (Element element3 : elements1) {
                        if (!element3.attr("class").equals("quote")) {
                            String dic = element3.outerHtml().toString();
                            nowPlayEntity.setDirectors(dic);
                        }
                    }
                    Elements elements2 = element2.getElementsByClass("star").select("span");
                    for (Element ele3 : elements2) {
                        if (ele3.attr("property").equals("v:average")) {
                            String star = ele3.text();
                            nowPlayEntity.setRating(star);
                        }
                    }
                    if (!TextUtils.isEmpty(nowPlayEntity.getTitle()) || !TextUtils.isEmpty(nowPlayEntity.getId()))
                        mList.add(nowPlayEntity);
                }
            }

            mHandler.sendEmptyMessageDelayed(1, 2000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            srl_content.setRefreshing(false);

            if (fresh) {
                mList.clear();
            } else {
                if (mList.size() != 0) {
                    movieAdapter.loadMoreComplete();
                } else {
                    movieAdapter.loadMoreEnd(false);
                }
            }

            movieAdapter.getData().clear();
            movieAdapter.addData(mList);
            LoadingDialog.dismissDialog();
        }
    };

}
