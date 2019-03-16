package com.example.dwl.menuapplication.bean;

import cn.bmob.v3.BmobObject;

public class Person extends BmobObject {
    private String pname;
    private String pid;
    private String password;
    private String person_pic;
    private String personal_signature;


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPerson_pic() {
        return person_pic;
    }

    public void setPerson_pic(String person_pic) {
        this.person_pic = person_pic;
    }

    public String getPersonal_signature() {
        return personal_signature;
    }

    public void setPersonal_signature(String personal_signature) {
        this.personal_signature = personal_signature;
    }




}
