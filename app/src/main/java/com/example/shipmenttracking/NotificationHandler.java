package com.example.shipmenttracking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "shipment_notification_channel";
    private final int NOTIFICATION_ID = 0;

    private NotificationManager mNotificationManager;
    private Context mContext;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }
    private void createChannel(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Shipment Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.MAGENTA);
        channel.setDescription("Notification from Shipment Tracking app.");
        this.mNotificationManager.createNotificationChannel(channel);
    }

    public void send(String message){
        Intent intent = new Intent(mContext, TrackingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Shipment Tracking App")
                .setContentText(message)
                .setSmallIcon(R.drawable.shipping_car)
                .setContentIntent(pendingIntent);
        this.mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel(){
        this.mNotificationManager.cancel(NOTIFICATION_ID);
    }
}
