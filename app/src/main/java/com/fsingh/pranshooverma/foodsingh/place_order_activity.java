package com.fsingh.pranshooverma.foodsingh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Display;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class place_order_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView total_amount,final_amount;
    EditText address;
    Button finally_place_order;
    List<String> local_list=new ArrayList<>();
    int final_am=0;
    String mobile_number;
    ProgressDialog progress;

    SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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


        finally_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checking_net_permission())
                {
                    if(final_am==0)
                    {
                        Toast.makeText(place_order_activity.this, "You have not added anything in the cart", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String add=address.getText().toString();

                        send_to_deb(add);

                    }

                }
                else
                {
                    Toast.makeText(place_order_activity.this, "you dont have net connection...", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        re.add(str);
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
