package com.trungpt.downloadmaster.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.trungpt.downloadmaster.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Trung on 11/11/2015.
 */
public class CommonAdapter extends GeneralAdapter
{
    private List<Video> videos = new ArrayList<>();
    private Activity activity;

    public CommonAdapter(List<Video> videos, Activity activity)
    {
        this.videos = videos;
        this.activity = activity;
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
        ViewHolder holder;
        final Video video = videos.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
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
        Glide.with(activity)
                .load(video.getUrlThumbnail())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(holder.ivThumbnail);
        return convertView;
    }

    public List<Video> getVideos()
    {
        return videos;
    }

    public void setVideos(List<Video> videos)
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

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

}
