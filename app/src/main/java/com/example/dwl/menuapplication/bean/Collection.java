package com.example.dwl.menuapplication.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Collection extends BmobObject {


    private String menu_creator_name;
    private List<String> menu_pic;
    private String menu_name;
    private String menu_id;
    private String pid;

    public Collection() {
    }

    public Collection(String menu_creator_name, List<String> menu_pic, String menu_name, String menu_id, String pid) {
        this.menu_creator_name = menu_creator_name;
        this.menu_pic = menu_pic;
        this.menu_name = menu_name;
        this.menu_id = menu_id;
        this.pid = pid;
    }

    public String getMenu_creator_name() {
        return menu_creator_name;
    }

    public void setMenu_creator_name(String menu_creator_name) {
        this.menu_creator_name = menu_creator_name;
    }

    public List<String> getMenu_pic() {
        return menu_pic;
    }

    public void setMenu_pic(ArrayList<String> menu_pic) {
        this.menu_pic = menu_pic;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
