package com.trungpt.downloadmaster.ui.utils;

/**
 * Created by Trung on 11/25/2015.
 */
public class Constant
{
    public enum HOST_NAME
    {
        YOUTUBE, VIMEO, DAILYMOTION, FACEBOOK, VIMEO_PLAYER
    }

    public enum TYPE_VIDEO
    {
        TYPE_SS(1), TYPE_DASH(2), TYPE_HLS(3), TYPE_OTHER(4);
        private int value;

        TYPE_VIDEO(int value)
        {
            this.value = value;
        }

    }


    public static String VIMEO_BASE_URL = "https://api.vimeo.com";
    public static String VIMEO_PLAYER_BASE_URL = "https://player.vimeo.com";
    public static String DAILYMOTION_BASE_URL = "https://api.dailymotion.com";

    //    ================================VIMEO=======================================
    public final static String VIMEO_VIDEOS ="Videos";
    public final static String VIMEO_USERS ="Users";


}
