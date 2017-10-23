package com.fsingh.pranshooverma.foodsingh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class menu_category_wise extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progress;

    public static TextView cartitemcount, toolbarText,text,notifamount;

    RecyclerView recyclerView;
    List<String> dish_name=new ArrayList<>();
    List<String> dish_price=new ArrayList<>();
    View actionView;
    BroadcastReceiver broadcastReceiver;
    boolean nav=true;
    public static TextView cartitemcount1;
    NavigationView navigationView;
    RecyclerView.LayoutManager layoutManager;


    String category=null;
    static int position;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_category_wise);
        text = (TextView)findViewById(R.id.text);
        Typeface tp = Typeface.createFromAsset(getAssets(), "fonts/COPRGTB.TTF");
        text.setTypeface(tp);
        nav=true;
        /////////////////////////////////////////////////////////////////////////////////////////////
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

        ///////////////////////////////////////////////////////////////////
        //CODING CODING CODING


        initialize();

        getting_category_from_previous_activity();

        if (checking_net_permission()) {
            if (category != null) {
              //  receive_menu_for_category(category);
            } else {
                Display("Something went wrong");
            }

        } else {
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

        manipulatenavigationdrawer();
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);

        }

        populateUI();

        SetupBroadcastReceiver();
    }

    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals(constants.broaadcastReceiverMenu)){


                    notifamount.setVisibility(View.VISIBLE);
                    notifamount.setText(localdatabase.notifications+"");


                    Log.i("broadcastreceiver1", localdatabase.notifications+"");
                }else if(intent.getAction().equals(constants.menu2BroadcastReceiver)){
                    notifamount.setVisibility(View.INVISIBLE);
                }

            }


        };

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constants.menu2BroadcastReceiver);

        registerReceiver(broadcastReceiver,intentFilter);
        registerReceiver(broadcastReceiver,intentFilter2);
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

    private void receive_menu_for_category(final String cat) {
        progress.setMessage("Contacting Server....");
        progress.show();
        StringRequest str=new StringRequest(Request.Method.POST, constants.get_menu_category_wise, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }

                try {
                    JSONArray array=new JSONArray(response);
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject js=array.getJSONObject(i);
                        String n=js.getString("name");
                        String p="Rs."+js.getString("price");
                        dish_name.add(n);
                        dish_price.add(p);
                    }
                    send_to_adapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("category",cat);
                return maps;
            }
        };
        RequestQueue s= Volley.newRequestQueue(this);
        s.add(str);
    }

    private void getting_category_from_previous_activity() {
        Bundle d=getIntent().getExtras();
        category=d.getString("category");
        text.setText(category);
        position= d.getInt("position");

    //    Display(category);
    }

    private void initialize() {
         progress=new ProgressDialog(this);
         progress.setCancelable(false);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_menu_category_wise);
        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        toolbarText = (TextView)findViewById(R.id.toolbarText);
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        toolbarText.setTypeface(t);

        shared=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
    }

    private void send_to_adapter()
    {
       // adapter_menu_wise=new categoryAdapter_menu_wise(this,dish_name,dish_price);
       // recyclerView.setAdapter(adapter_menu_wise);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(3),true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void Display(String S)
    {
        Toast.makeText(this, S, Toast.LENGTH_SHORT).show();
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //writing function for Categories
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }




        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }



    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




















    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
                startActivity(new Intent(menu_category_wise.this, NotificationActivity.class));
            }
        });



        notifamount = (TextView)actionView.findViewById(R.id.notification_badge);
        if(localdatabase.notifications==0){
            notifamount.setVisibility(View.INVISIBLE);
        }else {
            notifamount.setVisibility(View.VISIBLE);
            notifamount.setText(localdatabase.notifications+"");
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
       // if (id == R.id.action_settings) {
         //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu) {
            // Handle the camera action
            Intent a=new Intent(getApplicationContext(),menu.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    private void populateUI() {
        MenuItemAdapter adapter = null;

        if(position!=-1) {

            List<MenuItems> menuItems = localdatabase.masterList.get(checkMaster(category)).getMenuList();
             adapter = new MenuItemAdapter(this, menuItems,position);
        }else{

            SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
            String tempJson = sharedPreferences.getString(constants.fav,"");
            if(tempJson.equals("")){
                Display("You Have No Favourites");
            }else{
                Gson gson = new Gson() ;
                FavouritesList f = gson.fromJson(tempJson, FavouritesList.class);
                List<MenuItems> menuItemses=f.getFavouriteList();
                adapter = new MenuItemAdapter(this, menuItemses,position);

            }

        }
        if(adapter!=null){
            recyclerView.setAdapter(adapter);
            //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(3),true));
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }


        //Toast.makeText(this, ""+Splash.masterMenuItems.size(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, ""+localdatabase.masterList.get(position).getMenuList(), Toast.LENGTH_LONG).show();

    }

    private int checkMaster(String category){
        for(int i=0; i<localdatabase.masterList.size(); i++){
            if(localdatabase.masterList.get(i).getName().equals(category)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver!=null)
            unregisterReceiver(broadcastReceiver);
    }

}
