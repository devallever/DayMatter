package com.zf.daymatter.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.zf.daymatter.R;
import com.zf.daymatter.data.DataListener;
import com.zf.daymatter.data.Event;
import com.zf.daymatter.data.Repository;
import com.zf.daymatter.utils.DateUtils;
import com.zf.daymatter.utils.RemindUtils;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Mac on 2018/3/7.
 */

public class CurrentAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "CurrentAlarmReceiver";


    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        //遍历 今天的所有倒计时事件
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Log.d(TAG, "onReceive: year = " + year);
        Log.d(TAG, "onReceive: month = " + month);
        Log.d(TAG, "onReceive: day = " + day);

        Repository.getIns().getEventListByDate(year, month + 1, day, new DataListener<List<Event>>() {
            @Override
            public void onSuccess(List<Event> data) {
                sendNotification(context, data);
            }

            @Override
            public void onFail(String msg) {}
        });
    }

    private void sendNotification(Context context, List<Event> eventList){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent actionIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,actionIntent , 0);

        for (Event event: eventList){
            Log.d(TAG, "onReceive: title = " + event.getTitle());

            String date = DateUtils.formatDate_Y_M_D_WEEK_New(context,
                    event.getYear(),
                    event.getMonth()-1,
                    event.getDay(),
                    event.getWeekDay());

            //通知
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(event.getTitle() + ": " + date)
                    .setContentIntent(contentIntent)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            notificationManager.notify(event.getId(), builder.build());
        }
    }
}
