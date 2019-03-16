package com.example.dwl.menuapplication;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dwl.menuapplication.adapter.GridAdapter;
import com.example.dwl.menuapplication.bean.Menu;
import com.example.dwl.menuapplication.bean.RecycleItemClickListener;
import com.example.dwl.menuapplication.bean.SpacesItemDecoration;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import java.util.ArrayList;
import java.util.List;




public class home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ArrayList<Menu>menuList;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView im_add;

    private TopRightMenu mTopRightMenu;
    private boolean showIcon = true;
    private boolean dimBg = true;
    private boolean needAnim = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set drawlayout
     //   drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        im_add=(ImageView) findViewById(R.id.add);
        im_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTopRightMenu = new TopRightMenu(home.this);
                List<MenuItem> menuItems = new ArrayList<>();
                menuItems.add(new MenuItem(R.mipmap.multichat, "发起多人聊天"));
                menuItems.add(new MenuItem(R.mipmap.addmember, "加好友"));
                menuItems.add(new MenuItem(R.mipmap.qr_scan, "扫一扫"));
                mTopRightMenu
//                        .setHeight(480)     //默认高度480
//                        .setWidth(320)      //默认宽度wrap_content
                        .showIcon(showIcon)     //显示菜单图标，默认为true
                        .dimBackground(dimBg)           //背景变暗，默认为true
                        .needAnimationStyle(needAnim)   //显示动画，默认为true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                        .addMenuList(menuItems)
                        .addMenuItem(new MenuItem(R.mipmap.facetoface, "面对面快传"))
                        .addMenuItem(new MenuItem(R.mipmap.pay, "付款"))
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                Toast.makeText(home.this, "点击菜单:" + position, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .showAsDropDown(im_add, -225, 0);


            }
        });



        //set a toolbar
        toolbar= (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("首页");
//        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close){
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

        //set recycleview
        recyclerView= (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //initData();
        RecycleItemClickListener itemClickListener=new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Log.e("position","="+position);
//                Toast.makeText(MainActivity.this, productList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent();
//                intent.setClass(MainActivity.this,ProductDetailActivity.class);
//                startActivity(intent);
            }
        };
        GridAdapter adapter=new GridAdapter(menuList,itemClickListener,home.this);
        recyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration=new SpacesItemDecoration(1);
        recyclerView.addItemDecoration(decoration);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home_tb_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    private void initData() {
//        productList=new ArrayList<Product>();
//        Product p1=new Product(R.mipmap.p1,"我是照片1");
//        productList.add(p1);
//        Product p2=new Product(R.mipmap.p2,"我是照片2");
//        productList.add(p2);
//        Product p3=new Product(R.mipmap.p3,"我是照片3");
//        productList.add(p3);
//        Product p4=new Product(R.mipmap.p4,"我是照片4");
//        productList.add(p4);
//        Product p5=new Product(R.mipmap.p5,"我是照片5");
//        productList.add(p5);
//        Product p6=new Product(R.mipmap.p6,"我是照片6");
//        productList.add(p6);
//        Product p7=new Product(R.mipmap.p2,"我是照片7");
//        productList.add(p7);
//        Product p8=new Product(R.mipmap.p1,"我是照片8");
//        productList.add(p8);
//        Product p9=new Product(R.mipmap.p4,"我是照片9");
//        productList.add(p9);
//        Product p10=new Product(R.mipmap.p6,"我是照片10");
//        productList.add(p10);
//        Product p11=new Product(R.mipmap.p3,"我是照片11");
//        productList.add(p11);
//
//    }


}
