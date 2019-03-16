package com.example.dwl.menuapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.dwl.menuapplication.activity.PersonPage;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.adapter.FolloweeBaseAdapter;
import com.example.dwl.menuapplication.bean.Following;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FolloweeFragment extends Fragment {
    RecyclerView rv;
    FolloweeBaseAdapter adapter;

    RefreshLayout mRefreshLayout;
    ArrayList<String> namelist;
    ArrayList<String> idlist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =  inflater.inflate(R.layout.fragment_followee, container, false);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        Toolbar mToolBar = (Toolbar) activity.findViewById(R.id.toolbar_main);
//        mToolBar.setVisibility(View.GONE);

        namelist=new ArrayList<String>();
        idlist = new ArrayList<String>();
        mRefreshLayout = v.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                namelist.clear();
                idlist.clear();
                BmobQuery<Following> query = new BmobQuery<Following>();
                query.addWhereEqualTo("pid", SPUtils.getInstance().getString("pid"));
                query.findObjects(new FindListener<Following>() {
                    @Override
                    public void done(List<Following> object, BmobException e) {
                        if (e == null) {
                            adapter.setNewData(object);
                            for (int i = 0; i < object.size(); i++) {
                                namelist.add(object.get(i).getFollowing_name());
                                idlist.add(object.get(i).getFollow_pid());
                            }


                        } else {
                            Log.e("BMOB", e.toString());
                            //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                refreshlayout.finishRefresh(2000);
            }
        });


        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                namelist.clear();
                idlist.clear();
                BmobQuery<Following> query = new BmobQuery<Following>();
                query.addWhereEqualTo("pid", SPUtils.getInstance().getString("pid"));
                query.findObjects(new FindListener<Following>() {
                            @Override
                            public void done(List<Following> object, BmobException e) {
                                if (e == null) {
                                 adapter.setNewData(object);
                                    for (int i = 0; i < object.size(); i++) {
                                        namelist.add(object.get(i).getFollowing_name());
                                        idlist.add(object.get(i).getFollow_pid());
                                    }

                                } else {
                                    Log.e("BMOB", e.toString());
                                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                refreshlayout.finishRefresh(2000);
            }

        });

       initView(v);


       return v;
    }



//    private void requestData() {




//    }

    private void initView(View v) {

        rv = v.findViewById(R.id.rv);//找到当前类所对应的xml中的recyview的id


        //适配器中装入数据，便于适配器管理
        adapter = new FolloweeBaseAdapter(R.layout.followee_item_list,null);//适配器用来获取每个列表的单行数据 item_list是自定义的单行数据适配器的样式

        //设置点击 头像和点击标题均跳转到主页
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.iv_head:
                        Intent intent = new Intent(getActivity(), PersonPage.class);
                        intent.putExtra("followee_name",namelist.get(position));
                        intent.putExtra("follow_id",idlist.get(position));
                        startActivity(intent);

                       // ToastUtils.showShort("position"+position);
                        //ToastUtils.showShort("点击head，此处添加代码，跳转到主页");
                        break;
                    case R.id.tv_title:
                       // ToastUtils.showShort("点击title，此处添加代码，跳转到主页");
                        break;
                }
            }
        });
        adapter.isFirstOnly(true);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        BmobQuery<Following> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("pid", SPUtils.getInstance().getString("pid"));
        categoryBmobQuery.findObjects(new FindListener<Following>() {
            @Override
            public void done(List<Following> object, BmobException e) {
                if (e == null) {
                    adapter.setNewData(object);
                    for (int i = 0; i < object.size(); i++) {
                        namelist.add(object.get(i).getFollowing_name());
                        idlist.add(object.get(i).getFollow_pid());
                    }
                   // Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}
