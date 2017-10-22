package com.fsingh.pranshooverma.foodsingh;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Tanmay on 14-10-2017.
 */

public class MenuItems implements Parcelable{




    private String Id, Name, Category, Price, Image, Status;
    //private boolean isFav  = false;
    private int quantity = 0;

    MenuItems(String Id, String Name, String Category, String Price, String Image, String Status){
        this.Id = Id;
        this.Name = Name;
        this.Category = Category;
        this.Price = Price;
        this.Image = Image;
        this.Status = Status;
        //this.isFav = isFav;
    }

    protected MenuItems(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Category = in.readString();
        Price = in.readString();
        Image = in.readString();
        quantity = in.readInt();

    }

    public static final Creator<MenuItems> CREATOR = new Creator<MenuItems>() {
        @Override
        public MenuItems createFromParcel(Parcel in) {
            return new MenuItems(in);
        }

        @Override
        public MenuItems[] newArray(int size) {
            return new MenuItems[size];
        }
    };

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId(){
        return Id;
    }

    public String getName(){
        return Name;
    }
    public String getCategory(){
        return Category;
    }

    public String getPrice(){
        return Price;
    }

    public String getImage(){

        return Image;
    }

    public String getStatus() {
        return Status;
    }



    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(Name);
        parcel.writeString(Category);
        parcel.writeString(Price);
        parcel.writeString(Image);
        parcel.writeInt(quantity);
    }
}
