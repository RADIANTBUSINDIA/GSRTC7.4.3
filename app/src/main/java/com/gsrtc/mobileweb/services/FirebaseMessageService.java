package com.gsrtc.mobileweb.services;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.Language_Select;

import java.io.IOException;
import java.net.URL;

import androidx.core.app.NotificationCompat;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);

        Log.d("Msg", "Message received ["+remoteMessage+"]");
        Log.d("Msg", "Body!!!!!!!! received ["+remoteMessage.getData().get("image")+"]");

        // Create Notification
        Intent intent = new Intent(this, Language_Select.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap image = null;
        try {
            URL url = new URL(remoteMessage.getData().get("image"));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.gsrtc_loader_logo)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setLargeIcon(image)
//                .setSubText("Incoming Client")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(remoteMessage.getData().get("title")))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image)
                        .bigLargeIcon(image))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent);

//        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager
                .notify(1410, notificationBuilder.build());
    }
}


//{
//	"data": {
//		"image": "https://static.pexels.com/photos/4825/red-love-romantic-flowers.jpg",
//		"message": "Firebase Push Message Using API",
//		"AnotherActivity": "True"
//	},
//	"to" : "cAKDgrNlbus:APA91bG7fZeJ4mRdrsSkE9Z--HhQ-5Hv_fPKSfQZRa-nWwUDSnvgVG_K53kVeVfJhuRLkXN99992o7CLFD5WWVsjy7R1Ed7kGq6Ng5hbIo343UZVyg0m7wwm3ibWjHcyw2lXKVBrzUtP"
//}

//{
//	"to": "cAKDgrNlbus:APA91bG7fZeJ4mRdrsSkE9Z--HhQ-5Hv_fPKSfQZRa-nWwUDSnvgVG_K53kVeVfJhuRLkXN99992o7CLFD5WWVsjy7R1Ed7kGq6Ng5hbIo343UZVyg0m7wwm3ibWjHcyw2lXKVBrzUtP",
//	"notification": {
//		"body": "Shubham Gupta",
//		"title": "He is Associate Software Engineer in Hexaware Technologies PVT LTD.",
//		"icon": "https://pbs.twimg.com/profile_images/988274520032272385/VnqVwUCB.jpg"
//	}
//}