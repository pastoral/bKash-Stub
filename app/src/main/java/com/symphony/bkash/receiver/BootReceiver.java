package com.symphony.bkash.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.symphony.bkash.FirstActivity;
import com.symphony.bkash.NewsWebActivity;
import com.symphony.bkash.UploaderService;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by monir.sobuj on 12/23/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    public String packagename = "com.bKash.customerapp";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Boot", "onReceive: BootReceiver");
        Toast.makeText(context, "OnReceive", Toast.LENGTH_LONG).show();
        String mac = "00:00:00:00:00";
        String activated = "0";
        String model = "Symphony";
        PackageManager pm = context.getPackageManager();

        model = getSystemProperty("ro.product.device");

        if(model == null || model.isEmpty()){
            model = getSystemProperty("ro.build.product");
        }

        activated = String.valueOf(isPackageInstalled(packagename,pm));

        TelephonyManager telemamanger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String getSimSerialNumber = telemamanger.getSimSerialNumber();
            //String getSimSerialNumber = String.valueOf(telemamanger.) ;
            String getSimNumber = telemamanger.getLine1Number();
            String imsi = telemamanger.getSubscriberId();
            String operator = telemamanger.getNetworkOperatorName();
            String imei1 = telemamanger.getImei(0);
            String imei2 = telemamanger.getImei(1);
            mac = getMAC();
            String android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
//            Log.d("IMSI", imsi);
//            Log.d("SIM1", getSimSerialNumber);
//            Log.d("SIM2", getSimNumber);
            Intent serviceIntent = new Intent(context, UploaderService.class);
            // add infos for the service which file to download and where to store
            serviceIntent.putExtra(UploaderService.IMEI1, imei1);
            serviceIntent.putExtra(UploaderService.IMEI2, imei2);
            serviceIntent.putExtra(UploaderService.SIM_Number, getSimSerialNumber);
            serviceIntent.putExtra(UploaderService.MAC, mac);
            serviceIntent.putExtra(UploaderService.ANDROID_ID, android_id);
            serviceIntent.putExtra(UploaderService.Activated, activated);
            serviceIntent.putExtra(UploaderService.Model, "Symphony "+model);
            context.startService(serviceIntent);
        }
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
}
