package com.fsingh.pranshooverma.foodsingh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRANSHOO VERMA on 12/09/2017.
 */

public class constants {

    public static String login_details_url="http://mindwires.in/foodsingh_app/logini_save_and_check.php";
    public static String login_check_url="http://mindwires.in/foodsingh_app/login.php";
    public  static  String get_categories="http://mindwires.in/foodsingh_app/get_categories2.php";
    public static String get_menu_category_wise="http://mindwires.in/foodsingh_app/get_menu.php";
    public static String send_to_debian="http://mindwires.in/foodsingh_app/place_order.php";
    public static String order_history="http://mindwires.in/foodsingh_app/get_order_history.php";
    public static String discount_coupon="http://mindwires.in/foodsingh_app/get_discount.php";

    public static List<String> items_name=new ArrayList<>();
    public static List<String> items_price=new ArrayList<>();


    //this list is going to the database and to the restaurant owner
    public static List<String> item_name_deb=new ArrayList<>();
    public static List<String>  item_quant_deb=new ArrayList<>();
}
