package com.example.dwl.menuapplication.bean;

public class Product {
   public int img;
    public String title;

    public Product(int img, String title) {
        this.img = img;
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
