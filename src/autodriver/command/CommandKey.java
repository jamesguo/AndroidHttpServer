package autodriver.command;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation;
import android.content.Context;
import android.httpserver.util.ViewScanner;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import autodriver.util.KeyboardMapper;
import ctrip.base.logical.component.CtripBaseApplication;

public class CommandKey extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		try {
			JSONObject params = request.params();
			long elementId = params.optLong("elementId", 0);
			final String text = params.optString("text", "");
			final View view = ViewScanner.findViewByID(elementId);
			if (view instanceof TextView) {
				Handler handler = new Handler(Looper.getMainLooper());
				handler.post(new Runnable() {

					@Override
					public void run() {
						((TextView) view).setText(text);
						InputMethodManager imm = (InputMethodManager) CtripBaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
				});
			} else {
				int[] location = new int[2];
				view.getLocationOnScreen(location);
				CommandClick.tapPosition(location[0] + view.getMeasuredWidth() / 2, location[1] + view.getMeasuredHeight() / 2);
				final View focusView = CtripBaseApplication.getInstance().getCurrentActivity().getCurrentFocus();
				if (focusView != null && focusView instanceof TextView) {
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {

						@Override
						public void run() {
							((TextView) focusView).setText(text);
						}
					});
				} else {
					int length = text.length();
					for (int i = 0; i < length; i++) {
						char a = text.charAt(i);
						int key = KeyboardMapper.getPrimaryKeyCode(a);
						if (key != 0) {
							tapKey(key);
						}
					}
				}
				if (focusView != null) {
					InputMethodManager imm = (InputMethodManager) CtripBaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
				}
			}

			AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
			actionProtocol.actionCode = request.actionCode;
			actionProtocol.SeqNo = request.SeqNo;
			actionProtocol.result = (byte) 0;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", "success");
			actionProtocol.json = jsonObject;
			return actionProtocol;
		} catch (Exception e) {
			AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
			actionProtocol.actionCode = request.actionCode;
			actionProtocol.SeqNo = request.SeqNo;
			actionProtocol.result = (byte) 1;
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("value", "fail");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			actionProtocol.json = jsonObject;
			return actionProtocol;
		}
	}

	public static void tapKey(int key) {
		Instrumentation m_Instrumentation = new Instrumentation();
		m_Instrumentation.sendKeyDownUpSync(key);
	}
}
