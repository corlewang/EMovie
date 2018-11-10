package com.laughing.emovie.fragment;

import android.os.Bundle;
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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.laughing.emovie.R;
import com.laughing.emovie.adapter.MovieAdapter;
import com.laughing.emovie.entity.NowPlayEntity;
import com.laughing.emovie.utils.CustomLinearLayoutManager;
import com.laughing.emovie.utils.GsonUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created by florentchampigny on 24/04/15.
 */
public class NowPlayFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout srl_layout;
    private MovieAdapter movieAdapter;
    private View containerView;
    private int count = 10;
    private String requestUrl, city;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = inflater.inflate(R.layout.fragment_recyclerview, null);
            mRecyclerView = (RecyclerView) containerView.findViewById(R.id.recyclerView);
            srl_layout = (SwipeRefreshLayout) containerView.findViewById(R.id.srl_layout);
            movieAdapter = new MovieAdapter(R.layout.list_item_card_small);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(movieAdapter);
        }

        srl_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(0);
            }
        });
        movieAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(1);
            }
        }, mRecyclerView);

        Bundle bundle = getArguments();
        requestUrl = bundle.getString("requestUrl");
        city = bundle.getString("city");

        getData(0);
        return containerView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param status=0 刷新
     *                 获取数据
     */
    private void getData(final int status) {
        RequestParams params = new RequestParams(requestUrl);
//        params.setSslSocketFactory(...); // 设置ssl
        if (!TextUtils.isEmpty(city))
            params.addQueryStringParameter("city", "深圳");
        if (status == 0)
            params.addQueryStringParameter("start", status + "");
        else
            params.addQueryStringParameter("start", movieAdapter.getData().size() + "");

        params.addQueryStringParameter("count", count + "");

        Log.e("params: ", params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NowPlayEntity nowPlay = GsonUtils.fromJson(result, NowPlayEntity.class);

                if (status == 0) {
                    movieAdapter.getData().clear();
                } else {
                    if (nowPlay.getSubjects().size() == 0) {
                        movieAdapter.loadMoreEnd(true);
                    } else {
                        movieAdapter.loadMoreComplete();
                    }
                }

                movieAdapter.addData(nowPlay.getSubjects());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                srl_layout.setRefreshing(false);
            }
        });
    }

}

