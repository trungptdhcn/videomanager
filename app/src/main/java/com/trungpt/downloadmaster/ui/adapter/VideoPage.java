package com.trungpt.downloadmaster.ui.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/25/2015.
 */
public class VideoPage
{
    private List<Video> videos = new ArrayList<>();
    private String nextPage;

    public VideoPage(List<Video> videos)
    {
        this.videos = videos;
    }

    public List<Video> getVideos()
    {
        return videos;
    }

    public void setVideos(List<Video> videos)
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
