package com.androidhttpserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.json.JSONObject;

import android.app.Activity;
import android.httpserver.SimpleWebServer;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;
import autodriver.command.AndroidActionProtocol;
import autodriver.command.AndroidActionType;
import autodriver.command.CommandKey;
import ctrip.base.logical.component.CtripBaseApplication;

public class MainActivity extends Activity {
	EditText editText;
	public static final String url = "172.16.156.234";
	public static final int port = 6100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CtripBaseApplication.getInstance().setCurrentActivity(this);
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.edit);

		// AssetManager assetManager = getAssets();
		// try {
		// String[] filePaths = assetManager.list("");
		// for (String filename : filePaths) {
		// String str[] = getAssets().list(filename);
		// if (str.length > 0) {
		// continue;
		// }
		// FileOutputStream fileOutputStream =
		// getApplicationContext().openFileOutput(filename, MODE_PRIVATE);
		//
		// InputStream sourceFile = assetManager.open(filename);
		// copyFile(sourceFile, fileOutputStream);
		// fileOutputStream.close();
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
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
					Thread.sleep(3000);
				} catch (Exception e) {
					// TODO: handle exception
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
