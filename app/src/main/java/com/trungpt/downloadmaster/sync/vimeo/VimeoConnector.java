package com.trungpt.downloadmaster.sync.vimeo;

import com.trungpt.downloadmaster.sync.common.RestfulService;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.sync.vimeo.request.VimeoRequestDTO;
import com.trungpt.downloadmaster.sync.vimeo.user.UserVimeoResponseDTO;
import com.trungpt.downloadmaster.sync.vimeo.user.UserVimeoResponseDetailDTO;
import com.trungpt.downloadmaster.ui.adapter.User;
import com.trungpt.downloadmaster.ui.adapter.UserPage;
import com.trungpt.downloadmaster.ui.adapter.Video;
import com.trungpt.downloadmaster.ui.adapter.VideoPage;
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
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .search("videos"
                        , vimeoRequestDTO.getKeyWord()
                        , vimeoRequestDTO.getSort()
                        , vimeoRequestDTO.getDirection()
                        , vimeoRequestDTO.getFilter()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        List<Video> videos = new ArrayList<>();
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

    public VideoPage videoCategories(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .videosCategories(vimeoRequestDTO.getCategory()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertToVideoPage(vimeoResponseDTO);
    }

    public VideoPage convertToVideoPage(VimeoResponseDTO vimeoResponseDTO)
    {
        List<Video> videos = new ArrayList<>();
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

    public UserPage searchUser(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        UserVimeoResponseDTO userVimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .searchUser(vimeoRequestDTO.getKeyWord()
                        , vimeoRequestDTO.getSort()
                        , vimeoRequestDTO.getDirection()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        List<User> users = new ArrayList<>();
        if (userVimeoResponseDTO != null)
        {
            List<UserVimeoResponseDetailDTO> userVimeoResponseDetailDTOs = userVimeoResponseDTO.getUserVimeoResponseDetailDTOs();
            for (UserVimeoResponseDetailDTO userVimeoResponseDetailDTO : userVimeoResponseDetailDTOs)
            {
                User user = new User();
                user.setUrl("https://vimeo.com/" + userVimeoResponseDetailDTO.getUri());
                if (userVimeoResponseDetailDTO.getVimeoPicturesDTO() != null)
                {
                    VimeoResponsePicturesDTO vimeoResponsePicturesDTO = userVimeoResponseDetailDTO.getVimeoPicturesDTO();
                    List<VimeoResponsePictureSizeDTO> vimeoResponsePicturesDTOs
                            = vimeoResponsePicturesDTO.getVimeoPicturesSizeDTO();
                    user.setUrlCover(userVimeoResponseDetailDTO.getVimeoPicturesDTO().getVimeoPicturesSizeDTO().get(vimeoResponsePicturesDTOs.size() - 1).getLink());
                }
                user.setName(userVimeoResponseDetailDTO.getName());
                user.setDescription(userVimeoResponseDetailDTO.getBio());
                user.setFollows(userVimeoResponseDetailDTO.getVimeoResponseMetadataDTO()
                        .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseFollowingDTO().getTotal());
                user.setVideos(userVimeoResponseDetailDTO.getVimeoResponseMetadataDTO()
                        .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseVideoDTO().getTotal());
                users.add(user);
            }
        }
        UserPage userPage = new UserPage(users);
        userPage.setNextPage(userVimeoResponseDTO.getPaging().getNextPage());
        return userPage;
    }
}
