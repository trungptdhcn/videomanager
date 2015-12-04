package com.trungpt.downloadmaster.sync.vimeo.user;

import com.google.gson.annotations.SerializedName;
import com.trungpt.downloadmaster.sync.vimeo.VimeoPagingResponseDTO;
import com.trungpt.downloadmaster.sync.vimeo.VimeoResponseDetailDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 12/4/2015.
 */
public class UserVimeoResponseDTO
{
    private VimeoPagingResponseDTO paging;
    @SerializedName("data")
    private List<UserVimeoResponseDetailDTO> userVimeoResponseDetailDTOs = new ArrayList<>();

    public VimeoPagingResponseDTO getPaging()
    {
        return paging;
    }

    public void setPaging(VimeoPagingResponseDTO paging)
    {
        this.paging = paging;
    }

    public List<UserVimeoResponseDetailDTO> getUserVimeoResponseDetailDTOs()
    {
        return userVimeoResponseDetailDTOs;
    }

    public void setUserVimeoResponseDetailDTOs(List<UserVimeoResponseDetailDTO> userVimeoResponseDetailDTOs)
    {
        this.userVimeoResponseDetailDTOs = userVimeoResponseDetailDTOs;
    }
}
