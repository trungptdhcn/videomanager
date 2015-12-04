package com.trungpt.downloadmaster.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnItemClick;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.BaseFragment;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.dailymotion.request.DailymotionRequestDTO;
import com.trungpt.downloadmaster.sync.vimeo.request.VimeoRequestDTO;
import com.trungpt.downloadmaster.sync.youtube.YoutubeRequestDTO;
import com.trungpt.downloadmaster.ui.activity.DailymotionDetailActivity;
import com.trungpt.downloadmaster.ui.activity.YoutubeVideoDetailActivity;
import com.trungpt.downloadmaster.ui.adapter.CommonAdapter;
import com.trungpt.downloadmaster.ui.adapter.Video;
import com.trungpt.downloadmaster.ui.adapter.VideoPage;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskMostPopular;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskSearchData;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.EndlessScrollListener;
import com.trungpt.downloadmaster.ui.utils.Constant;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Trung on 11/24/2015.
 */
public class DailymotionFragment extends BaseFragment implements AsyncTaskListener, TextView.OnEditorActionListener
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.edtSearch)
    EditText edtSearch;
    CommonAdapter adapter;
    String nextPage;
    DailymotionRequestDTO requestDTO;

    @Override
    public int getLayout()
    {
        return R.layout.dailymotion_fragment;
    }

    @Override
    public void setDataToView(Bundle savedInstanceState)
    {
        edtSearch.setOnEditorActionListener(this);
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPage(page);
                    AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(getActivity(), Constant.HOST_NAME.DAILYMOTION, requestDTO);
                    asyncTask.setListener(DailymotionFragment.this);
                    asyncTask.execute();
                    return true;
                }
                else
                {
                    return false;
                }

            }
        };
        listView.setOnScrollListener(endlessScrollListener);
    }

    @Override
    public void prepare()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void complete(Object obj)
    {
        VideoPage videoPage = (VideoPage) obj;
        if (videoPage != null)
        {
            List<Video> videos = videoPage.getVideos();
            nextPage = videoPage.getNextPage();
            if (adapter == null)
            {
                adapter = new CommonAdapter(videos, getActivity());
                listView.setAdapter(adapter);
            }
            else
            {
                adapter.getVideos().addAll(videos);
                adapter.notifyDataSetChanged();
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    public void search()
    {
        requestDTO = new DailymotionRequestDTO
                .DailymotionRequestBuilder(edtSearch.getText().toString())
                .fields("title,channel,country,description,duration,id,poster,thumbnail_720_url,url,owner.screenname,views_total,owner.fans_total,owner.avatar_240_url")
                .flags("no_live,no_premium")
                .sort("visited")
                .page(1)
                .limit(10)
                .build();
        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(getActivity(), Constant.HOST_NAME.DAILYMOTION, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        search();
        adapter = null;
        return false;
    }

    @OnItemClick(R.id.list_view)
    public void itemClick(int position)
    {
        Intent intent = new Intent(getActivity(), DailymotionDetailActivity.class);
        intent.putExtra("video", (Video) adapter.getItem(position));
        startActivity(intent);
    }
}
