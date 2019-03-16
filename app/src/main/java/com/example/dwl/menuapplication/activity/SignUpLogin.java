package com.example.dwl.menuapplication.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.dwl.menuapplication.MyApp;
import com.example.dwl.menuapplication.R;
import com.example.dwl.menuapplication.bean.Following;
import com.example.dwl.menuapplication.bean.User;
import com.example.dwl.menuapplication.utils.StringUtils;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class SignUpLogin extends AppCompatActivity {

    EditText mEtPhone,mEtPassWord, phone2, pass2, confirmPass, name2;
    RelativeLayout relativeLayout, relativeLayout2;
    LinearLayout mainLinear,img;
    TextView signUp,login;
    //TextView forgetPass;
    ImageView logo,back,qq,wx,wb;
    LinearLayout.LayoutParams params, params2;
    FrameLayout.LayoutParams params3;
    FrameLayout mainFrame;
    ObjectAnimator animator2, animator1;
    MyApp Account;
    MyApp my;
    CheckBox remember,auto_login;
    boolean flag,flag2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);

        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params3 = new FrameLayout.LayoutParams(inDp(50), inDp(50));

        signUp = (TextView) findViewById(R.id.tv_register);
        name2 = (EditText) findViewById(R.id.name2);
        login = (TextView) findViewById(R.id.tv_action);////////
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPassWord = (EditText) findViewById(R.id.et_passWord);
        remember=(CheckBox) findViewById(R.id.remember_pwd);
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
        qq=(ImageView) findViewById(R.id.img_qq);
        wx=(ImageView) findViewById(R.id.img_weixin);
        wb=(ImageView) findViewById(R.id.img_weibo);
        img = (LinearLayout) findViewById(R.id.img);
        phone2 = (EditText) findViewById(R.id.phone2);

        //forgetPass = (TextView) findViewById(R.id.forget);
        pass2 = (EditText) findViewById(R.id.pass2);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        confirmPass = (EditText) findViewById(R.id.pass3);
        back = (ImageView) findViewById(R.id.backImg);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relative2);
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);

        logo = new ImageView(this);
        logo.setImageResource(R.drawable.logo);
        logo.setLayoutParams(params3);


        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag = isChecked;
            }
        });
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag2 = isChecked;
            }
        });

        if(EmptyUtils.isNotEmpty(SPUtils.getInstance().getString("account"))){
            remember.setChecked(true);
        }else{
            remember.setChecked(false);
        }

        if(EmptyUtils.isNotEmpty(SPUtils.getInstance().getString("account"))){
            auto_login.setChecked(true);
        }else{
            auto_login.setChecked(false);
        }

        mEtPhone.setText(SPUtils.getInstance().getString("account"));
        mEtPassWord.setText(SPUtils.getInstance().getString("pass"));

        relativeLayout.post(new Runnable() {
            @Override
            public void run() {

                logo.setX((relativeLayout2.getRight() / 2));
                logo.setY(inDp(50));
                mainFrame.addView(logo);
            }
        });

        params.weight = (float) 0.75;
        params2.weight = (float) 4.25;

        mainLinear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mainLinear.getWindowVisibleDisplayFrame(r);
                int screenHeight = mainFrame.getRootView().getHeight();


                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) {
                    // keyboard is opened
                    if (params.weight == 4.25) {

                        animator1 = ObjectAnimator.ofFloat(back, "scaleX", (float) 1.95);
                        animator2 = ObjectAnimator.ofFloat(back, "scaleY", (float) 1.95);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2);
                        //set.setDuration(1000);
                        set.setDuration(800);
                        set.start();

                    } else {

                        animator1 = ObjectAnimator.ofFloat(back, "scaleX", (float) 1.75);
                        animator2 = ObjectAnimator.ofFloat(back, "scaleY", (float) 1.75);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2);
                       // set.setDuration(500);
                        set.setDuration(400);
                        set.start();
                    }
                } else {
                    // keyboard is closed
                    animator1 = ObjectAnimator.ofFloat(back, "scaleX", 3);
                    animator2 = ObjectAnimator.ofFloat(back, "scaleY", 3);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(animator1, animator2);
                    //set.setDuration(500);
                    set.setDuration(400);
                    set.start();
                }
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (params.weight == 4.25) {

                    String account2 = phone2.getText().toString();
                    if(TextUtils.isEmpty(account2)||(!StringUtils.isMobile(account2)))
                    {
                        Toast.makeText(getApplicationContext(),"请输入有效的11位手机号码",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final String name = name2.getText().toString();
                    String password2 = pass2.getText().toString();
                    if (TextUtils.isEmpty(password2) || password2.length() < 6)
                    {
                        Toast.makeText(getApplicationContext(),"请输入6-12由数字和字母组成的密码",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String comfirm = confirmPass.getText().toString();
                    if(!TextUtils.equals(comfirm,password2)){
                        Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        // final Person person=
                        //BmobUser u = new BmobUser();
                        final User u = new User();
                        u.setUsername(name);
                        u.setMobilePhoneNumber(account2);
                        u.setPassword(password2);
                        u.setPersonal_signature("这个人很懒，什么都没有留下");
                        u.setPerson_pic("http://bmob-cdn-24019.b0.upaiyun.com/2019/03/05/2fbf9f8f4065554f805357bfd70ee486.png");

                        // u.setEmail("123456@qq.com");
                        BmobQuery<User> query = new BmobQuery<>();
                        query.setLimit(100).setSkip(1).order("-createdAt")
                                .findObjects(new FindListener<User>() {
                                    @Override
                                    public void done(List<User> object, BmobException e) {
                                        if (e == null) {
                                            String temp = Integer.toString(object.size()+2);
                                            //ToastUtils.showShort("pid"+temp);
                                            u.setPid(temp);

                                            u.signUp(new SaveListener<BmobUser>() {
                                                @Override
                                                public void done(BmobUser user, BmobException e) {
                                                    if (e == null) {
                                                        Snackbar.make(view, "注册成功", Snackbar.LENGTH_LONG).show();
                                                    } else {
                                                        Snackbar.make(view, "该账号已注册：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                    }
                                                    return;
                                                }
                                            });
                                        } else {
                                            Log.e("BMOB", e.toString());
                                        }
                                    }
                                });
                        //Snackbar.make(relativeLayout, "Sign Up Complete", Snackbar.LENGTH_SHORT).show();
                        return;
                    }



                }
                phone2.setVisibility(View.VISIBLE);
                name2.setVisibility(View.VISIBLE);
                pass2.setVisibility(View.VISIBLE);
                confirmPass.setVisibility(View.VISIBLE);

                final ChangeBounds bounds = new ChangeBounds();
                //bounds.setDuration(1500);
                bounds.setDuration(1000);
                bounds.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {


                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(signUp, "translationX", mainLinear.getWidth() / 2 - relativeLayout2.getWidth() / 2 - signUp.getWidth() / 2);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX", -relativeLayout2.getX());
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(signUp, "rotation", 0);

                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(mEtPhone, "alpha", 1, 0);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(mEtPassWord, "alpha", 1, 0);
                        //ObjectAnimator animator6 = ObjectAnimator.ofFloat(forgetPass, "alpha", 1, 0);

                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(login, "rotation", 90);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(login, "y", relativeLayout2.getHeight() / 2);
                        ObjectAnimator animator9 = ObjectAnimator.ofFloat(phone2, "alpha", 0, 1);

                        ObjectAnimator animator10 = ObjectAnimator.ofFloat(confirmPass, "alpha", 0, 1);
                        ObjectAnimator animator11 = ObjectAnimator.ofFloat(pass2, "alpha", 0, 1);
                        ObjectAnimator animator12 = ObjectAnimator.ofFloat(signUp, "y", login.getY());

                        ObjectAnimator animator13 = ObjectAnimator.ofFloat(back, "translationX", img.getX());
                        ObjectAnimator animator14 = ObjectAnimator.ofFloat(signUp, "scaleX", 2);
                        ObjectAnimator animator15 = ObjectAnimator.ofFloat(signUp, "scaleY", 2);

                        ObjectAnimator animator16 = ObjectAnimator.ofFloat(login, "scaleX", 1);
                        ObjectAnimator animator17 = ObjectAnimator.ofFloat(login, "scaleY", 1);
                        ObjectAnimator animator18 = ObjectAnimator.ofFloat(logo, "x", relativeLayout2.getRight() / 2 - relativeLayout.getRight());

                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2, animator3, animator4, animator5,/* animator6,*/ animator7,
                                animator8, animator9, /*animator10,*/ animator11, animator12, animator13, animator14, animator15, animator16, animator17, animator18);
                        //set.setDuration(1500).start();
                        set.setDuration(1000).start();

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {


                        mEtPhone.setVisibility(View.INVISIBLE);
                        mEtPassWord.setVisibility(View.INVISIBLE);
                       // forgetPass.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {


                    }
                });

                TransitionManager.beginDelayedTransition(mainLinear, bounds);

                params.weight = (float) 4.25;
                params2.weight = (float) 0.75;


                relativeLayout.setLayoutParams(params);
                relativeLayout2.setLayoutParams(params2);

            }
        });

        if(flag2){
            Intent intent = new Intent(SignUpLogin.this, MainActivity.class);
            startActivity(intent);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (params2.weight == 4.25) {
                    final String account = mEtPhone.getText().toString();
                    if (TextUtils.isEmpty(account) || (!StringUtils.isMobile(account))) {
                        Toast.makeText(getApplicationContext(), "请输入有效的11位手机号码", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final String pass = mEtPassWord.getText().toString();
                    if (TextUtils.isEmpty(pass) || pass.length() < 6) {
                        Toast.makeText(getApplicationContext(), "请输入6-12由数字和字母组成的密码", Toast.LENGTH_SHORT).show();
                        //  return;
                    }
                    else {
                        if(flag)
                        {
                            SPUtils.getInstance().put("account",account);
                            SPUtils.getInstance().put("pass",pass);

                        }else{
                            SPUtils.getInstance().remove("account");
                            SPUtils.getInstance().remove("pass");
                        }
                        //BmobUser user = new BmobUser();
                        final User user = new User();
                        user.setUsername(account);
                        user.setPassword(pass);
                        user.login(new SaveListener<BmobUser>() {
                            @Override
                            public void done(BmobUser bmobUser, BmobException e) {
                                if (e == null) {
                                    BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
//                                    Snackbar.make(view, "登录成功：" + user.getUsername(), Snackbar.LENGTH_LONG).show();
//                                    Snackbar.make(relativeLayout2, "Login Complete", Snackbar.LENGTH_SHORT).show();
////                                    Intent intent = new Intent(SignUpLogin.this, MainActivity.class);

                                    BmobQuery<User> query = new BmobQuery<User>();
                                    query.addWhereEqualTo("mobilePhoneNumber",account);
                                    query.findObjects(new FindListener<User>() {
                                        @Override
                                        public void done(List<User> object, BmobException e) {
                                            if (e == null) {
                                                Account = (MyApp)getApplicationContext();
                                                Account.setAccount(account);

                                                my=(MyApp)getApplicationContext();
                                                my.setMy(object.get(0));
                                                SPUtils.getInstance().put("pid",object.get(0).getPid());

                                                SPUtils.getInstance().put("pid",object.get(0).getPid());

                                                BmobQuery<User> query = new BmobQuery<User>();
                                                query.addWhereEqualTo("pid",object.get(0).getPid());
                                                query.findObjects(new FindListener<User>() {
                                                    @Override
                                                    public void done(List<User> object, BmobException e) {
                                                        if (e == null) {
                                                            SPUtils.getInstance().put("login_person_name",object.get(0).getUsername());

                                                        } else {
                                                            Log.e("BMOB", e.toString());
                                                            //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });


                                                LogUtils.iTag("pid_login",object.get(0).getPid());
                                            } else {
                                                Log.e("BMOB", e.toString());
                                                //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                                    //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
                                    User user2 = BmobUser.getCurrentUser(User.class);
                                    if (!TextUtils.isEmpty(user2.getObjectId())) {
                                        BmobIM.connect(user2.getObjectId(), new ConnectListener() {
                                            @Override
                                            public void done(String uid, BmobException e) {
                                                if (e == null) {
                                                    //连接成功
                                                    //ToastUtils.showShort("连接成功！");
                                                    Log.e("BMOB", "连接成功！");
                                                } else {
                                                    //连接失败
                                                    //ToastUtils.showShort("连接失败!");
                                                    Log.e("BMOB", "连接失败！");
                                                }
                                            }
                                        });
                                    }



//                                    startActivity(intent);
                                    Intent intent=new Intent();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setClass(SignUpLogin.this,MainActivity.class);
                                    startActivity(intent);

//                                    Account = (MyApp)getApplicationContext();
//                                    Account.setAccount(account);
                        //登录前先存用户的pid



                                   // startActivity(new Intent(SignUpLogin.this,MainActivity.class));
                                } else {
//                                    Snackbar.make(view, "登录失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    Log.e("reason",e.toString());
                                }
                            }
                        });
                    }
                    return;
                }
                mEtPhone.setVisibility(View.VISIBLE);
                mEtPassWord.setVisibility(View.VISIBLE);
                //forgetPass.setVisibility(View.VISIBLE);


                final ChangeBounds bounds = new ChangeBounds();
                //bounds.setDuration(1500);
                bounds.setDuration(1000);
                bounds.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {


                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(login, "translationX", mainLinear.getWidth() / 2 - relativeLayout.getWidth() / 2 - login.getWidth() / 2);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX", (relativeLayout.getX()));
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(login, "rotation", 0);

                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(mEtPhone, "alpha", 0, 1);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(mEtPassWord, "alpha", 0, 1);
                       // ObjectAnimator animator6 = ObjectAnimator.ofFloat(forgetPass, "alpha", 0, 1);

                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(signUp, "rotation", 90);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(signUp, "y", relativeLayout.getHeight() / 2);
                        ObjectAnimator animator9 = ObjectAnimator.ofFloat(phone2, "alpha", 1, 0);

                        ObjectAnimator animator10 = ObjectAnimator.ofFloat(confirmPass, "alpha", 1, 0);
                        ObjectAnimator animator11 = ObjectAnimator.ofFloat(pass2, "alpha", 1, 0);
                        ObjectAnimator animator12 = ObjectAnimator.ofFloat(login, "y", signUp.getY());

                        ObjectAnimator animator13 = ObjectAnimator.ofFloat(back, "translationX", -img.getX());
                        ObjectAnimator animator14 = ObjectAnimator.ofFloat(login, "scaleX", 2);
                        ObjectAnimator animator15 = ObjectAnimator.ofFloat(login, "scaleY", 2);

                        ObjectAnimator animator16 = ObjectAnimator.ofFloat(signUp, "scaleX", 1);
                        ObjectAnimator animator17 = ObjectAnimator.ofFloat(signUp, "scaleY", 1);
                        ObjectAnimator animator18 = ObjectAnimator.ofFloat(logo, "x", logo.getX() + relativeLayout2.getWidth());


                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2, animator3, animator4, animator5, /*animator6,*/ animator7,
                                animator8, animator9, /*animator10,*/ animator11, animator12, animator13, animator14, animator15, animator16, animator17, animator18);
                        //set.setDuration(1500).start();
                        set.setDuration(1000).start();
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {

                        phone2.setVisibility(View.INVISIBLE);
                        name2.setVisibility(View.INVISIBLE);
                        pass2.setVisibility(View.INVISIBLE);
                        confirmPass.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });

                TransitionManager.beginDelayedTransition(mainLinear, bounds);

                params.weight = (float) 0.75;
                params2.weight = (float) 4.25;

                relativeLayout.setLayoutParams(params);
                relativeLayout2.setLayoutParams(params2);



            }
        });

        qq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"抱歉，该功能正在开发中",Toast.LENGTH_SHORT).show();
            }

        });

        wx.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"抱歉，该功能正在开发中",Toast.LENGTH_SHORT).show();
            }

        });

        wb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"抱歉，该功能正在开发中",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private int inDp(int dp) {

        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;


    }
}
