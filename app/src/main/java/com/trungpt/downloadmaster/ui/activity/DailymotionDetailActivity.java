package com.trungpt.downloadmaster.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.util.Util;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.dailymotion.direct.DailymotionDirectMetaDataDTO;
import com.trungpt.downloadmaster.sync.dailymotion.direct.DailymotionDirectQualityDTO;
import com.trungpt.downloadmaster.sync.dailymotion.direct.DailymotionDirectQuatitiesDTO;
import com.trungpt.downloadmaster.ui.adapter.Video;
import com.trungpt.downloadmaster.ui.asynctask.AsyncDailymotionParser;
import com.trungpt.downloadmaster.ui.customview.VideoControllerView;
import com.trungpt.downloadmaster.ui.customview.player.*;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DailymotionDetailActivity extends Activity implements SurfaceHolder.Callback
        , CompoundButton.OnCheckedChangeListener, DemoPlayer.Listener, FullScreenListener
{

    @Bind(R.id.progressPreparing)
    ProgressBar progressPreparing;
    @Bind(R.id.controller)
    FrameLayout frController;
    @Bind(R.id.video_view)
    SurfaceView videoSurface;
    @Bind(R.id.information)
    ViewGroup information;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvPublisherName)
    TextView tvPublisherName;
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
    @Bind(R.id.ivPublisherAvatar)
    ImageView ivPublisherAvatar;
    @Bind(R.id.tvSub)
    TextView tvSub;
    @Bind(R.id.videoSurfaceContainer)
    RelativeLayout root;
    Video video;
    private DemoPlayer player;
    private boolean isFullScreen = false;
    private long playerPosition;
    private boolean playerNeedsPrepare;
    Uri urlVideo;
    int videoType;
    VideoControllerView controller;
    public static final int TIME_OUT_CONTROLLER = 3000;
    private boolean enableBackgroundAudio;
    AsyncDailymotionParser asyncDailymotionParser;
    Map<String, DailymotionDirectQualityDTO> hashMapUrl = new HashMap<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailymotion_detail);
        ButterKnife.bind(this);
        controller = new VideoControllerView(this);
        controller.setAnchorView(frController);
        root.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    toggleControlsVisibility();
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    view.performClick();
                }
                return true;
            }
        });
        cbExpand.setOnCheckedChangeListener(this);
        setFullScreen();
    }

    private void toggleControlsVisibility()
    {
        if (controller != null)
        {
            if (controller.isShowing())
            {
                controller.hide();
            }
            else
            {
                controller.show(TIME_OUT_CONTROLLER);
            }
        }
    }

    private void setVideoInformation()
    {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        showDescription();
        tvName.setText(video.getTitle());
        tvPublisherName.setText(video.getAuthor());
        tvSub.setText(video.getSubscribe() != null ? (nf.format(video.getSubscribe()) + " followings") : "0 followings");
        Glide.with(DailymotionDetailActivity.this)
                .load(video.getAuthorAvatarUrl())
                .placeholder(R.drawable.ic_default)
                .centerCrop()
                .crossFade()
                .into(ivPublisherAvatar);
        tvViews.setText(video.getViews() != null ? nf.format(video.getViews()) : "0");
        tvLikes.setText(video.getLikes() != null ? nf.format(video.getLikes()) : "0");
        tvDisLikes.setText(video.getDisLikes() != null ? nf.format(video.getDisLikes()) : "0");
        if (StringUtils.isNotEmpty(video.getDescription()))
        {

            tvDescription.setText(Html.fromHtml(video.getDescription()));
        }
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showDescription()
    {
        if (cbExpand.isChecked())
        {
            tvDescription.setVisibility(View.VISIBLE);
        }
        else
        {
            tvDescription.setVisibility(View.GONE);
        }
    }


    @Override
    public void onNewIntent(Intent intent)
    {
        releasePlayer();
        playerPosition = 0;
        setIntent(intent);
    }


    private void preparePlayer(boolean playWhenReady)
    {
        if (player == null)
        {
            player = new DemoPlayer(getRendererBuilder(urlVideo, videoType));
            player.addListener(this);
            player.seekTo(playerPosition);
            playerNeedsPrepare = true;
            CustomMediaControl playerControl = player.getPlayerControl();
            playerControl.setFullScreenListener(this);
            controller.setMediaPlayer(playerControl);
            controller.setEnabled(true);
        }
        if (playerNeedsPrepare)
        {
            player.prepare();
            playerNeedsPrepare = false;
        }
        controller.show();
        player.setSurface(videoSurface.getHolder().getSurface());
        player.setPlayWhenReady(playWhenReady);
    }

    private DemoPlayer.RendererBuilder getRendererBuilder(Uri uri, int type)
    {
        String userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        switch (type)
        {
            case 1:
                return new SmoothStreamingRendererBuilder(this, userAgent, uri.toString(),
                        new SmoothStreamingTestMediaDrmCallback());
            case 2:
                return new DashRendererBuilder(this, userAgent, uri.toString(),
                        new WidevineTestMediaDrmCallback(""));
            case 3:
                return new HlsRendererBuilder(this, userAgent, uri.toString());
            case 4:
                return new ExtractorRendererBuilder(this, userAgent, uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (player != null)
        {
            player.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (player != null)
        {
            player.blockingClearSurface();
        }
    }

    private void releasePlayer()
    {
        if (player != null)
        {
            playerPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        progressPreparing.getIndeterminateDrawable()
                .setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        progressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK);
        progressDialog.setMessage("Get data...!");
        Intent i = getIntent();
        video = (Video) i.getSerializableExtra("video");
        if (video != null)
        {
            setVideoInformation();
            asyncDailymotionParser = new AsyncDailymotionParser(video.getUrl())
            {
                @Override
                public void prepare()
                {
                    progressDialog.show();
                }

                @Override
                public void conplete(Object o)
                {
                    DailymotionDirectMetaDataDTO dailymotionDirectMetaDataDTO = (DailymotionDirectMetaDataDTO) o;
                    if (dailymotionDirectMetaDataDTO != null)
                    {
                        DailymotionDirectQuatitiesDTO dailymotionDirectQuatitiesDTO = dailymotionDirectMetaDataDTO.getDailymotionDirectQuatities();
                        if (null != dailymotionDirectQuatitiesDTO)
                        {
                            List<DailymotionDirectQualityDTO> _auto = dailymotionDirectQuatitiesDTO.get_auto();
                            List<DailymotionDirectQualityDTO> _240 = dailymotionDirectQuatitiesDTO.get_240();
                            List<DailymotionDirectQualityDTO> _380 = dailymotionDirectQuatitiesDTO.get_380();
                            List<DailymotionDirectQualityDTO> _480 = dailymotionDirectQuatitiesDTO.get_480();
                            List<DailymotionDirectQualityDTO> _720 = dailymotionDirectQuatitiesDTO.get_720();
                            if (_auto != null && _auto.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                            {
                                hashMapUrl.put("_auto", _auto.get(0));
                            }
                            if (_240 != null && _240.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                            {
                                hashMapUrl.put("_240", _240.get(0));
                            }
                            if (_380 != null && _380.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                            {
                                hashMapUrl.put("_380", _380.get(0));
                            }
                            if (_480 != null && _480.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                            {
                                hashMapUrl.put("_480", _480.get(0));
                            }
                            if (_720 != null && _720.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                            {
                                hashMapUrl.put("_720", _720.get(0));
                            }
                        }
                        if (hashMapUrl.get("_auto") != null)
                        {
                            videoSurface.getHolder().addCallback(DailymotionDetailActivity.this);
                            urlVideo = Uri.parse(hashMapUrl.get("_auto").getUrl());
                            videoType = 3;
                        }
                        else
                        {
                            videoType = 4;
                            if (hashMapUrl.get("_720") != null)
                            {
                                urlVideo = Uri.parse(hashMapUrl.get("_720").getUrl());
                                preparePlayer(true);
                                player.setBackgrounded(false);
                                return;
                            }
                            if (hashMapUrl.get("_480") != null)
                            {
                                urlVideo = Uri.parse(hashMapUrl.get("_480").getUrl());
                                preparePlayer(true);
                                player.setBackgrounded(false);
                                return;
                            }
                            if (hashMapUrl.get("_380") != null)
                            {
                                urlVideo = Uri.parse(hashMapUrl.get("_380").getUrl());
                                preparePlayer(true);
                                player.setBackgrounded(false);
                                return;
                            }
                            if (hashMapUrl.get("_240") != null)
                            {
                                urlVideo = Uri.parse(hashMapUrl.get("_240").getUrl());
                                preparePlayer(true);
                                player.setBackgrounded(false);
                                return;
                            }
                        }
                    }
                    preparePlayer(true);
                    player.setBackgrounded(false);
                    progressDialog.dismiss();
                }
            };
            if (null == urlVideo)
            {
                asyncDailymotionParser.execute();
            }
        }

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (!enableBackgroundAudio)
        {
            releasePlayer();
        }
        else
        {
            player.setBackgrounded(true);
        }
    }

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

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState)
    {
        if (playbackState == ExoPlayer.STATE_ENDED)
        {
            controller.show(TIME_OUT_CONTROLLER);
        }
        String text = "playWhenReady=" + playWhenReady + ", playbackState=";
        switch (playbackState)
        {
            case ExoPlayer.STATE_BUFFERING:
                progressPreparing.setVisibility(View.VISIBLE);
                break;
            case ExoPlayer.STATE_ENDED:
                progressPreparing.setVisibility(View.GONE);
                controller.show(500000);
                break;
            case ExoPlayer.STATE_IDLE:
                progressPreparing.setVisibility(View.GONE);
                controller.show(500000);
                break;
            case ExoPlayer.STATE_PREPARING:
                progressPreparing.setVisibility(View.VISIBLE);
                controller.show(50000);
                break;
            case ExoPlayer.STATE_READY:
                progressPreparing.setVisibility(View.GONE);
                controller.show(3000);
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(Exception e)
    {
        if (e instanceof UnsupportedDrmException)
        {
            // Special case DRM failures.
            UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
            int stringId = Util.SDK_INT < 18 ? R.string.drm_error_not_supported
                    : unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                    ? R.string.drm_error_unsupported_scheme : R.string.drm_error_unknown;
            Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_LONG).show();
        }
        playerNeedsPrepare = true;
        controller.show(TIME_OUT_CONTROLLER);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                   float pixelWidthAspectRatio)
    {
    }

    @Override
    public boolean isFullScreen()
    {
        return isFullScreen;
    }

    @Override
    public void toggleFullScreen()
    {
        setFullScreen();
    }


    public void setFullScreen()
    {
        if (isFullScreen())
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoSurface.getLayoutParams();
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) frController.getLayoutParams();
            params.width = width;
            params.height = height;// -80 for android controls
            params.setMargins(0, 0, 0, 0);
            params2.width = width;
            params2.height = height;// -80 for android controls
            params2.setMargins(0, 0, 0, 0);
            information.setVisibility(View.GONE);
            controller.updateFullScreen();
            isFullScreen = false;
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoSurface.getLayoutParams();
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) frController.getLayoutParams();
            params.width = width;
            params.height = (int) getResources().getDimension(R.dimen.size_of_video);
            params.setMargins(0, 0, 0, 0);
            params2.width = width;
            params2.height = (int) getResources().getDimension(R.dimen.size_of_video);
            params2.setMargins(0, 0, 0, 0);
            information.setVisibility(View.VISIBLE);
            controller.updateFullScreen();
            isFullScreen = true;
        }
    }
}
