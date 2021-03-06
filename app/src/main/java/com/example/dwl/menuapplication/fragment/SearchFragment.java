package com.example.dwl.menuapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.dwl.menuapplication.MenuDetail;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.Search;
import com.example.dwl.menuapplication.bean.GlideImageClient;
import com.example.dwl.menuapplication.bean.ImageLoadFactory;
import com.example.dwl.menuapplication.bean.vPage.BezierViewPager;
import com.example.dwl.menuapplication.bean.vPage.CardPagerAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class SearchFragment extends Fragment {
    private MaterialSearchView searchView;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v=inflater.inflate(R.layout.fragment_search, container, false);

        /*搜索框的*/

        initEvent();

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("");
      //  toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted);
    //    toolbar.setTitle("搜索");
        //setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        searchView = (MaterialSearchView) v.findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                BmobQuery<com.example.dwl.menuapplication.bean.Menu> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("menu_name", query);
                categoryBmobQuery.findObjects(new FindListener<com.example.dwl.menuapplication.bean.Menu>() {
                    @Override
                    public void done(List<com.example.dwl.menuapplication.bean.Menu> object, BmobException e) {
                        if (e == null) {
                            if(object.size()==0){
                                ToastUtils.showShort("数据库暂无此菜~");
                                return ;
                            }
                            else {
                                Intent intent = new Intent(getActivity(), MenuDetail.class);
                                intent.putExtra("menu_name",query);
                                startActivity(intent);

                            }

                        } else {

                        }
                    }
                });


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        //点击事件
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String query = (String) adapterView.getItemAtPosition(i);
                searchView.setQuery(query,true);

            }
        });

        /*    搜索框的      */

        ImageLoadFactory.getInstance().setImageClient(new GlideImageClient());
        initImgList();


        int mWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
       // float heightRatio = 0.565f;  //高是宽的 0.565 ,根据图片比例
        float heightRatio = 0.565f;
        CardPagerAdapter cardAdapter = new CardPagerAdapter(getActivity());
        cardAdapter.addImgUrlList(imgList);

        //设置阴影大小，即vPage  左右两个图片相距边框  maxFactor + 0.3*CornerRadius   *2
        //设置阴影大小，即vPage 上下图片相距边框  maxFactor*1.5f + 0.3*CornerRadius
        int maxFactor = mWidth / 25;
        cardAdapter.setMaxElevationFactor(maxFactor);

        int mWidthPading = mWidth / 8;

        //因为我们adapter里的cardView CornerRadius已经写死为10dp，所以0.3*CornerRadius=3
        //设置Elevation之后，控件宽度要减去 (maxFactor + dp2px(3)) * heightRatio
        //heightMore 设置Elevation之后，控件高度 比  控件宽度* heightRatio  多出的部分
        float heightMore = (1.5f * maxFactor + dp2px(3)) - (maxFactor + dp2px(3)) * heightRatio;
        int mHeightPading = (int) (mWidthPading * heightRatio - heightMore);

        BezierViewPager viewPager = (BezierViewPager) v.findViewById(R.id.view_page);
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, (int) (mWidth * heightRatio)));
        viewPager.setPadding(mWidthPading, mHeightPading, mWidthPading, mHeightPading);
        viewPager.setClipToPadding(false);
        viewPager.setAdapter(cardAdapter);
        viewPager.showTransformer(0.2f);



//
//        BezierRoundView bezRound = (BezierRoundView) findViewById(R.id.bezRound);
//        bezRound.attach2ViewPage(viewPager);


//        ImageView iv_bg = (ImageView) v.findViewById(R.id.iv_bg);
//        iv_bg.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, (int) ((mWidth * heightRatio) + dp2px(60))));



        return v;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private List<Object> imgList;

    public void initImgList(){
        imgList=new ArrayList<>();
//        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533974978237&di=aa67212ea09517b3729c4394e6c6e92d&imgtype=jpg&src=http%3A%2F%2Fimg4.imgtn.bdimg.com%2Fit%2Fu%3D1244239179%2C175145384%26fm%3D214%26gp%3D0.jpg");
//        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533974984588&di=3aca1b010d7b67b027b69987f3823c52&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F586364185dbc1.jpg");
//        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533974992976&di=6bf3b70ace9b54762620cb1595b75294&imgtype=0&src=http%3A%2F%2Fwww.fahao.cc%2Fuploadfiles%2F201702%2F16%2F20170216023654105.jpg");
//        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534569703&di=fa557bb817a9bc3d64be9400546cb974&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.tuwandata.com%2Fv2%2Fthumb%2Fall%2FMjAxZSwwLDAsNCwzLDEsLTEsMSw%3D%2Fu%2Fwww.tuwan.com%2Fuploads%2Fallimg%2F1503%2F04%2F765-150304143511-50.jpg");
//



        imgList.add(R.mipmap.p1);
        imgList.add(R.mipmap.p2);
        imgList.add(R.mipmap.p3);
        imgList.add(R.mipmap.p4);

    }


    /*搜索框的*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
    }


//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);
//
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//
//        return true;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*搜索框的*/
    private void initEvent() {
        final TextView tv1=v.findViewById(R.id.tv1);
        final TextView tv2=v.findViewById(R.id.tv2);
        final TextView tv3=v.findViewById(R.id.tv3);
        final TextView tv4=v.findViewById(R.id.tv4);
        final TextView tv5=v.findViewById(R.id.tv5);
        final TextView tv6=v.findViewById(R.id.tv6);
        final TextView tv7=v.findViewById(R.id.tv7);
        final TextView tv8=v.findViewById(R.id.tv8);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuDetail.class);
                intent.putExtra("menu_name",tv1.getText().toString());
                startActivity(intent);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuDetail.class);
                intent.putExtra("menu_name",tv2.getText().toString());
                startActivity(intent);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuDetail.class);
                intent.putExtra("menu_name",tv3.getText().toString());
                startActivity(intent);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuDetail.class);
                intent.putExtra("menu_name",tv4.getText().toString());
                startActivity(intent);
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuDetail.class);
                intent.putExtra("menu_name",tv5.getText().toString());
                startActivity(intent);
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuDetail.class);
                intent.putExtra("menu_name",tv6.getText().toString());
                startActivity(intent);
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuDetail.class);
                intent.putExtra("menu_name",tv7.getText().toString());
                startActivity(intent);
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuDetail.class);
                intent.putExtra("menu_name",tv8.getText().toString());
                startActivity(intent);
            }
        });

    }
}


