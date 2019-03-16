package com.example.dwl.menuapplication.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.dwl.menuapplication.MenuDetail;
import com.example.dwl.menuapplication.MessageEvent;
import com.example.dwl.menuapplication.MyApp;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.adapter.PersonListAdapter;
import com.example.dwl.menuapplication.bean.Menu;
import com.example.dwl.menuapplication.bean.User;
import com.example.dwl.menuapplication.utils.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyMainPage extends AppCompatActivity {

    private ImageView change_person_info_icon;

    private int mOffset = 0;
    private int mScrollY = 0;
    RecyclerView rv;
    PersonListAdapter mPersonListAdapter;
    String followee_name;
    ImageView person_img;
    TextView person_name;
    ImageView toolbar_avater;
    TextView title;
    private TextView signature;
    private String avatar_Url;
    private String name;
    private String account;
    MyApp Account;
    ArrayList<String> MyPositionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_main_page);

        MyPositionList=new ArrayList<String>();
//        followee_name= getIntent().getStringExtra("followee_name");
        initData();
        person_img=findViewById(R.id.person_avatar);
        person_name=findViewById(R.id.person_name);
        toolbar_avater=findViewById(R.id.toolbar_avatar);
        title=findViewById(R.id.title);
        signature = findViewById(R.id.signature);

        Account = (MyApp)getApplicationContext();

        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("mobilePhoneNumber", Account.getAccount());
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    Glide.with(MyMainPage.this)
                            .load(object.get(0).getPerson_pic())
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(person_img);

                    Glide.with(MyMainPage.this)
                            .load(object.get(0).getPerson_pic())
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(toolbar_avater);
                    person_name.setText(object.get(0).getUsername());
                    title.setText(object.get(0).getUsername());
                    signature.setText(object.get(0).getPersonal_signature());
                    // Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });

//       这里应该是返回按钮的实现功能
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                String[] Info = new String[2];
                Info[0] = avatar_Url;
                Info[1] = name;
                EventBus.getDefault().post(new MessageEvent(Info));
              //  startActivity(new Intent(MyMainPage.this,MainActivity.class));

                finish();
            }
        });

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);

        final View parallax = findViewById(R.id.parallax);
        final View buttonBar = findViewById(R.id.buttonBarLayout);
        final NestedScrollView scrollView = findViewById(R.id.scrollView);
        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                MyPositionList.clear();
                BmobQuery<Menu> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("create_person_id",  SPUtils.getInstance().getString("pid"));
                categoryBmobQuery.findObjects(new FindListener<Menu>() {
                    @Override
                    public void done(List<Menu> object, BmobException e) {
                        if (e == null) {
                            mPersonListAdapter.setNewData(object);
                            for (int i = 0; i < object.size(); i++) {
                                MyPositionList.add(object.get(i).getMenu_name());
                            }
                        } else {
                            Log.e("BMOB", e.toString());
                            //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                refreshLayout.finishRefresh(3000);
            }


            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
               MyPositionList.clear();
                BmobQuery<Menu> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("create_person_id",  SPUtils.getInstance().getString("pid"));
                categoryBmobQuery.findObjects(new FindListener<Menu>() {
                    @Override
                    public void done(List<Menu> object, BmobException e) {
                        if (e == null) {
                            mPersonListAdapter.setNewData(object);
                            for (int i = 0; i < object.size(); i++) {
                                MyPositionList.add(object.get(i).getMenu_name());
                            }
                        } else {
                            Log.e("BMOB", e.toString());
                            //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                refreshLayout.finishLoadmore(2000);
            }

            //   @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
//            @Override
//            public void onHeaderPulling(@NonNull RefreshHeader header, float percent, int offset, int bottomHeight, int maxDragHeight) {
//                mOffset = offset / 2;
//                parallax.setTranslationY(mOffset - mScrollY);
//                toolbar.setAlpha(1 - Math.min(percent, 1));
//            }
//            @Override
//            public void onHeaderReleasing(@NonNull RefreshHeader header, float percent, int offset, int bottomHeight, int maxDragHeight) {
//                mOffset = offset / 2;
//                parallax.setTranslationY(mOffset - mScrollY);
//                toolbar.setAlpha(1 - Math.min(percent, 1));
//            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = DensityUtil.dp2px(170);
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)&0x00ffffff;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    buttonBar.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    parallax.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });
        buttonBar.setAlpha(0);
        toolbar.setBackgroundColor(0);

        person_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyMainPage.this,ActivityRxPhoto.class);
                startActivity(intent);
            }
        });

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        avatar_Url = messageEvent.getMessage()[0];
        name = messageEvent.getMessage()[1];

        Log.e("fileurl111", avatar_Url);
        Glide.with(MyMainPage.this)
                .load(avatar_Url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(person_img);
        //EventBus.getDefault().post(new MessageEvent(messageEvent.getMessage()));
        person_name.setText(name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initData() {
        rv=findViewById(R.id.person_rv);


        //适配器中装入数据，便于适配器管理
        mPersonListAdapter = new PersonListAdapter(R.layout.person_page_list,null );//适配器用来获取每个列表的单行数据 item_list是自定义的单行数据适配器的样式

        //设置点击 头像和点击标题均跳转到主页
        mPersonListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.menu_name:
//                        ToastUtils.showShort("点击head，此处添加代码，跳转到主页");
                        break;
                    case R.id.menu_pic:
                        Intent intent = new Intent(MyMainPage.this, MenuDetail.class);
                        intent.putExtra("menu_name",MyPositionList.get(position));
                        startActivity(intent);
                        break;
                }
            }
        });

        mPersonListAdapter.isFirstOnly(true);
        mPersonListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rv.setLayoutManager(new LinearLayoutManager(MyMainPage.this));
        rv.setAdapter(mPersonListAdapter);
        rv.setNestedScrollingEnabled(false);


        BmobQuery<Menu> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("pid",  SPUtils.getInstance().getString("pid"));
        categoryBmobQuery.findObjects(new FindListener<Menu>() {
            @Override
            public void done(List<Menu> object, BmobException e) {
                if (e == null) {
                    mPersonListAdapter.setNewData(object);
                    for (int i = 0; i < object.size(); i++) {
                        MyPositionList.add(object.get(i).getMenu_name());
                    }
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }
}
