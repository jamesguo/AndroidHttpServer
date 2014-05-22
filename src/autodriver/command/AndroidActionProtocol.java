package autodriver.command;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidActionProtocol {
	public static int elementID;
	public int actionCode = AndroidActionType.NULL;
	public int SeqNo = 0;
	/**
	 * ��ʾҵ��ɹ�/ʧ�ܡ� 0: ҵ��ɹ���Response�ֶη���ҵ��response���ġ�
	 * 
	 * 1: ҵ��ʧ�ܡ�Response�ֶη���һ�������Response���ġ�
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
