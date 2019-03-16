package com.example.dwl.menuapplication.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.bean.Menu;

import java.util.List;

public class PersonListAdapter extends BaseQuickAdapter<Menu, BaseViewHolder> {
    public PersonListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(final BaseViewHolder helper,Menu item) {
        //在每个数据行内部设置各种属性
        //与followee_item_list绑定
        helper.addOnClickListener(R.id.menu_name)
                .addOnClickListener(R.id.menu_pic);
        helper.setText(R.id.menu_name,item.getMenu_name());
      //  helper.setText(R.id.tv_content,"美食家："+item.getMenu_creator_name());

        Glide.with(mContext).load(item.getMenu_avatar()).into((ImageView) helper.getView(R.id.menu_pic));
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