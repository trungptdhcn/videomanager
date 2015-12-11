package com.trungpt.downloadmaster.ui.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import com.trungpt.downloadmaster.sync.dailymotion.DailymotionConnector;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.sync.vimeo.VimeoConnector;
import com.trungpt.downloadmaster.sync.youtube.YoutubeConnector;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.utils.Constant;

/**
 * Created by trung on 12/7/2015.
 */
public class AsyncTaskGetData extends AsyncTask<Void, Void, Object>
{
    AsyncTaskListener listener;
    private Constant.HOST_NAME host_name;
    YoutubeConnector ytbConnect;
    VimeoConnector vimeoConnect;
    DailymotionConnector dailymotionConnector;
    private RequestDTO requestDTO;

    public AsyncTaskGetData(Context context, Constant.HOST_NAME host_name, RequestDTO requestDTO)
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
        listener.prepare();
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        Object o = null;
        switch (host_name)
        {
            case YOUTUBE:
//                o = ytbConnect.search(requestDTO);
                break;
            case VIMEO:
                o = vimeoConnect.getVideosByUser(requestDTO);
                break;
            case DAILYMOTION:
                o = dailymotionConnector.getVideosByUser(requestDTO);
        }
        return o;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        listener.complete(o);
    }

    public AsyncTaskListener getListener()
    {
        return listener;

    }

    public void setListener(AsyncTaskListener listener)
    {
        this.listener = listener;
    }
}
