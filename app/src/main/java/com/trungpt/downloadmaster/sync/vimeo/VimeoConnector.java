package com.trungpt.downloadmaster.sync.vimeo;

import com.trungpt.downloadmaster.sync.common.RestfulService;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.sync.vimeo.channel.ChannelVimeoHeaderResponseDTO;
import com.trungpt.downloadmaster.sync.vimeo.channel.UserVimeoOfChannelResponseDTO;
import com.trungpt.downloadmaster.sync.vimeo.request.VimeoRequestDTO;
import com.trungpt.downloadmaster.sync.vimeo.channel.ChannelVimeoResponseDetailDTO;
import com.trungpt.downloadmaster.sync.vimeo.channel.ChannelVimeoResponseDTO;
import com.trungpt.downloadmaster.ui.adapter.*;
import com.trungpt.downloadmaster.ui.utils.Constant;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoConnector
{
    public VideoPage search(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        if (((VimeoRequestDTO) requestDTO).getType().equals(Constant.VIMEO_VIDEOS))
        {
            VideoPage videoPage = searchVideo(vimeoRequestDTO);
            return videoPage;
        }
        else
        {
            return searchUser(vimeoRequestDTO);
        }

    }

    private VideoPage searchVideo(VimeoRequestDTO vimeoRequestDTO)
    {
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .search("videos"
                        , vimeoRequestDTO.getKeyWord()
                        , vimeoRequestDTO.getSort()
                        , vimeoRequestDTO.getDirection()
                        , vimeoRequestDTO.getFilter()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertToVideoPage(vimeoResponseDTO);
    }

    public VideoPage videoCategories(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .videosCategories(vimeoRequestDTO.getCategory()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertToVideoPage(vimeoResponseDTO);
    }

    public VideoPage getVideosByUser(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .getVideosByUser(vimeoRequestDTO.getCategory()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertToVideoPage(vimeoResponseDTO);
    }

    public VideoPage convertToVideoPage(VimeoResponseDTO vimeoResponseDTO)
    {
        List<Item> videos = new ArrayList<>();
        if (vimeoResponseDTO != null)
        {
            List<VimeoResponseDetailDTO> vimeoResponseDetailDTOs = vimeoResponseDTO.getVimeoResponseDetailDTOs();
            for (VimeoResponseDetailDTO vimeoResponseDetailDTO : vimeoResponseDetailDTOs)
            {
                Video video = new Video();
                String uri = vimeoResponseDetailDTO.getUri();
                String regex = "[0-9].+$";
                String vimeo_id = "";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(uri);
                while (matcher.find())
                {
                    vimeo_id = matcher.group(0);
                }
                video.setUrl("https://vimeo.com/" + vimeo_id);
                if (vimeoResponseDetailDTO.getVimeoPicturesDTO() != null)
                {
                    VimeoResponsePicturesDTO vimeoResponsePicturesDTO = vimeoResponseDetailDTO.getVimeoPicturesDTO();
                    List<VimeoResponsePictureSizeDTO> vimeoResponsePicturesDTOs
                            = vimeoResponsePicturesDTO.getVimeoPicturesSizeDTO();
                    video.setUrlThumbnail(vimeoResponseDetailDTO.getVimeoPicturesDTO().getVimeoPicturesSizeDTO().get(vimeoResponsePicturesDTOs.size() - 1).getLink());
                }
                video.setTitle(vimeoResponseDetailDTO.getName());
                video.setDescription(vimeoResponseDetailDTO.getDescription());
                video.setLikes(vimeoResponseDetailDTO.getVimeoResponseMetadataDTO()
                        .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseLikesDTO().getTotal());
                video.setViews(vimeoResponseDetailDTO.getVimeoResponseStatsDTO().getPlays());
                video.setDisLikes(new BigInteger("0"));
                video.setId(vimeo_id);
                video.setAuthor(vimeoResponseDetailDTO.getVimeoResponseUserDTO().getName());
                video.setEmbed(vimeoResponseDetailDTO.getEmbedDTO().getHtml());
                int duration = vimeoResponseDetailDTO.getDuration();
                int minutes = duration / (60);
                int seconds = duration % 60;
                String str = String.format("%d:%02d", minutes, seconds);
                video.setDuration(str);
                if (vimeoResponseDetailDTO.getVimeoResponseUserDTO() != null)
                {
                    VimeoResponseUserDTO responseUserDTO = vimeoResponseDetailDTO.getVimeoResponseUserDTO();
                    video.setSubscribe
                            (responseUserDTO.getVimeoResponseMetaDataDTO()
                                    .getVimeoResponseMetaDataConnectionsDTO()
                                    .getVimeoResponseFollowingDTO().getTotal());
                    if (responseUserDTO.getVimeoResponsePicturesDTO() != null)
                    {
                        List<VimeoResponsePictureSizeDTO> vimeoResponsePictureSizeDTOs = responseUserDTO
                                .getVimeoResponsePicturesDTO().getVimeoPicturesSizeDTO();
                        if (vimeoResponsePictureSizeDTOs != null && vimeoResponsePictureSizeDTOs.size() >= 1)
                        {
                            video.setAuthorAvatarUrl(vimeoResponsePictureSizeDTOs.get(1).getLink());
                        }
                    }
                }
                videos.add(video);
            }
        }
        VideoPage videoPage = new VideoPage(videos);
        videoPage.setNextPage(vimeoResponseDTO.getPaging().getNextPage());
        return videoPage;
    }

    public VideoPage searchUser(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        ChannelVimeoResponseDTO channelVimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .searchUser(vimeoRequestDTO.getKeyWord()
                        , vimeoRequestDTO.getSort()
                        , vimeoRequestDTO.getDirection()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        List<Item> channels = new ArrayList<>();
        if (channelVimeoResponseDTO != null)
        {
            List<ChannelVimeoResponseDetailDTO> channelVimeoResponseDetailDTOs = channelVimeoResponseDTO.getChannelVimeoResponseDetailDTOs();
            for (ChannelVimeoResponseDetailDTO channelVimeoResponseDetailDTO : channelVimeoResponseDetailDTOs)
            {
                Channel channel = new Channel();
                String uri = channelVimeoResponseDetailDTO.getUri();
                String regex = "[0-9].+$";
                String channel_id = "";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(uri);
                while (matcher.find())
                {
                    channel_id = matcher.group(0);
                }
                channel.setId(channel_id);
                channel.setUrl("https://vimeo.com/" + channelVimeoResponseDetailDTO.getUri());
                if (channelVimeoResponseDetailDTO.getVimeoPicturesDTO() != null)
                {
                    VimeoResponsePicturesDTO vimeoResponsePicturesDTO = channelVimeoResponseDetailDTO.getVimeoPicturesDTO();
                    List<VimeoResponsePictureSizeDTO> vimeoResponsePicturesDTOs
                            = vimeoResponsePicturesDTO.getVimeoPicturesSizeDTO();
                    channel.setUrlCover(channelVimeoResponseDetailDTO.getVimeoPicturesDTO().getVimeoPicturesSizeDTO().get(vimeoResponsePicturesDTOs.size() - 1).getLink());
                }
                channel.setName(channelVimeoResponseDetailDTO.getName());
                channel.setDescription(channelVimeoResponseDetailDTO.getDescription());
                channel.setFollows(channelVimeoResponseDetailDTO.getVimeoResponseMetadataDTO()
                        .getVimeoResponseMetaDataConnectionsDTO().getUserOfChannel().getTotal());
                channel.setVideos(channelVimeoResponseDetailDTO.getVimeoResponseMetadataDTO()
                        .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseVideoDTO().getTotal());
                if (channelVimeoResponseDetailDTO.getImageCover() != null)
                {
                    ChannelVimeoHeaderResponseDTO channelVimeoHeaderResponseDTO
                            = channelVimeoResponseDetailDTO.getImageCover();
                    if (channelVimeoHeaderResponseDTO.getSizes() != null
                            && channelVimeoHeaderResponseDTO.getSizes().size() > 0)
                    {
                        channel.setUrlCover(channelVimeoHeaderResponseDTO.getSizes().get(
                                channelVimeoHeaderResponseDTO.getSizes().size() - 1).getLink());
                    }
                }
                if (channelVimeoResponseDetailDTO.getUserVimeoOfChannelResponseDTO() != null)
                {
                    UserVimeoOfChannelResponseDTO userOfChannelDTO
                            = channelVimeoResponseDetailDTO.getUserVimeoOfChannelResponseDTO();
                    channel.setNameOfUser(userOfChannelDTO.getName());
                    if (userOfChannelDTO.getVimeoPicturesDTO() != null && userOfChannelDTO.getVimeoPicturesDTO().getVimeoPicturesSizeDTO() != null
                            && userOfChannelDTO.getVimeoPicturesDTO().getVimeoPicturesSizeDTO().size() > 0)
                    {
                        List<VimeoResponsePictureSizeDTO> avatarOfUserOfChannel = userOfChannelDTO.getVimeoPicturesDTO()
                                .getVimeoPicturesSizeDTO();
                        channel.setUrlUserAvatar(avatarOfUserOfChannel.get(avatarOfUserOfChannel.size() - 1).getLink());
                    }
                    channel.setBio(userOfChannelDTO.getBio());
                    channel.setUserOffollows(userOfChannelDTO.getVimeoResponseMetadataDTO()
                            .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseFollowingDTO().getTotal());
                    channel.setLikesOfUser(userOfChannelDTO.getVimeoResponseMetadataDTO()
                            .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseLikesDTO().getTotal());
                    channel.setUserLocation(userOfChannelDTO.getLocation());
                    Matcher userIdMatcher = pattern.matcher(userOfChannelDTO.getUri());
                    while (userIdMatcher.find())
                    {
                        String user_id = userIdMatcher.group(0);
                        channel.setUserId(user_id);
                    }
                }
                channels.add(channel);
            }
        }
        VideoPage videoPage = new VideoPage(channels);
        videoPage.setNextPage(channelVimeoResponseDTO.getPaging().getNextPage());
        return videoPage;
    }
}
