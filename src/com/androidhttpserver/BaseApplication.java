package com.androidhttpserver;

import android.app.Activity;
import android.app.Application;

public class BaseApplication extends Application {
	Activity currentActivity;
	static BaseApplication instance;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
	}
	public static BaseApplication getApplicationInstance() {
		return instance;
	}
	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity activity) {
		this.currentActivity = activity;
	}
}
