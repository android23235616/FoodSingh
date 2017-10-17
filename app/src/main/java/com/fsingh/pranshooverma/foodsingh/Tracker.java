package com.fsingh.pranshooverma.foodsingh;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tracker extends AppCompatActivity {

    static TextView order_no, repeat_order,price,date,fooditems,foodqt,driverinfo,driver_number;
    ImageView trackimage;
    Intent i;
    FoodItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        order_no = setTextId(this.order_no,R.id.order_number);
        repeat_order = setTextId(this.repeat_order,R.id.repeatorder);
        price = setTextId(this.price,R.id.order_rev);
        date = setTextId(this.date,R.id.date);
        fooditems=setTextId(this.fooditems,R.id.foodname);
        foodqt = setTextId(this.foodqt,R.id.foodqt);
        driver_number = setTextId(this.driver_number,R.id.number_info);
        trackimage = (ImageView)findViewById(R.id.trackimage);
        i = getIntent();

        if(i!=null){
            item = i.getExtras().getParcelable("object");
           //processFoodNames(item.getItem());
            if(item!=null) {
                  test(item.getItem());
                //item.getItem();
            }
        }else{
            Display("i is null");
        }

       // getResponse(constants.order_details);


    }

    private void processFoodNames(String item) {
        fooditems.append(" ");
        //Display(item);
        String[] foods = item.split(",");
        Pattern p1 = Pattern.compile("\\d+");
        for (int i=0; i<foods.length; i++){
            foods[i].replace("(B\\/L)","k");
            Display(foods[i]+" 1here");
            Matcher m = p1.matcher(foods[i]);
            if(m.find()) {
                Display(foods[i]+" 2here");
                String qt = m.group(0);

                String name = foods[i].substring(0, foods[i].length() - qt.length() - 1);
                fooditems.append(name + "\n");
                foodqt.append(qt + "\n");
            }else{
                Display(foods[i]+" 3here");
                Display(foods[i]);
            }

        }
    }

    private void test(String foods1){
       // String foods1 = "DRAGON CHICKEN (B\\/L) x1, HUNAN CHICKEN (B\\/L) x1, Coke 750ml x1";
        fooditems.append(" ");
        String[] foods = foods1.split(",");

        // Create a Pattern object
        Pattern p1 = Pattern.compile("x\\d+");
        for (int i=0; i<foods.length; i++){
            Matcher m = p1.matcher(foods[i]);
            //foods[i].replace("(B\\/L)","k");
            if(m.find()) {
                // Display(foods[i]+" 2here");
                String qt =m.group(0);
                qt = qt.substring(1);
                String name = foods[i].substring(0, foods[i].length() - qt.length() - 1);

                Display("name "+name+" qt "+qt+" "+i);

                fooditems.append(name+"\n");
                foodqt.append(qt+"\n");
            }else{
                // Display(foods[i]+" 3here");
                //Display(foods[i]);
                Display("NOT FOUND AT "+i);
            }
        }
    }

    private void Display(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Log.i("",s);

    }

    private TextView setTextId(TextView t, int id){
        t = (TextView)findViewById(id);
        return t;
    }

    private void getResponse(String url){

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Display(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Display(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //
                Map<String, String> map = new HashMap<>();
                map.put("id",item.getId().substring(2));
                return  map;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
}
