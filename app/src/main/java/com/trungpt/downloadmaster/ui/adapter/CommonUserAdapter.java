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
 * Created by Trung on 12/4/2015.
 */
public class CommonUserAdapter extends GeneralAdapter
{
    private List<User> users = new ArrayList<>();
    private Activity activity;

    public CommonUserAdapter(List<User> users, Activity activity)
    {
        this.users = users;
        this.activity = activity;
    }

    @Override
    public int getCount()
    {
        return users.size();
    }

    @Override
    public Object getItem(int position)
    {
        return users.get(position);
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
        final User user = users.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView != null)
        {
            holder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = inflater.inflate(R.layout.list_user_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        DecimalFormat df = new DecimalFormat("#,###");
        holder.tvName.setText(user.getName());
        holder.tvFollows.setText(user.getFollows() != null ? df.format(user.getFollows()) : "0");
        holder.tvVideos.setText(user.getVideos() != null ? df.format(user.getVideos()) : "0");
        holder.tvDescription.setText(user.getDescription());
        Glide.with(activity)
                .load(user.getUrlCover())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(holder.ivAvatar);
        return convertView;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void getUsers(List<User> users)
    {
        this.users = users;
    }

    static class ViewHolder
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
        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

}
