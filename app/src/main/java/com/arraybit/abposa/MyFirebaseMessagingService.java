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
import com.arraybit.global.SharePreferenceManage;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String newOrders, newBookings, cancleOrders, cancleBookings;
    String message, title = "Central Air", type, summaryType;
    int offerId;
    private static int idNewOrder = 0;
    private static int idNewBooking = 0;
    private static int idCancleOrder = 0;
    private static int idCancleBooking = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //Calling method to generate notification
//        if (remoteMessage.getNotification() != null) {
//            message = remoteMessage.getNotification().getBody();
//            title = remoteMessage.getNotification().getTitle();
//        }

        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().containsKey("title")) {
                title = remoteMessage.getData().get("title");
            }
            if (remoteMessage.getData().containsKey("body")) {
                message = remoteMessage.getData().get("body");
            }
            if (remoteMessage.getData().containsKey("type")) {
                type = remoteMessage.getData().get("type");
            }

            if (type.equals("orderSummary")) {
                String count = "";
                if (remoteMessage.getData().containsKey("summaryType")) {
                    summaryType = remoteMessage.getData().get("summaryType");
                }
                if (remoteMessage.getData().containsKey("count")) {
                    count= remoteMessage.getData().get("count");
                }

                if(summaryType.equals("newOrders")){
//                if (remoteMessage.getData().containsKey("newOrders")) {
//                    newOrders = remoteMessage.getData().get("newOrders");;
                    newOrders = count;
                    if (newOrders != null && !newOrders.equals("") && !newOrders.equals("null")) {
                        if (!newOrders.equals(Globals.newOrder)) {
                            Globals.newOrder = newOrders;
                        }
                    }
                }
                if(summaryType.equals("newBookings")){
//                    if (remoteMessage.getData().containsKey("newBookings")) {
//                    newBookings = remoteMessage.getData().get("newBookings");
                    newBookings = count;
                    if (newBookings != null && !newBookings.equals("") && !newBookings.equals("null")) {
                        if (!newOrders.equals(Globals.newOrder)) {
                            Globals.newBooking = newBookings;
                        }
                    }
                }
                if(summaryType.equals("cancleOrders")){
//                    if (remoteMessage.getData().containsKey("cancleOrders")) {
//                    cancleOrders = remoteMessage.getData().get("cancleOrders");
                    cancleOrders = count;
                    if (cancleOrders != null && !cancleOrders.equals("") && !cancleOrders.equals("null")) {
                        if (!newOrders.equals(Globals.newOrder)) {
                            Globals.cancleOrder = cancleOrders;
                        }
                    }
                }
                if(summaryType.equals("cancleBookings")){
//                    if (remoteMessage.getData().containsKey("cancleBookings")) {
//                    cancleBookings = remoteMessage.getData().get("cancleBookings");
                    cancleBookings = count;
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
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            String isPush = objSharePreferenceManage.GetPreference("NotificationSettingPreference", "Push", MyFirebaseMessagingService.this);
            if (isPush != null && !isPush.equals("") && isPush.equals("true")) {
                sendNotification();
            }
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification() {
        try {
            Intent intent;
            if (type.equals("orderSummary")) {
                intent = new Intent(this, HomeActivity.class);
            } else if (type.equals("offer")) {
                intent = new Intent(this, OfferDetailActivity.class);
                intent.putExtra("isDirect", true);
                intent.putExtra("OfferMasterId", offerId);
            } else {
                intent = new Intent(this, HomeActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (type.equals("orderSummary")) {
                if (newBookings != null) {
                    notificationBuilder.setContentText(message + " " + newBookings);
                    notificationManager.notify(idNewBooking++, notificationBuilder.build());
                } else if (newOrders != null) {
                    notificationBuilder.setContentText(message + " " + newOrders);
                    notificationManager.notify(idNewOrder++, notificationBuilder.build());
                } else if (cancleBookings != null) {
                    notificationBuilder.setContentText(message + " " + cancleBookings);
                    notificationManager.notify(idCancleBooking++, notificationBuilder.build());
                } else if (cancleOrders != null) {
                    notificationBuilder.setContentText(message + " " + cancleOrders);
                    notificationManager.notify(idCancleOrder++, notificationBuilder.build());
                }
//            } else if (type.equals("offer")) {
//                notificationBuilder.setContentText(message);
            }else {
                notificationBuilder.setContentText(message);
                notificationManager.notify(0, notificationBuilder.build());
            }

//            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}