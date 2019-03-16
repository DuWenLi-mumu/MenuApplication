package com.example.dwl.menuapplication.bean.vPage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dwl.menuapplication.Category;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.bean.ImageLoadFactory;

import java.util.ArrayList;
import java.util.List;




public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<Object> mData;
    private Context mContext;

    private int MaxElevationFactor = 9;

    @Override
    public int getMaxElevationFactor() {
        return MaxElevationFactor;
    }

    @Override
    public void setMaxElevationFactor(int MaxElevationFactor) {
        this.MaxElevationFactor = MaxElevationFactor;
    }

    public CardPagerAdapter(Context context) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();

        this.mContext = context;
    }

    public void addImgUrlList(List<Object> imgUrlList) {
        mData.addAll(imgUrlList);

        for (int i = 0; i < imgUrlList.size(); i++) {
            mViews.add(null);
        }
    }


    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.adapter, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (cardItemClickListener != null) {
//                    cardItemClickListener.onClick(position);
//                }

            }
        });

        onTouchViewPager(view, position);
        // ★★★这句话很重要！！！别忘了写！！！

        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        cardView.setMaxCardElevation(MaxElevationFactor);
        mViews.set(position, cardView);
        return view;
    }



    /**
     * ★当手指按住轮播图不动时，轮播图停止滚动；当点击轮播图时，跳转到相关界面
     */
    public void onTouchViewPager(View view, final int position) {
        // 给图片注册触摸事件监听器
        view.setOnTouchListener(new View.OnTouchListener() {

            private long downTime;
            private int downX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 按下去时，记录按下的坐标和时间，用于判断是否是点击事件
                     //`   handler.removeCallbacksAndMessages(null);// 手指按下时，取消所有事件，即轮播图不在滚动了
                        downX = (int) event.getX();
                        downTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:// 抬起手指时，判断落下抬起的时间差和坐标，符合以下条件为点击
                        // Toast.makeText(context, "手指抬起了", 0).show();
                        if (System.currentTimeMillis() - downTime < 500
                                && Math.abs(downX - event.getX()) < 30) {// ★考虑到手按下和抬起时的坐标不可能完全重合，这里给出30的坐标偏差
                           //跳转到对应的类别页面
                            //Toast toast=Toast.makeText(mContext.getApplicationContext(), "默认的Toast"+position, Toast.LENGTH_SHORT);
                          //  toast.show();

                            Intent intent = new Intent(mContext, Category.class);
                            intent.putExtra("position",position);
                            mContext.startActivity(intent);


                        }
                       // startRoll();
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Object imgUrl, View view) {
        ImageView iv = (ImageView) view.findViewById(R.id.item_iv);
        ImageLoadFactory.getInstance().loadImage(iv,imgUrl,iv.getContext());

    }

    private OnCardItemClickListener cardItemClickListener;

    public interface OnCardItemClickListener {
        void onClick(int position);
    }

    public void setOnCardItemClickListener(OnCardItemClickListener cardItemClickListener) {
        this.cardItemClickListener = cardItemClickListener;
    }

}
