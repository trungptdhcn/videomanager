package com.trungpt.downloadmaster;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.trungpt.downloadmaster.common.BaseActivity;
import com.trungpt.downloadmaster.ui.fragment.*;


public class MainActivity extends BaseActivity
{
    @Bind(R.id.activity_main_viewpager)
    ViewPager viewPager;
    @Bind(R.id.activity_main_tab)
    ViewGroup flTabContainer;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    @Bind(R.id.toolbar_actionbar)
    Toolbar toolbar;
    FragmentPagerItems pages;
    FragmentPagerItemAdapter adapter;

    @Override
    public void setDataToView(Bundle savedInstanceState)
    {
        flTabContainer.addView(LayoutInflater.from(this).inflate(R.layout.tab_container, flTabContainer, false));
        SmartTabLayout tabLayout = (SmartTabLayout) findViewById(R.id.tab_indicator_menu_viewpagertab);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final Resources res = tabLayout.getContext().getResources();
        viewPager.setOffscreenPageLimit(5);
        pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of("Youtube", YoutubeFragment.class));
        pages.add(FragmentPagerItem.of("Vimeo", VimeoFragment.class));
        pages.add(FragmentPagerItem.of("Dailymotion", DailymotionFragment.class));
        pages.add(FragmentPagerItem.of("Facebook", FacebookFragment.class));
        pages.add(FragmentPagerItem.of("Xvideos", XvideoFragment.class));
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            public void onPageScrollStateChanged(int state)
            {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        ivLogo.setImageResource(R.drawable.youtube_logo);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.red_simi_press));
                        break;
                    case 1:
                        ivLogo.setImageResource(R.drawable.vimeo_logo);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.blue_vimeo));
                        break;
                    case 2:
                        ivLogo.setImageResource(R.drawable.dailymotion_logo);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.gray_21));
                        break;
                    case 3:
                        ivLogo.setImageResource(R.drawable.facebook_logo);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.blue_facebook));
                        break;
                    case 4:
                        ivLogo.setImageResource(R.drawable.youtube_logo);
                        break;
                }
            }
        });
    }

    @Override
    public int getLayout()
    {
        return R.layout.activity_main;
    }
}
