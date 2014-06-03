package autodriver.command;

import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.DisplayMetrics;
import ctrip.base.logical.component.CtripBaseApplication;

public class CommandDeviceInfo extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		DisplayMetrics displayMetrics = CtripBaseApplication.getInstance().getResources().getDisplayMetrics();
		AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
		actionProtocol.actionCode = request.actionCode;
		actionProtocol.SeqNo = request.SeqNo;
		actionProtocol.result = (byte) 0;
		JSONObject res = new JSONObject();
		try {
			res.put("height", displayMetrics.heightPixels);
			res.put("width", displayMetrics.widthPixels);
			res.put("OS", "Android");
			res.put("Version", "" + Build.VERSION.SDK_INT);
			res.put("NAME", Build.MODEL + "-" + android.os.Build.ID);
			String serial = null;
			try {
				Class<?> c = Class.forName("android.os.SystemProperties");
				Method get = c.getMethod("get", String.class);
				serial = (String) get.invoke(c, "ro.serialno");
				System.out.println(serial);
				res.put("NAME", Build.MODEL + "-" + serial);
			} catch (Exception ignored) {

			}
			res.put("MODEL", Build.MODEL);
			res.put("BOARD", Build.BOARD);
			res.put("MANUFACTURER", Build.MANUFACTURER);
			actionProtocol.json = res;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return actionProtocol;
	}

}
