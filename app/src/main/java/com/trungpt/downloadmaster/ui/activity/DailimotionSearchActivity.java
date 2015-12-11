package com.trungpt.downloadmaster.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.dailymotion.request.DailymotionRequestDTO;
import com.trungpt.downloadmaster.ui.adapter.*;
import com.trungpt.downloadmaster.ui.adapter.model.VimeoCategory;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskSearchData;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.EndlessScrollListener;
import com.trungpt.downloadmaster.ui.utils.Constant;
import com.trungpt.downloadmaster.ui.utils.ResourceUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class DailimotionSearchActivity extends Activity implements AsyncTaskListener
        , TextView.OnEditorActionListener
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

    CommonAdapter adapter;
    String nextPage;
    DailymotionRequestDTO requestDTO;
    String[] type = new String[]{Constant.DAILYMOTION_VIDEOS, Constant.DAILYMOTION_PLAYLIST};
    String[] sort_video = new String[]{"Recent", "Visited", "Visited-hour", "Visited-today"
            , "Visited-week", "Visited-month", "Comment", "Relevance", "Random"
            , "Ranking", "Trending", "Old", ""};
    String[] sort_playlist = new String[]{"Recent", "Relevance", "Alpha", "Most"
            , "Least", "AlphaAz", "AlphaZa",};
    //    ============
    SpinnerCommonAdapter choseTypeAdapter;
    SpinnerCommonAdapter choseSortAdapter;
    SpinnerCategoryAdapter choseDurationAdapter;
    SpinnerCategoryAdapter choseCountryAdapter;
    Dialog dialog;
    String field;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailimotion_search);
        ButterKnife.bind(this);
//============================Date========================
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPage(page);
                    AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(DailimotionSearchActivity.this
                            , Constant.HOST_NAME.DAILYMOTION, requestDTO);
                    asyncTask.setListener(DailimotionSearchActivity.this);
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
        edtSearch.setOnEditorActionListener(this);
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

    public void search()
    {
        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, Constant.HOST_NAME.DAILYMOTION, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
    }


    @OnClick(R.id.btAdvancedFilter)
    public void click()
    {
        adapter = null;
        dialog = new Dialog(this);
        dialog.setTitle("Advanced Filter");
        dialog.setContentView(R.layout.dialog_dailymotion_advanced_filter);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Spinner spinnerType = (Spinner) dialog.findViewById(R.id.spinner_type);
        final Spinner spinnerShort = (Spinner) dialog.findViewById(R.id.spinner_sort);
        final Spinner spinnerDuration = (Spinner) dialog.findViewById(R.id.spinner_duration);
        final Spinner spinnerCountry = (Spinner) dialog.findViewById(R.id.spinner_country);
        Button btCancel = (Button) dialog.findViewById(R.id.dialog_dailymotion_advanced_filter_btCancel);
        Button btOk = (Button) dialog.findViewById(R.id.dialog_dailymotion_advanced_filter_btOk);
        final CheckBox cbQuantity = (CheckBox) dialog.findViewById(R.id.cbQuantity);

        choseTypeAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, type);
        choseTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        choseSortAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, sort_video);
        choseSortAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        try
        {
            List<VimeoCategory> durationList = ResourceUtils.getDuaration(this, R.xml.dailymotion_duration);
            choseDurationAdapter = new SpinnerCategoryAdapter(this, R.layout.spinner_selector_item, durationList);
            choseDurationAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            List<VimeoCategory> countries = ResourceUtils.getListCountryCode(this);
            choseCountryAdapter = new SpinnerCategoryAdapter(this, R.layout.spinner_selector_item, countries);
            choseCountryAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        spinnerType.setAdapter(choseTypeAdapter);
        spinnerShort.setAdapter(choseSortAdapter);
        spinnerDuration.setAdapter(choseDurationAdapter);
        spinnerCountry.setAdapter(choseCountryAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if (position == 0)
                {
                    choseSortAdapter.setItems(sort_video);
                    choseSortAdapter.notifyDataSetChanged();
                    field = Constant.DAILYMOTION_VIDEO_FIELDS;
                }
                else
                {
                    choseSortAdapter.setItems(sort_playlist);
                    choseSortAdapter.notifyDataSetChanged();
                    field = Constant.DAILYMOTION_PLAYLIST_FIELDS;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                choseSortAdapter.setItems(sort_video);
                choseSortAdapter.notifyDataSetChanged();
            }

        });

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
                //        =====================Setup Request===================
                String country = ((VimeoCategory) spinnerCountry.getSelectedItem()).getId();
                String visited = (String) spinnerShort.getSelectedItem();
                boolean isHD = cbQuantity.isChecked();
                Integer longerThan = null;
                Integer shorterThan = null;
                if (spinnerDuration.getSelectedItemPosition() == 1)
                {
                    shorterThan = 4;
                }
                else if (spinnerDuration.getSelectedItemPosition() == 2)
                {
                    shorterThan = 10;
                }
                else if (spinnerDuration.getSelectedItemPosition() == 3)
                {
                    longerThan = 10;
                }
                String flag;
                if (isHD)
                {
                    flag = Constant.DAILYMOTION_VIDEO_FLAG + ",hd";
                }
                else
                {
                    flag = Constant.DAILYMOTION_VIDEO_FLAG;
                }
                if (edtSearch.getText() != null)
                {
                    requestDTO = new DailymotionRequestDTO
                            .DailymotionRequestBuilder(edtSearch.getText().toString())
                            .fields(field)
                            .flags(flag)
                            .sort(visited.toLowerCase())
                            .country(country)
                            .longerThan(longerThan)
                            .shorterThan(shorterThan)
                            .page(1)
                            .limit(10)
                            .type((String) spinnerType.getSelectedItem())
                            .build();
                    search();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(DailimotionSearchActivity.this, "Please insert keyword to search", Toast.LENGTH_LONG).show();
                }

//        ==============================================

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        adapter = null;
        String query = "";
        if (edtSearch.getText() != null)
        {
            query = edtSearch.getText().toString();

        }
        requestDTO = new DailymotionRequestDTO
                .DailymotionRequestBuilder(query)
                .fields(Constant.DAILYMOTION_VIDEO_FIELDS)
                .flags(Constant.DAILYMOTION_VIDEO_FLAG)
                .page(1)
                .limit(10)
                .type(Constant.DAILYMOTION_VIDEOS)
                .build();
        search();
        return false;
    }

    @OnItemClick(R.id.list_view)
    public void itemClick(int position)
    {
        Item item = (Item) adapter.getItem(position);
        if (item instanceof Video)
        {
            Intent intent = new Intent(this, DailymotionDetailActivity.class);
            intent.putExtra("video", (Video) adapter.getItem(position));
            startActivity(intent);
        }
        else if (item instanceof Channel)
        {
            Intent intent = new Intent(this, ListVideoDailymotionActivity.class);
            intent.putExtra("user", (Channel) adapter.getItem(position));
            startActivity(intent);
        }
    }
}
