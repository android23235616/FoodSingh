package com.fsingh.pranshooverma.foodsingh;

/**
 * Created by PRANSHOO VERMA on 15/10/2017.
 */

public class Favourites {

    private String name,price,img;

    public Favourites(String name, String price, String img) {
        this.name = name;
        this.price = price;
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {

        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImg() {
        return img;
    }
}
