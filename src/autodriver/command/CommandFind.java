package autodriver.command;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.httpserver.util.ViewScanner;
import android.view.View;
import android.view.ViewGroup;
import autodriver.util.BlockDelayUtil;
import autodriver.util.ExecuteBlock;
import ctrip.base.logical.component.CtripBaseApplication;

public class CommandFind extends BaseCommand {
	public static enum FindType {
		CLASS_NAME("classname", 0), NAME("name", 1), ID("id", 2);
		// 成员变量
		public String name;
		public int index;

		// 构造方法
		private FindType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		// 普通方法
		public static FindType getType(int index) {
			for (FindType c : FindType.values()) {
				if (c.getIndex() == index) {
					return c;
				}
			}
			return null;
		}

		// get set 方法
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	@Override
	public AndroidActionProtocol excute(AndroidActionProtocol request) {
		try {
			JSONObject params = request.params();
			int findType = params.optInt("findType", FindType.NAME.index);
			int timeout = params.optInt("timeout", 3);
			String value = params.optString("value", "");
			JSONArray reuslt = findViews(findType, value, timeout);
			AndroidActionProtocol actionProtocol = new AndroidActionProtocol();
			actionProtocol.actionCode = request.actionCode;
			actionProtocol.SeqNo = request.SeqNo;
			if (reuslt.length() > 0) {
				actionProtocol.result = (byte)0;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("elements", reuslt.toString());
				actionProtocol.json = jsonObject;
			} else {
				actionProtocol.result = (byte)1;
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

	public static JSONArray findViews(int findType, String value, int timeout) {
		JSONArray arrayList = new JSONArray();
		BlockDelayUtil.runBlock(new ExecuteBlock() {

			@Override
			public int excute(Object... objects) {
				int findType = (Integer) objects[0];
				String value = (String) objects[1];
				JSONArray arrayList = (JSONArray) objects[2];
				Activity activity = CtripBaseApplication.getInstance().getCurrentActivity();
				View rootView = activity.getWindow().getDecorView();
				if (findType == FindType.ID.index) {
					try {
						long id = Long.valueOf(value.trim());
						View view = ViewScanner.findViewByID(id);
						if (view != null) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", rootView.hashCode());
							arrayList.put(jsonObject);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					viewScan(rootView, findType, value, arrayList);
				}
				if (arrayList.length() > 0) {
					return ExecuteBlock.StepResultSuccess;
				} else {
					return ExecuteBlock.StepResultWait;
				}
			}
		}, timeout, findType, value, arrayList);
		return arrayList;
	}

	public static void viewScan(View rootView, int findType, String value, JSONArray reslutList) {
		if (rootView != null && rootView.getVisibility() == View.VISIBLE) {
			FindType find = FindType.getType(findType);
			Class<?> viewclass = rootView.getClass();
			switch (find) {
			case CLASS_NAME:
				if (viewclass.getSimpleName().equals(value)) {
					JSONObject jsonObject = new JSONObject();
					try {
						jsonObject.put("id", rootView.hashCode());
						reslutList.put(jsonObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case NAME:
				JSONArray fieldArray = new JSONArray();
				Field[] fields = viewclass.getDeclaredFields();
				for (Field field : fields) {
					try {
						field.setAccessible(true);
						Object fieldValue = field.get(rootView);
						JSONObject fieldDescription = new JSONObject();
						fieldDescription.put("name", field.getName());
						if (fieldValue instanceof Float) {
							fieldDescription.put("type", "float");
							fieldDescription.put("value", value);
							fieldArray.put(fieldDescription);
						} else if (fieldValue instanceof Double) {
							fieldDescription.put("type", "double");
							fieldDescription.put("value", value);
							fieldArray.put(fieldDescription);
						} else if (fieldValue instanceof Integer) {
							fieldDescription.put("type", "int");
							fieldDescription.put("value", value);
							fieldArray.put(fieldDescription);
						} else if (fieldValue instanceof Long) {
							fieldDescription.put("type", "long");
							fieldDescription.put("value", value);
							fieldArray.put(fieldDescription);
						} else if (fieldValue instanceof Boolean) {
							fieldDescription.put("type", "BOOL");
							if ((Boolean) fieldValue) {
								fieldDescription.put("value", "YES");
							} else {
								fieldDescription.put("value", "NO");
							}
							fieldArray.put(fieldDescription);
						} else if (fieldValue instanceof String) {
							fieldDescription.put("type", "String");
							fieldDescription.put("value", value);
							fieldArray.put(fieldDescription);
						} else {
							if (value != null) {
								fieldDescription.put("type", value.getClass().getSimpleName());
								fieldDescription.put("value", "Object:" + value);
							} else {
								fieldDescription.put("type", field.getType().getName());
								fieldDescription.put("value", "Object:" + value);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				int usefulFieldCount = fieldArray.length();

				for (int i = 0; i < usefulFieldCount; i++) {
					try {
						JSONObject object = (JSONObject) fieldArray.get(i);
						if (object.get("value").equals(value)) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", rootView.hashCode());
							reslutList.put(jsonObject);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}

			if (rootView instanceof ViewGroup) {
				int count = ((ViewGroup) rootView).getChildCount();
				for (int i = 0; i < count; i++) {
					try {
						View childView = ((ViewGroup) rootView).getChildAt(i);
						viewScan(childView, findType, value, reslutList);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}
	}
}
