package com.example.dwl.menuapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.bean.Menu;
import com.example.dwl.menuapplication.bean.Product;
import com.example.dwl.menuapplication.bean.RecycleItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MasonryView>{
    private ArrayList<Menu> menus;
    private static RecycleItemClickListener itemClickListener;
    Context mContext;


    public GridAdapter(ArrayList<Menu> list, RecycleItemClickListener clickListener, Context context) {
        menus=list;
        itemClickListener=clickListener;
        mContext =context;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView masonryView, int position) {
      //  masonryView.imageView.setImageResource(menus.get(position).getMenu_pic());
       // masonryView.imageView.setImageURI(Uri.parse(menus.get(position).getMenu_pic().get(0)));
//        switch (position%3){
//            case 0:
//                masonryView.imageView.setLayoutParams(new RelativeLayout.LayoutParams(((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth(), 150));
//                break;
//            case 1:
//                masonryView.imageView.setLayoutParams(new RelativeLayout.LayoutParams(((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth(), 50));
//                break;
//            case 2:
//                masonryView.imageView.setLayoutParams(new RelativeLayout.LayoutParams(((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth(), 200));
//                break;
//        }
        Glide.with(mContext).load(menus.get(position).getMenu_avatar()).into(masonryView.imageView);
        masonryView.textView.setText(menus.get(position).getMenu_name());


    }


    @Override
    public int getItemCount() {
        return menus.size();
    }

    //viewholder
    public static class MasonryView extends  RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private TextView textView;


        public MasonryView(View itemView){
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.im_item );
            textView= (TextView) itemView.findViewById(R.id.tv_content);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v,this.getLayoutPosition());
        }


    }


}
