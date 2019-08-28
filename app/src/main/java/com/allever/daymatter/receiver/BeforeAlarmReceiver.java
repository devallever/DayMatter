package com.allever.daymatter.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.allever.daymatter.R;
import com.allever.daymatter.data.DataListener;
import com.allever.daymatter.data.Event;
import com.allever.daymatter.data.Repository;
import com.allever.daymatter.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Mac on 2018/3/7.
 */

public class BeforeAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "BeforeAlarmReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        Calendar c = Calendar.getInstance();

        //当前日历加1天
        c.add(Calendar.DAY_OF_MONTH, 1);

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
                    .setSmallIcon(R.drawable.ic_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(event.getTitle() + ": " + date)
                    .setContentIntent(contentIntent)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            notificationManager.notify(event.getId(), builder.build());
        }
    }
}
