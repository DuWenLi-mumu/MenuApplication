package com.example.dwl.menuapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.dwl.menuapplication.MenuDetail;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.activity.PostRecipeActivity;
import com.example.dwl.menuapplication.adapter.GridAdapter;
import com.example.dwl.menuapplication.bean.Following;
import com.example.dwl.menuapplication.bean.Menu;
import com.example.dwl.menuapplication.bean.Product;
import com.example.dwl.menuapplication.bean.RecycleItemClickListener;
import com.example.dwl.menuapplication.bean.SpacesItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;//首页导航栏的toolbar
    //    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private ArrayList<Menu> menuList;
    //    private ActionBarDrawerToggle drawerToggle;
    GridAdapter adapter;
    public int time = 0;
    private ImageView fab;
    RefreshLayout mRefreshLayout;
    ArrayList<String> menuPositionList;


    /** 上次点击返回键的时间 */
    private long lastBackPressed;
    /** 两次点击的间隔时间 */
    private static final int QUIT_INTERVAL = 3000;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        //set recycleview
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //  initData();
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        menuPositionList=new ArrayList<String>();
        fab = view.findViewById(R.id.fab);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                time = time + 1;
                menuList.clear();
                menuPositionList.clear();
                BmobQuery<Menu> query = new BmobQuery<>();
                query.setLimit(10).setSkip(10 * time).order("-createdAt")
                        .findObjects(new FindListener<Menu>() {
                            @Override
                            public void done(List<Menu> object, BmobException e) {
                                if (e == null) {

                                    if (object.size() == 0) {
                                        Toast.makeText(getActivity(), "没有更新的啦", Toast.LENGTH_SHORT).show();
                                        time = 0;
                                        BmobQuery<Menu> query = new BmobQuery<>();
                                        query.setLimit(10).setSkip(0).order("-createdAt")
                                                .findObjects(new FindListener<Menu>() {
                                                    @Override
                                                    public void done(List<Menu> object, BmobException e) {
                                                        if (e == null) {
                                                            for (int i = 0; i < object.size(); i++) {

                                                                Menu p1 = new Menu();
                                                                p1.setMenu_avatar(object.get(i).getMenu_avatar());
                                                                p1.setMenu_name(object.get(i).getMenu_name());
                                                                menuList.add(p1);
                                                                menuPositionList.add(object.get(i).getMenu_name());
                                                            }
                                                            RecycleItemClickListener itemClickListener = new RecycleItemClickListener() {
                                                                @Override
                                                                public void onItemClick(View view, int position) {
                                                                  // ToastUtils.showShort("点击了"+position);
                                                                    Intent intent = new Intent(getActivity(), MenuDetail.class);
                                                            intent.putExtra("menu_name",menuPositionList.get(position));
                                                            startActivity(intent);
                                                                }
                                                            };

                                                            adapter = new GridAdapter(menuList, itemClickListener, getContext());
                                                            recyclerView.setAdapter(adapter);
                                                            SpacesItemDecoration decoration = new SpacesItemDecoration(1);
                                                            recyclerView.addItemDecoration(decoration);


                                                        } else {
                                                            // ...
                                                            LogUtils.iTag("获取数据failed");
                                                        }
                                                    }
                                                });
                                        return;
                                    }

                                    for (int i = 0; i < object.size(); i++) {

                                        Menu p1 = new Menu();
                                        p1.setMenu_avatar(object.get(i).getMenu_avatar());
                                        p1.setMenu_name(object.get(i).getMenu_name());
                                        menuList.add(p1);
                                        menuPositionList.add(object.get(i).getMenu_name());
                                    }
                                    RecycleItemClickListener itemClickListener = new RecycleItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            //ToastUtils.showShort("点击了"+position);
                                            Intent intent = new Intent(getActivity(), MenuDetail.class);
                                            intent.putExtra("menu_name",menuPositionList.get(position));
                                            startActivity(intent);
                                        }
                                    };
                                    adapter = new GridAdapter(menuList, itemClickListener, getContext());
                                    recyclerView.setAdapter(adapter);
                                    SpacesItemDecoration decoration = new SpacesItemDecoration(1);
                                    recyclerView.addItemDecoration(decoration);
                                } else {
                                    // ...
                                    LogUtils.iTag("获取数据failed");
                                }
                            }
                        });

                refreshlayout.finishRefresh(2000);
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                time = time + 1;
                BmobQuery<Menu> query = new BmobQuery<>();
                query.setLimit(10).setSkip(10 * time).order("-createdAt")
                        .findObjects(new FindListener<Menu>() {
                            @Override
                            public void done(List<Menu> object, BmobException e) {
                                if (e == null) {

                                    if (object.size() < 10) {
                                        Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
//                                       mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                                        return;
                                    }

                                    for (int i = 0; i < object.size(); i++) {

                                        Menu p1 = new Menu();
                                        p1.setMenu_avatar(object.get(i).getMenu_avatar());
                                        p1.setMenu_name(object.get(i).getMenu_name());
                                        menuList.add(p1);
                                        menuPositionList.add(object.get(i).getMenu_name());
                                    }
                                    RecycleItemClickListener itemClickListener = new RecycleItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                        //    ToastUtils.showShort("点击了"+position);
                                            Intent intent = new Intent(getActivity(), MenuDetail.class);
                                            intent.putExtra("menu_name",menuPositionList.get(position));
                                            startActivity(intent);
                                        }
                                    };

                                    adapter = new GridAdapter(menuList, itemClickListener, getContext());
                                    recyclerView.setAdapter(adapter);
                                    SpacesItemDecoration decoration = new SpacesItemDecoration(1);
                                    recyclerView.addItemDecoration(decoration);


                                } else {
                                    // ...
                                    LogUtils.iTag("获取数据failed");
                                }
                            }
                        });

                refreshlayout.finishRefresh(2000);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PostRecipeActivity.class));
            }
        });
        menuList = new ArrayList<Menu>();
        BmobQuery<Menu> query = new BmobQuery<>();
        query.setLimit(10).setSkip(0).order("-createdAt")
                .findObjects(new FindListener<Menu>() {
                    @Override
                    public void done(List<Menu> object, BmobException e) {
                        if (e == null) {

                            for (int i = 0; i < object.size(); i++) {

//                                LogUtils.iTag("picUrl",object.get(i).getMenu_avatar());
//                                LogUtils.iTag("menuname",object.get(i).getMenu_name());
                                Menu p1 = new Menu();
                                p1.setMenu_avatar(object.get(i).getMenu_avatar());
                                p1.setMenu_name(object.get(i).getMenu_name());
                                menuList.add(p1);
                                menuPositionList.add(object.get(i).getMenu_name());
                            }
                            RecycleItemClickListener itemClickListener = new RecycleItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    //ToastUtils.showShort("点击了"+position);
                                    Intent intent = new Intent(getActivity(), MenuDetail.class);
                                    intent.putExtra("menu_name",menuPositionList.get(position));
                                    startActivity(intent);
                                }
                            };

                            adapter = new GridAdapter(menuList, itemClickListener, getContext());
                            recyclerView.setAdapter(adapter);
                            // recyclerView.scrollToPosition(menuList.size()-1);
                            SpacesItemDecoration decoration = new SpacesItemDecoration(1);
                            recyclerView.addItemDecoration(decoration);


                        } else {
                            // ...
                            LogUtils.iTag("获取数据failed");
                        }
                    }
                });


        return view;
    }


}


