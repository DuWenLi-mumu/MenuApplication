package com.example.dwl.menuapplication.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.dwl.menuapplication.Glide4Engine;
import com.example.dwl.menuapplication.MenuDetail;
import com.example.dwl.menuapplication.MessageEvent;
import com.example.dwl.menuapplication.MyApp;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.bean.User;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.functions.Consumer;

public class ActivityRxPhoto extends AppCompatActivity {
    //    @BindView(R.id.iv_avatar)
//    ImageView ivAvatar;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private ImageView iv_avatar;
    //private ImageView Myavatar;
    //private ImageView test;
    private TextView tv_name;
    private NavigationView navigationView;
    private ImageView icon_image;
    private String ObjectId;
    private List<Uri> result;
    private String UrlinPhone;
    private ImageView Goback;
    //private String NewName = "小兰";
    private String NewName = "0";
    private String FileUrl;
    private TextView Signature;
//    private String NewSign = "1111";
    private String NewSign;
    private String OldName;
    private String OldSign;
    private String account;
    private Boolean isModified = false;

    Button exit;
    MyApp Account;
//    private boolean isChangeAvatar = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //RxBarTool.noTitle(this);

        //test = findViewById(R.id.test);
        setContentView(R.layout.activity_von_photo);
        iv_avatar = findViewById(R.id.iv_avatar);
        tv_name = findViewById(R.id.tv_name);
        Goback = findViewById(R.id.goback);
        Signature = findViewById(R.id.editText2);

        Account = (MyApp)getApplicationContext();
        exit=findViewById(R.id.btn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.getInstance().remove("account");
                SPUtils.getInstance().remove("pass");
                Intent intent = new Intent(ActivityRxPhoto.this, SignUpLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        BmobQuery<User> PersonBmobQuery = new BmobQuery<>();
        PersonBmobQuery.addWhereEqualTo("mobilePhoneNumber", Account.getAccount());
        PersonBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    Glide.with(ActivityRxPhoto.this)
                            .load(object.get(0).getPerson_pic())
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(iv_avatar);
                    OldName = object.get(0).getUsername();
                    tv_name.setText(object.get(0).getUsername());
                    OldSign = object.get(0).getPersonal_signature();
                    Signature.setText(object.get(0).getPersonal_signature());
                    ObjectId = object.get(0).getObjectId();

                    Log.e("-------lan",ObjectId);
                } else {
                    Log.e("BMOB", e.toString());
                    //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted)
                            initInfo();

                    }
                });


    }

    private void initInfo() {

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matisse.from(ActivityRxPhoto.this)
                        .choose(MimeType.ofAll())//图片类型
                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(1)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "com.example.dwl.menuapplication.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new Glide4Engine())//图片加载引擎
                        .forResult(REQUEST_CODE_CHOOSE);//
            }
        });

        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(ActivityRxPhoto.this).builder()
                        .setTitle("请输入新的用户名")
                        .setEditText("");
                myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("------result", myAlertInputDialog.getResult());
                        NewName = myAlertInputDialog.getResult();
                        tv_name.setText(NewName);
                        isModified = true;
                        myAlertInputDialog.dismiss();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertInputDialog.dismiss();
                    }
                });
                myAlertInputDialog.show();
            }
        });

        Signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(ActivityRxPhoto.this).builder()
                        .setTitle("请输入新的个性签名")
                        .setEditText("");
                myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("------result", myAlertInputDialog.getResult());
                        NewSign = myAlertInputDialog.getResult();
                        Signature.setText(NewSign);
                        isModified = true;
                        myAlertInputDialog.dismiss();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertInputDialog.dismiss();
                    }
                });
                myAlertInputDialog.show();
            }
        });

        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NewName == null)
                    Log.e("newname","no");
                if(NewSign == null)
                    Log.e("newsign","NO");



                if(isModified){
                    final User user = BmobUser.getCurrentUser(User.class);
                    user.setUsername(null);
                    user.setPerson_pic(null);
                    user.setPersonal_signature(null);
                    if(!OldName.equals(tv_name.getText())){
                        Log.e("yes","good");
                        user.setUsername(tv_name.getText().toString());
                    }
                    if(FileUrl != null) {
                        Log.e("fileurl", FileUrl);
                        user.setPerson_pic(FileUrl);
                    }
                    if(!OldSign.equals(Signature.getText())){
                        user.setPersonal_signature(Signature.getText().toString());
                    }
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                               // startActivity(new Intent(ActivityRxPhoto.this,MyMainPage.class));
                                finish();
                                String[] Info = new String[2];
                                Info[0] = FileUrl;
                                Info[1] = tv_name.getText().toString();
                                EventBus.getDefault().post(new MessageEvent(Info));
                                Log.e("success","yes");
                            } else {
                                Log.e("error", e.getMessage());

                            }
                        }
                    });
                }
                else{
                   // startActivity(new Intent(ActivityRxPhoto.this,MyMainPage.class));
                    finish();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            result = Matisse.obtainResult(data);
//            Log.e("------url", result.toString());
            Glide.with(ActivityRxPhoto.this)
                    .load(result.get(0))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(iv_avatar);

            UrlinPhone = getRealFilePath(ActivityRxPhoto.this,result.get(0));
            Log.e("------urlinphone", UrlinPhone);
            if(UrlinPhone != null){
                final LoadingDialog loadingDialog = new LoadingDialog(this);
                loadingDialog.setMessage("正在上传头像");
                loadingDialog.show();

                final BmobFile bmobFile = new BmobFile(new File(UrlinPhone));
                bmobFile.uploadblock(new UploadFileListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //Toast.makeText(ActivityRxPhoto.this, "上传文件成功:", Toast.LENGTH_SHORT).show();
                            FileUrl = bmobFile.getFileUrl().toString();
                            isModified = true;
                            Log.e("-----fileurl", FileUrl);
                            loadingDialog.dismiss();
                        }

                    }

                    @Override
                    public void onProgress(Integer value) {
                        // 返回的上传进度（百分比）
                    }
                });
            }

        }

    }

    public static String getRealFilePath (Context context, Uri uri){
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if (scheme == null) realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir;
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                }
            }
        }

        return realPath;
    }
}
