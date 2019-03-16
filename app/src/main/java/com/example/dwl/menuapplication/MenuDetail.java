package com.example.dwl.menuapplication;

import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.canking.minipay.Config;
import com.canking.minipay.MiniPayUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.dwl.menuapplication.adapter.CollectBaseAdapter;
import com.example.dwl.menuapplication.adapter.MenuDetailAdapter;
import com.example.dwl.menuapplication.bean.Collection;
import com.example.dwl.menuapplication.bean.Following;
import com.example.dwl.menuapplication.bean.Menu;
import com.example.dwl.menuapplication.bean.User;
import com.example.dwl.menuapplication.utils.StatusBarUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class MenuDetail extends AppCompatActivity {

    private ImageView iv;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private String Menu_name;
    private String menu_detail;
    private ArrayList<String> menu_urls;
    private TextView detail;
    private RecyclerView rv_pic;
    private MenuDetailAdapter adapter;
    private ImageView collect_icon;
    private Menu mymenu;
    private Collection mycollection;
    private boolean collected=false;
    private Following myfollowing;
    private User mUser;
    private ImageView attention_img;
    private boolean attentioned = false;
    Toolbar toolbar;
    private int mOffset = 0;
    private int mScrollY = 0;
    private boolean recipeismy=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        Menu_name = getIntent().getStringExtra("menu_name");
        detail = findViewById(R.id.detail);
        rv_pic = findViewById(R.id.menu_urls_pic);
        attention_img = findViewById(R.id.attention_img);
        iv = findViewById(R.id.iv);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        final NestedScrollView scrollView = findViewById(R.id.scrollView_detail);


     toolbar = findViewById(R.id.toolbar_detail);
        //     setSupportActionBar(toolbar);//自己添加,会多出美食秀这几个字

        //状态栏透明和间距处理
//        StatusBarUtil.immersive(this);
//        StatusBarUtil.setPaddingSmart(this, toolbar);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = DensityUtil.dp2px(170);
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)&0x00ffffff;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    //buttonBar.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    iv.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });
        toolbar.setBackgroundColor(0);

        mCollapsingToolbarLayout.setTitle(Menu_name);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        //iv.setImageResource(R.mipmap.ic_bg);
        menu_urls = new ArrayList<String>();

        //适配器中装入数据，便于适配器管理
        adapter = new MenuDetailAdapter(R.layout.item_detail, null);
        adapter.isFirstOnly(true);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rv_pic.setLayoutManager(new LinearLayoutManager(this));
        rv_pic.setAdapter(adapter);
