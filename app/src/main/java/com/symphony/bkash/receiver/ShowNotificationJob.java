package com.symphony.bkash.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.symphony.bkash.FirstActivity;
import com.symphony.bkash.NewsWebActivity;
import com.symphony.bkash.R;
import com.symphony.bkash.UploaderService;
import com.symphony.bkash.util.ConnectionUtils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by monir.sobuj on 12/3/2018.
 */

public class ShowNotificationJob extends Job {
    static final String TAG = "show_notification_job_tag";
    public String packagename = "com.bKash.customerapp";
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        /*PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), FirstActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(getContext())
                .setContentTitle("Android Job Demo")
                .setContentText("Notification from Android Job Demo App.")
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getContext())
                .notify(new Random().nextInt(), notification);*/


        String mac = "00:00:00:00:00";
        String activated = "0";
        String model = "Symphony";
        Context ctx = getContext();
        PackageManager pm = ctx.getPackageManager();
        Intent intent = new Intent(ctx, NewsWebActivity.class);
        model = ConnectionUtils.getSystemProperty("ro.product.device");
        if(model == null || model.isEmpty()){
            model = ConnectionUtils.getSystemProperty("ro.build.product");
        }
        activated = String.valueOf(ConnectionUtils.isPackageInstalled(packagename,pm));
        TelephonyManager telemamanger = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String getSimSerialNumber = telemamanger.getSimSerialNumber();
            String getSimNumber = telemamanger.getLine1Number();
            //String imsi = telemamanger.getSubscriberId();
            //String operator = telemamanger.getNetworkOperatorName();
            String imei1 = telemamanger.getImei(0);
            String imei2 = telemamanger.getImei(1);
            mac = ConnectionUtils.getMAC();
            String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Intent serviceIntent = new Intent(ctx, UploaderService.class);
            serviceIntent.putExtra(UploaderService.IMEI1, imei1);
            serviceIntent.putExtra(UploaderService.IMEI2, imei2);
            serviceIntent.putExtra(UploaderService.SIM_Number, getSimSerialNumber);
            serviceIntent.putExtra(UploaderService.MAC, mac);
            serviceIntent.putExtra(UploaderService.ANDROID_ID, android_id);
            serviceIntent.putExtra(UploaderService.Activated, activated);
            serviceIntent.putExtra(UploaderService.Model, "Symphony "+model);
            ctx.startService(serviceIntent);
        }

        return Result.SUCCESS;
    }


    public static void schedulePeriodic() {
        new JobRequest.Builder(ShowNotificationJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();
    }






}
