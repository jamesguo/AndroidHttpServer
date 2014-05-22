package com.androidhttpserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.httpserver.SimpleWebServer;
import android.os.Bundle;
import android.view.Menu;
import ctrip.base.logical.component.CtripBaseApplication;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CtripBaseApplication.getInstance().setCurrentActivity(this);
		setContentView(R.layout.activity_main);
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