//        使页面滑动的时候不卡顿
        rv_pic.setNestedScrollingEnabled(false);

        collect_icon = (ImageView) findViewById(R.id.collect_icon);

        //得到当前菜谱的所有信息
        BmobQuery<Menu> categoryBmobQuery1 = new BmobQuery<>();
        categoryBmobQuery1.addWhereEqualTo("menu_name", Menu_name);
        categoryBmobQuery1.findObjects(new FindListener<Menu>() {
            @Override
            public void done(List<Menu> object, BmobException e) {
                if (e == null) {
                    //查找当前菜谱成功
                    mymenu = object.get(0);
                    String menuName = mymenu.getMenu_name();


                    //查询collect表中是否存在收藏记录
                    BmobQuery<Collection> eq1 = new BmobQuery<Collection>();
                    eq1.addWhereEqualTo("pid", SPUtils.getInstance().getString("pid"));
                    BmobQuery<Collection> eq2 = new BmobQuery<Collection>();
                    eq2.addWhereEqualTo("menu_name", menuName);
                    List<BmobQuery<Collection>> andQuerys = new ArrayList<BmobQuery<Collection>>();
                    andQuerys.add(eq1);
                    andQuerys.add(eq2);
                    BmobQuery<Collection> query = new BmobQuery<Collection>();
                    query.and(andQuerys);
                    query.findObjects(new FindListener<Collection>() {
                        @Override
                        public void done(List<Collection> object, BmobException e) {
                            if (e == null) {
                                //在collection表中没有找到

                                if (object.size() <= 0) {
                                    collected = false;
                                    collect_icon.setImageResource(R.drawable.collect_origin);
                                    mycollection = new Collection(mymenu.getCreate_person_name(), mymenu.getMenu_pic(), mymenu.getMenu_name(),
                                            mymenu.getMenu_id(), SPUtils.getInstance().getString("pid"));
                                } else {
                                    collected = true;
                                    collect_icon.setImageResource(R.drawable.collect2);
                                    mycollection = object.get(0);

                                }

                            } else {
                                //在collection表中没有找到
                                collected = false;
                                collect_icon.setImageResource(R.drawable.collect_origin);
                            }
                        }
                    });


                    //查询找到关注的这个人的全部信息，为关注的后序事件做准备
                    BmobQuery<User> categoryBmobQuery = new BmobQuery<User>();
                    categoryBmobQuery.addWhereEqualTo("pid", mymenu.getCreate_person_id());
                    categoryBmobQuery.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> object, BmobException e) {
                            if (e == null) {
                                if (object.size() <= 0) {
                                    //执行查询语句成功，但是没有找打这个人
                                    //不可能找不到
                                    ToastUtils.showShort("数据库中没有找到作者");

                                } else {
                                    //执行查询语句成功，并且找到了这个人
                                    //保存作者的所有信息；
                                    mUser = object.get(0);
                                    myfollowing = new Following(mUser.getPersonal_signature(), mUser.getUsername(), mUser.getPid(), SPUtils.getInstance().getString("pid"), mUser.getPerson_pic());

                                    MyApp myApp;
                                    myApp = (MyApp)getApplicationContext();
                                    if(myApp.getMy().getPid().equals(myfollowing.getFollow_pid())){
                                        recipeismy=true;
                                    }



                                    if(recipeismy){
                                        attention_img.setImageResource(R.drawable.myself);
                                        attentioned=false;

                                    }else{
                                        //查询following表中我是否关注了作者
                                        BmobQuery<Following> eq3 = new BmobQuery<Following>();
                                        eq3.addWhereEqualTo("pid", SPUtils.getInstance().getString("pid"));
                                        BmobQuery<Following> eq4 = new BmobQuery<Following>();
                                        eq4.addWhereEqualTo("follow_pid", mUser.getPid());
                                        List<BmobQuery<Following>> andQuerys2 = new ArrayList<BmobQuery<Following>>();
                                        andQuerys2.add(eq3);
                                        andQuerys2.add(eq4);
                                        BmobQuery<Following> query2 = new BmobQuery<Following>();
                                        query2.and(andQuerys2);
                                        query2.findObjects(new FindListener<Following>() {
                                            @Override
                                            public void done(List<Following> object, BmobException e) {
                                                if (e == null) {
                                                    //在following表中执行查询语句成功
                                                    if (object.size() <= 0) {
                                                        //没有找到关注记录，重新new一个
                                                        attentioned = false;
                                                        attention_img.setImageResource(R.drawable.attention_orign);
                                                        myfollowing = new Following(mUser.getPersonal_signature(), mUser.getUsername(), mUser.getPid(), SPUtils.getInstance().getString("pid"), mUser.getPerson_pic());
                                                    } else {
                                                        //找到了相关的记录，调整显示
                                                        attention_img.setImageResource(R.drawable.attentioned);
                                                        attentioned = true;
                                                        myfollowing = object.get(0);
                                                        //ToastUtils.showShort("查找关注并记录成功");
                                                    }
                                                } else {
                                                    //查询出错
                                                    attention_img.setImageResource(R.drawable.attention_orign);
                                                    attentioned = false;
                                                    ToastUtils.showShort("在following表中执行查询语句出错");
                                                }
                                            }
                                        });
                                    }
                                }
                            } else {
                                //执行查找人的语句失败
                                ToastUtils.showShort("执行查找人的语句失败");
                                Log.i("myerror", e.toString());
                            }
                        }
                    });




                } else {
                    //查找当前菜谱失败.额 这里是不可能失败的
                    ToastUtils.showShort("查找当前菜谱语句执行失败");
                }
            }
        });



        attention_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeismy){
                    attention_img.setImageResource(R.drawable.myself);
                    attentioned=false;
                    ToastUtils.showShort("不能关注自己哦");
                }else{
                    if (!attentioned) {
                        //如果没有关注作者，插入关注记录，修改显示图标
                        Following f = new Following(mUser.getPersonal_signature(), mUser.getUsername(), mUser.getPid(), SPUtils.getInstance().getString("pid"), mUser.getPerson_pic());
                        myfollowing=f;
                        f.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId,BmobException e) {
                                if(e==null){
                                    attentioned = true;
                                    attention_img.setImageResource(R.drawable.attentioned);
                                    ToastUtils.showShort("关注成功");

                                }else{
                                    ToastUtils.showShort("关注失败");
                                }
                            }
                        });


                    } else {
                        //取消关注，需要删除数据库中的关注记录
                        Following p = new Following(mUser.getPersonal_signature(), mUser.getUsername(), mUser.getPid(), SPUtils.getInstance().getString("pid"), mUser.getPerson_pic());
                        p.setObjectId(myfollowing.getObjectId());
                        p.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.showShort("取消关注成功");
                                    attentioned = false;
                                    attention_img.setImageResource(R.drawable.attention_orign);
                                } else {
                                    ToastUtils.showShort("取消关注失败");
                                    Log.e("-------error",e.toString());
                                }
                            }
                        });

                    }

                }
            }
        });



        collect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!collected) {

                    //点击了收藏
                   // collect_icon.setImageResource(R.drawable.collect2);
                    //添加到收藏表中
                    mycollection.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            //返回objectID
                            if (e == null) {
                                collect_icon.setImageResource(R.drawable.collect2);
                                collected = true;
                                ToastUtils.showShort("收藏成功！！！");
                            } else {
                                ToastUtils.showShort("收藏失败！");

                            }
                        }
                    });
                } else {

                    //取消了收藏
                    Collection  collection = new Collection();
                    collection.setObjectId(mycollection.getObjectId());
                    collection.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //collect_icon.setImageResource(R.drawable.collect_origin);
                                collect_icon.setImageResource(R.drawable.collect_origin);
                                collected = false;
                                ToastUtils.showShort("取消收藏成功");
                            } else {
                                //取消收藏失败
                                ToastUtils.showShort("取消收藏失败");
                            }
                        }
                    });

                }
            }
        });


        //查询菜单详情页面信息
        BmobQuery<Menu> categoryBmobQuery2 = new BmobQuery<>();
        categoryBmobQuery2.addWhereEqualTo("menu_name", Menu_name);
        categoryBmobQuery2.findObjects(new FindListener<Menu>() {
            @Override
            public void done(List<Menu> object, BmobException e) {
                if (e == null) {
                    //  menu_detail=object.get(0).getMenu_detail();
                    detail.setText(object.get(0).getMenu_detail());
                    //menu_urls=object.get(0).getMenu_pic();
                    adapter.setNewData(object.get(0).getMenu_pic());
                    Glide.with(MenuDetail.this).load(object.get(0).getMenu_avatar()).into((ImageView) iv);

                } else {

                }
            }
        });

        //进行信息的展示
//        detail.setText(menu_detail);
        //适配器中装入数据，便于适配器管理
        adapter = new MenuDetailAdapter(R.layout.item_detail, null);


        adapter.isFirstOnly(true);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rv_pic.setLayoutManager(new LinearLayoutManager(this));
        rv_pic.setAdapter(adapter);


    }

    public void onClick(View v) {
        if (v.getId() == R.id.go_pay) {
            MiniPayUtils.setupPay(this, new Config.Builder("FKX06158ACUEPLLOJYVK79?t=1552356936899", R.mipmap.ic_zhifubao, R.mipmap.ic_weixin).build());
        }
    }

}
