package autodriver.command;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.httpserver.util.ViewPreview;
import android.view.View;
import autodriver.util.TypeConvertUtil;
import ctrip.base.logical.component.CtripBaseApplication;

public class CommandScreenShot extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		Activity activity = CtripBaseApplication.getInstance().getCurrentActivity();
		View rootView = activity.getWindow().getDecorView();
		Bitmap bitmap = ViewPreview.convertViewToBitmap(rootView);
		if (bitmap != null) {
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
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
		} else {
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
		return null;
	}

}
