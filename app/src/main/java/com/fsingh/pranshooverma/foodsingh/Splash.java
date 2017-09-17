package com.fsingh.pranshooverma.foodsingh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.DefaultRetryPolicy;
import com.fsingh.pranshooverma.foodsingh.BuildConfig;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {


    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    ProgressDialog progressBar;
    boolean checker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checker=false;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        ImageView AnimationTarget = (ImageView)findViewById(R.id.progressBar);
       // Glide.with(this).load(R.drawable.signin).into(AnimationTarget);
       //Display("Loading! Please Wait");

progressBar = ProgressDialog.show(this, "Loading","Please Wait");
        sharedPreferences = getSharedPreferences("foodsingh", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","User");
        String number = sharedPreferences.getString("mobile","000");

        uploadDetails(name, number);
    }

    private void uploadDetails(String name, final String number) {

        StringRequest str = new StringRequest(Request.Method.POST, constants.set_version, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressBar.isShowing()){
                    progressBar.cancel();
                }
                try {

                    JSONObject object= new JSONObject(response);
                    String result = object.getString("message");
                    if(result.equals("SUCCESS")){

                        startActivity(new Intent(Splash.this, menu.class));
                        finish();
                    }else{
                        Display("Data Transfer Failed! Please check Network connection and try again.");
                        finish();
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
                if(progressBar.isShowing()){
                    progressBar.cancel();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> map = new HashMap<>();
                map.put("mobile", number);
                map.put("version",getAppVersion());
                return map;

            }
        };

        RequestQueue request  = Volley.newRequestQueue(this);
        request.add(str);

        str.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void Display(String s){
        Toast.makeText(this, s,Toast.LENGTH_LONG).show();
    }

    private  String getAppVersion(){
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        return versionCode+"";
    }

    @Override
    public void onPause(){
        super.onPause();
        if(progressBar.isShowing()){
            progressBar.cancel();
            checker=true;
        }
    }

    @Override

    public void onResume(){
        super.onResume();
        if(checker){
            if(progressBar!=null){
                if(!progressBar.isShowing()){
                    progressBar = ProgressDialog.show(Splash.this, "Loading.", "Please Wait!");
                }
            }
        }
    }
}
