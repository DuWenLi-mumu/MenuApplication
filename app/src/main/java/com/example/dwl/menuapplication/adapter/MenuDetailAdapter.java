package com.example.dwl.menuapplication.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.dwl.menuapplication.R;

import java.util.List;

public class MenuDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MenuDetailAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(final BaseViewHolder helper,String item) {
        //在每个数据行内部设置各种属性
        //与followee_item_list绑定
        helper.addOnClickListener(R.id.pic_menu_one);

        Glide.with(mContext).load(item).into((ImageView) helper.getView(R.id.pic_menu_one));
//        helper.setText(R.id.tv_title,item.getMenu_name());
//        helper.setText(R.id.tv_content,"美食家："+item.getMenu_creator_name());
//
//        Glide.with(mContext).load(item.getMenu_pic().get(0)).into((ImageView) helper.getView(R.id.iv_head));
        //helper.setImageResource(R.id.iv_head,item.get);

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