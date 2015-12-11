package com.trungpt.downloadmaster.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.trungpt.downloadmaster.ui.activity.VimeoDetailActivity;
import com.trungpt.downloadmaster.ui.activity.VimeoSearchActivity;
import com.trungpt.downloadmaster.ui.adapter.CommonAdapter;
import com.trungpt.downloadmaster.ui.adapter.Item;
import com.trungpt.downloadmaster.ui.adapter.Video;
import com.trungpt.downloadmaster.ui.adapter.VideoPage;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskMostPopular;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.EndlessScrollListener;
import com.trungpt.downloadmaster.ui.utils.Constant;

import java.util.List;

/**
 * Created by Trung on 12/3/2015.
 */
public class VimeoFragment extends BaseFragment implements AsyncTaskListener
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view)
    ListView listView;
    String nextPage;
    CommonAdapter adapter;
    VimeoRequestDTO requestDTO;

    @Bind(R.id.menu1)
    FloatingActionMenu menu1;

    @Bind(R.id.fab_up)
    FloatingActionButton fabUp;
    @Bind(R.id.fab_search)
    FloatingActionButton fabSearch;
    @Bind(R.id.fab_filter)
    FloatingActionButton fabFilter;

    @Override
    public int getLayout()
    {
        return R.layout.vimeo_fragment;
    }

    @Override
    public void setDataToView(Bundle savedInstanceState)
    {
        requestDTO = new VimeoRequestDTO
                .VimeoRequestBuilder("")
                .category("music")
                .build();
        AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(getActivity(), Constant.HOST_NAME.VIMEO, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPageToken(page + "");
                    AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(getActivity(), Constant.HOST_NAME.VIMEO, requestDTO);
                    asyncTask.setListener(VimeoFragment.this);
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
        menu1.setOnMenuButtonClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (menu1.isOpened())
                {
                }
                menu1.toggle(true);
            }
        });
        menu1.setClosedOnTouchOutside(true);

        fabSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), VimeoSearchActivity.class));
            }
        });
    }

    @Override
    public void loadDataWhenFragmentVisible()
    {
        super.loadDataWhenFragmentVisible();
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

    @OnItemClick(R.id.list_view)
    public void itemClick(int position)
    {
        Intent intent = new Intent(getActivity(), VimeoDetailActivity.class);
        intent.putExtra("video", (Video) adapter.getItem(position));
        startActivity(intent);
    }
}

