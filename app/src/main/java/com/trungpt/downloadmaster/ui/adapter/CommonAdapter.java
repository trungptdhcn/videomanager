package com.trungpt.downloadmaster.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.trungpt.downloadmaster.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/11/2015.
 */
public class CommonAdapter extends BaseAdapter
{
    private List<Item> videos = new ArrayList<>();
    private Activity activity;

    public CommonAdapter(List<Item> videos, Activity activity)
    {
        this.videos = videos;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (videos.get(position) instanceof Video)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getCount()
    {
        return videos.size();
    }

    @Override
    public Object getItem(int position)
    {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Item item = videos.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        if (getItemViewType(position) == 0)
        {
            ViewHolder holder;
            Video video = (Video) item;
            if (convertView != null)
            {
                holder = (ViewHolder) convertView.getTag();
            }
            else
            {

                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            DecimalFormat df = new DecimalFormat("#,###");
            holder.title.setText(video.getTitle());
            holder.tvLike.setText(video.getLikes() != null ? df.format(video.getLikes()) : "0");
            holder.tvViews.setText(video.getViews() != null ? df.format(video.getViews()) : "0");
            holder.tvDisLike.setText(video.getDisLikes() != null ? df.format(video.getDisLikes()) : "0");
            holder.tvAuthor.setText(String.valueOf(video.getAuthor()));
            holder.tvDuration.setText(video.getDuration());
            Glide.with(activity)
                    .load(video.getUrlThumbnail())
                    .centerCrop()
                    .placeholder(R.drawable.ic_default)
                    .crossFade()
                    .into(holder.ivThumbnail);
        }
        else if (getItemViewType(position) == 1)
        {
            ViewHolderUser holder;
            Channel channel = (Channel) item;
            if (convertView != null)
            {
                holder = (ViewHolderUser) convertView.getTag();
            }
            else
            {

                convertView = inflater.inflate(R.layout.list_channel_item, parent, false);
                holder = new ViewHolderUser(convertView);
                convertView.setTag(holder);
            }
            DecimalFormat df = new DecimalFormat("#,###");
            holder.tvName.setText(channel.getName());
            holder.tvFollows.setText(channel.getFollows() != null ? (df.format(channel.getFollows())+" subscribes") : "0 subscribes");
            holder.tvVideos.setText(channel.getVideos() != null ? (df.format(channel.getVideos())) : "0");
            holder.tvDescription.setText(channel.getDescription());
            Glide.with(activity)
                    .load(channel.getUrlCover())
                    .centerCrop()
                    .placeholder(R.drawable.ic_default)
                    .crossFade()
                    .into(holder.ivAvatar);
        }
        return convertView;
    }

    public List<Item> getVideos()
    {
        return videos;
    }

    public void setVideos(List<Item> videos)
    {
        this.videos = videos;
    }


    static class ViewHolder
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
        @Bind(R.id.ivAvatar)
        ImageView ivThumbnail;
        @Bind(R.id.tvDuration)
        TextView tvDuration;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderUser
    {
        @Bind(R.id.tvName)
        TextView tvName;
        @Bind(R.id.tvFollows)
        TextView tvFollows;
        @Bind(R.id.tvVideos)
        TextView tvVideos;
        @Bind(R.id.tvDescription)
        TextView tvDescription;
        @Bind(R.id.ivAvatar)
        ImageView ivAvatar;

        public ViewHolderUser(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
