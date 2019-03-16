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
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.dwl.menuapplication.IM.MessageActivity;
import com.example.dwl.menuapplication.MenuDetail;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.adapter.PersonListAdapter;
import com.example.dwl.menuapplication.bean.Following;
import com.example.dwl.menuapplication.bean.Menu;
import com.example.dwl.menuapplication.bean.Person;
import com.example.dwl.menuapplication.bean.User;
import com.example.dwl.menuapplication.utils.StatusBarUtil;
import com.flyco.roundview.RoundTextView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class PersonPage extends AppCompatActivity {

    private int mOffset = 0;
    private int mScrollY = 0;
    RecyclerView rv;
    PersonListAdapter mPersonListAdapter;
    ImageView person_img;
    TextView person_name;
    ImageView toolbar_avater;
    TextView title;
    boolean attentioned;
    String followee_name;
    String follow_id;
    String following_avatar;
    String following_person_s;
    String myPid;
    String Chatid;
    String ChatAvatar;
    String ChatName;
    Following mFollowing;
    RoundTextView attention;
    ArrayList<String> personPositionList;
    TextView sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_page);


        followee_name= getIntent().getStringExtra("followee_name");
        Log.e("followname",followee_name);
        follow_id=getIntent().getStringExtra("follow_id");
        Log.e("eeeee",follow_id);
        following_avatar=getIntent().getStringExtra("following_avatar");
        following_person_s=getIntent().getStringExtra("following_person_s");
        myPid=getIntent().getStringExtra("myPid");
        person_img=findViewById(R.id.person_avatar);
        person_name=findViewById(R.id.person_name);
        toolbar_avater=findViewById(R.id.toolbar_avatar);
        title=findViewById(R.id.title);
        attention=findViewById(R.id.attention);

        sign=findViewById(R.id.signature);
        initData();

//        查询用户的用户名以及头像
        BmobQuery<Following> UserBmobQuery = new BmobQuery<>();
        UserBmobQuery.addWhereEqualTo("following_name", followee_name);
        UserBmobQuery.findObjects(new FindListener<Following>() {
            @Override
            public void done(List<Following> object, BmobException e) {
                if (e == null) {
                if(object.size()<=0){
                    //执行following表查询语句成功，但是没有找到这个名字

                    }else {
                    //执行following表查询语句成功，找到了这个人
                        person_name.setText(object.get(0).getFollowing_name());
                        Glide.with(PersonPage.this).load(object.get(0).getFollowing_avatar()).into((ImageView) person_img);
                        title.setText(object.get(0).getFollowing_name());
                        Glide.with(PersonPage.this).load(object.get(0).getFollowing_avatar()).into((ImageView) toolbar_avater);
                        sign.setText(object.get(0).getFollowing_person_s());

                    }
                }else {
                   //执行查询语句失败
                }
            }
        });


