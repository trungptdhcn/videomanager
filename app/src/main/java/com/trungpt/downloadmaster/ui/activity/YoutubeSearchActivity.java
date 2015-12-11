package com.trungpt.downloadmaster.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.dailymotion.request.DailymotionRequestDTO;
import com.trungpt.downloadmaster.sync.youtube.YoutubeRequestDTO;
import com.trungpt.downloadmaster.ui.adapter.*;
import com.trungpt.downloadmaster.ui.adapter.model.VimeoCategory;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskMostPopular;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskSearchData;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.EndlessScrollListener;
import com.trungpt.downloadmaster.ui.utils.Constant;
import com.trungpt.downloadmaster.ui.utils.ResourceUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class YoutubeSearchActivity extends Activity implements AsyncTaskListener
{

    @Bind(R.id.btAdvancedFilter)
    ImageButton btAdvancedFilter;
    //=============================
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.edtSearch)
    EditText edtSearch;

    Dialog dialog;
    YoutubeRequestDTO requestDTO;
    CommonAdapter adapter;
    String nextPage;


//    String[] contentType = new String[]{Constant.YOUTUBE_ALL
//            , Constant.YOUTUBE_VIDEOS, Constant.YOUTUBE_CHANNELS, Constant.YOUTUBE_PLAYLIST};
    String[] uploads = new String[]{Constant.YOUTUBE_UPLOAD_ALL
            , Constant.YOUTUBE_UPLOAD_TODAY, Constant.YOUTUBE_UPLOAD_THISWEEK
            , Constant.YOUTUBE_UPLOAD_THISMONTH, Constant.YOUTUBE_UPLOAD_THISYEAR};
//    String[] duration = new String[]{Constant.YOUTUBE_DURATION_ALL
//            , Constant.YOUTUBE_DURATION_SHORT, Constant.YOUTUBE_DURATION_MEDIUM, Constant.YOUTUBE_DURATION_LONG};

