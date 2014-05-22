package autodriver.command;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidActionProtocol {
	public static int elementID;
	public int actionCode = AndroidActionType.NULL;
	public int SeqNo = 0;
	/**
	 * 表示业务成功/失败。 0: 业务成功。Response字段返回业务response报文。
	 * 
	 * 1: 业务失败。Response字段返回一个错误的Response报文。
	 */
	public byte result = 0;
	public String body = "";
	public JSONObject json;

	public AndroidActionProtocol() {

	}

	public JSONObject params() throws JSONException {
		final JSONObject paramsObj = json.getJSONObject("params");
		return paramsObj;
	}

	public boolean isElementCommand() {
		try {
			return json.getString("action").startsWith("element:");
		} catch (final JSONException e) {
			return false;
		}
	}

}
