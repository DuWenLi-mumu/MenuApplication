package com.example.dwl.menuapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.activity.base.BaseActivity;
import com.example.dwl.menuapplication.adapter.pageradapter.PreviewViewPager;
import com.example.dwl.menuapplication.adapter.pageradapter.base.BaseFragmentPagerAdapter;
import com.example.dwl.menuapplication.fragment.ImagePreviewFragment;
import com.example.dwl.menuapplication.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewPagerActivity extends BaseActivity {
    private PreviewViewPager mViewPager;
    private Toolbar toolbar;
    @Override
    protected void findViewByIDS() {
        mViewPager = myFindViewsById(R.id.id_vp);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_view_pager;
    }


    private boolean isShowBar = true;

    private List<String> imagList = new ArrayList<String>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private int position = 0;

    public static final String POSITION = "position";
    public static final String PICLIST = "piclist";
    private BaseFragmentPagerAdapter mAdapter;

    public static Intent newInstance(Context context, int position, ArrayList<String> imagList) {
        Intent i = new Intent(context, PhotoViewPagerActivity.class);
        i.putExtra(POSITION, position);
        i.putStringArrayListExtra(PICLIST, imagList);
        return i;
    }

    @Override
    protected void onGenerate() {
        initFirst();
        initToolBar();
        initViewPager();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(com.example.imagepicker1.R.id.toolbar);
        toolbar.setTitle((position + 1) + "/" + imagList.size());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.example.imagepicker1.R.mipmap.ic_back);
    }

    private void initFirst() {
        position = getIntent().getIntExtra(POSITION, 0);
        imagList = getIntent().getStringArrayListExtra(PICLIST);
    }

    private void initViewPager() {
        for (int i = 0; i < imagList.size(); i++) {
            fragments.add(ImagePreviewFragment.newInstance(imagList.get(i)));
        }
        mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle((position + 1) + "/" + imagList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    public void switchBarVisibility() {
        toolbar.setVisibility(isShowBar ? View.GONE : View.VISIBLE);
        if (isShowBar) {
            hideStatusBar();
        } else {
            showStatusBar();
        }
        isShowBar = !isShowBar;
    }
}
