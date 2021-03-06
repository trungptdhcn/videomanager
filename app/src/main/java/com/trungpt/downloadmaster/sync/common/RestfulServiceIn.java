package com.trungpt.downloadmaster.sync.common;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.ResponseBody;
import com.trungpt.downloadmaster.sync.dailymotion.DailymotionDTO;
import com.trungpt.downloadmaster.sync.vimeo.VimeoResponseDTO;
import com.trungpt.downloadmaster.sync.vimeo.direct.VimeoDirectDTO;
import com.trungpt.downloadmaster.sync.vimeo.user.UserVimeoResponseDTO;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Trung on 5/30/2015.
 */
public interface RestfulServiceIn
{
//    @GET("/latest")
//    public void getListFeedsWithParams(@Query("page") Integer page, @Query("limit") Integer limit,
//                                       @Query("gid") String gid, @Query("f") Boolean f, @Query("type") String type, Callback<ListFeedDTO> callback);
//
//    @GET("/latest")
//    public  void getListFeeds(Callback<ListFeedDTO> callback);
//
//    @GET("/post")
//    public FeedDetailDTO getFeedDetail(@Query("slug") String slug);
//    @GET("/post")
//    public void getFeedDetail(@Query("slug") String slug, Callback<FeedDetailDTO> callback);

//    @Headers({
//            "Accept: application/vnd.vimeo.*+json; version=3.2",
//            "Authorization: bearer b7cb6935fae643704ecf844b216cacb4"
//    })
//    @GET("/categories")
//    public VimeoCategoriesDTO getCategories();

//    @Headers({
//            "Accept: application/vnd.vimeo.*+json; version=3.2",
//            "Authorization: bearer b7cb6935fae643704ecf844b216cacb4"
//    })
//    @GET("/categories/{category}/videos")
//    public VimeoInfoDTO getVideoOfCategory(@Path("category") String category);

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer b7cb6935fae643704ecf844b216cacb4"
    })
    @GET("/{videos}")
    public VimeoResponseDTO search(@Path("videos") String slug
            , @Query("query") String query
            , @Query("sort") String sort
            , @Query("direction") String direction
            , @Query("filter") String filter
            , @Query("per_page") int perpage
            , @Query("page") String page);

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer b7cb6935fae643704ecf844b216cacb4"
    })
    @GET("/categories/{category}/videos")
    public VimeoResponseDTO videosCategories(@Path("category") String category, @Query("per_page") int perpage, @Query("page") String page);

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer b7cb6935fae643704ecf844b216cacb4"
    })
    @GET("/users")
    public UserVimeoResponseDTO searchUser(
              @Query("query") String query
            , @Query("sort") String sort
            , @Query("direction") String direction
            , @Query("per_page") int perpage
            , @Query("page") String page);


    @GET("/video/{id}/config")
    public void getDirectLink(@Path("id") String id, Callback<VimeoDirectDTO> callback);



//    @Headers({
//            "Accept: application/vnd.vimeo.*+json; version=3.2",
//            "Authorization: bearer b7cb6935fae643704ecf844b216cacb4"
//    })
//    @GET("/videos/{video_id}")
//    public VimeoDTO getVideoById(@Path("video_id") String video_id);

    //    @GET("/videos")
//    public DailymotionDTO getMostPopularVideoDailymotion(@Query("fields") String fields
//            , @Query("flags") String flags, @Query("sort") String sort
//            , @Query("page") Long page, @Query("limit") Long limit);
//    @GET("/channel/{id}/videos")
//    public DailymotionDTO getVideosOfChannel(@Path("id") String id
//            , @Query("fields") String fields
//            , @Query("sort") String sort
//            , @Query("page") Long page
//            , @Query("limit") Long limit);
//
    @GET("/videos")
    public DailymotionDTO searchDailymotion(
            @Query("search") String keywords
            , @Query("fields") String fields
            , @Query("flags") String flags
            , @Query("sort") String sort
            , @Query("page") int page
            , @Query("limit") int limit);

}
