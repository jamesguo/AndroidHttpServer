package autodriver.command;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation;
import android.httpserver.util.ViewScanner;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import autodriver.util.BlockDelayUtil;

public class CommandClick extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		try {
			JSONObject params = request.params();
			long elementId = params.optLong("elementId", 0);
			View view = ViewScanner.findViewByID(elementId);
			if (view != null) {
				int[] location = new int[2];
				view.getLocationOnScreen(location);
				tapPosition(location[0] + view.getMeasuredWidth() / 2, location[1] + view.getMeasuredHeight() / 2);
				BlockDelayUtil.waitforTime(1);

				AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
				actionProtocol.actionCode = request.actionCode;
				actionProtocol.SeqNo = request.SeqNo;
				actionProtocol.result = (byte) 0;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("value", "success");
				actionProtocol.json = jsonObject;
				return actionProtocol;
			} else {
				AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
				actionProtocol.actionCode = request.actionCode;
				actionProtocol.SeqNo = request.SeqNo;
				actionProtocol.result = (byte) 1;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("errorinfo", "can not find " + elementId);
				actionProtocol.json = jsonObject;
				return actionProtocol;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
		actionProtocol.actionCode = request.actionCode;
		actionProtocol.SeqNo = request.SeqNo;
		actionProtocol.result = (byte) 1;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("errorinfo", "can not find");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actionProtocol.json = jsonObject;
		return actionProtocol;
	}

	public static void tapPosition(float x, float y) {
		Instrumentation m_Instrumentation = new Instrumentation();
		m_Instrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
		m_Instrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
	}
}
