package com.trungpt.downloadmaster.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import com.trungpt.downloadmaster.R;
import com.trungpt.downloadmaster.common.StringUtils;
import com.trungpt.downloadmaster.sync.dto.RequestDTO;
import com.trungpt.downloadmaster.sync.vimeo.request.VimeoRequestDTO;
import com.trungpt.downloadmaster.ui.adapter.*;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskSearchData;
import com.trungpt.downloadmaster.ui.asynctask.AsyncTaskSearchUsers;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskListener;
import com.trungpt.downloadmaster.ui.listener.AsyncTaskUsersListener;
import com.trungpt.downloadmaster.ui.listener.EndlessScrollListener;
import com.trungpt.downloadmaster.ui.utils.Constant;
import com.trungpt.downloadmaster.ui.utils.ResourceUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class VimeoSearchActivity extends Activity implements AsyncTaskListener, CompoundButton.OnCheckedChangeListener
        , TextView.OnEditorActionListener,AsyncTaskUsersListener
{
    @Bind(R.id.rlAdvancedSearch)
    RelativeLayout rlAdvancedSearch;
    @Bind(R.id.btAdvancedSearch)
    CheckBox btAdvancedSearch;


    @Bind(R.id.spinner_type)
    AppCompatSpinner spType;
    @Bind(R.id.spinner_sort)
    AppCompatSpinner spSort;
    //    ======================
    @Bind(R.id.spinner_direction)
    AppCompatSpinner spDirection;
    @Bind(R.id.spinner_filter)
    AppCompatSpinner spFilter;


    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.edtSearch)
    EditText edtSearch;

    @Bind(R.id.btDone)
    Button btDone;

    GeneralAdapter adapter;
    String nextPage;
    VimeoRequestDTO requestDTO;
    Animation inAnimation;
    Animation outAnimation;
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
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.enter);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.exit);

        choseTypeAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, type);
        choseTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
        spType.setAdapter(choseTypeAdapter);

        choseSortAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, sort);
        choseTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
        spSort.setAdapter(choseSortAdapter);

        choseDirectionAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, direction);
        choseDirectionAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
        spDirection.setAdapter(choseDirectionAdapter);

        choseFillterAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, filter);
        choseFillterAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
        spFilter.setAdapter(choseFillterAdapter);
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
        btAdvancedSearch.setOnCheckedChangeListener(this);
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
            List<Video> videos = videoPage.getVideos();
            nextPage = videoPage.getNextPage();
            if (adapter == null)
            {
                adapter = new CommonAdapter(videos, this);
                listView.setAdapter(adapter);
            }
            else
            {
                ((CommonAdapter)adapter).getVideos().addAll(videos);
                adapter.notifyDataSetChanged();
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    public void search(RequestDTO requestDTO)
    {
        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, Constant.HOST_NAME.VIMEO, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        requestDTO = new VimeoRequestDTO
                .VimeoRequestBuilder(edtSearch.getText().toString())
                .build();
        search(requestDTO);
        adapter = null;
        return false;
    }

    @OnItemClick(R.id.list_view)
    public void itemClick(int position)
    {
        Intent intent = new Intent(this, VimeoDetailActivity.class);
        intent.putExtra("video", (Video) adapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (isChecked)
        {
            rlAdvancedSearch.setVisibility(View.VISIBLE);
            rlAdvancedSearch.startAnimation(inAnimation);
        }
        else
        {
            rlAdvancedSearch.setVisibility(View.GONE);
            rlAdvancedSearch.startAnimation(outAnimation);
        }
    }

    @OnClick(R.id.btDone)
    public void advancedFilter()
    {
        adapter = null;
        String sort = (String) spSort.getSelectedItem();
        String filter = (String) spFilter.getSelectedItem();
        String direction = (String) spDirection.getSelectedItem();
        requestDTO = new VimeoRequestDTO
                .VimeoRequestBuilder(edtSearch.getText().toString())
                .sort(sort.toLowerCase())
                .filter(filter)
                .direction(direction.toLowerCase())
                .build();
        if (spType.getSelectedItem().equals(Constant.VIMEO_VIDEOS))
        {
            search(requestDTO);
        }
        else if (spType.getSelectedItem().equals(Constant.VIMEO_USERS))
        {
            searchUser(requestDTO);
        }
    }

    private void searchUser(RequestDTO requestDTO)
    {
        AsyncTaskSearchUsers taskSearchUsers = new AsyncTaskSearchUsers(this, Constant.HOST_NAME.VIMEO, requestDTO);
        taskSearchUsers.setListener(this);
        taskSearchUsers.execute();
    }

    @Override
    public void prepareUser()
    {

    }

    @Override
    public void completeUser(Object obj)
    {
        UserPage userPage = (UserPage) obj;
        if (userPage != null)
        {
            List<User> users = userPage.getUsers();
            nextPage = userPage.getNextPage();
            if (adapter == null)
            {
                adapter = new CommonUserAdapter(users, this);
                listView.setAdapter(adapter);
            }
            else
            {
                ((CommonUserAdapter)adapter).getUsers().addAll(users);
                adapter.notifyDataSetChanged();
            }
        }
        progressBar.setVisibility(View.GONE);
    }
}
