package com.arraybit.abposa;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arraybit.global.Globals;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String newOrders, newBookings, cancleOrders, cancleBookings;
    String message, title = "Central Air", type;
    int offerId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //Calling method to generate notification
        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();
        }

        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().containsKey("type")) {
                type = remoteMessage.getData().get("type");
            }

            if (type.equals("orderSummary")) {
                if (remoteMessage.getData().containsKey("newOrders")) {
                    newOrders = remoteMessage.getData().get("newOrders");
                    if (newOrders != null && !newOrders.equals("") && !newOrders.equals("null")) {
                        if (!newOrders.equals(Globals.newOrder)) {
                            Globals.newOrder = newOrders;
                        }
                    }
                }
                if (remoteMessage.getData().containsKey("newBookings")) {
                    newBookings = remoteMessage.getData().get("newBookings");
                    if (newBookings != null && !newBookings.equals("") && !newBookings.equals("null")) {
                        if (!newOrders.equals(Globals.newOrder)) {
                            Globals.newBooking = newBookings;
                        }
                    }
                }
                if (remoteMessage.getData().containsKey("cancleOrders")) {
                    cancleOrders = remoteMessage.getData().get("cancleOrders");
                    if (cancleOrders != null && !cancleOrders.equals("") && !cancleOrders.equals("null")) {
                        if (!newOrders.equals(Globals.newOrder)) {
                            Globals.cancleOrder = cancleOrders;
                        }
                    }
                }
                if (remoteMessage.getData().containsKey("cancleBookings")) {
                    cancleBookings = remoteMessage.getData().get("cancleBookings");
                    if (cancleBookings != null && !cancleBookings.equals("") && !cancleBookings.equals("null")) {
                        if (!newOrders.equals(Globals.newOrder)) {
                            Globals.cancleBooking = cancleBookings;
                        }
                    }
                }
            } else if (type.equals("offer")) {
                if (remoteMessage.getData().containsKey("offerId")) {
                    offerId = Integer.parseInt(remoteMessage.getData().get("offerId"));
                }
            }
        }
        if (type != null) {
            sendNotification();
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification() {
        try {
            Intent intent = null;
            if (type.equals("orderSummary")) {
                intent = new Intent(this, HomeActivity.class);
            } else if (type.equals("offer")) {
                intent = new Intent(this, OfferDetailActivity.class);
                intent.putExtra("isDirect", true);
                intent.putExtra("OfferMasterId", offerId);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bitmap;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.central_air_logo);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.central_air_logo);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.central_air)
                    .setLargeIcon(bitmap)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 250, 100, 250, 100, 250})
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent);

            if (type.equals("orderSummary")) {
                if (newBookings != null) {
                    notificationBuilder.setContentText(message + " " + newBookings);
                } else if (newOrders != null) {
                    notificationBuilder.setContentText(message + " " + newOrders);
                } else if (cancleBookings != null) {
                    notificationBuilder.setContentText(message + " " + cancleBookings);
                } else if (cancleOrders != null) {
                    notificationBuilder.setContentText(message + " " + cancleOrders);
                }
            } else if (type.equals("offer")) {
                notificationBuilder.setContentText(message);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}