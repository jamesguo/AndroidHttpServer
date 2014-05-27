package autodriver.command;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.httpserver.util.ViewScanner;
import android.view.View;
import android.view.ViewGroup;
import autodriver.util.BlockDelayUtil;
import autodriver.util.ExecuteBlock;
import autodriver.util.TypeConvertUtil;
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
			int timeout = params.optInt("timeout", 10);
			String value = TypeConvertUtil.getSimpleStr(params.optString("value", ""));
			JSONArray reuslt = findViews(findType, value, timeout);
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
				if (findType == FindType.ID.index) {
					try {
						long id = Long.valueOf(value.trim());
						View view = ViewScanner.findViewByID(id);
						if (view != null) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", view.hashCode());
							arrayList.put(jsonObject);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					viewsScan(findType, value, arrayList);
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

	public static void viewsScan(int findType, String target, JSONArray reslutList) {
		ArrayList<View> result = ViewScanner.getAllWindowViews();
		if (result != null) {
			for (View view : result) {
				viewScan(view, findType, target, reslutList);
			}
		}
	}

	public static void viewScan(View rootView, int findType, String target, JSONArray reslutList) {
		if (rootView != null && rootView.getVisibility() == View.VISIBLE) {
			FindType find = FindType.getType(findType);
			Class<?> viewclass = rootView.getClass();
			switch (find) {
			case CLASS_NAME:
				if (viewclass.getSimpleName().equals(target)) {
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
				ArrayList<Field> result = new ArrayList<Field>();
				ViewScanner.getAllFields(rootView, viewclass, result);
				for (Field field : result) {
					int modifie = field.getModifiers();
					//
					// public static final int PUBLIC = 0x00000001;
					//
					// public static final int PRIVATE =0x00000002;
					//
					// public static final int PROTECTED = 0x00000004;
					//
					// public static final int STATIC =0x00000008;
					//
					// public static final int FINAL =0x00000010;
					//
					// public static final int SYNCHRONIZED = 0x00000020;
					//
					// native transient volatile synchronized final static
					// protected private public
					// 0 0 0 0 0 0 0 0 0

					if ((modifie & 8) != 0 || (modifie & 16) != 0) {
						continue;
					}
					try {
						field.setAccessible(true);
						Object fieldValue = field.get(rootView);
						JSONObject fieldDescription = new JSONObject();
						fieldDescription.put("name", field.getName());
						if (fieldValue instanceof Float) {
							continue;
							// fieldDescription.put("type", "float");
							// fieldDescription.put("value", fieldValue);
						} else if (fieldValue instanceof Double) {
							continue;
							// fieldDescription.put("type", "double");
							// fieldDescription.put("value", fieldValue);
						} else if (fieldValue instanceof Integer) {
							if (field.getName().equals("mBackgroundResource") && (Integer) fieldValue > 0) {
								String name = CtripBaseApplication.getInstance().getResources().getResourceName((Integer) fieldValue);
								name = name.substring(name.lastIndexOf("/") + 1);
								fieldDescription.put("type", "String");
								fieldDescription.put("value", name);
								if (name.equals(target) && !fieldDescription.get("name").equals("mBtnRightText") && !fieldDescription.get("name").equals("mTextSubmitValue")) {
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("id", rootView.hashCode());
									reslutList.put(jsonObject);
									break;
								}
								// fieldArray.put(fieldDescription);
							} else if (field.getName().equals("mID") && (Integer) fieldValue > 0) {
								try {
									String name = CtripBaseApplication.getInstance().getResources().getResourceName((Integer) fieldValue);
									name = name.substring(name.lastIndexOf("/") + 1);
									fieldDescription.put("type", "String");
									fieldDescription.put("value", name);
									if (name.equals(target) && !fieldDescription.get("name").equals("mBtnRightText") && !fieldDescription.get("name").equals("mTextSubmitValue")) {
										JSONObject jsonObject = new JSONObject();
										jsonObject.put("id", rootView.hashCode());
										reslutList.put(jsonObject);
										break;
									}

									// fieldArray.put(fieldDescription);
								} catch (Exception e) {
									fieldDescription.put("type", "int");
									fieldDescription.put("value", fieldValue);
								}
							} else {
								fieldDescription.put("type", "int");
								fieldDescription.put("value", fieldValue);
							}
						} else if (fieldValue instanceof Long) {
							continue;
							// fieldDescription.put("type", "long");
							// fieldDescription.put("value", fieldValue);
						} else if (fieldValue instanceof Boolean) {
							continue;
							// fieldDescription.put("type", "BOOL");
							// if ((Boolean) fieldValue) {
							// fieldDescription.put("value", "YES");
							// } else {
							// fieldDescription.put("value", "NO");
							// }
						} else if (fieldValue instanceof CharSequence) {

							fieldDescription.put("type", "String");
							fieldDescription.put("value", TypeConvertUtil.getSimpleStr((String) fieldValue));
							if (TypeConvertUtil.getSimpleStr((String) fieldValue).equals(target) && !fieldDescription.get("name").equals("mBtnRightText")
									&& !fieldDescription.get("name").equals("mTextSubmitValue")) {
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("id", rootView.hashCode());
								reslutList.put(jsonObject);
								break;
							}
							// if (field.getName().equals("mText") ||
							// field.getName().equals("mHint")) {
							// //
							// System.out.println();
							// }
						} else {
							continue;
							// if (fieldValue != null) {
							// fieldDescription.put("type",
							// fieldValue.getClass().getSimpleName());
							// fieldDescription.put("value", "Object:" +
							// fieldValue);
							// } else {
							// fieldDescription.put("type",
							// field.getType().getName());
							// fieldDescription.put("value", "Object:" +
							// fieldValue);
							// }
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
			if (reslutList.length() > 0) {
				return;
			} else {
				if (rootView instanceof ViewGroup) {
					try {
						Field childViewsField = ViewGroup.class.getDeclaredField("mChildren");
						childViewsField.setAccessible(true);
						View[] mChildren = (View[]) childViewsField.get(rootView);
						int count = mChildren.length;
						for (int i = 0; i < count; i++) {
							try {
								View childView = mChildren[i];
								if (reslutList.length() > 0) {
									break;
								} else {
									if (childView.getVisibility() != View.GONE) {
										viewScan(childView, findType, target, reslutList);
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
			}
			// if (rootView instanceof ViewGroup) {
			// int count = ((ViewGroup) rootView).getChildCount();
			// for (int i = 0; i < count; i++) {
			// try {
			// View childView = ((ViewGroup) rootView).getChildAt(i);
			// viewScan(childView, findType, value, reslutList);
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			// }
			// }
		}
	}
}
