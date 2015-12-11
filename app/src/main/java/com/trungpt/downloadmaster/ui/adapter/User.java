package com.trungpt.downloadmaster.ui.adapter;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by trung on 12/8/2015.
 */
public class User  extends Item
{
    private String name;
    private String id;
    private String urlAvatar;
    private String bio;
    private BigInteger follows;
    private BigInteger likes;
    private String location;
    private String website;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUrlAvatar()
    {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar)
    {
        this.urlAvatar = urlAvatar;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public BigInteger getFollows()
    {
        return follows;
    }

    public void setFollows(BigInteger follows)
    {
        this.follows = follows;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public BigInteger getLikes()
    {
        return likes;
    }

    public void setLikes(BigInteger likes)
    {
        this.likes = likes;
    }
}
