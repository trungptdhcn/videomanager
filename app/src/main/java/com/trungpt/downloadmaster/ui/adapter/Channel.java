package com.trungpt.downloadmaster.ui.adapter;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Trung on 12/4/2015.
 */
public class Channel extends Item implements Serializable
{
    private String name;
    private String url;
    private String description;
    private String website;
    private String urlCover;
    private String id;
    private BigInteger follows;
    private BigInteger videos;
    //====================================
    private String nameOfUser;
    private String userId;
    private String urlUserAvatar;
    private String bio;
    private BigInteger userOffollows;
    private BigInteger likesOfUser;
    private String userLocation;
    private String userWebsite;

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

    public String getNameOfUser()
    {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser)
    {
        this.nameOfUser = nameOfUser;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUrlUserAvatar()
    {
        return urlUserAvatar;
    }

    public void setUrlUserAvatar(String urlUserAvatar)
    {
        this.urlUserAvatar = urlUserAvatar;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public BigInteger getUserOffollows()
    {
        return userOffollows;
    }

    public void setUserOffollows(BigInteger userOffollows)
    {
        this.userOffollows = userOffollows;
    }

    public String getUserLocation()
    {
        return userLocation;
    }

    public void setUserLocation(String userLocation)
    {
        this.userLocation = userLocation;
    }

    public String getUserWebsite()
    {
        return userWebsite;
    }

    public void setUserWebsite(String userWebsite)
    {
        this.userWebsite = userWebsite;
    }

    public BigInteger getLikesOfUser()
    {
        return likesOfUser;
    }

    public void setLikesOfUser(BigInteger likesOfUser)
    {
        this.likesOfUser = likesOfUser;
    }
}