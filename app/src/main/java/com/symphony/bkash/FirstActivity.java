package com.symphony.bkash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;

import com.symphony.bkash.data.model.PostInfo;
import com.symphony.bkash.data.remote.TokenDataApiService;
import com.symphony.bkash.data.remote.TokenDataApiUtils;
import com.symphony.bkash.receiver.ShowNotificationJob;
import com.symphony.bkash.receiver.VersionChecker;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class FirstActivity extends BaseActivity {

    public String packagename = "com.bKash.customerapp";
    public String link = "https://play.google.com/store/";
    public static String[] permisionList = { "android.permission.READ_PHONE_STATE","android.permission.READ_CONTACTS"}; //,"android.permission.READ_CONTACTS"
    public static final int permsRequestCode = 20;
    public Intent serviceIntent;
    String mLatestVersionName, installVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        FirstActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);
        VersionChecker versionChecker = new VersionChecker();
        try {
            mLatestVersionName = versionChecker.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowNotificationJob.schedulePeriodic();
        String mac = "00:00:00:00:00";
        String activated = "0";
        String model = "Symphony";
        PackageManager pm = getApplicationContext().getPackageManager();

        Intent intent = new Intent(getApplicationContext(), NewsWebActivity.class);
        Log.d("URLPush", link);
        //packagename = packagename;

        model = getSystemProperty("ro.product.device");

        if(model == null || model.isEmpty()){
            model = getSystemProperty("ro.build.product");
        }

        activated = String.valueOf(isPackageInstalled(packagename,pm));

        TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(FirstActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String getSimSerialNumber = telemamanger.getSimSerialNumber();
            //String getSimSerialNumber = String.valueOf(telemamanger.) ;
            String getSimNumber = telemamanger.getLine1Number();
            String imsi = telemamanger.getSubscriberId();
            String operator = telemamanger.getNetworkOperatorName();
            String imei1 = telemamanger.getImei(0);
            String imei2 = telemamanger.getImei(1);
            mac = getMAC();
            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
//            Log.d("IMSI", imsi);
//            Log.d("SIM1", getSimSerialNumber);
//            Log.d("SIM2", getSimNumber);
            Intent serviceIntent = new Intent(this, UploaderService.class);
            // add infos for the service which file to download and where to store
            serviceIntent.putExtra(UploaderService.IMEI1, imei1);
            serviceIntent.putExtra(UploaderService.IMEI2, imei2);
            serviceIntent.putExtra(UploaderService.SIM_Number, getSimSerialNumber);
            serviceIntent.putExtra(UploaderService.MAC, mac);
            serviceIntent.putExtra(UploaderService.ANDROID_ID, android_id);
            serviceIntent.putExtra(UploaderService.Activated, activated);
            serviceIntent.putExtra(UploaderService.Model, "Symphony "+model);
            startService(serviceIntent);
        }

        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);



        if(activated.equals("1")){
            intent = getPackageManager().getLaunchIntentForPackage(packagename);

        }

        else{
            if (link.contains("https://play.google.com/store/")) {
                Log.d("URLPushDetect", "Not installed");
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("market://details?id=" + packagename));
                // startActivity(intent);
            } else{
                Log.d("URLPushDetect", "Normal");
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("targetUrl", link);
                intent.putExtra("SYSTRAY", "systray");


                //startActivity(intent);

            }
        }

        if(ContextCompat.checkSelfPermission(FirstActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            //startActivity(intent);
        }
    }

    private String getMAC(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }


    public String getSystemProperty(String key) {
        String value = null;

        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    private int isPackageInstalled(String packageName, PackageManager packageManager) {

        int found = 1;

        try {

            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = 0;
        }

        return found;
    }

}


