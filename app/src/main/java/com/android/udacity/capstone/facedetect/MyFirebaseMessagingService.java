package com.android.udacity.capstone.facedetect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mokriya on 28/02/17.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private int NOTIFICATION_ID=1;
   // private NotificationCompat.Builder builder=null;
    private String message=null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Messaging Service",remoteMessage.toString());
        String data = remoteMessage.getNotification().getBody();
        if(data.contains("http:")){
            Log.d("TAG","go img");
            message = "unknown person at office";
            getBitmapfromUrl(data,message);
        } else {
            Log.d("TAG","no img");
            message =data.replace("@_@", ", ")+" arrived at office";
            sendNotification(message, null);
        }

    }



    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(String messageBody, Bitmap image) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(image)/*Notification icon image*/
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        if(image!=null){
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(image));/*Notification with Image*/
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl,String message) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            sendNotification(message,bitmap);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


    /*private void sendNotification(final RemoteMessage.Notification notification) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        final RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_image);


        String url = notification.getBody();
        Intent i = new Intent(Intent.ACTION_VIEW);



        builder = new NotificationCompat.Builder(this);
        Notification notificationBulder = builder.build();
        if(notification.getBody().contains("http:")){
            i.setData(Uri.parse(url));
        } else {
            //show text
            contentView.setTextViewText(R.id.tv_name, notification.getBody());
        }

        builder.setContent(contentView);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

*//*
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon())
                        .setContentText(notification.getBody())
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND);

        //builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description));*//*


        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        mNotificationManager.notify(NOTIFICATION_ID, notificationBulder);
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return  R.mipmap.ic_launcher;
    }*/

}

