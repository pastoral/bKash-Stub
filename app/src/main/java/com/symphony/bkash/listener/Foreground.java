package com.symphony.bkash.listener;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.evernote.android.job.JobManager;
import com.symphony.bkash.receiver.DemoJobCreator;

/**
 * Created by monir.sobuj on 12/26/2018.
 */

public class Foreground implements Application.ActivityLifecycleCallbacks {

    private static Foreground instance;
    //Application app;

    public static void init(Application app){
        if (instance == null){
            instance = new Foreground();
            app.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static Foreground get(){
        return instance;
    }

    private Foreground(){}

    private boolean foreground;

    public boolean isForeground(){
        return foreground;
    }

    public boolean isBackground(){
        return !foreground;
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        JobManager.create(activity.getApplication()).addJobCreator(new DemoJobCreator());
        foreground = true;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        foreground = true;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        foreground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        foreground = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
