package com.trungpt.downloadmaster.sync.vimeo.channel;

import com.google.gson.annotations.SerializedName;
import com.trungpt.downloadmaster.sync.vimeo.VimeoResponseMetaDataDTO;
import com.trungpt.downloadmaster.sync.vimeo.VimeoResponsePicturesDTO;

/**
 * Created by trung on 12/8/2015.
 */
public class UserVimeoOfChannelResponseDTO
{
    private String uri;
    private String name;
    private String link;
    private String location;
    private String bio;
    @SerializedName("pictures")
    private VimeoResponsePicturesDTO vimeoPicturesDTO;
    @SerializedName("metadata")
    private VimeoResponseMetaDataDTO vimeoResponseMetadataDTO;

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public VimeoResponsePicturesDTO getVimeoPicturesDTO()
    {
        return vimeoPicturesDTO;
    }

    public void setVimeoPicturesDTO(VimeoResponsePicturesDTO vimeoPicturesDTO)
    {
        this.vimeoPicturesDTO = vimeoPicturesDTO;
    }

    public VimeoResponseMetaDataDTO getVimeoResponseMetadataDTO()
    {
        return vimeoResponseMetadataDTO;
    }

    public void setVimeoResponseMetadataDTO(VimeoResponseMetaDataDTO vimeoResponseMetadataDTO)
    {
        this.vimeoResponseMetadataDTO = vimeoResponseMetadataDTO;
    }
}
