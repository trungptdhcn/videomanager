package com.trungpt.downloadmaster.ui.adapter;

import java.math.BigInteger;

/**
 * Created by Trung on 12/4/2015.
 */
public class User
{
    private String name;
    private String url;
    private String description;
    private String website;
    private String urlCover;
    private String id;
    private BigInteger follows;
    private BigInteger videos;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getUrlCover()
    {
        return urlCover;
    }

    public void setUrlCover(String urlCover)
    {
        this.urlCover = urlCover;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public BigInteger getFollows()
    {
        return follows;
    }

    public void setFollows(BigInteger follows)
    {
        this.follows = follows;
    }

    public BigInteger getVideos()
    {
        return videos;
    }

    public void setVideos(BigInteger videos)
    {
        this.videos = videos;
    }
}
