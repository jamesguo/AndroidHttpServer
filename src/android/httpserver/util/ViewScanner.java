package android.httpserver.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import ctrip.base.logical.component.CtripBaseApplication;

public class ViewScanner {
	public static String hierachySnapshot() {
		Activity activity = CtripBaseApplication.getInstance().getCurrentActivity();
		View rootView = activity.getWindow().getDecorView();
		try {
			JSONArray windowObjects = new JSONArray();
			JSONObject windowObject = scanView(rootView);
			windowObject.put("preview", "/preview?id=" + rootView.hashCode());
			windowObjects.put(windowObject);
			JSONObject response = new JSONObject();
			response.put("windows", windowObjects);
			response.put("screen_w", rootView.getMeasuredWidth() / 2);
			response.put("screen_h", rootView.getMeasuredHeight() / 2);
			response.put("version", "1.0");
			return response.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	private static View recursiverForView(long id, View rootView) {
		if (rootView.hashCode() == id) {
			return rootView;
		}
		if (rootView instanceof ViewGroup) {
			int count = ((ViewGroup) rootView).getChildCount();
			for (int i = 0; i < count; i++) {
				try {
					View childView = ((ViewGroup) rootView).getChildAt(i);
					View result = recursiverForView(id, childView);
					if (result != null) {
						return result;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return null;
	}

	public static View findViewByID(long id) {
		Activity activity = CtripBaseApplication.getInstance().getCurrentActivity();
		View rootView = activity.getWindow().getDecorView();
		return recursiverForView(id, rootView);
	}

	public static Class<?> getAllFields(Object object, Class<?> _class, ArrayList<Field> result) {
		if (_class == null)
			return null;
		else {
			Field[] fields = _class.getDeclaredFields();
			ArrayList<Field> currentFields = new ArrayList<Field>(Arrays.asList(fields));
			result.addAll(currentFields);
			return getAllFields(object, _class.getSuperclass(), result);
		}
	}

	private static JSONObject scanView(View rootView) {
		if (rootView != null) {
			JSONObject jsonObject = new JSONObject();
			try {
				Class<?> viewclass = rootView.getClass();
				String classname = viewclass.getSimpleName();
				int[] location = new int[2];
				rootView.getLocationOnScreen(location);
				jsonObject.put("class", ":" + classname);
				jsonObject.put("layer_screen_x", location[0] / 2);
				jsonObject.put("layer_screen_y", location[1] / 2);
				jsonObject.put("layer_bounds_w", rootView.getMeasuredWidth() / 2);
				jsonObject.put("layer_bounds_h", rootView.getMeasuredHeight() / 2);
				jsonObject.put("id", "" + rootView.hashCode());
				JSONArray fieldArray = new JSONArray();
				ArrayList<Field> result = new ArrayList<Field>();
				getAllFields(rootView, viewclass, result);
				for (Field field : result) {
					field.setAccessible(true);
					Object value = field.get(rootView);
					JSONObject fieldDescription = new JSONObject();
					fieldDescription.put("name", field.getName());
					if (value instanceof Float) {
						fieldDescription.put("type", "float");
						fieldDescription.put("value", value);
					} else if (value instanceof Double) {
						fieldDescription.put("type", "double");
						fieldDescription.put("value", value);
					} else if (value instanceof Integer) {
						fieldDescription.put("type", "int");
						fieldDescription.put("value", value);
					} else if (value instanceof Long) {
						fieldDescription.put("type", "long");
						fieldDescription.put("value", value);
					} else if (value instanceof Boolean) {
						fieldDescription.put("type", "BOOL");
						if ((Boolean) value) {
							fieldDescription.put("value", "YES");
						} else {
							fieldDescription.put("value", "NO");
						}
					} else if (value instanceof CharSequence) {
						fieldDescription.put("type", "String");
						fieldDescription.put("value", value);
					} else {
						if (value != null) {
							fieldDescription.put("type", value.getClass().getSimpleName());
							fieldDescription.put("value", "Object:" + value);
						} else {
							fieldDescription.put("type", field.getType().getName());
							fieldDescription.put("value", "Object:" + value);
						}
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
					for (int i = 0; i < count; i++) {
						try {
							View childView = ((ViewGroup) rootView).getChildAt(i);
							JSONObject object = scanView(childView);
							childViewsArray.put(object);
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
