package com.trungpt.downloadmaster.sync.dailymotion;

import android.util.Log;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.trungpt.downloadmaster.sync.common.RestfulService;
import com.trungpt.downloadmaster.sync.dailymotion.request.DailymotionRequestDTO;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.ui.adapter.Video;
import com.trungpt.downloadmaster.ui.adapter.VideoPage;
import com.trungpt.downloadmaster.ui.utils.Constant;
import retrofit.Callback;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/25/2015.
 */
public class DailymotionConnector
{
    public VideoPage search(RequestDTO requestDTO)
    {
        DailymotionRequestDTO dailymotionRequestDTO = (DailymotionRequestDTO) requestDTO;
        DailymotionDTO dailymotionDTO = RestfulService.getInstance(Constant.HOST_NAME.DAILYMOTION).searchDailymotion(
                dailymotionRequestDTO.getKeyWord()
                , dailymotionRequestDTO.getFields()
                , dailymotionRequestDTO.getFlags()
                , dailymotionRequestDTO.getSort()
                , dailymotionRequestDTO.getPage()
                , dailymotionRequestDTO.getLimit());
        List<Video> videos = new ArrayList<>();
        if (dailymotionDTO != null)
        {
            List<DailymotionDetailDTO> dailymotionDetailDTOs = dailymotionDTO.getDailymotionDetailDTOs();
            if (dailymotionDetailDTOs != null && dailymotionDetailDTOs.size() > 0)
            {
                for (DailymotionDetailDTO dailymotionDetailDTO : dailymotionDetailDTOs)
                {
                    Video video = new Video();
                    video.setId(dailymotionDetailDTO.getId());
                    video.setTitle(dailymotionDetailDTO.getTitle());
                    video.setUrl(dailymotionDetailDTO.getUrl());
                    video.setUrlThumbnail(dailymotionDetailDTO.getThumbnail_720_url());
                    video.setDescription(dailymotionDetailDTO.getDescription());
                    video.setViews(dailymotionDetailDTO.getViews_total());
                    video.setDuration(dailymotionDetailDTO.getDuration());
                    video.setLikes(new BigInteger("0"));
                    video.setDisLikes(new BigInteger("0"));
                    video.setAuthor(dailymotionDetailDTO.getAuthor());
                    video.setSubscribe(dailymotionDetailDTO.getSubscribe());
                    video.setAuthorAvatarUrl(dailymotionDetailDTO.getAuthorAvatar());
                    videos.add(video);
                }
            }
        }
        VideoPage videoPage = new VideoPage(videos);
        videoPage.setNextPage(dailymotionDTO.getPage());
        return videoPage;
    }
}
