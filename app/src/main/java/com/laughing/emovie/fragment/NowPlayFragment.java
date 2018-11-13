package com.laughing.emovie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
public class NowPlayFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private View containerView;
    private String key;
    private List<NowPlayEntity> mList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_recyclerview, null);
        mRecyclerView = (RecyclerView) containerView.findViewById(R.id.recyclerView);

        mList = new ArrayList<>();
        movieAdapter = new MovieAdapter(R.layout.list_item_card_small);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(movieAdapter);

        mRecyclerView.setNestedScrollingEnabled(true);

        movieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent().setClass(getActivity(), MovieDetailActivity.class)
                        .putExtra("id", movieAdapter.getData().get(position).getId()));
            }
        });

        LoadingDialog.getInstance(getActivity()).setMessage("加载中").show();
        new GetDataThd().start();
        return containerView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private class GetDataThd extends Thread {
        @Override
        public void run() {
            super.run();
            getData(0);
        }

    }

    /**
     * @param status=0 刷新
     *                 获取数据
     */
    private void getData(final int status) {
        try {
            Document document = Jsoup.connect
                    ("https://movie.douban.com/cinema/nowplaying/shenzhen/").get();

            Bundle bundle = getArguments();
            key = bundle.getString("key");
            if (TextUtils.isEmpty(key))
                return;

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


//        RequestParams params = new RequestParams(requestUrl);
////        params.setSslSocketFactory(...); // 设置ssl
//        if (!TextUtils.isEmpty(city))
//            params.addQueryStringParameter("city", "深圳");
//        if (status == 0)
//            params.addQueryStringParameter("start", status + "");
//        else
//            params.addQueryStringParameter("start", movieAdapter.getData().size() + "");
//
//        params.addQueryStringParameter("count", count + "");
//
//        Log.e("params: ", params.toString());
//        x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                NowPlayEntity nowPlay = GsonUtils.fromJson(result, NowPlayEntity.class);
//
//                if (status == 0) {
//                    movieAdapter.getData().clear();
//                } else {
//                    if (nowPlay.getSubjects().size() == 0) {
//                        movieAdapter.loadMoreEnd(false);
//                    } else {
//                        movieAdapter.loadMoreComplete();
//                    }
//                }
//
//                movieAdapter.addData(nowPlay.getSubjects());
//                movieAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFinished() {
//                srl_layout.setRefreshing(false);
//            }
//        });
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            movieAdapter.getData().clear();
            movieAdapter.addData(mList);
            LoadingDialog.dismissDialog();
        }
    };

}
