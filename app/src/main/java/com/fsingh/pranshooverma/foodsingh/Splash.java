package com.fsingh.pranshooverma.foodsingh;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.DefaultRetryPolicy;
import com.fsingh.pranshooverma.foodsingh.BuildConfig;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Splash extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    Context ctx;
    boolean rec = false;

    TextView header;

    ProgressBar progressbar;
    private boolean dataLoaded = false;
    GoogleApiClient apiClient;
    boolean redundent = false;
    private boolean LocationChecked = false, LocationPermission = false;
    boolean checker = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checker = false;
        rec = false;
        ctx = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
       progressBar = (ProgressBar)findViewById(R.id.real_progressbar) ;
        sharedPreferences = getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
        localdatabase.notifications = sharedPreferences.getInt(constants.notifAmount,0);
        header = (TextView)findViewById(R.id.metadata);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        header.setTypeface(tf);

        // Glide.with(this).load(R.drawable.signin).into(AnimationTarget);
        //Display("Loading! Please Wait");



        editor = sharedPreferences.edit();


        if (!checkPermission()) {
            // requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            LocationPermission = false;
        }else{
            LocationPermission = true;
           if(!isLocationEnabled(this)){
                GoToLocations();
           }else{
               Initiate_Meta_Data();
           }
        }



    }

    private void New_Details(String name, String number, String main_url) {

        RequestQueue request = Volley.newRequestQueue(Splash.this);

        StringRequest st = new StringRequest(Request.Method.GET, main_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataLoaded = true;

                try {
                    JSONObject mainObject = new JSONObject(response);
                    localdatabase.metaData = new MetaData(mainObject.getString("discount"), mainObject.getString("latest_version")
                    ,mainObject.getString("service"),mainObject.getString("min_order"),mainObject.getString("msg_api")
                    );

                    localdatabase.discount = Integer.parseInt(mainObject.getString("discount"));
                    localdatabase.aboutText = mainObject.getString("about_text");
                    localdatabase.aboutImage = mainObject.getString("about_image");

                    JSONArray Categories = mainObject.getJSONArray("categories");
                    for (int i=0; i<Categories.length(); i++){
                        String name,image,cuisine, time, combo;
                        List<MenuItems> menuItemsList = new ArrayList<>();
                        JSONObject tempObject = Categories.getJSONObject(i);
                        name = tempObject.getString("name");
                        image = tempObject.getString("image");
                        cuisine = tempObject.getString("cuisine");
                        time = tempObject.getString("time");
                        combo = tempObject.getString("combo");
                        JSONArray miniMenu = tempObject.getJSONArray("menu");

                        for(int j=0; j<miniMenu.length(); j++){
                            String id, name_, category, price, image_, status, detail;
                            JSONObject miniTempObject = miniMenu.getJSONObject(j);
                            id = miniTempObject.getString("id");
                            name_ = miniTempObject.getString("name");
                            category = miniTempObject.getString("category");
                            price = miniTempObject.getString("price");
                            image_ = miniTempObject.getString("image");
                            status = miniTempObject.getString("status");
                            detail = miniTempObject.getString("detail");
                            MenuItems menuItems = new MenuItems(id,name_,category,price,image_, status, detail);
                            menuItemsList.add(menuItems);
                            String available = miniTempObject.getString("status");

                            if(available.equals("NA")){
                                UnavailableItems ii = new UnavailableItems(name_,Integer.parseInt(price));
                                localdatabase.unavailableItemsList.add(ii);
                            }
                        }
                        MasterMenuItems menuItemsObject = new MasterMenuItems(name,image,cuisine, combo,menuItemsList,time);
                        localdatabase.masterList.add(menuItemsObject);
                    }
                    JSONArray BannerImages = mainObject.getJSONArray("home_images");

                    for(int i=0; i<BannerImages.length(); i++){
                        localdatabase.BannerUrls.add(BannerImages.getJSONObject(i).getString("url"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Display(e.toString());
                }finally {

                    if(LocationChecked&&dataLoaded) {
                        if(!redundent) {
                            redundent = true;

                            startActivity(new Intent(Splash.this, menu.class));
                            finish();
                        }
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Display(error.toString());
            }
        });

        request.add(st);

        st.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void Initiate_Meta_Data() {

        if (!checkPermission()) {
            // requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            LocationPermission = false;
        } else {
            LocationPermission = true;
            New_Details("","",constants.main_url);
        }

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        //return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));
    }

    @Override
    public void onStart() {
        super.onStart();
        apiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        if(checkPermission()){
            apiClient.connect();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        apiClient.disconnect();
    }


    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void Display(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private String getAppVersion() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        return versionCode + "";
    }


    @Override
    public void onPause() {
        super.onPause();
       /* if (progressBar != null)
            if (progressBar.isShowing()) {
                progressBar.cancel();
                checker = true;
            }*/
    }

    @Override

    public void onResume() {
        super.onResume();
       }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (grantResults.length > 0) {
            if(grantResults[1]!=PackageManager.PERMISSION_GRANTED){
                Display("Permission Denied");
                finish();
            }else{
                apiClient.connect();
                if(!isLocationEnabled(this)){
                    GoToLocations();
                }
            }
        }

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



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        });

        dialog.show();

    }
    private void GoToLocations(){
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showDialog2(this,"You Need To Enable\n Your loction to use this Application.");

        }else{
            Initiate_Meta_Data();
        }
    }

    private void showLog(String s) {
        Log.i(constants.logTag, s);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showLog("Connected Api Client");
        if (apiClient != null) {
            LocationRequest locationRequest = LocationRequest.create().setInterval(2000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                LocationPermission = false;

                Display("Permission Denied");


            }else {

                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, Splash.this);
                LocationPermission = true;
                if(!isLocationEnabled(this)){
                    GoToLocations();
                }else{
                    Initiate_Meta_Data();
                }
            }

    }
    }

    @Override
    public void onConnectionSuspended(int i) {
        showLog("Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        showLog("Connection Failed");

    }

    @Override
    public void onLocationChanged(Location location) {

        showLog("Location at "+location.getLongitude()+", "+location.getLongitude());
        localdatabase.deliveryLocation = location;
        localdatabase.city=getCity(location.getLatitude(),location.getLongitude());


        LocationChecked = true;
        if(LocationChecked&&dataLoaded) {
            if(!redundent) {
                redundent = true;
                startActivity(new Intent(Splash.this, menu.class));
                finish();
            }
        }


    }

    private String getCity(double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            Display(e.toString());
        }
        String address, city;
        if(addresses.size()>0) {
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            return city;
        }

        return "Location Not Found";
    }
}
