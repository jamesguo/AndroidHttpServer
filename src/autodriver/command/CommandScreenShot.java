package autodriver.command;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.httpserver.util.ViewPreview;
import android.view.View;
import autodriver.core.AutoDriver;
import autodriver.util.TypeConvertUtil;

public class CommandScreenShot extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		View rootView = AutoDriver.getRootView();
		if (rootView == null) {
			AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
			actionProtocol.actionCode = request.actionCode;
			actionProtocol.SeqNo = request.SeqNo;
			actionProtocol.result = (byte) 1;
			JSONObject res = new JSONObject();
			try {
				res.put("errorinfo", "can not take screenshot");
				actionProtocol.json = res;
				return actionProtocol;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			Bitmap bitmap = ViewPreview.convertViewToBitmap(rootView);
			if (bitmap != null) {
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 60, arrayOutputStream);
				byte[] bytearray = arrayOutputStream.toByteArray();
				bitmap.recycle();
				bitmap = null;
				String image = TypeConvertUtil.bytesToHexString(bytearray);
				AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
				actionProtocol.actionCode = request.actionCode;
				actionProtocol.SeqNo = request.SeqNo;
				actionProtocol.result = (byte) 0;
				JSONObject res = new JSONObject();
				try {
					res.put("value", "success");
					res.put("ImageData", image);
					actionProtocol.json = res;
					return actionProtocol;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
		actionProtocol.actionCode = request.actionCode;
		actionProtocol.SeqNo = request.SeqNo;
		actionProtocol.result = (byte) 0;
		JSONObject res = new JSONObject();
		try {
			res.put("errorinfo", "can not take screenshot");
			actionProtocol.json = res;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return actionProtocol;
	}

}
