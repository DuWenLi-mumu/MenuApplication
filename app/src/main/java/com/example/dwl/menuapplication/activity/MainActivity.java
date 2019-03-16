package com.example.dwl.menuapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.dwl.menuapplication.IM.DialogActivity;
import com.example.dwl.menuapplication.IM.MessageActivity;
import com.example.dwl.menuapplication.MessageEvent;
import com.example.dwl.menuapplication.MyApp;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.bean.User;
import com.example.dwl.menuapplication.fragment.CollectFragment;
import com.example.dwl.menuapplication.fragment.FolloweeFragment;
import com.example.dwl.menuapplication.fragment.HomeFragment;
import com.example.dwl.menuapplication.fragment.SearchFragment;
import com.example.dwl.menuapplication.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment fragment1;
    private FolloweeFragment fragment2;
    //    private WriteFragment fragment3;
    private CollectFragment fragment4;
    private SearchFragment fragment5;
    private Fragment[] fragments;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView im_add;
    private int lastfragment;//用于记录上个选择的Fragment
    private NavigationView nav;
    private ImageView icon_image;
    private TextView user_name;
    private NavigationView navigationView;
    //    private String account;
    MyApp Account;
//
//    /** 上次点击返回键的时间 */
//    private long lastBackPressed;
//    /** 两次点击的间隔时间 */
//    private static final int QUIT_INTERVAL = 3000;


//
//    //重写onbackPressed
//    @Override
//    public void onBackPressed() {
//        long backPressed = System.currentTimeMillis();
//        super.onBackPressed();
//        if (backPressed - lastBackPressed > QUIT_INTERVAL) {
//            lastBackPressed = backPressed;
//            Toast.makeText(this,"再按一次退出",Toast.LENGTH_LONG).show();
//
//        } else {
//            finish();
//            System.exit(0);
//        }
//
//    }
private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        initNavigationView();


        //set drawlayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //set a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //状态栏透明处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        EventBus.getDefault().register(this);




    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void Event(MessageEvent messageEvent) {
//        Glide.with(MainActivity.this)
//                .load(messageEvent.getMessage()[0])
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .into(icon_image);
//        user_name.setText(messageEvent.getMessage()[1]);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav);

        navigationView.inflateHeaderView(R.layout.nav_header);
        navigationView.inflateMenu(R.menu.menu_drawer);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:

                        drawerLayout.closeDrawers();
                   // ToastUtils.showShort("点击了主页");
                        break;
                    case R.id.menu_search:
                        drawerLayout.closeDrawers();
                        bottomNavigationView.setSelectedItemId(R.id.id4);
                        if (lastfragment != 3) {
                            toolbar.setVisibility(View.GONE);
                            switchFragment(lastfragment, 3);
                            lastfragment = 3;

                        }
                        //

                        break;
                    case R.id.menu_message:
                        Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_collection:
                        drawerLayout.closeDrawers();
                        bottomNavigationView.setSelectedItemId(R.id.id3);
                        if (lastfragment != 2) {
                            toolbar.setVisibility(View.GONE);
                            switchFragment(lastfragment, 2);
                            lastfragment = 2;

                        }
                        break;
                    case R.id.menu_followee:
                        drawerLayout.closeDrawers();
                        bottomNavigationView.setSelectedItemId(R.id.id2);
                        if (lastfragment != 1) {
                            toolbar.setVisibility(View.GONE);
                            switchFragment(lastfragment, 1);
                            lastfragment = 1;

                        }
                        break;

                }


                return false;
            }
        });

        View navHeaderView = navigationView.getHeaderView(0);
        navHeaderView.setMinimumHeight(500);
        //设置监听事件
        icon_image = navHeaderView.findViewById(R.id.Myavatar);
        user_name = navHeaderView.findViewById(R.id.user_name);
        BmobQuery<User> PersonBmobQuery = new BmobQuery<>();


        Account = (MyApp) getApplicationContext();
       // Log.e("account", Account.getAccount());

        PersonBmobQuery.addWhereEqualTo("pid", SPUtils.getInstance().getString("pid"));
        PersonBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    Glide.with(MainActivity.this)
                            .load(object.get(0).getPerson_pic())
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(icon_image);
                    user_name.setText(object.get(0).getUsername());
                    //AvatarUrl = object.get(0).getPerson_pic().toString();
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        icon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MyMainPage.class));
            }
        });
    }

            //初始化fragment和fragment数组
            private void initFragment() {

                fragment1 = new HomeFragment();
                fragment2 = new FolloweeFragment();
                // fragment3 = new WriteFragment();
                fragment4 = new CollectFragment();
                fragment5 = new SearchFragment();
                fragments = new Fragment[]{fragment1, fragment2, fragment4, fragment5};
                lastfragment = 0;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragment1).show(fragment1).commit();
                bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnv);

                bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
            }

            //判断选择的菜单
            private BottomNavigationView.OnNavigationItemSelectedListener changeFragment = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.id1: {
                            if (lastfragment != 0) {
                                toolbar.setVisibility(View.VISIBLE);
                                switchFragment(lastfragment, 0);
                                lastfragment = 0;


                            }

                            return true;
                        }
                        case R.id.id2: {
                            if (lastfragment != 1) {
                                toolbar.setVisibility(View.GONE);
                                switchFragment(lastfragment, 1);
                                lastfragment = 1;


                            }

                            return true;
                        }
                        case R.id.id3: {
                            if (lastfragment != 2) {
                                toolbar.setVisibility(View.GONE);
                                switchFragment(lastfragment, 2);
                                lastfragment = 2;

                            }

                            return true;
                        }
                        case R.id.id4: {
                            if (lastfragment != 3) {
                                toolbar.setVisibility(View.GONE);
                                switchFragment(lastfragment, 3);
                                lastfragment = 3;

                            }

                            return true;
                        }
//                case R.id.id5:
//                {
//                    if(lastfragment!=4)
//                    {
//                        toolbar.setVisibility(View.GONE);
//                        switchFragment(lastfragment,4);
//                        lastfragment=4;
//
//                    }
//
//                    return true;
//                }


                    }


                    return false;
                }
            };

            //切换Fragment
            private void switchFragment(int lastfragment, int index) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
                if (fragments[index].isAdded() == false) {
                    transaction.add(R.id.mainview, fragments[index]);


                }
                transaction.show(fragments[index]).commitAllowingStateLoss();


            }

    //上次按下返回键的系统时间
    private long lastBackTime = 0;
    //当前按下返回键的系统时间
    private long currentBackTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获返回键按下的事件
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //获取当前系统时间的毫秒数
            currentBackTime = System.currentTimeMillis();
            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
            if(currentBackTime - lastBackTime > 2 * 1000){
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                lastBackTime = currentBackTime;
            }else{ //如果两次按下的时间差小于2秒，则退出程序
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
