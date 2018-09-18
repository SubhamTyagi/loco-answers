package ai.loko.hk.ui.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ai.loko.hk.ui.MainActivity;
import ai.loko.hk.ui.activities.Test;
import ai.loko.hk.ui.model.NotificationD;
import ui.R;

public class NotificationUtils {

    private static final int NOTIFICATION_ID = 200;
     private static final String CHANNEL_ID = "myChannel";
    //set action in payload

    private static final String URL = "url";
    //set data in payload
    private static final String ACTIVITY = "activity";

    Map<String, Class> activityMap = new HashMap<>();
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //Populate activity map
        activityMap.put("MainActivity", MainActivity.class);
        activityMap.put("Test", Test.class);

    }

    public void displayNotification(NotificationD notificationD, Intent resultIntent) {
        {
            String message = notificationD.getMessage();
            String title = notificationD.getTitle();

            String iconUrl = notificationD.getIconUrl();
            String action = notificationD.getAction();
            String destination = notificationD.getActionDestination();
            Bitmap iconBitMap = null;

            if (iconUrl != null) {
                iconBitMap = getBitmapFromURL(iconUrl);
            }
            final int icon = R.mipmap.ic_launcher;

            PendingIntent resultPendingIntent;

            if (URL.equals(action)) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
                resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
            } else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
                resultIntent = new Intent(mContext, activityMap.get(destination));
                resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            } else {
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            }


            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID);

            Notification notification;

            if (iconBitMap == null) {
                //When Inbox Style is applied, user can expand the notification
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(message);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(inboxStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(message)
                        .build();

            } else {
                //If Bitmap is created from URL, show big icon
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                bigPictureStyle.bigPicture(iconBitMap);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(bigPictureStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(message)
                        .build();
            }

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    /**
     * Downloads push notification image before displaying it in
     * the notification tray
     *
     * @param strURL : URL of the notification Image
     * @return : BitMap representation of notification Image
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            java.net.URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Playing notification sound
     */
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
