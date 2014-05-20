package fi.iki.elonen;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.androidhttpserver.BaseApplication;

public class ViewScanner {
	public static String hierachySnapshot() {
		Activity activity = BaseApplication.getApplicationInstance().getCurrentActivity();
		View rootView = activity.getWindow().getDecorView();
		try {
			JSONArray windowObjects = new JSONArray();
			JSONObject windowObject = scanView(rootView);
			windowObject.put("preview", "/preview?id=" + activity.getWindow().hashCode());
			windowObjects.put(windowObject);
			JSONObject response = new JSONObject();
			response.put("windows", windowObject);
			response.put("screen_w", rootView.getMeasuredWidth());
			response.put("screen_h", rootView.getMinimumHeight());
			response.put("version", "1.0");
			return response.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
	private static JSONObject scanView(View rootView) {
		if (rootView != null) {
			JSONObject jsonObject = new JSONObject();
			try {
				Class<?> viewclass = rootView.getClass();
				String classname = viewclass.getSimpleName();
				jsonObject.put("class", ":" + classname);
				jsonObject.put("layer_bounds_x", 0);
				jsonObject.put("layer_bounds_y", 0);
				jsonObject.put("layer_bounds_w", rootView.getMeasuredWidth());
				jsonObject.put("layer_bounds_h", rootView.getMeasuredHeight());
				jsonObject.put("layer_position_x", "" + rootView.getTop());
				jsonObject.put("layer_position_y", "" + rootView.getLeft());
				jsonObject.put("layer_anchor_x", "" + 0.5);
				jsonObject.put("layer_anchor_y", "" + 0.5);
				JSONArray fieldArray = new JSONArray();
				Field[] fields = viewclass.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					Object value = field.get(rootView);
					JSONObject fieldDescription = new JSONObject();
					fieldDescription.put("name", field.getName());
					if(value instanceof Float){
						fieldDescription.put("type", "float");
						fieldDescription.put("value", value);
					}else if(value instanceof Double){
						fieldDescription.put("type", "double");
						fieldDescription.put("value", value);
					}else if(value instanceof Integer){
						fieldDescription.put("type", "int");
						fieldDescription.put("value", value);
					}else if(value instanceof Long){
						fieldDescription.put("type", "long");
						fieldDescription.put("value", value);
					}else if(value instanceof Boolean){
						fieldDescription.put("type", "BOOL");
						if ((Boolean) value) {
							fieldDescription.put("value", "YES");
						} else {
							fieldDescription.put("value", "NO");
						}
					} else if (value instanceof String) {
						fieldDescription.put("type", "char");
						fieldDescription.put("value", value);
					} else {
						fieldDescription.put("type", value.getClass().getSimpleName());
						fieldDescription.put("value", "Object:" + value);
					}
					fieldArray.put(fieldDescription);
				}
				JSONObject viewArgDescription = new JSONObject();
				viewArgDescription.put("name", classname);
				viewArgDescription.put("props", fieldArray);
				JSONArray viewDescriptionArray = new JSONArray();
				viewDescriptionArray.put(viewArgDescription);

				jsonObject.put("props", viewDescriptionArray);

				JSONArray childViewsArray = new JSONArray();
				if (rootView instanceof ViewGroup) {
					int count = ((ViewGroup) rootView).getChildCount();
					for(int i=0;i<count;i++){
						try {
							View childView = ((ViewGroup) rootView).getChildAt(i);
							JSONObject object = scanView(childView);
							childViewsArray.put(0, object);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				jsonObject.put("views", childViewsArray);

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject;
		}
		return null;
	}
}
