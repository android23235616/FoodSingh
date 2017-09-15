package com.fsingh.pranshooverma.foodsingh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Display;
import android.view.Gravity;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class place_order_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView total_amount,final_amount,textCoupon;
    EditText address;
    Button finally_place_order;
    List<String> local_list=new ArrayList<>();
    int final_am=0;
    String mobile_number;
    ProgressDialog progress;

    int counter=0,ii=0;
    int discount_amount=0;
    String CouponText="";

    RelativeLayout relativeLayout_coupon;

    SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_place_order_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        TextView toolbarText = (TextView) findViewById(R.id.toolbarText);
        toolbarText.setTypeface(t);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /////////////////////////////////////
//coding coding

        initialize();

        gettin_amount();

        coupon_from_data();

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


            finally_place_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checking_net_permission()) {
                        if (final_am == 0) {
                            Toast.makeText(place_order_activity.this, "You have not added anything in the cart", Toast.LENGTH_SHORT).show();
                        } else {
                            String add = address.getText().toString();
                            ii=0;
                            send_to_deb(add);

                        }

                    } else {
                        Toast.makeText(place_order_activity.this, "you dont have net connection...", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            relativeLayout_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counter++;

                    if (counter % 2 == 1) {
                        if (final_am != 0 & discount_amount != 0) {
                            Snackbar.make(view, "Coupon applied.....", Snackbar.LENGTH_SHORT).show();
                            String mainString = String.valueOf(final_am - (((discount_amount) * final_am) / 100));
                            final_amount.setText(mainString);
                            multiLineStrikeThrough(textCoupon, CouponText);

                        } else {
                            Snackbar.make(view, "Cant apply the coupon.....", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(view, "Coupon removed.....", Snackbar.LENGTH_SHORT).show();
                        final_amount.setText(String.valueOf(final_am));
                        multiLineStrikeThrough(textCoupon, "");
                    }

                }
            });

        }
    }

    private void multiLineStrikeThrough(TextView description, String textContent){

        description.setText(textContent, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable)description.getText();
        spannable.setSpan(new StrikethroughSpan(), 0, textContent.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if(textContent.equals(""))
            description.setText(CouponText);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);

    }

    private void coupon_from_data() {
        if(checking_net_permission())
        {
            getting_coupon();
        }
        else
        {
            Display("No internet Connection");
        }
    }

    private void getting_coupon() {

        progress.setMessage("Fetching coupon code....");

        if(!progress.isShowing())
        {
         progress.show();
        }

        StringRequest str=new StringRequest(Request.Method.POST, constants.discount_coupon, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    String dis=obj.getString("discount");
                    discount_amount= Integer.parseInt(dis);
                    if(dis.equals("0"))
                    {
                        textCoupon.setText("No coupon to reedem");

                        textCoupon.setGravity(Gravity.CENTER);
                    }
                    else
                    {
                        CouponText="Click this to redeem "+dis+" % discount on final amount";
                        textCoupon.setText("Click this to redeem "+dis+" % discount on final amount");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue r=Volley.newRequestQueue(this);
        r.add(str);

    }


    private void  Display(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void send_to_deb(final String addy) {
        progress.setMessage("Sending Order........");
        progress.setCancelable(false);
        progress.show();

        Toast.makeText(this, addy+"\n"+String.valueOf(final_am)+"\n"+local_list+"\n"+mobile_number, Toast.LENGTH_SHORT).show();
        StringRequest str=new StringRequest(Request.Method.POST, constants.send_to_debian, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                Toast.makeText(place_order_activity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(place_order_activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("item",local_list.toString());
                maps.put("amount", String.valueOf(final_am));
                maps.put("mobile",mobile_number);
                maps.put("address",addy);

                return maps;
            }
        };
        RequestQueue re= Volley.newRequestQueue(this);
        if(ii==0) {
            re.add(str);
            ii++;
        }
        str.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void gettin_amount() {
        Bundle aa=getIntent().getExtras();
        total_amount.setText(aa.getString("sum"));
        final_amount.setText(aa.getString("sum"));

        final_am= Integer.parseInt(aa.getString("sum"));

        for(int i=0;i<constants.item_name_deb.size();i++)
        {
            local_list.add(constants.item_name_deb.get(i)+"-"+constants.item_quant_deb.get(i)+", ");
        }
    }

    private void initialize() {
        TextView t1 = (TextView)findViewById(R.id.textView2);
        TextView t2 = (TextView)findViewById(R.id.textView4);
        TextView t3 = (TextView)findViewById(R.id.textView7);
        TextView couponText = (TextView)findViewById(R.id.coupontext);
        total_amount=(TextView)findViewById(R.id.total_amount);
        final_amount=(TextView)findViewById(R.id.final_amount);
        address=(EditText) findViewById(R.id.delivery_address);
        finally_place_order=(Button)findViewById(R.id.final_order);
        progress=new ProgressDialog(this);
        shared=getSharedPreferences("foodsingh",MODE_PRIVATE);
        mobile_number=shared.getString("mobile","123");
        if(mobile_number.equals("123"))
        {
            Toast.makeText(this, "Number is missing,kindly logout and log in again", Toast.LENGTH_SHORT).show();
            finish();
        }
        relativeLayout_coupon=(RelativeLayout)findViewById(R.id.relative_layout_coupon);
        textCoupon=(TextView) findViewById(R.id.textView_coupon);
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        total_amount.setTypeface(t);
        final_amount.setTypeface(t);
        address.setTypeface(t);
        finally_place_order.setTypeface(t);
        t1.setTypeface(t);
        t2.setTypeface(t);
        t3.setTypeface(t);
        textCoupon.setTypeface(t);
        couponText.setTypeface(t);


    }

    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////






    //////////////////////////////////////////////////////////////////////////////////////////////////////

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
        getMenuInflater().inflate(R.menu.place_order_activity, menu);
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
