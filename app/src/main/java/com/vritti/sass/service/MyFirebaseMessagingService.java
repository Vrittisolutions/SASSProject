package com.vritti.sass.service;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.vritti.sass.DowngradeCondition;
import com.vritti.sass.MainActivity;
import com.vritti.sass.R;
import com.vritti.sass.WorkAuthorizationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String FinalObj;
    public static SharedPreferences userpreferences;
    String notification4;

    NotificationChannel channel;

    private RemoteMessage remoteMessage;
    private JSONObject obj;
    private boolean flag;
    ArrayList<DowngradeCondition>downgradeConditionArrayList=new ArrayList<>();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        notification4 = remoteMessage.getData().get("message");
        flag = false;
        PowerManager pm = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(isScreenOn==false)
        {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }else {

        }

        getnotification(notification4);

    }

    @SuppressLint("NewApi")
    private void getnotification(String notification4) {

        if (!flag) {
            String MsgType = "";
            String Msgcontent = "";
            try {
                obj = new JSONObject(notification4);
                //   MsgType = obj.getString("MsgType");
                Msgcontent = obj.getString("Msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!flag) {
                sendnotification(Msgcontent);
               /* DowngradeCondition downgradeCondition=new DowngradeCondition();
                downgradeCondition.setMessage(Msgcontent);
                downgradeConditionArrayList.add(downgradeCondition);
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(downgradeConditionArrayList);
                editor.putString("Downgrade", json);
                editor.commit();*/
                flag = true;
            }

        } else {
            Log.e("notification", "already send notification");
        }
    }

    private void sendnotification(String msgcontent) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_ONE_SHOT//FLAG_UPDATE_CURRENT
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel("one",
                    "Downgrade",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            channel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            channel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "one")
                .setContentTitle("Downgrade Notification")
                .setContentText(msgcontent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msgcontent))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(notification4)
                .setGroupSummary(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSubText("Tap to open  List");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.dummy);
            notificationBuilder.setColor(getResources().getColor(R.color.red));
        } else {
//                notificationBuilder.setSmallIcon(R.drawable.noti_imag);
//                notificationBuilder.setColor(getResources().getColor(R.color.rating_bar_color));
            notificationBuilder.setSmallIcon(R.mipmap.ic_logo);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);

        }
      /*  Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;
*/
        notificationManager.notify(0, notificationBuilder.build());
    }


    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        final String message=intent.getStringExtra("message");

        if (message!=null) {
            // if (isAppRunning(message)) ;
            if(isApplicationSentToBackground(getApplicationContext())){
                getnotification(message);
            }
        }
    }




    private boolean isApplicationSentToBackground(Context mcontext) {
        ActivityManager am = (ActivityManager) mcontext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mcontext.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}