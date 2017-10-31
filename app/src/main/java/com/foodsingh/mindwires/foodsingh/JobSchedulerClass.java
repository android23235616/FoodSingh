package com.foodsingh.mindwires.foodsingh;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

/**
 * Created by Tanmay on 31-10-2017.
 */

public class JobSchedulerClass {

    public static void setUpJobScheduler(Context context){
        ComponentName comp = new ComponentName(context, KitchenService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0,comp);
        builder.setMinimumLatency(100);
        builder.setOverrideDeadline(1000);

        JobScheduler job = context.getSystemService(JobScheduler.class);
        job.schedule(builder.build());
    }
}
