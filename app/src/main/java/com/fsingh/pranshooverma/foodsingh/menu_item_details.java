package com.fsingh.pranshooverma.foodsingh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class menu_item_details extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView plus,minus,item_image;
    int p;
    TextView item_name,item_price,quantity,cartitemcount1,unav;
    String name,price,image,item_quantity;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImageView fav;
    Button addFav;
    MenuItems mainItem;
    int isAvailable;
    boolean isFav = false;
    Gson gson;

    FavouritesList myList;

    List<MenuItems> favList = new ArrayList<>();

    MenuItems item;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemoveTop();
        setContentView(R.layout.activity_menu_item_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gson = new Gson();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initialize();
        getting_intents();

        if(isAvailable==1){


        }else{
            unav.setVisibility(View.INVISIBLE);
        }

       refreshList();

        if(isFav) {

            fav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_fav));
            addFav.setText("REMOVE FROM FAVOURITES");
        }else{
            fav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_not_fav));
            addFav.setText("ADD TO FAVOURITES");
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(isAvailable!=1){
                  int check=0;
                  Toast.makeText(menu_item_details.this, "plus", Toast.LENGTH_SHORT).show();
                  int quantiti = item.getQuantity();
                  quantiti=quantiti+1;
                  quantity.setText(String.valueOf(quantiti));
                  item.setQuantity(quantiti);
                  for(int i=0;i<localdatabase.cartList.size();i++)
                  {
                      if(name.equalsIgnoreCase(localdatabase.cartList.get(i).getName()))
                      {
                          Toast.makeText(menu_item_details.this, "Got it", Toast.LENGTH_SHORT).show();
                          localdatabase.cartList.remove(i);
                          localdatabase.cartList.add(item);
                          check=1;

                      }

                  }
                  if(check!=1) {
                      localdatabase.cartList.add(item);
                  }

                  cartitemcount1.setText(localdatabase.cartList.size()+"");
              }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menu_item_details.this, "minus", Toast.LENGTH_SHORT).show();
            }
        });

        addFav.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (isAvailable != 1) {
                    if (isFav) {
                        isFav = false;

                        fav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_not_fav));
                        addFav.setText("ADD TO FAVOURITES");
                        favList.remove(Exists(item));

                    } else {
                        isFav = true;
                        addFav.setText("REMOVE FROM FAVOURITES");
                        fav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_fav));
                        favList.add(mainItem);

                    }

                    save();
                }
            }
        });
    }


    private void RemoveTop(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void refreshList(){
        myList = getMyList();
        favList = myList.getFavouriteList();
    }

    private void save() {

        DisplayList(favList);

        FavouritesList temp = new FavouritesList(favList);
        String tempJson = gson.toJson(temp);
        editor.putString(constants.fav,tempJson);
        editor.apply();

    }

    private int Exists(MenuItems item){

        for(int i=0; i<favList.size(); i++){
            if(favList.get(i).getId().equals(item.getId())
                    &&favList.get(i).getName().equals(item.getName())
                    ){
                return i;
            }
        }
        return -1;


    }

    private void DisplayList(List<MenuItems> item){
        for (int i=0; i<item.size(); i++){
            Log.i("details Fav "+i, item.get(i).getName());
        }
    }

    private void getting_intents() {
        Bundle b=getIntent().getExtras();
        name=b.getString("item_name");
        price=b.getString("item_price");
        image=b.getString("item_image");
        item=b.getParcelable("object");
        item_quantity=b.getString("item_quantity");
        position=b.getInt("position");
        isFav = b.getBoolean("isfav");
        mainItem = b.getParcelable("mainobject");
        isAvailable = b.getInt("avail");
        p = b.getInt("mainposition");

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
        TextView description = (TextView)findViewById(R.id.descrption);
        fav = (ImageView)findViewById(R.id.fav);
        unav = (TextView)findViewById(R.id.txt_unavailable);
        addFav = (Button)findViewById(R.id.add_to_fav);

        sp = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/Alisandra Demo.ttf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(),"fonts/FREESCPT.TTF");
        //Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/Alisandra Demo.ttf");
        item_name.setTypeface(tf1,Typeface.BOLD);
        item_price.setTypeface(tf1,Typeface.BOLD);
        description.setTypeface(tf2);

        editor = sp.edit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }

        Intent as=new Intent(this,menu_category_wise.class);
        Bundle a=new Bundle();
        a.putString("category",item.getCategory());
        a.putInt("position", p);
        as.putExtras(a);
        startActivity(as);
        finish();
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
                startActivity(new Intent(menu_item_details.this, NotificationActivity.class));
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

    private void Display(String s){
        Toast.makeText(this,s, Toast.LENGTH_LONG).show();
    }

    public FavouritesList getMyList() {

        String tempJson = sp.getString(constants.fav, "");

        if(tempJson.equals("")){
            List<MenuItems> tempList = new ArrayList<>();
           return new FavouritesList(tempList);
        }else{
            FavouritesList f = gson.fromJson(tempJson, FavouritesList.class);
            return f;
        }

    }
}