//    String[] sort = new String[]{Constant.YOUTUBE_SORT_RELEVANCE
//            , Constant.YOUTUBE_SORT_DATE
//            , Constant.YOUTUBE_SORT_RATING
//            , Constant.YOUTUBE_SORT_ALPHABETA
//            , Constant.YOUTUBE_SORT_VIDEOCOUNT
//            , Constant.YOUTUBE_SORT_VIEWCOUNT};
    SpinnerCategoryAdapter contentTypeAdapter;
    SpinnerCategoryAdapter durationAdapter;
    SpinnerCategoryAdapter countryAdapter;
    SpinnerCategoryAdapter sortAdapter;
    SpinnerCategoryAdapter videoTypeAdapter;
    SpinnerCategoryAdapter videoDefinitionAdapter;
    SpinnerCategoryAdapter videoDimensionAdapter;
    SpinnerCommonAdapter uploadDateAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_search);
        ButterKnife.bind(this);
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPageToken(nextPage);
                    AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(YoutubeSearchActivity.this, Constant.HOST_NAME.YOUTUBE, requestDTO);
                    asyncTask.setListener(YoutubeSearchActivity.this);
                    asyncTask.execute();
                    return true;
                }
                else
                {
                    return false;
                }

            }
        };
        listView.setOnScrollListener(endlessScrollListener);
    }

    @OnClick(R.id.btAdvancedFilter)
    public void filter()
    {
        dialog = new Dialog(this);
        dialog.setTitle("Advanced Filter");
        dialog.setContentView(R.layout.dialog_youtube_advanced_filter);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Spinner spContentType = (Spinner) dialog.findViewById(R.id.spinner_type);
        final Spinner spUploadTime = (Spinner) dialog.findViewById(R.id.spinner_update_time);
        final Spinner spDuration = (Spinner) dialog.findViewById(R.id.spinner_duration);
        final Spinner spCountry = (Spinner) dialog.findViewById(R.id.spinner_country);
        final Spinner spSort = (Spinner) dialog.findViewById(R.id.spinner_sort);
        final Spinner spDefinition = (Spinner) dialog.findViewById(R.id.spinner_definition);
        final Spinner spDimension = (Spinner) dialog.findViewById(R.id.spinner_dimension);
        final Spinner spVideoType = (Spinner) dialog.findViewById(R.id.spinner_videoType);
        Button btCancel = (Button) dialog.findViewById(R.id.dialog_youtube_advanced_filter_btCancel);
        Button btOk = (Button) dialog.findViewById(R.id.dialog_youtube_advanced_filter_btOk);

        uploadDateAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, uploads);
        uploadDateAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
        spUploadTime.setAdapter(uploadDateAdapter);

        try
        {
            List<VimeoCategory> contentTypes = ResourceUtils.getStringResource(this,R.xml.youtube_type);
            contentTypeAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, contentTypes);
            contentTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spContentType.setAdapter(contentTypeAdapter);

            List<VimeoCategory> countries = ResourceUtils.getListCountryCode(this);
            countryAdapter = new SpinnerCategoryAdapter(this, R.layout.spinner_selector_item, countries);
            countryAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spCountry.setAdapter(countryAdapter);

            List<VimeoCategory> durations = ResourceUtils.getStringResource(this,R.xml.youtube_duration);
            durationAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, durations);
            durationAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spDuration.setAdapter(durationAdapter);

            List<VimeoCategory> sorts = ResourceUtils.getStringResource(this,R.xml.youtube_order);
            sortAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, sorts);
            sortAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spSort.setAdapter(sortAdapter);

            List<VimeoCategory> videoTypes = ResourceUtils.getStringResource(this,R.xml.youtube_video_type);
            videoTypeAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, videoTypes);
            videoTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spVideoType.setAdapter(videoTypeAdapter);

            List<VimeoCategory> dimension = ResourceUtils.getStringResource(this,R.xml.youtube_dimension);
            videoDimensionAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, dimension);
            videoDimensionAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spDimension.setAdapter(videoDimensionAdapter);

            List<VimeoCategory> definition = ResourceUtils.getStringResource(this,R.xml.youtube_definition);
            videoDefinitionAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, definition);
            videoDefinitionAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spDefinition.setAdapter(videoDefinitionAdapter);
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        btCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adapter = null;
                String contentType = ((VimeoCategory) spContentType.getSelectedItem()).getId();
                String region = ((VimeoCategory) spCountry.getSelectedItem()).getId();
                String duration = ((VimeoCategory) spDuration.getSelectedItem()).getId();
                String sort = ((VimeoCategory) spSort.getSelectedItem()).getId();
                String definition = ((VimeoCategory) spDefinition.getSelectedItem()).getId();
                String demension = ((VimeoCategory) spDimension.getSelectedItem()).getId();
                String videoType = ((VimeoCategory) spVideoType.getSelectedItem()).getId();
                if (edtSearch.getText() != null)
                {
                    requestDTO = new YoutubeRequestDTO
                            .YoutubeRequestBuilder(edtSearch.getText().toString())
                            .type(contentType)
                            .regionCode(region)
                            .videoDuration(duration)
                            .order(sort)
                            .videoDefinition(definition)
                            .videoDimension(demension)
                            .videoType(videoType)
                            .build();
                }
                search();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void search()
    {
        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, Constant.HOST_NAME.YOUTUBE, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
    }

    @Override
    public void prepare()
    {
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void complete(Object obj)
    {
        VideoPage videoPage = (VideoPage) obj;
        if (videoPage != null)
        {
            List<Item> videos = videoPage.getVideos();
            nextPage = videoPage.getNextPage();
            if (adapter == null)
            {
                adapter = new CommonAdapter(videos, this);
                listView.setAdapter(adapter);

            }
            else
            {
                adapter.getVideos().addAll(videos);
                adapter.notifyDataSetChanged();
            }
        }
        progressBar.setVisibility(View.GONE);
    }
}
