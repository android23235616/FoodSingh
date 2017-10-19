package com.fsingh.pranshooverma.foodsingh;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.cast.CastRemoteDisplayApi;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Map;

/**
 * Created by Soumya Deb on 18-10-2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    Handler h;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        h = new Handler();

        sendNotification2(remoteMessage.getData());
        FirebaseRemoteConfig r1 = FirebaseRemoteConfig.getInstance();
        Log.i("chutia",r1.getString("hello"));



    }





    private void sendNotification2(Map<String,String> map){

        final String title = map.get("title");
        String body = map.get("body");

       h.post(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(FirebaseMessagingService.this,title+" here",Toast.LENGTH_LONG).show();
           }
       });
        Log.i("chutia",title+" sdf");



        Context context = this;

        Intent intent= new Intent(this, Splash.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

// Android 2.x does not support remote view + custom notification concept using
// support library
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            mBuilder.setSmallIcon(R.drawable.logo7);
            mBuilder.setContentTitle("FS")
                    .setStyle(
                            new NotificationCompat.BigTextStyle()
                                    .bigText(body))
                    .setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND)
                    //.setLights(Color.WHITE, 500, 500)
                    .setContentText(title);
        } else {
            //RemoteViews customNotificationView = new RemoteViews(getPackageName(),
                   // R.layout.notificationwindow);

            //customNotificationView.setImageViewBitmap(R.id.notifimg, BitmapFactory.decodeResource(this.getResources(),R.drawable.background));
           // customNotificationView.setTextViewText(R.id.notificationTitle, Util.notificationTitle);
            //customNotificationView.setTextViewText(R.id.notificationContent, notificationMessage);

          //  mBuilder.setContent(customNotificationView);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(body);
            mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.background)).setBigContentTitle(title));
            mBuilder.setSmallIcon(R.drawable.logo7);
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);

           // mBuilder.setLights(Color.WHITE, 500, 500);
        }//
// build notification
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(1000, mBuilder.build());

    }

    private RemoteViews getComplexNotificationView() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(
                this.getPackageName(),
                R.layout.notification_menu_layout
        );
        return notificationView;
    }
    private void Display(Map<String, String > map){
        String m = map.get("image");
        Log.i("notif2323",m);
    }

    private void SendNotification(RemoteMessage remoteMessage){
        String notificationTitle = remoteMessage.getNotification().getTitle();
        String notificationBody = remoteMessage.getNotification().getBody();
        Map<String,String> map = remoteMessage.getData();



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this).setContent(getComplexNotificationView())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notificationTitle+" chutia")
                        .setContentText(notificationBody+" chutia");



        int mNotificationId = (int)System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
