package com.fsingh.pranshooverma.foodsingh;

import android.content.Intent;
import android.media.Image;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class menu_item_details extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView plus,minus,item_image;
    TextView item_name,item_price,quantity,cartitemcount1;
    String name,price,image,item_quantity;
    MenuItems item;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///////////////////////////////////////////////////////
        //CODING PART

        initialize();
        getting_intents();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menu_item_details.this, "plus", Toast.LENGTH_SHORT).show();
                //int quantiti=item.getQuantity();
                ///quantiti++;
                //quantity.setText(String.valueOf(quantiti));
                Toast.makeText(menu_item_details.this, String.valueOf(localdatabase.cartList.contains(item)), Toast.LENGTH_SHORT).show();
                //item.setQuantity(quantiti);


            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menu_item_details.this, "minus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getting_intents() {
        Bundle b=getIntent().getExtras();
        name=b.getString("item_name");
        price=b.getString("item_price");
        image=b.getString("item_image");
        item=b.getParcelable("object");
        item_quantity=b.getString("item_quantity");
        position=b.getInt("position");
        if (item_image.equals("")){
            quantity.setText("0");
        }
        else
        {
            quantity.setText(item_quantity);
        }

        Glide.with(getApplicationContext()).load(image).skipMemoryCache(true).thumbnail(0.05f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(item_image);
        item_price.setText(price);
        item_name.setText(name);

    }

    private void initialize() {
        plus=(ImageView)findViewById(R.id.plus_slide);
        minus=(ImageView)findViewById(R.id.minus_slide);
        quantity=(TextView)findViewById(R.id.item_quantity_slide);
        item_name=(TextView) findViewById(R.id.item_name);
        item_image=(ImageView)findViewById(R.id.item_image);
        item_price=(TextView) findViewById(R.id.item_price);



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

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ssd=new Intent(getApplicationContext(),cart.class);
                startActivity(ssd);
            }
        });

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
}
