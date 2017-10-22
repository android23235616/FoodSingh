package com.fsingh.pranshooverma.foodsingh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class about_us extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
TextView text_about;
    ImageView img_about;
    ProgressDialog progress;
    public static TextView cartitemcount1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RemoveTop();
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    ///////////////////////////////////////////////////////////////////////////////////////
        //coding

        initialize();

        text_about.setText(localdatabase.aboutText);



        if(checking_net_permission())
        {
            Glide.with(about_us.this).load(localdatabase.aboutImage).skipMemoryCache(true).thumbnail(0.05f)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(img_about);

        }


    }

    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void initialize() {
        text_about=(TextView) findViewById(R.id.text_about);
        img_about=(ImageView) findViewById(R.id.img_about);
      /*  progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setCancelable(false);
        progress.show();
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem=menu.findItem(R.id.action_cart);
        View actionView= MenuItemCompat.getActionView(menuItem);
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
                startActivity(new Intent(about_us.this, NotificationActivity.class));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void RemoveTop(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
