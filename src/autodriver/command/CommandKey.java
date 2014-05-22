package autodriver.command;

import org.json.JSONObject;

import android.app.Instrumentation;
import android.httpserver.util.ViewScanner;
import android.view.View;
import android.widget.TextView;
import autodriver.util.KeyboardMapper;

public class CommandKey extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		try {
			JSONObject params = request.params();
			long elementId = params.optLong("elementId", 0);
			String text = params.optString("text", "");
			View view = ViewScanner.findViewByID(elementId);
			if (view instanceof TextView) {
				((TextView) view).setText(text);
			} else {
				int[] location = new int[2];
				view.getLocationOnScreen(location);
				CommandClick.tapPosition(location[0] + view.getMeasuredWidth() / 2, location[1] + view.getMeasuredHeight() / 2);
				int length = text.length();
				for (int i = 0; i < length; i++) {
					char a = text.charAt(i);
					int key = KeyboardMapper.getPrimaryKeyCode(a);
					if (key != 0) {
						tapKey(key);
					}
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
			// TODO: handle exception
		}
		return null;
	}

	public static void tapKey(int key) {
		Instrumentation m_Instrumentation = new Instrumentation();
		m_Instrumentation.sendKeyDownUpSync(key);
	}
}
