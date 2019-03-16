package com.example.dwl.menuapplication.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {


    private String pid;
    private String person_pic;
    private String personal_signature;

    public String getPid() {
        return pid;
    }

    public User setPid(String pid) {
        this.pid = pid;
        return this;
    }


    public String getPerson_pic() {
        return person_pic;
    }

    public User setPerson_pic(String person_pic) {
        this.person_pic = person_pic;
        return this;
    }

    public String getPersonal_signature() {
        return personal_signature;
    }

    public User setPersonal_signature(String persinal_signature) {
        this.personal_signature = persinal_signature;
        return this;
    }
}
