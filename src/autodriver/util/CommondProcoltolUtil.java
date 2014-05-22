package autodriver.util;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import autodriver.command.AndroidActionProtocol;

public class CommondProcoltolUtil {

	public static AndroidActionProtocol buileActionCommand(byte[] beanByte) {
		// byte[] head = new byte[9];
		// byte[] body = new byte[beanByte.length - head.length];
		// System.arraycopy(beanByte, 0, head, 0, head.length);
		// System.arraycopy(beanByte, head.length, body, 0, body.length);
		// AndroidActionCommand bean = new AndroidActionCommand();
		// byte[] seqNo = new byte[4];
		// System.arraycopy(beanByte, 0, seqNo, 0, seqNo.length);
		// bean.actionCode = TypeConvertUtil.bytesToInt(seqNo);
		// bean.result = head[4];
		// seqNo = new byte[4];
		// System.arraycopy(beanByte, 5, seqNo, 0, seqNo.length);
		// bean.SeqNo = TypeConvertUtil.bytesToInt(seqNo);
		// try {
		// bean.body = new String(body, "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		AndroidActionProtocol bean = new AndroidActionProtocol();
		try {
			String message = new String(beanByte, "UTF-8");
			// if (beanByte.length < 20000) {
				JSONObject jsonObject = new JSONObject(message);
				bean.actionCode = jsonObject.optInt("actionCode");
				bean.result = (byte) jsonObject.optInt("result");
				bean.SeqNo = jsonObject.optInt("seqNo");
				bean.body = jsonObject.optString("body");
			// } else {
			// bean.actionCode = AndroidActionCommandType.SCREENSHOT;
			// bean.result = (byte) 0;
			// bean.SeqNo = 0;
			// bean.body = message;
			// }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}

	public static byte[] buileResponse(AndroidActionProtocol responseBean) {
		try {
			byte[] pre = new byte[4];
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("actionCode", responseBean.actionCode);
			jsonObject.put("result", responseBean.result);
			jsonObject.put("seqNo", responseBean.SeqNo);
			if (responseBean.json != null) {
				responseBean.body = responseBean.json.toString();
			}
			jsonObject.put("body", responseBean.body);
			byte[] bodyByte = new byte[0];
			bodyByte = jsonObject.toString().getBytes("UTF-8");
			byte[] length = TypeConvertUtil.intToByte(bodyByte.length);
			System.arraycopy(length, 0, pre, 0, length.length);
			byte[] request = TypeConvertUtil.combineByteArr(pre, bodyByte);
			return request;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] buileHead(int actionCode, byte result, int seqNo) {
		byte[] head = new byte[9];
		byte[] seq = new byte[4];
		seq = TypeConvertUtil.intToByte(actionCode);
		System.arraycopy(seq, 0, head, 0, seq.length);
		head[4] = result;
		seq = new byte[4];
		seq = TypeConvertUtil.intToByte(seqNo);
		System.arraycopy(seq, 0, head, 5, seq.length);
		return head;
	}
}
