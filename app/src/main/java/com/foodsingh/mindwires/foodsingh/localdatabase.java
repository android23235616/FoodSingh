package com.foodsingh.mindwires.foodsingh;

import android.location.Location;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class localdatabase {

    public static boolean loaded = false;

    public static String drinks = "false";

    public static List<SuperCategories> superCategoriesList = new ArrayList<>();

    public static String share_text, share_url;

    public static String lane;

    public static int kitchen  = 0;

    public static TextView notifmount;

    public static String delivery = "";

    public static int cache = 0;

    public static String about_text, about_img;

    public static List<CouponClass> couponClassList = new ArrayList<>();

    public static String cartCoupon = "false";

    public static final String url[] = {
            "http://foodsingh.com/images/app/sliders/k-timing.jpg" ,
            "https://static.pexels.com/photos/5317/food-salad-restaurant-person-medium.jpg",
            "https://static.pexels.com/photos/62097/pexels-photo-62097-medium.jpeg",
            "https://static.pexels.com/photos/5249/bread-food-restaurant-people-medium.jpg",
            "https://static.pexels.com/photos/86753/pexels-photo-86753-medium.jpeg",
            "https://static.pexels.com/photos/131044/pexels-photo-131044-medium.jpeg",
            "https://static.pexels.com/photos/5122/food-healthy-man-person-medium.jpeg",
            "https://static.pexels.com/photos/104987/pexels-photo-104987-medium.jpeg",
            "https://static.pexels.com/photos/9708/food-pizza-restaurant-eating-medium.jpg",

            "https://static.pexels.com/photos/1398/food-vegetables-meal-kitchen-medium.jpg"
            ,"https://static.pexels.com/photos/47725/hamburger-food-meal-tasty-47725-medium.jpeg",
            "https://static.pexels.com/photos/132694/pexels-photo-132694-medium.jpeg",
            "https://static.pexels.com/photos/6863/food-plate-toast-restaurant-medium.jpg",
            "https://static.pexels.com/photos/106343/pexels-photo-106343-medium.jpeg",
            "https://static.pexels.com/photos/128403/pexels-photo-128403-medium.jpeg",
            "https://static.pexels.com/photos/7765/food-medium.jpg",
            "https://static.pexels.com/photos/54296/pexels-photo-54296-medium.jpeg"
    };

        final String[] wallpaperFood = {"https://images6.alphacoders.com/517/thumb-1920-517780.jpg","https://images7.alphacoders.com/498/thumb-1920-498617.jpg"
        ,"https://images2.alphacoders.com/433/thumb-1920-43350.jpg","https://images2.alphacoders.com/633/thumb-1920-63312.jpg","https://images5.alphacoders.com/366/thumb-1920-366218.jpg"
        ,"https://images.alphacoders.com/548/thumb-1920-548027.jpg","https://images3.alphacoders.com/295/thumb-1920-2957.jpg",
                "https://images2.alphacoders.com/491/thumb-1920-491498.jpg","https://images7.alphacoders.com/317/thumb-1920-317368.jpg","https://images6.alphacoders.com/434/thumb-1920-434430.jpg"
        ,"https://images7.alphacoders.com/409/thumb-1920-409387.jpg","https://images6.alphacoders.com/342/thumb-1920-342888.jpg",
        "http://www.zastavki.com/pictures/640x480/2015/Food___Pizza_Pizza_with_pepper_105906_29.jpg","http://www.zastavki.com/pictures/640x480/2015/Food___Seafood_Red_fish_with_spinach_sauce_105828_29.jpg"
        ,"http://www.zastavki.com/pictures/640x480/2015/Food___Seafood_Sushi_rice_with_red_caviar_104529_29.jpg"
        ,"http://www.zastavki.com/pictures/640x480/2015/Food___Seafood_Cream_soup_with_prawns_105848_29.jpg"};

    final static String file_path = "/fav.tom";

    public static Location deliveryLocation = null;

    public static int notifications = 0;

    public static List<MasterMenuItems> masterList = new ArrayList<>();

    public static List<String> BannerUrls = new ArrayList<>();

    public static MetaData metaData;

    public static List<MenuItems> cartList = new ArrayList<>();

    public static String city = null;

    public static ArrayList<MenuItems> sidesList=new ArrayList<>();


    public static int deliveryCharge = 10;

    public static  int discount = 10;

    public static String couponCode = "FSDAY";

    public static List<UnavailableItems> unavailableItemsList = new ArrayList<>();

    public static String aboutText;

    public static String aboutImage;

    public static int amount = 0;

    public static String kitchenClosedText = "";

    public static String lane2 = "";

    public static String sublocality = "";

    public static String location_id = "";

    public static String shared_location_key="location";

}
