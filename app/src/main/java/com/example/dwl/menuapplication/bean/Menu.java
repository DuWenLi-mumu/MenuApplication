package com.example.dwl.menuapplication.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Menu extends BmobObject {

    private String create_person_id;
    private String menu_avatar;
    private String menu_detail;
    private String menu_id;
    private List<String> menu_pic;
    private String create_person_name;
    private String menu_name;
    private String menu_category;

    public Menu(String menu_avatar, String menu_name) {
        this.menu_avatar = menu_avatar;

        this.menu_name = menu_name;
    }

    public String getCreate_person_id() {
        return create_person_id;
    }

    public Menu(){}
    public void setCreate_person_id(String create_person_id) {
        this.create_person_id = create_person_id;
    }

    public String getMenu_avatar() {
        return menu_avatar;
    }

    public void setMenu_avatar(String menu_avatar) {
        this.menu_avatar = menu_avatar;
    }

    public String getMenu_detail() {
        return menu_detail;
    }

    public void setMenu_detail(String menu_detail) {
        this.menu_detail = menu_detail;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public List<String> getMenu_pic() {
        return menu_pic;
    }

    public void setMenu_pic(List<String> menu_pic) {
        this.menu_pic = menu_pic;
    }

    public String getCreate_person_name() {
        return create_person_name;
    }

    public void setCreate_person_name(String create_person_name) {
        this.create_person_name = create_person_name;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_category() {
        return menu_category;
    }

    public void setMenu_category(String menu_category) {
        this.menu_category = menu_category;
    }
}




