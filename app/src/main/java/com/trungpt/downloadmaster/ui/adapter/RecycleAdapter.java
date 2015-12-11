package com.trungpt.downloadmaster.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.ui.activity.DailymotionDetailActivity;
import com.trungpt.downloadmaster.ui.activity.ListVideoActivity;
import com.trungpt.downloadmaster.ui.activity.VimeoDetailActivity;
import com.trungpt.downloadmaster.ui.utils.Constant;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by trung on 12/7/2015.
 */
public class RecycleAdapter extends ParallaxRecyclerAdapter<Item>
{
    private Context context;
    private List<Item> data;
    private Constant.HOST_NAME typeHost;

    public RecycleAdapter(List<Item> data, Context context, Constant.HOST_NAME typeHost)
    {
        super(data);
        this.context = context;
        this.data = data;
        this.typeHost = typeHost;
    }

    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder
            , ParallaxRecyclerAdapter<Item> itemParallaxRecyclerAdapter, int position)
    {
        final Video video = (Video) data.get(position);
        DecimalFormat df = new DecimalFormat("#,###");
        ((ViewHolder) viewHolder).title.setText(video.getTitle());
        ((ViewHolder) viewHolder).tvLike.setText(video.getLikes() != null ? df.format(video.getLikes()) : "0");
        ((ViewHolder) viewHolder).tvViews.setText(video.getViews() != null ? df.format(video.getViews()) : "0");
        ((ViewHolder) viewHolder).tvDisLike.setText(video.getDisLikes() != null ? df.format(video.getDisLikes()) : "0");
        ((ViewHolder) viewHolder).tvAuthor.setText(String.valueOf(video.getAuthor()));
        ((ViewHolder) viewHolder).tvDuration.setText(video.getDuration());
        Glide.with(context)
                .load(video.getUrlThumbnail())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(((ViewHolder) viewHolder).ivThumbnail);
        ((ViewHolder) viewHolder).rlContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = null;
                switch (typeHost)
                {
                    case VIMEO:
                        intent = new Intent(context,VimeoDetailActivity.class);
                        break;
                    case DAILYMOTION:
                        intent = new Intent(context,DailymotionDetailActivity.class);
                        break;
                }
                intent.putExtra("video",video);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup
            , ParallaxRecyclerAdapter<Item> itemParallaxRecyclerAdapter, final int i)
    {
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCountImpl(ParallaxRecyclerAdapter<Item> itemParallaxRecyclerAdapter)
    {
        return data.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.tvName)
        TextView title;
        @Bind(R.id.tvLike)
        TextView tvLike;
        @Bind(R.id.tvDisLike)
        TextView tvDisLike;
        @Bind(R.id.tvAuthor)
        TextView tvAuthor;
        @Bind(R.id.tvViews)
        TextView tvViews;
        @Bind(R.id.tvDuration)
        TextView tvDuration;
        @Bind(R.id.ivAvatar)
        ImageView ivThumbnail;
        @Bind(R.id.rlContainer)
        RelativeLayout rlContainer;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
