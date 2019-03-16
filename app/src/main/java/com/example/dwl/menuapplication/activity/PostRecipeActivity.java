package com.example.dwl.menuapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.adapter.recycleview.ImageGridadapter;
import com.example.dwl.menuapplication.bean.Menu;
import com.example.dwl.menuapplication.bean.User;
import com.example.dwl.menuapplication.utils.StatusBarUtil;
import com.example.imagepicker1.ImagePicker;
import com.example.imagepicker1.utils.GridSpacingItemDecoration;
import com.example.imagepicker1.utils.ScreenUtils;
import com.example.imagepicker1.view.ImageSelectorActivity;
import com.hb.dialog.dialog.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import io.reactivex.functions.Consumer;


public class PostRecipeActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ImageGridadapter mAdapter;
    private GridLayoutManager gridManager;
    //private ImageView test;
    private EditText detail;
    private EditText title;
    private EditText classify;
    private TextView mCancel;
    private TextView mSend;
    private int maxImageNumber = 9;
    private int spanCount = 4;
    private ArrayList<String> pics;
    private int MenuSize;
    private String pid;
  LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postrecipe);
        ButterKnife.bind(this);
        Bmob.initialize(this, "96073a0eedb9adb5d9fc3fcf1fa71929");
        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted)
                            initRecyclerVIew();

                    }
                });
//        initRecyclerVIew();


      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_write);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //状态栏透明处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);

//        mCancel = findViewById(R.id.Cancel);
//        mCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // startActivity(new Intent(PostRecipeActivity.this, MainActivity.class));
//                finish();
//            }
//        });

        mSend = findViewById(R.id.Send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              loadingDialog = new LoadingDialog(PostRecipeActivity.this);
                loadingDialog.setMessage("正在上传菜谱");
                loadingDialog.show();
                UploadImage();

            }
        });
    }

    private void initRecyclerVIew() {
        //test = findViewById(R.id.testurl);
        detail = findViewById(R.id.detail);
        title = findViewById(R.id.recipe_title);
        classify = findViewById(R.id.recipe_classify);
        recyclerView = (RecyclerView) findViewById(R.id.id_RecyclerView);
        mAdapter = new ImageGridadapter(this, maxImageNumber);
        gridManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(gridManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, ScreenUtils.dip2px(this, 8), false));
        //设置Item增加、移除动画
        //recyclerView.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mAdapter.AddListenter(new ImageGridadapter.AddListenter() {
            @Override
            public void onAddClick(int needNumber) {
                //ImagePicker.getInstance().pickAvatar(PostRecipeActivity.this);
                ImagePicker.getInstance().pickImage(PostRecipeActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE, true, true, true, true);
                //ImagePicker.getInstance().takePhoto(PostRecipeActivity.this,true);//使用摄像头
            }

            @Override
            public void onItemClick(int position) {
                startActivity(PhotoViewPagerActivity.newInstance(PostRecipeActivity.this, position, (ArrayList<String>) mAdapter.getDataList()));
            }

            @Override
            public void onItemDeleteClick(int position) {
                mAdapter.deleteData(position);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            pics = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            //testtext.setText(pics.toString());
            Log.e("-----url",pics.get(0));
            mAdapter.addData(pics);
//            Glide.with(this)
//                    .load(pics.get(0))
//                    //.error(R.mipmap.ic_launcher)
//                    .into(test);
//            OkGo.<String>post("http://47.98.59.6:80")
//                    .tag(this)
//                    .isMultipart(true)
//                    .headers("header1", "headerValue1")
//                    .params("file1",new File(pics.get(0)))
//                    .upFile(new File(pics.get(0)))
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(Response<String> response) {
//                            //handleResponse(response);
//                            Toast.makeText(PostRecipeActivity.this,"chenggong",Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onError(Response<String> response) {
//                            //handleError(response);
//                            Toast.makeText(PostRecipeActivity.this,"shibai",Toast.LENGTH_SHORT).show();
//                        }
//                    });


        }
    }


    private void UploadImage() {
        final String[] filePaths = new String[pics.size()];
        for (int i = 0; i < pics.size(); i++) {
            filePaths[i] = pics.get(i);
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    //Toast.makeText(PostRecipeActivity.this, "dwl", Toast.LENGTH_SHORT).show();
                    UploadText(urls);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                Toast.makeText(PostRecipeActivity.this, "错误码" + statuscode + ",错误描述：" + errormsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });


    }

    private void UploadText(List<String> urls) {
        final Menu p2 = new Menu();
        //getCurrentTime();
        p2.setMenu_detail(detail.getText().toString());
        p2.setMenu_avatar(urls.get(0));
        p2.setMenu_name(title.getText().toString());
        p2.setCreate_person_name(SPUtils.getInstance().getString("login_person_name"));
        p2.setCreate_person_id(SPUtils.getInstance().getString("pid"));
        p2.setMenu_pic(urls);
        p2.setMenu_category(classify.getText().toString());

//        String bql ="select * from Menu";//查询所有的游戏得分记录
//        new BmobQuery<Menu>().doSQLQuery(bql,new SQLQueryListener<Menu>(){
//
//            @Override
//            public void done(BmobQueryResult<Menu> result, BmobException e) {
//                if(e ==null){
//                    List<Menu> list = (List<Menu>) result.getResults();
//                    if(list!=null && list.size()>0){
//                        Menu p2 = new Menu();
//                        p2.setMenu_id(String.valueOf(list.size()));
//                    }else{
//                        Log.i("smile", "查询成功，无数据返回");
//                    }
//                }else{
//                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
//                }
//            }
//        });

        BmobQuery<Menu> query = new BmobQuery<>();
        query.setLimit(100).setSkip(0).order("-createdAt")
                .findObjects(new FindListener<Menu>() {
                    @Override
                    public void done(List<Menu> object, BmobException e) {
                        if (e == null) {
                          p2.setMenu_id(object.size()+1+"");
                            p2.save(new SaveListener<String>() {
                                @Override
                                public void done(String objectId, BmobException e) {
                                    if (e == null) {
                                        loadingDialog.dismiss();
                                        Toast.makeText(PostRecipeActivity.this, "上传菜谱成功" , Toast.LENGTH_SHORT).show();
                                        //加入跳转回主页的逻辑
                                        startActivity(new Intent(PostRecipeActivity.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(PostRecipeActivity.this, "发送失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("----------------error", e.getMessage());
                                    }
                                }
                            });

                        } else {
                            // ...
                        }
                    }
                });



    }

    private String GetPid(String pname) {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("pname", pname);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                    pid = object.get(0).getPid();
                    //Log.e("person",pid);
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        //Log.e("rrrrrr", pid);
        return pid;
    }

    private int GetMenuSize() {
        String bql ="select * from Menu";//查询所有的游戏得分记录
        new BmobQuery<Menu>().doSQLQuery(bql,new SQLQueryListener<Menu>(){

            @Override
            public void done(BmobQueryResult<Menu> result, BmobException e) {
                if(e ==null){
                    List<Menu> list = (List<Menu>) result.getResults();
                    if(list!=null && list.size()>0){
                        MenuSize = list.size();
                    }else{
                        Log.i("smile", "查询成功，无数据返回");
                    }
                }else{
                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });
        Log.e("Menu", String.valueOf(MenuSize));
        return MenuSize;
    }


//    private void getCurrentTime() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH)+1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        int second = calendar.get(Calendar.SECOND);
//        Toast.makeText(PostRecipeActivity.this,"Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second,Toast.LENGTH_SHORT).show();
//    }
}
