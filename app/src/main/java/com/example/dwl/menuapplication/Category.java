package com.example.dwl.menuapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.example.dwl.menuapplication.adapter.GridAdapter;
import com.example.dwl.menuapplication.bean.Menu;
import com.example.dwl.menuapplication.bean.RecycleItemClickListener;
import com.example.dwl.menuapplication.bean.SpacesItemDecoration;
import com.example.dwl.menuapplication.utils.StatusBarUtil;
import com.zaaach.toprightmenu.TopRightMenu;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Category extends AppCompatActivity {

    public RecyclerView recyclerView;
    public Toolbar toolbar;
    public ArrayList<Menu> menuList_category;
    public ArrayList<String> menuPositionList;
    TextView tv_category;

    public int position;
    public String category;

    RecycleItemClickListener itemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        position = getIntent().getIntExtra("position", 0);

        tv_category=findViewById(R.id.text_category);

        switch (position) {
            case 0:
                category = "fruits";
                break;
            case 1:
                category = "vegetables";
                break;
            case 2:
                category = "meat";
                break;
            case 3:
                category = "noodle";
                break;
        }


        //set a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_category);
//        toolbar.setTitle(category);
//        toolbar.setTitleTextColor(Color.WHITE);

        tv_category.setText(category);


        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    finish();
//            }
//        });

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set recycleview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_category);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //  initData();
        menuList_category = new ArrayList<Menu>();
        menuPositionList=new ArrayList<String>();

        BmobQuery<Menu> BmobQuery = new BmobQuery<>();
        BmobQuery.addWhereEqualTo("menu_category", category);
        BmobQuery.findObjects(new FindListener<Menu>() {
            public void done(List<Menu> object, BmobException e) {
                if (e == null) {

                    for (int i = 0; i < object.size(); i++) {
                        Menu p1 = new Menu();
                        p1.setMenu_avatar(object.get(i).getMenu_avatar());
                        p1.setMenu_name(object.get(i).getMenu_name());
                        menuList_category.add(p1);
                        menuPositionList.add(object.get(i).getMenu_name());

                    }

                    itemClickListener = new RecycleItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Intent intent = new Intent(Category.this, MenuDetail.class);
                            intent.putExtra("menu_name",menuPositionList.get(position));
                            startActivity(intent);


                        }
                    };
                    GridAdapter adapter = new GridAdapter((ArrayList<Menu>) menuList_category, itemClickListener, Category.this);
                    recyclerView.setAdapter(adapter);
                    SpacesItemDecoration decoration = new SpacesItemDecoration(1);
                    recyclerView.addItemDecoration(decoration);

                  //  LogUtils.iTag("获取数据成功");
                    // ...
                } else {
                    // ...
                    LogUtils.iTag("获取数据failed");
                }
            }
        });


    }

    // private void initData() {


    //    toolbar.setTitle(category);

//        menuList_category = new ArrayList<Menu>();
//        BmobQuery<Menu> BmobQuery = new BmobQuery<>();
//        BmobQuery.addWhereEqualTo("menu_category", category);
//        BmobQuery.findObjects(new FindListener<Menu>() {
//            @Override
//            public void done(List<Menu> object, BmobException e) {
//                if (e == null) {
//                    for (int i = 0; i < object.size(); i++) {
//
//                        Menu p1 = new Menu(object.get(i).getMenu_avatar(), object.get(i).getMenu_name());
//                        menuList_category.add(p1);
//                    }
//                  //  Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
//                } else {
//                    Log.e("BMOB", e.toString());
//                  //  Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
//                }
//            }
//        });


    // }
}
