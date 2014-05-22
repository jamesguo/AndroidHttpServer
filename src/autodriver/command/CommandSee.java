package autodriver.command;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import autodriver.command.CommandFind.FindType;

public class CommandSee extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {

		try {
			JSONObject params = request.params();
			int findType = params.optInt("findType", FindType.NAME.index);
			int timeout = params.optInt("timeout", 3);
			String value = params.optString("value", "");
			JSONArray reuslt = CommandFind.findViews(findType, value, timeout);
			AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
			actionProtocol.actionCode = request.actionCode;
			actionProtocol.SeqNo = request.SeqNo;
			if (reuslt.length() > 0) {
				actionProtocol.result = (byte) 0;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("elements", reuslt.toString());
				actionProtocol.json = jsonObject;
			} else {
				actionProtocol.result = (byte) 1;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("errorinfo", "can not find " + value);
				actionProtocol.json = jsonObject;
			}
			return actionProtocol;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
