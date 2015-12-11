package com.trungpt.downloadmaster.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnItemClick;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.BaseFragment;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.vimeo.request.VimeoRequestDTO;
import com.trungpt.downloadmaster.sync.youtube.YoutubeRequestDTO;
import com.trungpt.downloadmaster.ui.activity.VimeoSearchActivity;
import com.trungpt.downloadmaster.ui.activity.YoutubeSearchActivity;
import com.trungpt.downloadmaster.ui.activity.YoutubeVideoDetailActivity;
import com.trungpt.downloadmaster.ui.adapter.CommonAdapter;
import com.trungpt.downloadmaster.ui.adapter.Item;
import com.trungpt.downloadmaster.ui.adapter.Video;
import com.trungpt.downloadmaster.ui.adapter.VideoPage;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskMostPopular;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskSearchData;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.EndlessScrollListener;
import com.trungpt.downloadmaster.ui.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Trung on 11/24/2015.
 */
public class YoutubeFragment extends BaseFragment implements AsyncTaskListener
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view)
    ListView listView;
    CommonAdapter adapter;
    String nextPage;
    YoutubeRequestDTO requestDTO;

    @Bind(R.id.menuOption)
    FloatingActionMenu menuOption;

    @Bind(R.id.fab_up)
    FloatingActionButton fabUp;
    @Bind(R.id.fab_search)
    FloatingActionButton fabSearch;
    @Bind(R.id.fab_filter)
    FloatingActionButton fabFilter;

    @Override
    public int getLayout()
    {
        return R.layout.youtube_fragment;
    }

    @Override
    public void setDataToView(Bundle savedInstanceState)
    {
        requestDTO = new YoutubeRequestDTO
                .YoutubeRequestBuilder("")
                .regionCode("vn")
                .build();
        AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(getActivity(), Constant.HOST_NAME.YOUTUBE, requestDTO);
        asyncTask.setListener(YoutubeFragment.this);
        asyncTask.execute();
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPageToken(nextPage);
                    AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(getActivity(), Constant.HOST_NAME.YOUTUBE, requestDTO);
                    asyncTask.setListener(YoutubeFragment.this);
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
        menuOption.setOnMenuButtonClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (menuOption.isOpened())
                {
                }
                menuOption.toggle(true);
            }
        });
        menuOption.setClosedOnTouchOutside(true);
        fabSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), YoutubeSearchActivity.class));
            }
        });
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
            List<Item> videos = videoPage.getVideos();
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

//    public void search()
//    {
//        String keyword = edtSearch.getText().toString();
//        String id = null;
//        String regularExpressionYoutube = "(https?://)?(www\\.)?(m\\.)?(yotu\\.be/|youtube\\.com/)?((.+/)?(watch(\\?v=|.+&v=))?(v=)?)([\\w_-]{11})(&.+)?";
//        if (keyword.matches(regularExpressionYoutube))
//        {
//            {
//                Pattern u1 = Pattern.compile("youtube.com/watch?.*v=([^&]*)");
//                Matcher um = u1.matcher(keyword);
//                if (um.find())
//                {
//                    id = um.group(1);
//                }
//            }
//
//            {
//                Pattern u2 = Pattern.compile("youtube.com/v/([^&]*)");
//                Matcher um = u2.matcher(keyword);
//                if (um.find())
//                {
//                    id = um.group(1);
//                }
//            }
//
//        }
//        requestDTO = new YoutubeRequestDTO
//                .YoutubeRequestBuilder(keyword)
//                .id(id)
//                .type("video")
//                .build();
//        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(getActivity(), Constant.HOST_NAME.YOUTUBE, requestDTO);
//        asyncTask.setListener(this);
//        asyncTask.execute();
//    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
//    {
//        search();
//        adapter = null;
//        return false;
//    }

    @OnItemClick(R.id.list_view)
    public void itemClick(int position)
    {
        Intent intent = new Intent(getActivity(), YoutubeVideoDetailActivity.class);
        intent.putExtra("video", (Video) adapter.getItem(position));
        startActivity(intent);
    }
}
