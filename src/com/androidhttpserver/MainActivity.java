package com.androidhttpserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.httpserver.SimpleWebServer;
import android.httpserver.util.ViewScanner;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import autodriver.command.AndroidActionProtocol;
import autodriver.command.AndroidActionType;
import autodriver.command.CommandKey;
import ctrip.base.logical.component.CtripBaseApplication;

public class MainActivity extends Activity {
	EditText editText;
	public static final String url = "192.168.2.1";
	public static final int port = 6100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CtripBaseApplication.getInstance().setCurrentActivity(this);
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.edit);
		AssetManager assetManager = getAssets();
		try {
			String[] filePaths = assetManager.list("");
			for (String filename : filePaths) {
				String str[] = getAssets().list(filename);
				if (str.length > 0) {
					continue;
				}
				FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(filename, MODE_PRIVATE);

				InputStream sourceFile = assetManager.open(filename);
				copyFile(sourceFile, fileOutputStream);
				fileOutputStream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				SimpleWebServer.main(new String[] { "-d", "/data/data/com.androidhttpserver/files" });
			}
		}).start();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
				}

				Window window = MainActivity.this.getWindow();
				WindowManager manager = window.getWindowManager();
				ArrayList<Field> arrayList = new ArrayList<Field>();
				ViewScanner.getAllFields(manager, manager.getClass(), arrayList);
				for (Field field : arrayList) {
					field.setAccessible(true);
					try {
						if (field.getName().equals("mGlobal")) {
							Object object = field.get(manager);
							Field rootViews = object.getClass().getField("mRoots");
							rootViews.setAccessible(true);
							ArrayList<View> views = (ArrayList<View>) rootViews.get(object);
							break;
						} else if (field.getName().equals("mRoots")) {
							Object object = field.get(manager);
							View[] views = (View[]) object;
							ArrayList<View> result = (ArrayList<View>) Arrays.asList(views);
							break;
						}
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (int i = 0; i < 4; i++) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							Socket client = new Socket();
							try {
								client.setTcpNoDelay(true);
								client.connect(new InetSocketAddress(url, port), 10 * 1000);
								client.setSoTimeout(0);
							} catch (Exception e) {
								e.printStackTrace();
								try {
									Thread.sleep(10000);
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
						}
					}).start();
				}

				AndroidActionProtocol protocol = new AndroidActionProtocol();
				protocol.actionCode = AndroidActionType.PRESSKEY;
				protocol.SeqNo = 3;
				try {
					JSONObject jsonObject = new JSONObject();
					JSONObject paramsObject = new JSONObject();
					paramsObject.put("elementId", editText.hashCode());
					paramsObject.put("text", "13465498794");
					jsonObject.put("params", paramsObject.toString());
					protocol.json = jsonObject;
				} catch (Exception e) {
					e.printStackTrace();
				}
				CommandKey commandKey = new CommandKey();
				commandKey.excute(protocol);
			}
		}).start();
		Toast toast = Toast.makeText(getApplicationContext(), "12333", Toast.LENGTH_LONG);
		toast.show();
	}

	public static void copyFile(InputStream inputStream, FileOutputStream targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲
		BufferedInputStream inBuff = new BufferedInputStream(inputStream);

		BufferedOutputStream outBuff = new BufferedOutputStream(targetFile);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		inputStream.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
