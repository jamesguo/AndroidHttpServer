package ctrip.base.logical.component;

import android.app.Activity;
import android.app.Application;

public class CtripBaseApplication extends Application {

	private Activity currentActivity;
	private static CtripBaseApplication sAppInstance;

	public static CtripBaseApplication getInstance() {
		return sAppInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CtripBaseApplication.sAppInstance = this;
	}

	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}
	public Activity getCurrentActivity() {
		return currentActivity;
	}

}