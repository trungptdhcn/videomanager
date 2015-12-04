package com.trungpt.downloadmaster.sync.youtube;

import android.content.Context;
import android.util.Log;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.sync.Configs;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.ui.adapter.Video;
import com.trungpt.downloadmaster.ui.adapter.VideoPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/25/2015.
 */
public class YoutubeConnector
{
    private YouTube youtube;
    private YouTube.Search.List searchQuery;
    private YouTube.Videos.List searchForDetails;
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
            mostPopularQuery = youtube.videos().list("id,snippet");
            mostPopularQuery.setKey(Configs.KEY_YOUTUBE);
            mostPopularQuery.setMaxResults(30l);
        }
        catch (IOException e)
        {
            Log.d("YC", "Could not initialize query most popular: " + e);
        }

        try
        {
            searchQuery = youtube.search().list("id,snippet");
            searchQuery.setKey(Configs.KEY_YOUTUBE);
            searchQuery.setType("video");
        }
        catch (IOException e)
        {
            Log.d("YC", "Could not initialize search: " + e);
        }
    }

    public Video getAuthorInfo(String authorId)
    {
        Video video = null;
        try
        {
            YouTube.Channels.List searchChannel = youtube.channels().list("statistics,snippet");
            searchChannel.setKey(Configs.KEY_YOUTUBE);
            searchChannel.setId(authorId);
            ChannelListResponse channelListResponse = searchChannel.execute();
            if (channelListResponse.getItems() != null && channelListResponse.getItems().size() > 0)
            {
                Channel channel = channelListResponse.getItems().get(0);
                video = new Video();
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
            searchQuery.setQ(youtubeRequestDTO.getKeyWord());
            searchQuery.setLocation(youtubeRequestDTO.getLocation());
            searchQuery.setOrder(youtubeRequestDTO.getOrder());
            searchQuery.setRegionCode(youtubeRequestDTO.getRegionCode());
            searchQuery.setTopicId(youtubeRequestDTO.getTopic());
            searchQuery.setType(youtubeRequestDTO.getType());
            searchQuery.setVideoDefinition(youtubeRequestDTO.getVideoDefinition());
            searchQuery.setVideoDimension(youtubeRequestDTO.getVideoDimension());
            searchQuery.setVideoDuration(youtubeRequestDTO.getVideoDuration());
            searchQuery.setVideoLicense(youtubeRequestDTO.getVideoLicense());
            searchQuery.setVideoType(youtubeRequestDTO.getVideoType());
            searchQuery.setMaxResults(MAX_RESULT);
            searchQuery.setPageToken(youtubeRequestDTO.getPageToken());

            SearchListResponse response = searchQuery.execute();
            List<SearchResult> results = response.getItems();

            List<Video> videos = new ArrayList<Video>();
            String ids = "";
            for (SearchResult result : results)
            {
                ids = ids + result.getId().getVideoId() + ",";
            }

            VideoPage videoPage = new VideoPage(videos);
            searchForDetails = youtube.videos().list("statistics,snippet");
            searchForDetails.setKey(Configs.KEY_YOUTUBE);
            searchForDetails.setId(ids);
            VideoListResponse resultDetails = searchForDetails.execute();
            for (com.google.api.services.youtube.model.Video videoYoutube : resultDetails.getItems())
            {
                Video video = new Video();
                video.setTitle(videoYoutube.getSnippet().getTitle());
                video.setDescription(videoYoutube.getSnippet().getDescription());
                video.setUrlThumbnail(videoYoutube.getSnippet().getThumbnails().getMedium().getUrl());
                video.setUrl("https://www.youtube.com/watch?v=" + videoYoutube.getId());
                video.setViews(videoYoutube.getStatistics().getViewCount());
                video.setLikes(videoYoutube.getStatistics().getLikeCount());
                video.setDisLikes(videoYoutube.getStatistics().getDislikeCount());
                video.setAuthor(videoYoutube.getSnippet().getChannelTitle());
                video.setId(videoYoutube.getId());
                video.setAuthorId(videoYoutube.getSnippet().getChannelId());
                videos.add(video);
            }
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
}
