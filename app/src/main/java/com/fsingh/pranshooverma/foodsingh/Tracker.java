package com.fsingh.pranshooverma.foodsingh;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tracker extends AppCompatActivity {

    static TextView order_no, repeat_order,price,date,fooditems,foodqt,driverinfo,driver_number, items,logistics;
    ImageView trackimage;
    Typeface tf,tf1;
    TextView toolbarText;
    String itemsString="", itemnames = "";

    boolean megacheck = false;

    Intent i;
    FoodItem item, newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemoveTop();

        setContentView(R.layout.orders);
        tf = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");
        tf1 = Typeface.createFromAsset(getAssets(),"fonts/COPRGTB.TTF");
        toolbarText = (TextView)findViewById(R.id.toolbarText);
        toolbarText.setTypeface(tf);
        toolbarText.setText("Orders");
        order_no = setTextId(this.order_no,R.id.order_number);
        repeat_order = setTextId(this.repeat_order,R.id.repeatorder);
        repeat_order = setTextId(this.repeat_order,R.id.repeatorder);
        price = setTextId(this.price,R.id.price);
        date = setTextId(this.date,R.id.date);
        fooditems=setTextId(this.fooditems,R.id.foodname);
        foodqt = setTextId(this.foodqt,R.id.foodqt);
        driver_number = setTextId(this.driver_number,R.id.number_info);
        driverinfo = setTextId(this.driver_number,R.id.info);
        logistics = setTextId(this.driver_number,R.id.logisticinfo);
        trackimage = (ImageView)findViewById(R.id.trackimage);
        items = (TextView)findViewById(R.id.items);
        i = getIntent();
        order_no.setTypeface(tf);
        repeat_order.setTypeface(tf);
        items.setTypeface(tf1);
        price.setTypeface(tf1);
        date.setTypeface(tf);
        fooditems.setTypeface(tf);
        foodqt.setTypeface(tf);
        driver_number.setTypeface(tf);
        driverinfo.setTypeface(tf);
        logistics.setTypeface(tf1);

        if(i!=null){
            item = i.getExtras().getParcelable("object");
           //processFoodNames(item.getItem());
            if(item!=null) {
                  test(item.getItem());
                   price.setText(item.getAmount());
                date.setText(item.getDate());
                order_no.setText(item.getId());

                //item.getItem();
            }
        }else{
            Display("i is null");
        }

        getResponse(constants.order_details);

        repeat_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!megacheck){
                    Intent i = new Intent(view.getContext(), CheckoutActivity.class);
                    i.putExtra("items",item.getItem());
                    i.putExtra("key",1);

                    i.putExtra("price",item.getAmount());
                    Log.i("tracker_price",itemsString.substring(0,itemsString.length()-1)+","+item.getAmount());
                    startActivity(i);
                }else{
                    String temp = itemsString;
                    temp.replace(",","\n");
                    showDialog2(Tracker.this,"Sorry!Only the following items are available right now. \n\n"+temp+"\n");
                }

            }
        });


    }

    public void showDialog2(Context activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog2);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");

        text.setTypeface(tf);

        TextView image = (TextView) dialog.findViewById(R.id.btn_dialog);
       // Glide.with(activity).load(pic).into(image);
        TextView dialogButton = (TextView)dialog.findViewById(R.id.cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CheckoutActivity.class);
                i.putExtra("items",itemsString);
                i.putExtra("key",1);

                i.putExtra("price", newItem.getAmount());
                Log.i("tracker_price",itemsString.substring(0,itemsString.length()-1)+","+item.getAmount());
                startActivity(i);
            }
        });

        dialog.show();

    }
    private void RemoveTop(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        for(int i=0; i<localdatabase.unavailableItemsList.size(); i++){
            Log.i("tracker2323",localdatabase.unavailableItemsList.get(i).getName());
        }

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

               // Display("name "+name+" qt "+qt+" "+i);

                fooditems.append(name+"\n");
                foodqt.append(qt+"\n");
                boolean uChecker = false;
                for(int j=0; j<localdatabase.unavailableItemsList.size(); j++){
                    uChecker = false;

                    if(localdatabase.unavailableItemsList.get(j).getName().equals(name.trim())){
                       // Display("Detected");
                        uChecker = true;
                        megacheck= true;
                        String new_Amount = String.valueOf(
                                Integer.parseInt(item.getAmount())-Integer.parseInt(qt.trim())*localdatabase.unavailableItemsList.get(j).getPrice());
                        newItem = new FoodItem(item.getId(),item.getItem(),new_Amount,item.getAddress(),item.getDate());
                      itemnames+=name+",";

                        break;
                    }else{
                        uChecker = false;
                       // Log.i("tracker2323",localdatabase.unavailableItemsList.get(i).getName()+",");
                    }
                }

                if(!uChecker){
                    itemsString += name + " x" + qt+", ";

                }

            }else{
                // Display(foods[i]+" 3here");
                //Display(foods[i]);
               // Display("NOT FOUND AT "+i);
            }
        }
    }

    private void Display(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Log.i("android23235616",s);

    }

    private TextView setTextId(TextView t, int id){
        t = (TextView)findViewById(id);
        return t;
    }

    private void getResponse(String url){

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String status = jsonObject.getString("status");
                    if(status.equals("0")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.orderplaced));
                    }else if(status.equals("1")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.processing));
                    }else if(status.equals("2")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.outfordelivery));
                    }else if(status.equals("3")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.delivered));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Display(e.toString());
                }

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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);


    }
}