//       这里应该是返回按钮的实现功能
        final Toolbar toolbar = findViewById(R.id.toolbar);
        //     setSupportActionBar(toolbar);//自己添加,会多出美食秀这几个字
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


        //利用mypid和followingid查询follow表，查看我是否关注了这个人
        BmobQuery<Following> eq1 = new BmobQuery<Following>();
        eq1.addWhereEqualTo("pid", SPUtils.getInstance().getString("pid"));

        BmobQuery<Following> eq2 = new BmobQuery<Following>();
        eq2.addWhereEqualTo("follow_pid", follow_id);

        Log.i("test pid", SPUtils.getInstance().getString("pid"));
        Log.i("test follow_pid", follow_id);

        List<BmobQuery<Following>> andQuerys = new ArrayList<BmobQuery<Following>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        BmobQuery<Following> query = new BmobQuery<Following>();
        query.and(andQuerys);

        query.findObjects(new FindListener<Following>() {
            @Override
            public void done(List<Following> object, BmobException e) {
                if(e==null){
                    Log.i("test obj size", String.valueOf(object.size()));
                    if(object.size()<=0){
//                        //说明我原来没有关注这个人
                        attentioned=false;
                        attention.setText("关注");
                        mFollowing=new Following(following_person_s,followee_name,follow_id,myPid,following_avatar);
                    }
                    else{
                        //我已经关注这个人了
                        mFollowing=object.get(0);
                        attentioned=true;
                        attention.setText("已关");
                    }

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });




//        //利用mypid和followingid查询follow表，查看我是否关注了这个人
//        BmobQuery<Following> eq1 = new BmobQuery<Following>();
//        eq1.addWhereEqualTo("pid", SPUtils.getInstance().getString("pid"));
//        BmobQuery<Following> eq2 = new BmobQuery<Following>();
//
//        Log.e("test myPid",SPUtils.getInstance().getString("pid"));
//        Log.e("test myFollowid",follow_id);
//
//        eq2.addWhereEqualTo("follow_id", follow_id);
//        List<BmobQuery<Following>> andQuerys = new ArrayList<BmobQuery<Following>>();
//        andQuerys.add(eq1);
//        andQuerys.add(eq2);
//        BmobQuery<Following> query = new BmobQuery<Following>();
//        query.and(andQuerys);
//        query.findObjects(new FindListener<Following>() {
//            @Override
//            public void done(List<Following> object, BmobException e) {
//                if (e == null){
//                    Log.e("test size", String.valueOf(object.size()));
//                    if(object.size()<=0){
//                        //说明我原来没有关注这个人
//                        attentioned=false;
//                        attention.setText("关注");
//                        mFollowing=new Following(following_person_s,followee_name,follow_id,myPid,following_avatar);
//                    }
//                    else{
//                        //我已经关注这个人了
//                        mFollowing=object.get(0);
//                        attentioned=true;
//                        attention.setText("已关");
//                    }
//
//                } else {
//                    //查询请求出错
//
//                    Log.e("test","request error");
//                }
//            }
//        });



        findViewById(R.id.attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击attention按钮
                if(!attentioned){
                    //我原来没有关注该用户，现在想要关注这个用户
                    //在数据库中需要插入这个用户
                    //添加关注到数据库
                    mFollowing.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                attentioned=true;
                                attention.setText("已关");
                                ToastUtils.showShort("关注成功！！！");
                                attentioned=true;
                            } else {
                                ToastUtils.showShort("关注失败");
                            }
                        }
                    });
                }else{
                    //我想取消关注，删掉数据库中的关注数据
                    Following following=new Following();
                    following.setObjectId(mFollowing.getObjectId());
                    following.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                attentioned=false;
                                attention.setText("关注");
                                attentioned=false;
                                ToastUtils.showShort("取消关注成功");
                            } else {
                                //取消收藏失败
                                ToastUtils.showShort("取消关注失败");
                            }
                        }
                    });

                }

            }
        });
        findViewById(R.id.leaveword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobQuery<User> UserBmobQuery = new BmobQuery<>();
                UserBmobQuery.addWhereEqualTo("pid", follow_id);
                UserBmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> object, BmobException e) {
                        if (e == null) {
//                            Chatid = object.get(0).getPid();
//                            ChatAvatar = object.get(0).getPerson_pic();
//                            ChatName = object.get(0).getUsername();
                            Intent intent = new Intent(PersonPage.this, MessageActivity.class);
                            intent.putExtra("Chatid",object.get(0).getObjectId());
                            intent.putExtra("ChatAvatar",object.get(0).getPerson_pic());
                            intent.putExtra("ChatName",object.get(0).getUsername());
                            startActivity(intent);
                            //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Log.e("BMOB", e.toString());
                            //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                //Toast.makeText(view.getContext(),"点击了留言",Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(3000);
            }


            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
//                super.onLoadmore(refreshlayout);
                refreshLayout.finishLoadmore(2000);
            }

            //   @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

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


    }

    private void Whoishe() {
        BmobQuery<User> UserBmobQuery = new BmobQuery<>();
        UserBmobQuery.addWhereEqualTo("pid", follow_id);
        UserBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    Chatid = object.get(0).getPid();
                    ChatAvatar = object.get(0).getPerson_pic();
                    ChatName = object.get(0).getUsername();
                    Log.e("info111",ChatName+Chatid);
                    //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initData() {
        rv=findViewById(R.id.person_rv);


        //适配器中装入数据，便于适配器管理
        mPersonListAdapter = new PersonListAdapter(R.layout.person_page_list,null );//适配器用来获取每个列表的单行数据 item_list是自定义的单行数据适配器的样式
        personPositionList=new ArrayList<String>();
        //设置点击 头像和点击标题均跳转到主页
        mPersonListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.menu_name:
//                        ToastUtils.showShort("点击head，此处添加代码，跳转到主页");
                        break;
                    case R.id.menu_pic:
                        Intent intent = new Intent(PersonPage.this, MenuDetail.class);
                        intent.putExtra("menu_name",personPositionList.get(position));
                        startActivity(intent);
                        break;
                }
            }
        });

        mPersonListAdapter.isFirstOnly(true);
        mPersonListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rv.setLayoutManager(new LinearLayoutManager(PersonPage.this));
        rv.setAdapter(mPersonListAdapter);
        rv.setNestedScrollingEnabled(false);



        BmobQuery<Menu> UserBmobQuery = new BmobQuery<>();
        UserBmobQuery.addWhereEqualTo("create_person_name", followee_name);
        UserBmobQuery.findObjects(new FindListener<Menu>() {
            @Override
            public void done(List<Menu> object, BmobException e) {
                if (e == null) {
                    mPersonListAdapter.setNewData(object);
                    for (int i = 0; i < object.size(); i++) {
                        personPositionList.add(object.get(i).getMenu_name());
                    }
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }
}
