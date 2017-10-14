package com.fsingh.pranshooverma.foodsingh;

/**
 * Created by Tanmay on 14-10-2017.
 */

public class MenuItems {

    private String Id, Name, Category, Price, Image;
    private int quantity = 0;

    MenuItems(String Id, String Name, String Category, String Price, String Image){
        this.Id = Id;
        this.Name = Name;
        this.Category = Category;
        this.Price = Price;
        this.Image = Image;
    }

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
}
