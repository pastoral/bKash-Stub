package com.symphony.bkash;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import com.symphony.bkash.data.model.BkashResponse;
import com.symphony.bkash.data.model.PostInfo;
import com.symphony.bkash.data.model.PostUserInfo;
import com.symphony.bkash.data.model.UpdateUserInfo;
import com.symphony.bkash.data.remote.TokenDataApiService;
import com.symphony.bkash.data.remote.TokenDataApiUtils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploaderService extends IntentService {
    private int result = Activity.RESULT_CANCELED;
    public static final String IMEI1 = "123335746891";
    public static final String IMEI2 = "1234567891";
    public static final String MAC = "12 we 45 bn 56jg 56 hg";
    public static final String SIM_Number = "23382346";
    public static final String Model = "result";
    public static final String HANDSET = "Symphony i120";
    public static final String Activated = "0";
    public static final String ANDROID_ID = "0898754";
    public static final String NOTIFICATION = "com.symphony.bkash.receiver";
    private String imei1,imei2,mac,sim,model,handset,activated,android_id;
    private TokenDataApiService tokenDataAPIService = TokenDataApiUtils.getUserDataAPIServices();
    SharedPreferences.Editor editor;

    public UploaderService(){
        super("UploaderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        imei1 = intent.getStringExtra(IMEI1);
        imei2 = intent.getStringExtra(IMEI2);
        mac = intent.getStringExtra(MAC);
        sim = intent.getStringExtra(SIM_Number);
        model = intent.getStringExtra(Model);
        activated = intent.getStringExtra(Activated);
        android_id = intent.getStringExtra(ANDROID_ID);
        String notification = intent.getStringExtra(NOTIFICATION);
        handset = intent.getStringExtra(HANDSET);
        SharedPreferences prefs = getSharedPreferences("PREF_BKASH_INSTALLER", MODE_PRIVATE);
        long info_id = prefs.getLong("info_id", 0);
        if(info_id == 0) {
            //postJob();
            sendInfo(imei1, imei2, mac, android_id, sim, activated, model);
        } else {
            updateInfo(info_id, imei1, imei2, mac, android_id, sim, activated, model);
        }
        editor = getSharedPreferences("PREF_BKASH_INSTALLER", MODE_PRIVATE).edit();
        editor.putString(IMEI1, imei1);
        editor.putString(IMEI2, imei2);
//        editor.putString(MAC, mac);
        editor.putString(SIM_Number, sim);
//        editor.putString(MODEL, model);
//        editor.putString(NOTIFICATION, notification);
//        editor.putString(HANDSET, handset);
        //String restoredText = prefs.getString("text", null);

        Log.d("IMEI1 ", prefs.getString(IMEI1, "No IMEI1"));
        Log.d("IMEI2 ", prefs.getString(IMEI2, "No IMEI2"));
        Log.d("IMEI1 ", prefs.getString(SIM_Number, "No SIM"));
    }

    public void sendInfo(String imei1,String imei2,String mac, String android_id, String sim, String activated, String model){
        tokenDataAPIService.saveInfo(imei1, imei2, mac,android_id,sim,activated,model).enqueue(new Callback<PostUserInfo>() {
            @Override
            public void onResponse(Call<PostUserInfo> call, Response<PostUserInfo> response) {
                if(response.body().getCode() == 200) {
                    editor.putLong("info_id", response.body().getStatus());
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<PostUserInfo> call, Throwable t) {
                Log.e("POST_STATUS", "Unable to submit post to API.");
            }
        });
    }

    public void updateInfo(long id, String imei1,String imei2,String mac, String android_id, String sim, String activated, String model){
        PostInfo postInfo = new PostInfo(imei1,imei2,mac,android_id,sim,activated,model);
        tokenDataAPIService.updateInfo(id, postInfo).enqueue(new Callback<BkashResponse>() {
            @Override
            public void onResponse(Call<BkashResponse> call, Response<BkashResponse> response) {

                if(response.body().getCode().equals("200")){
                    Log.i("POST_STATUS", "post submitted to API." + response.body().getStatus());
                }
                if(response.isSuccessful()) {
                    // showResponse(response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<BkashResponse> call, Throwable t) {
                Log.e("POST_STATUS", "Unable to submit post to API.");
            }
        });
    }
}
