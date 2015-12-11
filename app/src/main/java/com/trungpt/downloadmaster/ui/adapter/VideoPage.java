package com.trungpt.downloadmaster.ui.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/25/2015.
 */
public class VideoPage
{
    private List<Item> videos = new ArrayList<>();
    private String nextPage;

    public VideoPage(List<Item> videos)
    {
        this.videos = videos;
    }

    public List<Item> getVideos()
    {
        return videos;
    }

    public void setVideos(List<Item> videos)
    {
        this.videos = videos;
    }

    public String getNextPage()
    {
        return nextPage;
    }

    public void setNextPage(String nextPage)
    {
        this.nextPage = nextPage;
    }
}
