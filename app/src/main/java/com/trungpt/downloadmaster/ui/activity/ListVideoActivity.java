package com.trungpt.downloadmaster.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.vimeo.request.VimeoRequestDTO;
import com.trungpt.downloadmaster.ui.adapter.*;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskGetData;
import com.trungpt.downloadmaster.ui.customview.SimpleDividerItemDecoration;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.EndlessRecyclerOnScrollListener;
import com.trungpt.downloadmaster.ui.utils.Constant;

import java.text.DecimalFormat;
import java.util.List;

public class ListVideoActivity extends Activity implements AsyncTaskListener
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    String nextPage;
    RecycleAdapter adapter;
    VimeoRequestDTO requestDTO;
    Channel channel;
    private boolean isNormalAdapter = false;
    EndlessRecyclerOnScrollListener scrollListener;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_video);
        ButterKnife.bind(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        Intent i = getIntent();
        channel = (Channel) i.getSerializableExtra("user");
        header = getLayoutInflater().inflate(R.layout.cover_layout, recyclerView, false);
        ImageView ivCover = (ImageView) header.findViewById(R.id.ivCover);
        ImageView ivUserAvatar = (ImageView) header.findViewById(R.id.ivUserAvatar);
        TextView tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        TextView tvLikeOfUser = (TextView) header.findViewById(R.id.tvLikes);
        TextView tvFollowsOfUser = (TextView) header.findViewById(R.id.tvFollows);
        DecimalFormat df = new DecimalFormat("#,###");
        tvLikeOfUser.setText(channel.getLikesOfUser() != null ? (df.format(channel.getLikesOfUser()) + " likes") : "0 likes");
        tvFollowsOfUser.setText(channel.getUserOffollows() != null ? (df.format(channel.getUserOffollows()) + " subscribes") : "0 subscribes");
        Glide.with(this)
                .load(channel.getUrlUserAvatar())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(ivUserAvatar);
        Glide.with(this)
                .load(channel.getUrlCover())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(ivCover);
        requestDTO = new VimeoRequestDTO
                .VimeoRequestBuilder("")
                .category(channel.getId())
                .build();
        tvUserName.setText(channel.getNameOfUser());
        AsyncTaskGetData asyncTask = new AsyncTaskGetData(this, Constant.HOST_NAME.VIMEO, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
        scrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(final int current_page)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPageToken(current_page + "");
                    AsyncTaskGetData asyncTask = new AsyncTaskGetData(ListVideoActivity.this, Constant.HOST_NAME.VIMEO, requestDTO);
                    asyncTask.setListener(ListVideoActivity.this);
                    asyncTask.execute();
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
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
                adapter = new RecycleAdapter(videos, this,Constant.HOST_NAME.VIMEO);
                adapter.setParallaxHeader(header, recyclerView);
                recyclerView.setAdapter(adapter);
            }
            else
            {
                adapter.getData().addAll(videos);
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        }
        progressBar.setVisibility(View.GONE);
    }
}
