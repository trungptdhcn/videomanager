package com.trungpt.downloadmaster.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.vimeo.request.VimeoRequestDTO;
import com.trungpt.downloadmaster.ui.adapter.*;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskSearchData;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.EndlessScrollListener;
import com.trungpt.downloadmaster.ui.utils.Constant;

import java.util.List;

public class VimeoSearchActivity extends Activity implements AsyncTaskListener
        , TextView.OnEditorActionListener
{

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.edtSearch)
    EditText edtSearch;

    CommonAdapter adapter;
    String nextPage;
    VimeoRequestDTO requestDTO;
    Dialog dialog;
    String[] type = new String[]{Constant.VIMEO_VIDEOS, Constant.VIMEO_USERS};
    String[] sort = new String[]{"Relevant", "Date", "Alphabetical", "Plays", "Likes", "Comments", "Duration"};
    String[] direction = new String[]{"ASC", "DESC"};
    String[] filter = new String[]{"CC", "CC-BY", "CC-BY-SA", "CC-BY-ND", "CC-BY-NC", "CC-BY-NC-SA", "CC-BY-NC-ND", "in-progress"};
    //    ============
    SpinnerCommonAdapter choseTypeAdapter;
    SpinnerCommonAdapter choseSortAdapter;
    SpinnerCommonAdapter choseDirectionAdapter;
    SpinnerCommonAdapter choseFillterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vimeo_search);
        ButterKnife.bind(this);
//============================Date========================
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPageToken(page + "");
                    AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(VimeoSearchActivity.this, Constant.HOST_NAME.VIMEO, requestDTO);
                    asyncTask.setListener(VimeoSearchActivity.this);
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
        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, Constant.HOST_NAME.VIMEO, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
    }

    @OnClick(R.id.btAdvancedFilter)
    public void click()
    {
        adapter = null;
        dialog = new Dialog(this);
        dialog.setTitle("Advanced Filter");
        dialog.setContentView(R.layout.dialog_vimeo_advanced_filter);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Spinner spinnerType = (Spinner) dialog.findViewById(R.id.spinner_type);
        final Spinner spinnerShort = (Spinner) dialog.findViewById(R.id.spinner_sort);
        final Spinner spinnerDirection = (Spinner) dialog.findViewById(R.id.spinner_direction);
        final Spinner spinnerfillter = (Spinner) dialog.findViewById(R.id.spinner_filter);
        Button btCancel = (Button) dialog.findViewById(R.id.dialog_vimeo_advanced_filter_btCancel);
        Button btOk = (Button) dialog.findViewById(R.id.dialog_vimeo_advanced_filter_btOk);

        choseTypeAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, type);
        choseTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        choseSortAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, sort);
        choseSortAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        choseDirectionAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, direction);
        choseDirectionAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        choseFillterAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, filter);
        choseFillterAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        spinnerType.setAdapter(choseTypeAdapter);
        spinnerShort.setAdapter(choseSortAdapter);
        spinnerDirection.setAdapter(choseDirectionAdapter);
        spinnerfillter.setAdapter(choseFillterAdapter);
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
                String sort = (String) spinnerShort.getSelectedItem();
                String filter = (String) spinnerfillter.getSelectedItem();
                String direction = (String) spinnerDirection.getSelectedItem();
                String query = "";
                if (edtSearch.getText() != null)
                {
                    requestDTO = new VimeoRequestDTO
                            .VimeoRequestBuilder(query)
                            .sort(sort.toLowerCase())
                            .filter(filter)
                            .direction(direction.toLowerCase())
                            .type((String) spinnerType.getSelectedItem())
                            .build();
                    search();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(VimeoSearchActivity.this, "Please insert keyword to search", Toast.LENGTH_LONG).show();
                }

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        adapter = null;
        requestDTO = new VimeoRequestDTO
                .VimeoRequestBuilder(edtSearch.getText().toString())
                .type(Constant.VIMEO_VIDEOS)
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
            Intent intent = new Intent(this, VimeoDetailActivity.class);
            intent.putExtra("video", (Video) adapter.getItem(position));
            startActivity(intent);
        }
        else if (item instanceof Channel)
        {
            Intent intent = new Intent(this, ListVideoActivity.class);
            intent.putExtra("user", (Channel) adapter.getItem(position));
            startActivity(intent);
        }

    }

//
//    @OnClick(R.id.btDone)
//    public void advancedFilter()
//    {
//        String sort = (String) spSort.getSelectedItem();
//        String filter = (String) spFilter.getSelectedItem();
//        String direction = (String) spDirection.getSelectedItem();
//        requestDTO = new VimeoRequestDTO
//                .VimeoRequestBuilder(edtSearch.getText().toString())
//                .sort(sort.toLowerCase())
//                .filter(filter)
//                .direction(direction.toLowerCase())
//                .type((String) spType.getSelectedItem())
//                .build();
//        search();
//        adapter = null;
//    }
}
