package autodriver.command;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.httpserver.util.ViewScanner;
import android.view.View;
import android.view.ViewGroup;
import autodriver.util.TypeConvertUtil;
import ctrip.base.logical.component.CtripBaseApplication;

public class CommandViewDump extends BaseCommand {

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		try {
			AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
			actionProtocol.actionCode = request.actionCode;
			actionProtocol.SeqNo = request.SeqNo;
			actionProtocol.result = (byte) 0;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", "success");
			jsonObject.put("windows", viewsDump().toString());
			actionProtocol.json = jsonObject;
			return actionProtocol;
		} catch (Exception e) {
			// TODO: handle exception
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

	public static JSONArray viewsDump() {
		JSONArray reslutList = new JSONArray();
		ArrayList<View> result = ViewScanner.getAllWindowViews();
		if (result != null) {
			for (View view : result) {
				JSONObject jsonObject = viewDump(view);
				if (jsonObject != null) {
					reslutList.put(jsonObject);
				}
			}
		}
		return reslutList;
	}

	private static JSONObject viewDump(View rootView) {
		if (rootView != null && rootView.getVisibility() != View.GONE) {
			JSONObject jsonObject = new JSONObject();
			try {
				Class<?> viewclass = rootView.getClass();
				String classname = viewclass.getSimpleName();
				// int[] location = new int[2];
				// rootView.getLocationOnScreen(location);
				String classNitName = ":" + classname;
				jsonObject.put("class", classNitName);
				jsonObject.put("id", "" + rootView.hashCode());
				ArrayList<Field> result = new ArrayList<Field>();
				ViewScanner.getAllFields(rootView, viewclass, result);
				for (Field field : result) {
					int modifie = field.getModifiers();
					if ((modifie & 8) != 0 || (modifie & 16) != 0) {
						continue;
					}
					field.setAccessible(true);
					Object value = field.get(rootView);
					// JSONObject fieldDescription = new JSONObject();
					// fieldDescription.put("name", field.getName());
					if (value instanceof Float) {
						continue;
						// fieldDescription.put("type", "float");
						// fieldDescription.put("value", value);
					} else if (value instanceof Double) {
						continue;
						// fieldDescription.put("type", "double");
						// fieldDescription.put("value", value);
					} else if (value instanceof Integer) {
						if (field.getName().equals("mBackgroundResource") && (Integer) value > 0) {
							String name = CtripBaseApplication.getInstance().getResources().getResourceName((Integer) value);
							name = name.substring(name.lastIndexOf("/") + 1);
							// fieldDescription.put("type", "String");
							// fieldDescription.put("value", name);
							classNitName = "|" + name + classNitName;
							jsonObject.put("class", classNitName);
						} else if (field.getName().equals("mID") && (Integer) value > 0) {
							try {
								String name = CtripBaseApplication.getInstance().getResources().getResourceName((Integer) value);
								name = name.substring(name.lastIndexOf("/") + 1);
								classNitName = "|" + name + classNitName;
								jsonObject.put("class", classNitName);
								// fieldDescription.put("type", "String");
								// fieldDescription.put("value", name);
							} catch (Exception e) {
								continue;
								// fieldDescription.put("type", "int");
								// fieldDescription.put("value", value);
							}
						} else {
							continue;
							// fieldDescription.put("type", "int");
							// fieldDescription.put("value", value);
						}
					} else if (value instanceof Long) {
						continue;
						// fieldDescription.put("type", "long");
						// fieldDescription.put("value", value);
					} else if (value instanceof Boolean) {
						continue;
						// fieldDescription.put("type", "BOOL");
						// if ((Boolean) value) {
						// fieldDescription.put("value", "YES");
						// } else {
						// fieldDescription.put("value", "NO");
						// }
					} else if (value instanceof CharSequence) {
						if (field.getName().equals("mText") || field.getName().equals("mHint")) {
							classNitName = "|" + TypeConvertUtil.getSimpleStr((String) value) + classNitName;
							jsonObject.put("class", classNitName);
						}

					} else {
						continue;
					}
					// fieldArray.put(fieldDescription);
				}
				// JSONObject viewArgDescription = new JSONObject();
				// viewArgDescription.put("name", classname);
				// viewArgDescription.put("props", fieldArray);
				// JSONArray viewDescriptionArray = new JSONArray();
				// viewDescriptionArray.put(viewArgDescription);
				//
				// jsonObject.put("props", viewDescriptionArray);

				JSONArray childViewsArray = new JSONArray();

				if (rootView instanceof ViewGroup) {
					try {
						Field childViewsField = ViewGroup.class.getDeclaredField("mChildren");
						childViewsField.setAccessible(true);
						View[] mChildren = (View[]) childViewsField.get(rootView);
						int count = mChildren.length;
						for (int i = 0; i < count; i++) {
							try {
								View childView = mChildren[i];
								if (childView.getVisibility() != View.GONE) {
									JSONObject object = viewDump(childView);
									if (object != null) {
										childViewsArray.put(object);
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					} catch (NoSuchFieldException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
