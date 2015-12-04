package com.trungpt.downloadmaster.sync.vimeo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponseUserDTO
{
    private String name;
    private String location;
    @SerializedName("metadata")
    private VimeoResponseMetaDataDTO vimeoResponseMetaDataDTO;
    @SerializedName("pictures")
    private VimeoResponsePicturesDTO vimeoResponsePicturesDTO;
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public VimeoResponseMetaDataDTO getVimeoResponseMetaDataDTO()
    {
        return vimeoResponseMetaDataDTO;
    }

    public void setVimeoResponseMetaDataDTO(VimeoResponseMetaDataDTO vimeoResponseMetaDataDTO)
    {
        this.vimeoResponseMetaDataDTO = vimeoResponseMetaDataDTO;
    }

    public VimeoResponsePicturesDTO getVimeoResponsePicturesDTO()
    {
        return vimeoResponsePicturesDTO;
    }

    public void setVimeoResponsePicturesDTO(VimeoResponsePicturesDTO vimeoResponsePicturesDTO)
    {
        this.vimeoResponsePicturesDTO = vimeoResponsePicturesDTO;
    }
}
