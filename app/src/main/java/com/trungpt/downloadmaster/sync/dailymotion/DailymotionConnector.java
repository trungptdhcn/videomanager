package com.trungpt.downloadmaster.sync.dailymotion;

import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.common.RestfulService;
import com.trungpt.downloadmaster.sync.dailymotion.request.DailymotionRequestDTO;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.ui.adapter.Channel;
import com.trungpt.downloadmaster.ui.adapter.Item;
import com.trungpt.downloadmaster.ui.adapter.Video;
import com.trungpt.downloadmaster.ui.adapter.VideoPage;
import com.trungpt.downloadmaster.ui.utils.Constant;

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
        if (dailymotionRequestDTO.getType().equals(Constant.DAILYMOTION_PLAYLIST))
        {
            return seachPlayList(dailymotionRequestDTO);

        }
        else
        {
            return seachVideo(dailymotionRequestDTO);
        }
    }

    public VideoPage seachVideo(DailymotionRequestDTO dailymotionRequestDTO)
    {
        String keyword = null;
        if (StringUtils.isNotEmpty(dailymotionRequestDTO.getKeyWord()))
        {
            keyword = dailymotionRequestDTO.getKeyWord();
        }
        DailymotionDTO dailymotionDTO = RestfulService.getInstance(Constant.HOST_NAME.DAILYMOTION).searchDailymotion(
                keyword
                , dailymotionRequestDTO.getFields()
                , dailymotionRequestDTO.getFlags()
                , dailymotionRequestDTO.getSort()
                , dailymotionRequestDTO.getCountry()
                , dailymotionRequestDTO.getLongerThan()
                , dailymotionRequestDTO.getShorterThan()
                , dailymotionRequestDTO.getPage()
                , dailymotionRequestDTO.getLimit());
        return convertToVideo(dailymotionDTO);
    }

    public VideoPage seachPlayList(DailymotionRequestDTO dailymotionRequestDTO)
    {
        DailymotionPlayListDTO dailymotionPlayListDTO
                = RestfulService.getInstance(Constant.HOST_NAME.DAILYMOTION).searchDailymotionPlaylist(
                dailymotionRequestDTO.getKeyWord()
                , dailymotionRequestDTO.getFields()
                , dailymotionRequestDTO.getSort()
                , dailymotionRequestDTO.getPage()
                , dailymotionRequestDTO.getLimit());
        List<Item> playlist = new ArrayList<>();
        if (dailymotionPlayListDTO != null)
        {
            List<DailymotionPlayistDetailDTO> dailymotionPlayistDetailDTOs = dailymotionPlayListDTO.getDailymotionPlaylistDetailDTO();
            for (DailymotionPlayistDetailDTO dailymotionPlayistDetailDTO : dailymotionPlayistDetailDTOs)
            {
                Channel channel = new Channel();
                channel.setId(dailymotionPlayistDetailDTO.getId());
                channel.setName(dailymotionPlayistDetailDTO.getName());
                channel.setDescription(dailymotionPlayistDetailDTO.getDescription());
                channel.setUrlUserAvatar(dailymotionPlayistDetailDTO.getAvatarUrl());
                channel.setUrlCover(dailymotionPlayistDetailDTO.getCoverUrl());
                channel.setFollows(dailymotionPlayistDetailDTO.getFollows());
                channel.setVideos(dailymotionPlayistDetailDTO.getVideos());
                channel.setUserOffollows(dailymotionPlayistDetailDTO.getFollows());
                playlist.add(channel);
            }
        }
        VideoPage videoPage = new VideoPage(playlist);
        videoPage.setNextPage(dailymotionPlayListDTO.getPage());
        return videoPage;
    }

    private VideoPage convertToVideo(DailymotionDTO dailymotionDTO)
    {
        List<Item> videos = new ArrayList<>();
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
                    int duration = dailymotionDetailDTO.getDuration();
                    int minutes = duration / 60;
                    int seconds = duration % 60;
                    String str = String.format("%d:%02d", minutes, seconds);
                    video.setDuration(str);
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

    public VideoPage mostPopular(RequestDTO requestDTO)
    {
        DailymotionRequestDTO dailymotionRequestDTO = (DailymotionRequestDTO) requestDTO;
        DailymotionDTO dailymotionDTO = RestfulService.getInstance(Constant.HOST_NAME.DAILYMOTION).mostPopular(
                dailymotionRequestDTO.getFields()
                , dailymotionRequestDTO.getFlags()
                , dailymotionRequestDTO.getSort()
                , dailymotionRequestDTO.getPage()
                , dailymotionRequestDTO.getLimit());
        return convertToVideo(dailymotionDTO);
    }

    public VideoPage getVideosByUser(RequestDTO requestDTO)
    {
        DailymotionRequestDTO dailymotionRequestDTO = (DailymotionRequestDTO) requestDTO;
        DailymotionDTO dailymotionDTO = RestfulService.getInstance(Constant.HOST_NAME.DAILYMOTION)
                .getVideoByPlaylistId(
                        dailymotionRequestDTO.getPlaylistId()
                        , dailymotionRequestDTO.getFields()
                        , dailymotionRequestDTO.getSort()
                        , dailymotionRequestDTO.getPage()
                        , dailymotionRequestDTO.getLimit());
        return convertToVideo(dailymotionDTO);
    }
}
