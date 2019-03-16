package com.example.dwl.menuapplication.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Following extends BmobObject {

private String following_personal_s;

private String following_name;

private String follow_pid;

private String pid;

private String following_avatar;

    public Following(String following_person_s, String following_name, String follow_pid, String pid, String following_avatar) {
        this.following_personal_s = following_person_s;
        this.following_name = following_name;
        this.follow_pid = follow_pid;
        this.pid = pid;
        this.following_avatar = following_avatar;
    }

    public Following() {
    }

    public String getFollowing_person_s() {
        return following_personal_s;
    }

    public void setFollowing_person_s(String following_person_s) {
        this.following_personal_s = following_person_s;
    }

    public String getFollowing_name() {
        return following_name;
    }

    public void setFollowing_name(String following_name) {
        this.following_name = following_name;
    }

    public String getFollow_pid() {
        return follow_pid;
    }

    public void setFollow_pid(String follow_pid) {
        this.follow_pid = follow_pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFollowing_avatar() {
        return following_avatar;
    }

    public void setFollowing_avatar(String following_avatar) {
        this.following_avatar = following_avatar;
    }
}
