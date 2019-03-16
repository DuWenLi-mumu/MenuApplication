package com.example.dwl.menuapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.dwl.menuapplication.adapter.FolloweeBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注的人 下拉列表操作
 * 利用brvah框架实现不同的菜单样式
 */
public class Followee extends AppCompatActivity {
    RecyclerView rv;
    FolloweeBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followee);

        rv = findViewById(R.id.rv);//找到当前类所对应的xml中的recyview的id

        //创造数据，存放在ArrayList中
        ArrayList<String> namedatalist=new ArrayList<>();
        namedatalist.add("张三");
        namedatalist.add("李四");
        namedatalist.add("王五");
        namedatalist.add("木木");
        namedatalist.add("小明");
        namedatalist.add("小红");
        namedatalist.add("小兰");
        namedatalist.add("柯南");
        namedatalist.add("新一");
        namedatalist.add("工藤");

        //适配器中装入数据，便于适配器管理
        adapter = new FolloweeBaseAdapter(R.layout.followee_item_list,namedatalist );//适配器用来获取每个列表的单行数据 item_list是自定义的单行数据适配器的样式

        //设置点击 头像和点击标题均跳转到主页

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.iv_head:
                        ToastUtils.showShort("点击head，此处添加代码，跳转到主页");
                        break;
                    case R.id.tv_title:
                        ToastUtils.showShort("点击title，此处添加代码，跳转到主页");
                        break;
                }

            }
        });
        adapter.isFirstOnly(true);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


    }
}
