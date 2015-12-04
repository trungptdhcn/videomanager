package com.trungpt.downloadmaster.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.sync.Configs;
import com.trungpt.downloadmaster.sync.youtube.YoutubeConnector;
import com.trungpt.downloadmaster.ui.adapter.Video;

import java.text.NumberFormat;
import java.util.Locale;

public class YoutubeVideoDetailActivity extends YouTubeFailureRecoveryActivity
{
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.cbExpand)
    CheckBox cbExpand;
    @Bind(R.id.tvViews)
    TextView tvViews;
    @Bind(R.id.tvDescription)
    TextView tvDescription;
    @Bind(R.id.tvLikes)
    TextView tvLikes;
    @Bind(R.id.tvDisLikes)
    TextView tvDisLikes;
    @Bind(R.id.ivDownload)
    ImageView ivDownload;
    @Bind(R.id.tvPublisherName)
    TextView tvPublisherName;
    @Bind(R.id.tvSub)
    TextView tvSub;
    @Bind(R.id.ivPublisherAvatar)
    ImageView ivPublisherAvatar;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    Video video;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_video_detail);
        ButterKnife.bind(this);
        Intent i = getIntent();
        video = (Video) i.getSerializableExtra("video");

        if (video != null)
        {
            final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            new AsyncTask<Void, Void, Video>()
            {
                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected Video doInBackground(Void... params)
                {
                    YoutubeConnector youtubeConnector = new YoutubeConnector(YoutubeVideoDetailActivity.this);
                    return youtubeConnector.getAuthorInfo(video.getAuthorId());
                }

                @Override
                protected void onPostExecute(Video video)
                {
                    super.onPostExecute(video);
                    tvSub.setText(video.getSubscribe() != null ? (nf.format(video.getSubscribe())+" subscribes") : "0 subscribes");
                    Glide.with(YoutubeVideoDetailActivity.this)
                            .load(video.getAuthorAvatarUrl())
                            .placeholder(R.drawable.ic_default)
                            .centerCrop()
                            .crossFade()
                            .into(ivPublisherAvatar);
                    progressBar.setVisibility(View.GONE);
                }
            }.execute();

            if (cbExpand.isChecked())
            {
                tvDescription.setVisibility(View.VISIBLE);
            }
            else
            {
                tvDescription.setVisibility(View.GONE);
            }
            cbExpand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        tvDescription.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvDescription.setVisibility(View.GONE);
                    }
                }
            });
            tvName.setText(video.getTitle());
            tvPublisherName.setText(video.getAuthor());
            tvViews.setText(video.getViews() != null ? nf.format(video.getViews()) : "0");
            tvLikes.setText(video.getLikes() != null ? nf.format(video.getLikes()) : "0");
            tvDisLikes.setText(video.getDisLikes() != null ? nf.format(video.getDisLikes()) : "0");
            tvDescription.setText(video.getDescription());
            tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        }
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubeView.initialize(Configs.KEY_YOUTUBE, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored)
    {
        if (!wasRestored && video != null)
        {
            player.cueVideo(video.getId());
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider()
    {
        return (YouTubePlayerView) findViewById(R.id.youtube_player);
    }

}
