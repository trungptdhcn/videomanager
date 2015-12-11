package com.trungpt.downloadmaster.sync.vimeo.channel;

import com.google.gson.annotations.SerializedName;
import com.trungpt.downloadmaster.sync.vimeo.VimeoResponseMetaDataDTO;
import com.trungpt.downloadmaster.sync.vimeo.VimeoResponsePicturesDTO;

/**
 * Created by Trung on 12/4/2015.
 */
public class ChannelVimeoResponseDetailDTO
{
    private String uri;
    private String name;
    @SerializedName("description")
    private String description;
    private String link;
    @SerializedName("user")
    private UserVimeoOfChannelResponseDTO userVimeoOfChannelResponseDTO;
    @SerializedName("header")
    private ChannelVimeoHeaderResponseDTO imageCover;
    @SerializedName("pictures")
    private VimeoResponsePicturesDTO vimeoPicturesDTO;
    @SerializedName("metadata")
    private VimeoResponseMetaDataDTO vimeoResponseMetadataDTO;

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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public ChannelVimeoHeaderResponseDTO getImageCover()
    {
        return imageCover;
    }

    public void setImageCover(ChannelVimeoHeaderResponseDTO imageCover)
    {
        this.imageCover = imageCover;
    }

    public UserVimeoOfChannelResponseDTO getUserVimeoOfChannelResponseDTO()
    {
        return userVimeoOfChannelResponseDTO;
    }

    public void setUserVimeoOfChannelResponseDTO(UserVimeoOfChannelResponseDTO userVimeoOfChannelResponseDTO)
    {
        this.userVimeoOfChannelResponseDTO = userVimeoOfChannelResponseDTO;
    }
}
