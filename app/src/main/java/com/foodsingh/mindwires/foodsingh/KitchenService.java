package com.foodsingh.mindwires.foodsingh;

import android.app.IntentService;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class KitchenService extends JobService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.foodsingh.mindwires.foodsingh.action.FOO";
    private static final String ACTION_BAZ = "com.foodsingh.mindwires.foodsingh.action.BAZ";
    static Runnable runnable;

    static Handler h = new Handler();

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.foodsingh.mindwires.foodsingh.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.foodsingh.mindwires.foodsingh.extra.PARAM2";

    public KitchenService() {

    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if(localdatabase.metaData!=null)
        getKitchenStatus(constants.service_status_url);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }





    private void getKitchenStatus(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("service");
                    Intent i = new Intent();
                    localdatabase.metaData.setService(status);
                    i.setAction(constants.kitchenStatusBroadcast);
                    sendBroadcast(i);
                    JobSchedulerClass.setUpJobScheduler(KitchenService.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);
    }


}
