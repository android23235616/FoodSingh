package com.fsingh.pranshooverma.foodsingh;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ViewPager.OnPageChangeListener, AppBarLayout.OnOffsetChangedListener {

    ProgressDialog progress;
    RecyclerView recylerView;
    RecyclerView.LayoutManager layoutmanager;
    List<String> categories=new ArrayList<>();
    List<String> images=new ArrayList<>();
    categoryAdapter adapter;
    BroadcastReceiver broadcastReceiver;
    localdatabase local;
    ImageButton next, back;

    ImageView ad1, ad2;

    int counter_button=0;
    int counter_button2=0;
    int counter_button3=0;
    boolean nav=true;
    TextView attack;
    pagerAdapter pageradater;
    NavigationView navigationView;
    ViewPager pager;
    static int[] dotPositions = {R.id.view_pager_1, R.id.view_pager_2,R.id.view_pager_3,R.id.view_pager_4,R.id.view_pager_5,R.id.view_pager_6};
    AppBarLayout appBarLayout;
    public static int width;
    public static TextView cartitemcount1;
    View actionView;
    boolean swipe = false;
    RecyclerView.ItemDecoration itemDecoration;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences shared;

    ImageButton cuisine_btn,time_btn,combo_btn;

     int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        RemoveTop();

        setContentView(R.layout.activity_menu);
        //Display(versionStatus());



//FOR NAVIGATION DRAWERo
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///////////////////////////////////////////////////////////////////////////////////////
         manipulatenavigationdrawer();

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }

        nav=true;

        attack = (TextView)findViewById(R.id.attack);
        initialize();

        next = (ImageButton) findViewById(R.id.right_arrow);
        back = (ImageButton) findViewById(R.id.left_arrow);
        width = getScreenWidth();
        if(checking_net_permission())
        {
            getting_categories();
            Display("trigerring");

        }
        else {
            nav=false;
            setContentView(R.layout.no_internet);
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
            TextView calm = (TextView)findViewById(R.id.calm);
            final TextView retry = (TextView)findViewById(R.id.menu);
            calm.setTypeface(tf);
            retry.setTypeface(tf);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });

        }
        local = new localdatabase();
        pageradater = new pagerAdapter(local);
        pager.setAdapter(pageradater);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = pager.getCurrentItem() + 1;
                if(current == localdatabase.BannerUrls.size()) {
                    pager.setCurrentItem(0);
                    getPositionsForDots(0);
                }else{
                    pager.setCurrentItem(current);
                    getPositionsForDots(current%dotPositions.length);
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = pager.getCurrentItem() - 1;
                if (current >= 0) {
                    pager.setCurrentItem(current);
                    getPositionsForDots(current % dotPositions.length);
                }
                else{
                    pager.setCurrentItem(localdatabase.BannerUrls.size()-1);
                    getPositionsForDots(dotPositions.length-1);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe = true;
                recylerView.removeItemDecoration(itemDecoration);
                categories.clear();
                images.clear();
                getting_categories();

            }
        });

        cuisine_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter_button++;

                if(counter_button%2==0)
                {
                    //ununselected
                    cuisine_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cuisine_));
                    categories.clear();
                    images.clear();
                    send_to_adapter();
                    getting_categories();

                }
                else
                {
                    //selected
                    cuisine_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cuisine));
                    time_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.time_));
                    combo_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.combo));

                    categories.clear();
                    images.clear();

                    send_to_adapter();

                    for(int i=0;i<localdatabase.masterList.size();i++)
                    {
                        MasterMenuItems a=localdatabase.masterList.get(i);
                        if(a.getCuisine().equals("1"))
                        {
                            if(categories.contains(a.getName()) & images.contains(a.getImage()))
                            {

                            }
                            else
                            {
                                categories.add(a.getName());
                                images.add(a.getImage());

                            }

                        }
                    }
                    Toast.makeText(menu.this, String.valueOf((localdatabase.masterList.size())), Toast.LENGTH_SHORT).show();
                    send_to_adapter();


                }
           //     cuisine_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cuisine));

            }
        });
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counter_button2++;
                if(counter_button2%2==0)
                {
                    //unelected
                    time_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.time_));
                    categories.clear();
                    images.clear();
                    send_to_adapter();
                    getting_categories();

                }
                else
                {
                    //selected
                    time_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.time));
                    cuisine_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cuisine_));
                    combo_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.combo));

                    categories.clear();
                    images.clear();

                    send_to_adapter();
                    for(int i=0;i<localdatabase.masterList.size();i++)
                    {
                        MasterMenuItems a=localdatabase.masterList.get(i);
                        if(a.getTime().equals("1"))
                        {
                            if(categories.contains(a.getName()) & images.contains(a.getImage()))
                            {

                            }
                            else
                            {
                                categories.add(a.getName());
                                images.add(a.getImage());

                            }
                        }
                    }
                    send_to_adapter();


                }
            }
        });
        combo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  cuisine_btn.setBackgroundResource(R.drawable.menu_button);
                counter_button3++;
                if(counter_button3%2==0)
                {
                    //unselected
                    combo_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.combo));
                    categories.clear();
                    images.clear();
                    send_to_adapter();
                    getting_categories();
                }
                else
                {
                    combo_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.combo_));
                    cuisine_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cuisine_));
                    time_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.time_));

                    categories.clear();
                    images.clear();
                    send_to_adapter();

                    for(int i=0;i<localdatabase.masterList.size();i++)
                    {
                        MasterMenuItems a=localdatabase.masterList.get(i);
                        if(a.getCombo().equals("1"))
                        {
                            if(categories.contains(a.getName()) & images.contains(a.getImage()))
                            {

                            }
                            else
                            {
                                categories.add(a.getName());
                                images.add(a.getImage());

                            }}
                    }
                    send_to_adapter();


                }

            }
        });


        SetupBroadcastReceiver();

        if(localdatabase.couponClassList.size()>0){
            LoadBitmaps();
        }

    }

    private void LoadBitmaps(){
        LoadBitmap1 b1 = new LoadBitmap1(0);
       // if(localdatabase.couponClassList.size()>0)
        b1.execute(localdatabase.couponClassList.get(0).getUrl());
        LoadBitmap1 b2 = new LoadBitmap1(1);

        b2.execute(localdatabase.couponClassList.get(1).getUrl());
    }

    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                localdatabase.notifmount = (TextView)actionView.findViewById(R.id.notification_badge);
                if(intent.getAction().equals(constants.broaadcastReceiverMenu)){


                        localdatabase.notifmount.setVisibility(View.VISIBLE);
                        localdatabase.notifmount.setText(localdatabase.notifications+"");


                    Log.i("broadcastreceiver1", localdatabase.notifications+"");
                }else if(intent.getAction().equals(constants.menu2BroadcastReceiver)){
                    localdatabase.notifmount.setVisibility(View.INVISIBLE);
                }

            }


        };

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constants.menu2BroadcastReceiver);

        registerReceiver(broadcastReceiver,intentFilter);
        registerReceiver(broadcastReceiver,intentFilter2);
    }


    private void RemoveTop(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private String versionStatus(){
        SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return sharedPreferences.getString("version","null");
    }

    private void manipulatenavigationdrawer() {
        View v = navigationView.getHeaderView(0);
        Typeface tp = Typeface.createFromAsset(getAssets(), "fonts/COPRGTB.TTF");
        TextView t = (TextView) v.findViewById(R.id.welcome);
        t.setTypeface(tp);
        TextView location = (TextView)v.findViewById(R.id.location);
        location.setTypeface(tp);
        location.setText(localdatabase.city);
        ImageView back = (ImageView)v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawers();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","_");
        if(!name.equals("_")){
            t.setText("Hello, "+name);
        }
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void initialize() {
        ad1  = (ImageView)findViewById(R.id.advertisement1);
        ad2 = (ImageView)findViewById(R.id.advertisement2);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.addOnPageChangeListener(this);
        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        progress=new ProgressDialog(this);
        progress.setCancelable(false);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        //swipeRefreshLayout.canScrollVertically()
        recylerView=(RecyclerView) findViewById(R.id.recyclerView);
        recylerView.setFocusable(false);
        layoutmanager=new LinearLayoutManager(this);
        recylerView.setLayoutManager(layoutmanager);
        recylerView.setNestedScrollingEnabled(false);
        recylerView.setItemAnimator(new DefaultItemAnimator());

        setTypeface();
        shared=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);

        cuisine_btn=(ImageButton) findViewById(R.id.cuisine);
        time_btn=(ImageButton) findViewById(R.id.time);
        combo_btn=(ImageButton) findViewById(R.id.combo);

    }



    public void showDialog(Activity activity, String msg,int pic){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");

        text.setTypeface(tf);

        ImageView image = (ImageView) dialog.findViewById(R.id.btn_dialog);
        image.setImageBitmap(BitmapFactory.decodeResource(getResources(),pic));
        TextView dialogButton = (TextView)dialog.findViewById(R.id.cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void getPositionsForDots(int position){

        for (int i=0; i<dotPositions.length; i++){
            ImageView temp = (ImageView)findViewById(dotPositions[i]);
            temp.setAlpha(0.5f);
        }
        ImageView temp = (ImageView)findViewById(dotPositions[position]);
        temp.setAlpha(1f);

    }

    private void setTypeface(){
        ImageView t1 = (ImageView) findViewById(R.id.new_location);
        TextView t2 = (TextView)findViewById(R.id.attack);
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
       // t1.setTypeface(t);
        //t2.setTypeface(t);
    }

    public int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    public static int showRandomInteger(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }

    private void getting_categories() {
        if(c==0)
        {
            getting_service_status();
            c=1;
        }

        if (!swipeRefreshLayout.isRefreshing())
        {
            progress.setMessage("Fetching Data.....");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

                if (progress.isShowing()) {
                    progress.dismiss();
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
        int i;
        categories.clear();
        images.clear();
        for (i = 0; i < localdatabase.masterList.size(); i++) {
            MasterMenuItems a=localdatabase.masterList.get(i);
            String name=a.getName();
            String img=a.getImage();
            if((categories.contains(a.getName())) & (images.contains(a.getImage())))
            {
                //nothing should be added
            }
            else
            {
                categories.add(name);
                images.add(img);

            }
        }
        send_to_adapter();

    }

    private void getting_service_status() {
        if(progress.isShowing())
        {
            progress.dismiss();
        }
        else
        {
            if(!swipeRefreshLayout.isRefreshing()) {
                progress.setMessage("Getting Kitchen Status...");
                progress.show();
            }
        }
        if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
if(local.metaData!=null) {
    if (local.metaData.getservice().equals("true")) {
        Toast.makeText(this, "Kitchen is Open", Toast.LENGTH_SHORT).show();
        attack.setText("Kitchen is Open.");
        attack.setBackgroundColor(Color.parseColor("#7ee591"));
        //showDialog(this,"Kitchen is Closed\nPlease come back from 6 to 10",R.drawable.store);
    } else {
        showDialog(this, "Kitchen is Closed\nPlease come back from 6 pm to 10 pm", R.drawable.store);
        Toast.makeText(this, "Kitchen is closed", Toast.LENGTH_SHORT).show();
        attack.setText("Kitchen is Closed.");
        attack.setBackgroundColor(Color.parseColor("#dac598"));
    }
}
    }


    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void Display(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void send_to_adapter()
    {
        adapter=new categoryAdapter(this,categories,images);
        adapter.notifyDataSetChanged();
        recylerView.setAdapter(adapter);
        recylerView.setNestedScrollingEnabled(false);
//        recylerView.addItemDecoration(itemDecoration);
        recylerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        getPositionsForDots(position%dotPositions.length);
       // Display("Called at "+position);

    }

    @Override
    public void onPageSelected(int position) {
//       Display("Page selected at "+position);
        getPositionsForDots(position%dotPositions.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

  //      getPositionsForDots(3);

    }

    @Override
    public void onBackPressed() {
        if(nav){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }else{
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem=menu.findItem(R.id.action_cart);
         actionView= MenuItemCompat.getActionView(menuItem);
        cartitemcount1=(TextView) actionView.findViewById(R.id.cart_badge);

        cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
        ImageView cart = (ImageView)actionView.findViewById(R.id.cartimage);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ssd=new Intent(getApplicationContext(),cart.class);
                startActivity(ssd);
            }
        });

        ImageView notif = (ImageView)actionView.findViewById(R.id.notif);

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(menu.this, NotificationActivity.class));
            }
        });



         localdatabase.notifmount = (TextView)actionView.findViewById(R.id.notification_badge);
        if(localdatabase.notifications==0){
            localdatabase.notifmount.setVisibility(View.INVISIBLE);
        }else {
            localdatabase.notifmount.setVisibility(View.VISIBLE);
            localdatabase.notifmount.setText(localdatabase.notifications+"");
        }

        return true;
    }

    private  void updateUI(int d){
        if(localdatabase.notifmount!=null)
        localdatabase.notifmount.setText(d+"");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu) {
            // Handle the camera action
        } else if (id == R.id.cart) {

            Intent a=new Intent(getApplicationContext(),cart.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);


        } else if (id == R.id.orders) {
            Intent a=new Intent(getApplicationContext(),order_history.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);


        } else if (id == R.id.SignOut) {

            shared.edit().remove("address").apply();
            shared.edit().remove("password").apply();
            shared.edit().remove("mobile").apply();

            this.finish();
            Intent intent=new Intent(getApplicationContext(),login_page.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();


        }
        else if(id==R.id.details)
        {
            Intent a=new Intent(getApplicationContext(),details.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);

        }else if(id==R.id.notifications){
            final Intent a=new Intent(getApplicationContext(),NotificationActivity.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(a);
                }
            },1000);

        }else if(id==R.id.favNav){
            Intent as=new Intent(this,menu_category_wise.class);
            Bundle a=new Bundle();
            a.putString("category","Favourites");
            a.putInt("position", -1);
            as.putExtras(a);
            as.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(as);
        } else if (id == R.id.AboutUs) {
            Intent a = new Intent(getApplicationContext(), about_us.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }
        else if (id == R.id.Support) {
            Intent a = new Intent(getApplicationContext(), Support.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }
        else if(id==R.id.share)
        {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "FoodSingh");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.fsingh.pranshooverma.foodsingh&hl=en\n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //recreate();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override

    protected void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
            Log.i("getting unregistered","");
        }

    }

    private class LoadBitmap1 extends AsyncTask<String, Void, Bitmap>{
    private int img;
        public LoadBitmap1(int bitmap){

            this.img = bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap==null)
                return;
        if(img==0) {
            ad1.setImageBitmap(bitmap);
            ad1.setVisibility(View.VISIBLE);
        }else{
            ad2.setImageBitmap(bitmap);
            ad2.setVisibility(View.VISIBLE);
        }
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            return getBitmapFromURL(strings[0]);
        }
        public Bitmap getBitmapFromURL(String strURL) {
            try {
                URL url = new URL(strURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


    }


}
