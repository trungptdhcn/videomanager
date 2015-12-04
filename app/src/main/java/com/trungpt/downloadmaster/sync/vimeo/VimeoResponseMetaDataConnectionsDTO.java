package com.trungpt.downloadmaster.sync.vimeo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponseMetaDataConnectionsDTO
{
    @SerializedName("likes")
    private VimeoResponseLikesDTO vimeoResponseLikesDTO;
    @SerializedName("following")
    private VimeoResponseFollowingDTO vimeoResponseFollowingDTO;
    @SerializedName("videos")
    private VimeoResponseVideoDTO vimeoResponseVideoDTO;
    public VimeoResponseLikesDTO getVimeoResponseLikesDTO()
    {
        return vimeoResponseLikesDTO;
    }

    public void setVimeoResponseLikesDTO(VimeoResponseLikesDTO vimeoResponseLikesDTO)
    {
        this.vimeoResponseLikesDTO = vimeoResponseLikesDTO;
    }

    public VimeoResponseFollowingDTO getVimeoResponseFollowingDTO()
    {
        return vimeoResponseFollowingDTO;
    }

    public void setVimeoResponseFollowingDTO(VimeoResponseFollowingDTO vimeoResponseFollowingDTO)
    {
        this.vimeoResponseFollowingDTO = vimeoResponseFollowingDTO;
    }

    public VimeoResponseVideoDTO getVimeoResponseVideoDTO()
    {
        return vimeoResponseVideoDTO;
    }

    public void setVimeoResponseVideoDTO(VimeoResponseVideoDTO vimeoResponseVideoDTO)
    {
        this.vimeoResponseVideoDTO = vimeoResponseVideoDTO;
    }
}
