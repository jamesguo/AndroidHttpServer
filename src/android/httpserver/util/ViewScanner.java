package android.httpserver.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import autodriver.core.AutoDriver;
import ctrip.base.logical.component.CtripBaseApplication;
import ctrip.base.logical.component.widget.CtripTitleView;

public class ViewScanner {
	public static String hierachySnapshot() {
		View rootView = AutoDriver.getRootView();
		if (rootView == null) {
			return "";
		}
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
		if (rootView == null) {
			return null;
		}
		if (rootView.hashCode() == id) {
			return rootView;
		}
		if (rootView instanceof CtripTitleView) {
			Log.e("recursiverForView", "CtripTitleView");
		}
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
							View result = recursiverForView(id, childView);
							if (result != null) {
								return result;
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
		return null;
	}

	public static View findViewByID(long id) {
		View rootView = AutoDriver.getRootView();
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
		if (rootView != null && rootView.getVisibility() != View.GONE) {
			JSONObject jsonObject = new JSONObject();
			try {
				Class<?> viewclass = rootView.getClass();
				String classname = viewclass.getSimpleName();
				int[] location = new int[2];
				rootView.getLocationOnScreen(location);
				String classNitName = ":" + classname;
				jsonObject.put("class", classNitName);
				jsonObject.put("layer_screen_x", location[0] / 2);
				jsonObject.put("layer_screen_y", location[1] / 2);
				jsonObject.put("layer_bounds_w", rootView.getMeasuredWidth() / 2);
				jsonObject.put("layer_bounds_h", rootView.getMeasuredHeight() / 2);
				jsonObject.put("id", "" + rootView.hashCode());
				JSONArray fieldArray = new JSONArray();
				ArrayList<Field> result = new ArrayList<Field>();
				getAllFields(rootView, viewclass, result);
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
						if (field.getName().equals("mBackgroundResource") && (Integer) value > 0) {
							String name = CtripBaseApplication.getInstance().getResources().getResourceName((Integer) value);
							name = name.substring(name.lastIndexOf("/") + 1);
							fieldDescription.put("type", "String");
							fieldDescription.put("value", name);
							classNitName = "|" + name + classNitName;
							jsonObject.put("class", classNitName);
						} else if (field.getName().equals("mID") && (Integer) value > 0) {
							try {
								String name = CtripBaseApplication.getInstance().getResources().getResourceName((Integer) value);
								name = name.substring(name.lastIndexOf("/") + 1);
								classNitName = "|" + name + classNitName;
								jsonObject.put("class", classNitName);
								fieldDescription.put("type", "String");
								fieldDescription.put("value", name);
							} catch (Exception e) {
								fieldDescription.put("type", "int");
								fieldDescription.put("value", value);
							}
						} else {
							fieldDescription.put("type", "int");
							fieldDescription.put("value", value);
						}
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
						if (field.getName().equals("mText") || field.getName().equals("mHint")) {
							classNitName = "|" + value + classNitName;
							jsonObject.put("class", classNitName);
						}

					} else {
						continue;
						// if (value != null) {
						// // if (field.getName().equals("mBackground")) {
						// //
						// // }
						// fieldDescription.put("type",
						// value.getClass().getSimpleName());
						// fieldDescription.put("value", "Object:" + value);
						// } else {
						// fieldDescription.put("type",
						// field.getType().getName());
						// fieldDescription.put("value", "NULL");
						// }
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

				// if (rootView instanceof ViewGroup) {
				// int count = ((ViewGroup) rootView).getChildCount();
				// for (int i = 0; i < count; i++) {
				// try {
				// View childView = ((ViewGroup) rootView).getChildAt(i);
				// JSONObject object = scanView(childView);
				// if (object != null) {
				// childViewsArray.put(object);
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				// }
				// }
				if (rootView instanceof CtripTitleView) {
					Log.e("recursiverForView", "CtripTitleView");
				}
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
									JSONObject object = scanView(childView);
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
