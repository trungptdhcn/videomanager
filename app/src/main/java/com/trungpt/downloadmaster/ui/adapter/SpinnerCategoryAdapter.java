package com.trungpt.downloadmaster.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.ui.adapter.model.VimeoCategory;
import com.trungpt.downloadmaster.ui.utils.ResourceUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 12/4/2015.
 */
public class SpinnerCategoryAdapter extends ArrayAdapter<VimeoCategory>
{
    private List<VimeoCategory> vimeoCategories = new ArrayList<>();
    private Context context;
    private int layoutResourceId;

    public SpinnerCategoryAdapter(Context context, int layoutResourceId, List<VimeoCategory> vimeoCategories)
    {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.vimeoCategories = vimeoCategories;
    }

    @Override
    public int getCount()
    {
        return vimeoCategories.size();
    }

    @Override
    public VimeoCategory getItem(int position)
    {
        return vimeoCategories.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
//        return getCustomView(position, convertView, parent);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(R.layout.spinner_selector_item, parent, false);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTextViewItem);
        tvTitle.setText(vimeoCategories.get(position).getName());
        return v;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(layoutResourceId, parent, false);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTextViewItem);
        tvTitle.setText(vimeoCategories.get(position).getName());
        return v;
    }
}
