package com.example.dwl.menuapplication.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;


import com.chad.library.adapter.base.BaseViewHolder;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.bean.Following;

import java.util.List;

/**
 * 关注的人 列表加载适配器
 * 适配器用于数据的管理，在此管理每个关注的人员列表
 */
public class FolloweeBaseAdapter extends BaseQuickAdapter<Following, BaseViewHolder> {
    public FolloweeBaseAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(final BaseViewHolder helper,Following item) {
        //在每个数据行内部设置各种属性
        //与followee_item_list绑定
        helper.addOnClickListener(R.id.iv_head)
                .addOnClickListener(R.id.tv_title);
        helper.setText(R.id.tv_title,"美食家："+item.getFollowing_name());
        helper.setText(R.id.tv_content,"个性签名："+item.getFollowing_person_s());
        Glide.with(mContext).load(item.getFollowing_avatar()).into((ImageView) helper.getView(R.id.iv_head));

//        //设置点击内容时做的操作
//        helper.getView(R.id.tv_content).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //ToastUtils.showShort("我点击了个"+helper.getLayoutPosition()+"内容");
//                //添加代码 跳转到该人的主页
//                ToastUtils.showShort("此处添加代码，跳转到"+item.getPid()+"的主页");
//            }
//        });


    }


}
