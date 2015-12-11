package com.trungpt.downloadmaster.sync.youtube;

import android.content.Context;
import android.util.Log;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.Video;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.Configs;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.ui.adapter.*;
import com.trungpt.downloadmaster.ui.utils.Constant;

import javax.xml.datatype.Duration;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Trung on 11/25/2015.
 */
public class YoutubeConnector
{
    private YouTube youtube;
    private YouTube.Search.List searchQuery;
    private YouTube.Videos.List searchForVideoDetails;
    private YouTube.Channels.List searchForChannelDetails;
    private YouTube.Playlists.List searchForPlayListDetails;
    private YouTube.Videos.List mostPopularQuery;
    public static long MAX_RESULT = 5l;

    public YoutubeConnector(Context context)
    {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer()
        {
            @Override
            public void initialize(HttpRequest hr) throws IOException
            {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try
        {
            mostPopularQuery = youtube.videos().list("id,snippet,contentDetails,statistics");
            mostPopularQuery.setKey(Configs.KEY_YOUTUBE);
            mostPopularQuery.setMaxResults(MAX_RESULT);
        }
        catch (IOException e)
        {
            Log.d("YC", "Could not initialize query most popular: " + e);
        }

        try
        {
            searchQuery = youtube.search().list("id,snippet");
            searchQuery.setKey(Configs.KEY_YOUTUBE);
            searchQuery.setMaxResults(MAX_RESULT);

            searchForVideoDetails = youtube.videos().list("statistics,snippet,contentDetails");
            searchForVideoDetails.setKey(Configs.KEY_YOUTUBE);

            searchForPlayListDetails = youtube.playlists().list("snippet,contentDetails");
            searchForPlayListDetails.setKey(Configs.KEY_YOUTUBE);
        }
        catch (IOException e)
        {
            Log.d("YC", "Could not initialize search: " + e);
        }
    }

    public com.trungpt.downloadmaster.ui.adapter.Video getAuthorInfo(String authorId)
    {
        com.trungpt.downloadmaster.ui.adapter.Video video = null;
        try
        {
            YouTube.Channels.List searchChannel = youtube.channels().list("statistics,snippet,contentDetails");
            searchChannel.setKey(Configs.KEY_YOUTUBE);
            searchChannel.setId(authorId);
            ChannelListResponse channelListResponse = searchChannel.execute();
            if (channelListResponse.getItems() != null && channelListResponse.getItems().size() > 0)
            {
                Channel channel = channelListResponse.getItems().get(0);
                video = new com.trungpt.downloadmaster.ui.adapter.Video();
                video.setAuthorAvatarUrl(channel.getSnippet().getThumbnails().getDefault().getUrl());
                video.setSubscribe(channel.getStatistics().getSubscriberCount());
            }
        }
        catch (IOException e)
        {
            Log.d(YoutubeConnector.class.getName(), "Could not search: " + e);
            return null;
        }
        return video;
    }

    public VideoPage search(RequestDTO requestDTO)
    {
        YoutubeRequestDTO youtubeRequestDTO = (YoutubeRequestDTO) requestDTO;
        try
        {
            if (StringUtils.isNotEmpty(youtubeRequestDTO.getKeyWord()))
            {
                searchQuery.setQ(youtubeRequestDTO.getKeyWord());
            }
            if (StringUtils.isNotEmpty(youtubeRequestDTO.getLocation()))
            {
                searchQuery.setLocation(youtubeRequestDTO.getLocation());
            }
            if (StringUtils.isNotEmpty(youtubeRequestDTO.getOrder()))
            {
                searchQuery.setOrder(youtubeRequestDTO.getOrder());
            }
            if (StringUtils.isNotEmpty(youtubeRequestDTO.getRegionCode()))
            {
                searchQuery.setRegionCode(youtubeRequestDTO.getRegionCode());
            }
            String videoDefinition = youtubeRequestDTO.getVideoDefinition();
            String videoDimension = youtubeRequestDTO.getVideoDimension();
            String videoDuration = youtubeRequestDTO.getVideoDuration();
            String videoType = youtubeRequestDTO.getVideoType();
            if (StringUtils.isNotEmpty(videoDefinition)
                    || StringUtils.isNotEmpty(videoDuration)
                    || StringUtils.isNotEmpty(videoDimension)
                    || StringUtils.isNotEmpty(videoType))
            {
                searchQuery.setType("video");
            }
            else
            {
                searchQuery.setType(youtubeRequestDTO.getType());
            }
            if (StringUtils.isNotEmpty(videoDefinition))
            {
                searchQuery.setVideoDefinition(videoDefinition);
            }
            if (StringUtils.isNotEmpty(videoDimension))
            {
                searchQuery.setVideoDimension(videoDimension);
            }
            if (StringUtils.isNotEmpty(videoDuration))
            {
                searchQuery.setVideoDuration(videoDuration);
            }
            if (StringUtils.isNotEmpty(videoType))
            {
                searchQuery.setVideoType(videoType);
            }
            searchQuery.setMaxResults(MAX_RESULT);
            searchQuery.setPageToken(youtubeRequestDTO.getPageToken());
            //Get normal data
            SearchListResponse response = searchQuery.execute();
            List<SearchResult> results = response.getItems();
            List<Item> videos = new ArrayList<>();
//            ===================================
            String ids = "";
            if (youtubeRequestDTO.getType().equals(Constant.YOUTUBE_VIDEOS))
            {
                for (SearchResult result : results)
                {
                    ids = ids + result.getId().getVideoId() + ",";
                }
                searchForVideoDetails.setId(ids);
                VideoListResponse resultDetails = searchForVideoDetails.execute();
                for (com.google.api.services.youtube.model.Video videoYoutube : resultDetails.getItems())
                {
                    videos.add(convertTovideo(videoYoutube));
                }
            }
            else if(youtubeRequestDTO.getType().equals(Constant.YOUTUBE_PLAYLIST))
            {
                for (SearchResult result : results)
                {
                    ids = ids + result.getId().getPlaylistId() + ",";
                }
                searchForPlayListDetails.setId(ids);
                PlaylistListResponse resultDetails = searchForPlayListDetails.execute();
                for (Playlist playlist : resultDetails.getItems())
                {
                    videos.add(convertToPlayList(playlist));
                }
            }

            else
            {
                for (SearchResult result : results)
                {
                    ids = ids + result.getId().getChannelId() + ",";
                }
                searchForChannelDetails.setId(ids);
                ChannelListResponse resultDetails = searchForChannelDetails.execute();
                for (Channel channel : resultDetails.getItems())
                {
                    videos.add(convertToChannel(channel));
                }
            }
            VideoPage videoPage = new VideoPage(videos);
            if (response.getPageInfo() != null)
            {
                videoPage.setNextPage(response.getNextPageToken());
            }
            return videoPage;
        }
        catch (IOException e)
        {
            Log.d(YoutubeConnector.class.getName(), "Could not search: " + e);
            return null;
        }
    }

    private Item convertToChannel(Channel channel)
    {

        return new Item();
    }

    public VideoPage mostPopulars(RequestDTO requestDTO)
    {
        mostPopularQuery.setChart(Constant.MOST_POPULAR);
        mostPopularQuery.setRegionCode(((YoutubeRequestDTO) requestDTO).getRegionCode());
        try
        {
            VideoListResponse response = mostPopularQuery.execute();
            List<com.google.api.services.youtube.model.Video> items = response.getItems();
            List<Item> videos = new ArrayList<>();
            if (items != null && items.size() > 0)
            {
                for (com.google.api.services.youtube.model.Video videoYoutube : items)
                {
                    videos.add(convertTovideo(videoYoutube));
                }
            }
            VideoPage videoPage = new VideoPage(videos);
            if (response.getPageInfo() != null)
            {
                videoPage.setNextPage(response.getNextPageToken());
            }
            return videoPage;
        }
        catch (IOException e)
        {
            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }

    public com.trungpt.downloadmaster.ui.adapter.Video convertTovideo(com.google.api.services.youtube.model.Video videoYoutube)
    {
        com.trungpt.downloadmaster.ui.adapter. Video videoModel = new com.trungpt.downloadmaster.ui.adapter.Video();
        videoModel.setTitle(videoYoutube.getSnippet().getTitle());
        videoModel.setDescription(videoYoutube.getSnippet().getDescription());
        videoModel.setUrlThumbnail(videoYoutube.getSnippet().getThumbnails().getMedium().getUrl());
        videoModel.setUrl("https://www.youtube.com/watch?v=" + videoYoutube.getId());
        videoModel.setViews(videoYoutube.getStatistics().getViewCount());
        videoModel.setLikes(videoYoutube.getStatistics().getLikeCount());
        videoModel.setDisLikes(videoYoutube.getStatistics().getDislikeCount());
        videoModel.setAuthor(videoYoutube.getSnippet().getChannelTitle());
        videoModel.setId(videoYoutube.getId());
        String duration = videoYoutube.getContentDetails().getDuration();
        videoModel.setDuration(getTimeFromString(duration));
        videoModel.setAuthorId(videoYoutube.getSnippet().getChannelId());
        return videoModel;
    }

    public com.trungpt.downloadmaster.ui.adapter.Channel convertToPlayList(Playlist playlist)
    {
        com.trungpt.downloadmaster.ui.adapter.Channel channel = new com.trungpt.downloadmaster.ui.adapter.Channel();
        channel.setName(playlist.getSnippet().getTitle());
        channel.setDescription(playlist.getSnippet().getDescription());
        channel.setUrlCover(playlist.getSnippet().getThumbnails().getMedium().getUrl());
        channel.setUrl("https://www.youtube.com/watch?v=" + playlist.getId());
//        channel.setUserOffollows(playlist.getC);
//        channel.setLikes(playlist.getStatistics().getLikeCount());
//        channel.set(playlist.getStatistics().getDislikeCount());
        channel.setNameOfUser(playlist.getSnippet().getChannelTitle());
        channel.setId(playlist.getId());
        channel.setVideos(BigInteger.valueOf(playlist.getContentDetails().getItemCount()));
//        String duration = playlist.getContentDetails().getDuration();
//        channel.setDuration(getTimeFromString(duration));
//        channel.setAuthorId(playlist.getSnippet().getChannelId());
        return channel;
    }

    public String getDuration(String durationStr) throws ParseException
    {
        DateFormat df = new SimpleDateFormat("'PT'mm'M'ss'S'");
        Date d = df.parse(durationStr);
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        c.setTimeZone(TimeZone.getDefault());
        String hour = String.valueOf(c.get(Calendar.HOUR));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String second = String.valueOf(c.get(Calendar.SECOND));
        String duraion = "";
        if (StringUtils.isNotEmpty(hour))
        {
            duraion += hour + ":";
        }
        if (StringUtils.isNotEmpty(minute))
        {
            duraion += minute + ":";
        }
        if (StringUtils.isNotEmpty(second))
        {
            duraion += second;
        }
        return duraion;
    }

    private String getTimeFromString(String duration)
    {
        String time = "";
        boolean hourexists = false, minutesexists = false, secondsexists = false;
        if (duration.contains("H"))
        {
            hourexists = true;
        }
        if (duration.contains("M"))
        {
            minutesexists = true;
        }
        if (duration.contains("S"))
        {
            secondsexists = true;
        }
        if (hourexists)
        {
            String hour = "";
            hour = duration.substring(duration.indexOf("T") + 1,
                    duration.indexOf("H"));
            if (hour.length() == 1)
            {
                hour = "0" + hour;
            }
            time += hour + ":";
        }
        if (minutesexists)
        {
            String minutes = "";
            if (hourexists)
            {
                minutes = duration.substring(duration.indexOf("H") + 1,
                        duration.indexOf("M"));
            }
            else
            {
                minutes = duration.substring(duration.indexOf("T") + 1,
                        duration.indexOf("M"));
            }
            if (minutes.length() == 1)
            {
                minutes = "0" + minutes;
            }
            time += minutes + ":";
        }
        else
        {
            time += "00:";
        }
        if (secondsexists)
        {
            String seconds = "";
            if (hourexists)
            {
                if (minutesexists)
                {
                    seconds = duration.substring(duration.indexOf("M") + 1,
                            duration.indexOf("S"));
                }
                else
                {
                    seconds = duration.substring(duration.indexOf("H") + 1,
                            duration.indexOf("S"));
                }
            }
            else if (minutesexists)
            {
                seconds = duration.substring(duration.indexOf("M") + 1,
                        duration.indexOf("S"));
            }
            else
            {
                seconds = duration.substring(duration.indexOf("T") + 1,
                        duration.indexOf("S"));
            }
            if (seconds.length() == 1)
            {
                seconds = "0" + seconds;
            }
            time += seconds;
        }
        return time;
    }
}
