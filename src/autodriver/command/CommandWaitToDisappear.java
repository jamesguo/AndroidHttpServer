package autodriver.command;

import org.json.JSONObject;

import android.httpserver.util.ViewScanner;
import android.view.View;
import autodriver.util.BlockDelayUtil;
import autodriver.util.ExecuteBlock;

public class CommandWaitToDisappear extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		try {
			JSONObject params = request.params();
			long elementId = params.optLong("elementId", 0);
			View view = ViewScanner.findViewByID(elementId);
			if (view != null && view.getVisibility() == View.VISIBLE) {
				BlockDelayUtil.runBlock(new ExecuteBlock() {

					@Override
					public int excute(Object... objects) {
						View view = ViewScanner.findViewByID((Long) objects[0]);
						return (view == null || view.getVisibility() != View.VISIBLE) ? StepResultSuccess : StepResultWait;
					}
				}, 30, elementId);
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

}
