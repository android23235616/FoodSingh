package com.foodsingh.mindwires.foodsingh;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;

import com.android.volley.DefaultRetryPolicy;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Splash extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RequestQueue req;
    Thread t;
    ProgressBar progressBar;
    StringRequest sr;
    Dialog dialog;
    int i=0;
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
       // req = Volley.newRequestQueue(this);
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
             //  Initiate_Meta_Data();
           }
        }



    }




    private void New_Details(String name, String number, final String main_url) {
        i++;


        Calendar calendar = Calendar.getInstance();
        RequestQueue re = Volley.newRequestQueue(this);

        sr = new StringRequest(Request.Method.POST, main_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

            final Handler h = new Handler();

                t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startThread(response,h);
                    }
                });


                Log.i("mainresponse" + i, response);

                t.start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display(error.toString());
                Log.i("mainresponse", error.toString());
                Display("Please start the app again");
                Log.i("mainresponse",error.toString());
                //finish();

            }
        }){
            @Override
            public Map getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("latitude",localdatabase.deliveryLocation.getLatitude()+"");
                map.put("longitude",localdatabase.deliveryLocation.getLongitude()+"");
                return map;
            }
        };

        sr.setShouldCache(false);
       // RequestQueue queue = new RequestQueue(new NoCache(),new BasicNetwork(new HurlStack()));

        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        re.add(sr);

    }

    private void startThread(String response,Handler h){
        dataLoaded = true;

        try {
            JSONObject mainObject = new JSONObject(response);
            localdatabase.about_text =mainObject.getString("about_text");
            localdatabase.cartCoupon = mainObject.getString("cart_ad");
            localdatabase.about_img = mainObject.getString("about_image");
            localdatabase.metaData = new MetaData(mainObject.getString("discount"), mainObject.getString("latest_version")
                    ,mainObject.getString("service"),mainObject.getString("min_order"),mainObject.getString("msg_api")
            );



            localdatabase.discount = Integer.parseInt(mainObject.getString("discount"));
            localdatabase.aboutText = mainObject.getString("about_text");
            localdatabase.aboutImage = mainObject.getString("about_image");
            localdatabase.delivery = mainObject.getString("location");
            localdatabase.drinks = mainObject.getString("drinks");
            localdatabase.share_text =mainObject.getString("share_text");
            localdatabase.share_url = mainObject.getString("share_url");
            localdatabase.deliveryCharge = Integer.parseInt(mainObject.getString("delivery_charge"));
            localdatabase.kitchenClosedText = mainObject.getString("kitchen_closed_text");

            if(Integer.parseInt(localdatabase.metaData.getLatest_version())>BuildConfig.VERSION_CODE){
                showDialog2(Splash.this,"Please go to app store and download the latest version.");
                return;
            }

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
                    //detail = "";
                    MenuItems menuItems = new MenuItems(id,name_,category,price,image_, status, detail);
                    menuItemsList.add(menuItems);
                    String available = miniTempObject.getString("status");

                    if(available.equals("NA")){
                        UnavailableItems ii = new UnavailableItems(name_,Integer.parseInt(price));
                        localdatabase.unavailableItemsList.add(ii);
                    }
                }
                MasterMenuItems menuItemsObject = new MasterMenuItems(name,image,cuisine, combo,menuItemsList,time,tempObject.getString("drinks"), tempObject.getString("detail"));
                Log.i("checking details", tempObject.getString("drinks"));
                localdatabase.masterList.add(menuItemsObject);
            }
            JSONArray BannerImages = mainObject.getJSONArray("home_images");

            for(int i=0; i<BannerImages.length(); i++){
                localdatabase.BannerUrls.add(BannerImages.getJSONObject(i).getString("url"));
            }

            JSONArray getAdImages = mainObject.getJSONArray("ad_bars");

            for(int i=0; i<getAdImages.length(); i++){
                JSONObject tempJson =getAdImages.getJSONObject(i);
                CouponClass temp = new CouponClass(tempJson.getString("id"),tempJson.getString("coupon"),tempJson.getString("image"));
                localdatabase.couponClassList.add(temp);
            }

            JSONArray superCategories = mainObject.getJSONArray("super_categories");

            for(int i=0; i<superCategories.length(); i++){
                SuperCategories su = new SuperCategories(superCategories.getJSONObject(i).getString("id"),
                        superCategories.getJSONObject(i).getString("name"));
                localdatabase.superCategoriesList.add(su);
                Log.i("super_categories",localdatabase.superCategoriesList.get(i).getName());
            }

            if(LocationChecked&&dataLoaded) {
                if(!redundent) {
                    redundent = true;

                   h.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           startActivity(new Intent(Splash.this, menu.class));
                           finish();
                       }
                   },500);


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Display("Please start the app again");
            Log.i("mainresponse",e.toString());
            // finish();
        }finally {

        }

    }


    public static long getMinutesDifference(long timeStart,long timeStop){
        long diff = timeStop - timeStart;
        long diffMinutes = diff / (60 * 1000);

        return  diffMinutes;
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

        Log.i("called now", "called now");

    }

    @Override
    public void onStop() {
        super.onStop();
        apiClient.disconnect();
        if(req!=null){
            req.cancelAll(sr);
            Log.i("23235616","called on stop");
        }

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
        if(req!=null){
            req.cancelAll(sr);
            Log.i("23235616","called on stop");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(req!=null){
            req.cancelAll(sr);
            Log.i("23235616","called on stop");
        }

    }



    @Override

    public void onResume() {
        super.onResume();
        if(!isLocationEnabled(this)){
            GoToLocations();

        }else{
            if(dialog!=null){
                dialog.dismiss();

            }
        }
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
         dialog = new Dialog(activity);
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

                if(Integer.parseInt(localdatabase.metaData.getLatest_version())>BuildConfig.VERSION_CODE){
                    String url = localdatabase.share_url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    finish();
                }else{
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

            }
        });

        dialog.show();

    }
    private void GoToLocations(){
       /* LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showDialog2(this,"You Need To Enable\n Your loction to use this Application.");

        }else{
           // Initiate_Meta_Data();
        }*/
    }

    private void showLog(String s) {
        Log.i(constants.logTag, s);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==2){
            if(resultCode!=RESULT_OK){
               Display("You Won't be able to use this application right now.");
                finish();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showLog("Connected Api Client");
        if (apiClient != null) {
            LocationRequest locationRequest = LocationRequest.create().setInterval(200000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                LocationPermission = false;



            }else {

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);
                final String TAG = "LOCATION PERMISSION";
                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(apiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                Log.i(TAG, "All location settings are satisfied.");
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the result
                                    // in onActivityResult().
                                    status.startResolutionForResult(Splash.this, 2);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");

                                break;
                        }
                    }
                });


                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, Splash.this);
                LocationPermission = true;
                if(!isLocationEnabled(this)){
                    GoToLocations();
                }else{
                   // Initiate_Meta_Data();
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
    public void onLocationChanged(final Location location) {

        showLog("Location at "+location.getLongitude()+", "+location.getLongitude());
        localdatabase.deliveryLocation = location;


     //  localdatabase.city  = getCity(location.getLatitude(),location.getLongitude());



        LocationChecked = true;

        if(i==0) {
            AddressFetchingService.startActionFoo(this,location.getLatitude()+"",location.getLongitude()+"");
            Initiate_Meta_Data();

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
            //Display(e.toString());
        }
        String address, city;
        if(addresses.size()>0) {
            address = addresses.get(0).getAddressLine(0)+" , , "; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            Log.i("addressmine",addresses.get(0).getAdminArea()+", "+addresses.get(0).getFeatureName()+" , "+addresses.get(0).getLocality()+", "+address);
            localdatabase.lane = address;
            localdatabase.sublocality = addresses.get(0).getSubLocality();

            int comma = 0;
            for (int i =0; comma != 2; i++ ){
                if(address.charAt(i) == ','){
                    comma++;
                }
                if(comma == 2){
                    break;
                }
                localdatabase.lane2 += address.charAt(i);
            }


            return city;
        }

        return "Location Not Found";
    }
}
