package com.trungpt.downloadmaster.ui.adapter;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Trung on 11/25/2015.
 */
public class Video extends Item
{
    private String id;
    private String title;
    private BigInteger views = new BigInteger("0");
    private BigInteger likes = new BigInteger("0");
    private BigInteger disLikes = new BigInteger("0");
    private String description;
    private String type;
    private String author;
    private String url;
    private String urlThumbnail;
    private String duration;
    private BigInteger subscribe;
    private String embed;
    private String authorAvatarUrl;
    private String authorId;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrlThumbnail()
    {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail)
    {
        this.urlThumbnail = urlThumbnail;
    }

    public BigInteger getViews()
    {
        return views;
    }

    public void setViews(BigInteger views)
    {
        this.views = views;
    }

    public BigInteger getLikes()
    {
        return likes;
    }

    public void setLikes(BigInteger likes)
    {
        this.likes = likes;
    }

    public BigInteger getDisLikes()
    {
        return disLikes;
    }

    public void setDisLikes(BigInteger disLikes)
    {
        this.disLikes = disLikes;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public BigInteger getSubscribe()
    {
        return subscribe;
    }

    public void setSubscribe(BigInteger subscribe)
    {
        this.subscribe = subscribe;
    }

    public String getEmbed()
    {
        return embed;
    }

    public void setEmbed(String embed)
    {
        this.embed = embed;
    }

    public String getAuthorAvatarUrl()
    {
        return authorAvatarUrl;
    }

    public void setAuthorAvatarUrl(String authorAvatarUrl)
    {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    public String getAuthorId()
    {
        return authorId;
    }

    public void setAuthorId(String authorId)
    {
        this.authorId = authorId;
    }
}
