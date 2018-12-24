package com.symphony.bkash;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.symphony.bkash.listener.AppUpdateListener;
import com.symphony.bkash.receiver.ShowNotificationJob;
import com.symphony.bkash.receiver.VersionChecker;
import com.symphony.bkash.util.ConnectionUtils;

import java.util.concurrent.ExecutionException;

public class FirstActivity extends BaseActivity implements AppUpdateListener {

    public String packagename = "com.bKash.customerapp";
    public String link = "https://play.google.com/store/";
    public static String[] permisionList = { "android.permission.READ_PHONE_STATE","android.permission.READ_CONTACTS"}; //,"android.permission.READ_CONTACTS"
    public static final int permsRequestCode = 20;
    public static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        FirstActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);
        if(ConnectionUtils.isNetworkConnected(this)){
            ShowNotificationJob.schedulePeriodic();

            PackageManager pm = getApplicationContext().getPackageManager();
            if(ConnectionUtils.isPackageInstalled(packagename, pm) == 1){
                new VersionChecker(this, packagename, this);
                Intent i = getPackageManager().getLaunchIntentForPackage(packagename);
                startActivity(i);
            } else{

                gotoPlay();
            }
        } else {
            displayNoInternetDialog(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void displayNoInternetDialog(final Activity activity){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setCancelable(false);
        alert.setTitle(getString(R.string.no_connection_title));
        alert.setMessage(getString(R.string.no_connection_msg));
        alert.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                activity.finish();

            }
        });
        alert.show();
    }

    public void gotoPlay(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (link.contains("https://play.google.com/store/")) {
            Log.d("URLPushDetect", "Not installed");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packagename));
            startActivity(intent);
        } else{
            Log.d("URLPushDetect", "Normal");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("targetUrl", link);
            intent.putExtra("SYSTRAY", "systray");
            startActivity(intent);

        }
    }

    @Override
    public void onUpdate() {
        gotoPlay();
    }
}


