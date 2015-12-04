package com.trungpt.downloadmaster.ui.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.ui.adapter.model.VimeoCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 12/4/2015.
 */
public class ResourceUtils
{
    public static List<VimeoCategory> getListVimeoCategory(Context context) throws XmlPullParserException, IOException
    {
        XmlResourceParser xmlResourceParser = context.getResources().getXml(R.xml.vimeo_category);
        int eventType = xmlResourceParser.getEventType();
        List<VimeoCategory> vimeoCategories = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xmlResourceParser.getName().equals("category"))
                {
                    VimeoCategory vimeoCategory = new VimeoCategory(xmlResourceParser.getAttributeValue(0), xmlResourceParser.getAttributeValue(1));
                    vimeoCategories.add(vimeoCategory);
                }
            }
            eventType = xmlResourceParser.next();
        }
        return vimeoCategories;
    }

    public static List<VimeoCategory> getListDate(Context context) throws XmlPullParserException, IOException
    {
        XmlResourceParser xmlResourceParser = context.getResources().getXml(R.xml.vimeo_date);
        int eventType = xmlResourceParser.getEventType();
        List<VimeoCategory> vimeoCategories = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xmlResourceParser.getName().equals("category"))
                {
                    VimeoCategory vimeoCategory = new VimeoCategory(xmlResourceParser.getAttributeValue(0), xmlResourceParser.getAttributeValue(1));
                    vimeoCategories.add(vimeoCategory);
                }
            }
            eventType = xmlResourceParser.next();
        }
        return vimeoCategories;
    }

    public static List<VimeoCategory> getLicense(Context context) throws XmlPullParserException, IOException
    {
        XmlResourceParser xmlResourceParser = context.getResources().getXml(R.xml.vimeo_license);
        int eventType = xmlResourceParser.getEventType();
        List<VimeoCategory> vimeoCategories = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xmlResourceParser.getName().equals("category"))
                {
                    VimeoCategory vimeoCategory = new VimeoCategory(xmlResourceParser.getAttributeValue(0), xmlResourceParser.getAttributeValue(1));
                    vimeoCategories.add(vimeoCategory);
                }
            }
            eventType = xmlResourceParser.next();
        }
        return vimeoCategories;
    }

    public static List<VimeoCategory> getDuaration(Context context) throws XmlPullParserException, IOException
    {
        XmlResourceParser xmlResourceParser = context.getResources().getXml(R.xml.vimeo_duaration);
        int eventType = xmlResourceParser.getEventType();
        List<VimeoCategory> vimeoCategories = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xmlResourceParser.getName().equals("category"))
                {
                    VimeoCategory vimeoCategory = new VimeoCategory(xmlResourceParser.getAttributeValue(0), xmlResourceParser.getAttributeValue(1));
                    vimeoCategories.add(vimeoCategory);
                }
            }
            eventType = xmlResourceParser.next();
        }
        return vimeoCategories;
    }

    public static List<VimeoCategory> getShortBy(Context context) throws XmlPullParserException, IOException
    {
        XmlResourceParser xmlResourceParser = context.getResources().getXml(R.xml.vimeo_sort);
        int eventType = xmlResourceParser.getEventType();
        List<VimeoCategory> vimeoCategories = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xmlResourceParser.getName().equals("category"))
                {
                    VimeoCategory vimeoCategory = new VimeoCategory(xmlResourceParser.getAttributeValue(0), xmlResourceParser.getAttributeValue(1));
                    vimeoCategories.add(vimeoCategory);
                }
            }
            eventType = xmlResourceParser.next();
        }
        return vimeoCategories;
    }
}
