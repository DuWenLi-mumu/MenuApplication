package com.example.dwl.menuapplication;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.example.dwl.menuapplication.IM.DemoMessageHandler;
import com.example.dwl.menuapplication.bean.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

import static com.example.dwl.menuapplication.IM.BmobIMApplication.getMyProcessName;

public class MyApp extends Application {
    private String account;
    private User my;

    public User getMy() {
        return my;
    }

    public void setMy(User my) {
        this.my = my;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //blankj:utilcode的初始化
        Utils.init(this);
        // OkGo.init(this);

        Bmob.initialize(this, "96073a0eedb9adb5d9fc3fcf1fa71929");
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler());
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}