package com.fsingh.pranshooverma.foodsingh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login_page extends AppCompatActivity {
    EditText mobile,password;
    Button login,new_user;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);

        initialize();

        setTypeFace();

        if(check_if_logged_in())
        {
            Intent as1=new Intent(getApplicationContext(),menu.class);
            startActivity(as1);
            finish();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num=mobile.getText().toString();
                String pass=password.getText().toString();

                if(checking_net_permission())
                {
                    if(num.length()==10 & pass.length()>=4)
                    {
                        num="91"+num;
                        check_login_details(num,pass);
                    }
                    else
                    {
                        Display("Enter 10 Digit mobile number & password should have atleast 4 characters");
                    }

                }
                else
                {
                   // Display("No internet connection,kindly have it to proceed");
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
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(getApplicationContext(),Signup_OTP.class);
                startActivity(a);
                finish();
            }
        });

    }

    private void setTypeFace() {
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        mobile.setTypeface(t);
        password.setTypeface(t);
        login.setTypeface(t);
        new_user.setTypeface(t);
        TextView loginpage = (TextView)findViewById(R.id.logintofoodsingh);
        loginpage.setTypeface(t);

    }

    private Boolean check_if_logged_in() {
        SharedPreferences as=getSharedPreferences("foodsingh",MODE_PRIVATE);
        String pass=as.getString("password","123");
        if(pass.equals("123"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private void check_login_details(final String numy, final String passy) {
        progress.setMessage("Checking Credentials");
        progress.show();

        StringRequest das=new StringRequest(Request.Method.POST, constants.login_check_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    String status=obj.getString("status");
                    if(status.equals("1"))
                    {
                 //       Display("Successfully logged in");
                        SharedPreferences as=getSharedPreferences("foodsingh",MODE_PRIVATE);
                        SharedPreferences.Editor edit=as.edit();
                        edit.putString("password",passy);//100 defined for logged in
                        edit.putString("mobile",numy);
                        edit.apply();


                        Intent f=new Intent(getApplicationContext(),menu.class);
                        startActivity(f);
                        finish();
                    }
                    else
                    {
                        Display("Wrong credentials ,please try again");
                    }
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
                Display("Some error Occured may be due to bad internet connection");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("number",numy);
                maps.put("password",passy);
                return maps;
            }
        };

        RequestQueue as=Volley.newRequestQueue(this);
        as.add(das);
    }

    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void Display(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void initialize() {
        mobile=(EditText) findViewById(R.id.mob);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.btnlogin);
        new_user=(Button)findViewById(R.id.btnToSignUp);
        progress=new ProgressDialog(this);
    }
}
