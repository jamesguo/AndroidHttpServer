package autodriver.command;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import autodriver.command.CommandFind.FindType;
import autodriver.util.TypeConvertUtil;

public class CommandSee extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {

		try {
			JSONObject params = request.params();
			int findType = params.optInt("findType", FindType.NAME.index);
			int timeout = params.optInt("timeout", 5);
			String value = TypeConvertUtil.getSimpleStr(params.optString("value", ""));
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

}
