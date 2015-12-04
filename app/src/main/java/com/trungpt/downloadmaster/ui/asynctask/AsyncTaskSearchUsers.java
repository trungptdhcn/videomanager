package com.trungpt.downloadmaster.ui.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import com.trungpt.downloadmaster.sync.dailymotion.DailymotionConnector;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.sync.vimeo.VimeoConnector;
import com.trungpt.downloadmaster.sync.youtube.YoutubeConnector;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskUsersListener;
import com.trungpt.downloadmaster.ui.utils.Constant;

/**
 * Created by Trung on 12/4/2015.
 */
public class AsyncTaskSearchUsers extends AsyncTask<Void, Void, Object>
{
    AsyncTaskUsersListener listener;
    private Constant.HOST_NAME host_name;
    YoutubeConnector ytbConnect;
    VimeoConnector vimeoConnect;
    DailymotionConnector dailymotionConnector;
    private RequestDTO requestDTO;

    public AsyncTaskSearchUsers(Context context, Constant.HOST_NAME host_name, RequestDTO requestDTO)
    {
        this.host_name = host_name;
        this.requestDTO = requestDTO;
        ytbConnect = new YoutubeConnector(context);
        vimeoConnect = new VimeoConnector();
        dailymotionConnector = new DailymotionConnector();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        listener.prepareUser();
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        Object o = null;
        switch (host_name)
        {
            case YOUTUBE:
                break;
            case VIMEO:
                o = vimeoConnect.searchUser(requestDTO);
                break;
            case DAILYMOTION:
        }
        return o;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        listener.completeUser(o);
    }


    public void setListener(AsyncTaskUsersListener listener)
    {
        this.listener = listener;
    }
}
