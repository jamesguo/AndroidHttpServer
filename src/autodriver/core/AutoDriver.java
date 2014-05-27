package autodriver.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.httpserver.SimpleWebServer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import autodriver.command.AndroidActionProtocol;
import autodriver.command.AndroidActionType;
import autodriver.command.CommandClick;
import autodriver.command.CommandDeviceInfo;
import autodriver.command.CommandFind;
import autodriver.command.CommandKey;
import autodriver.command.CommandScreenShot;
import autodriver.command.CommandSee;
import autodriver.command.CommandViewDump;
import autodriver.command.CommandWaitToDisappear;
import autodriver.util.CommondProcoltolUtil;
import autodriver.util.TypeConvertUtil;
import ctrip.android.activity.CtripBaseActivityV2;
import ctrip.base.logical.component.CtripBaseApplication;

public class AutoDriver {
	// public static final String url = "172.16.156.234";
	public static final String url = "172.16.45.233";
	public static final int port = 6100;
	public static Socket client;
	public static boolean finished;
	public static Thread backThread;
	public static final int READ_BUFFER_LENGTH = 1024;
	public static InputStream inputStream;
	public static PrintWriter out;
	public static Runnable runnable = new Runnable() {

		@Override
		public void run() {
			listenerSocket();
		}
	};
	public static Runnable buildSocket = new Runnable() {

		@Override
		public void run() {
			boolean connect = false;
			while (!connect) {
				client = new Socket();
				try {
					client.setTcpNoDelay(true);
					client.connect(new InetSocketAddress(url, port), 10 * 1000);
					client.setSoTimeout(0);
					connect = true;
					finished = false;
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(10000);
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
			new Thread(runnable).start();
		}
	};

	public static void startWebService() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				AssetManager assetManager = CtripBaseApplication.getInstance().getAssets();
				try {
					String[] filePaths = assetManager.list("");
					for (String filename : filePaths) {
						String str[] = assetManager.list(filename);
						if (str.length > 0) {
							continue;
						}
						CtripBaseApplication.getInstance();
						FileOutputStream fileOutputStream = CtripBaseApplication.getInstance().openFileOutput(filename, Context.MODE_PRIVATE);

						InputStream sourceFile = assetManager.open(filename);
						copyFile(sourceFile, fileOutputStream);
						fileOutputStream.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SimpleWebServer.main(new String[] { "-d", "/data/data/ctrip.android.view/files" });
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

	public static void start() {
		startWebService();
		if (backThread == null || backThread.isAlive()) {
			backThread = new Thread(buildSocket);
			backThread.start();
		}
	}

	public static void goHome() {
		Activity activity = CtripBaseApplication.getInstance().getCurrentActivity();
		if (activity == null || !(activity instanceof CtripBaseActivityV2)) {
			return;
		}
		((CtripBaseActivityV2) activity).goHome(0);
	}
	public static View getRootView() {
		Activity activity = CtripBaseApplication.getInstance().getCurrentActivity();
		if (activity == null) {
			return null;
		}
		Window window = activity.getWindow();
		View rootView = window.getDecorView().getRootView();
		return rootView;
	}

	public static boolean isConnected() {
		if (client != null && !client.isClosed() && client.isConnected()) {
			return true;
		}
		return false;
	}

	protected static void listenerSocket() {
		while (!finished) {
			if (isConnected()) {
				try {
					inputStream = client.getInputStream();
					out = new PrintWriter(client.getOutputStream(), true);
					byte[] buffer = new byte[4];
					int lenght = 0;
					try {
						lenght = inputStream.read(buffer);
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
					int maxLength = -1;
					if (lenght == 4) {
						try {
							maxLength = TypeConvertUtil.bytesToInt(buffer);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							throw new RuntimeException("readByteSize=" + lenght + ",it must be 4!");
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
						break;
					}
					if (maxLength != -1) {
						byte[] inputeByte = null;
						try {
							inputStream = client.getInputStream();
							inputeByte = new byte[maxLength];
							if (maxLength > READ_BUFFER_LENGTH) {
								int readLength = 0;
								int readIndex = 0;
								while (readIndex < maxLength) {
									if (maxLength - readIndex > READ_BUFFER_LENGTH) {
										readLength = inputStream.read(inputeByte, readIndex, READ_BUFFER_LENGTH);
									} else {
										readLength = inputStream.read(inputeByte, readIndex, maxLength - readIndex);
									}
									if (readLength == -1) {
										while (readIndex < maxLength) {
											inputeByte[readIndex] = 32;
											readIndex++;
										}
										break;
									} else {
										readIndex += readLength;
									}
								}
							} else {
								inputStream.read(inputeByte);
							}
							final byte[] response = inputeByte;
							new Thread(new Runnable() {

								@Override
								public void run() {
									AndroidActionProtocol actionCommand = CommondProcoltolUtil.buileActionCommand(response);
									runCommand(actionCommand);
								}
							}).start();
						} catch (Exception e) {
							e.printStackTrace();
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			} else {
				break;
			}
		}
		restart();
	}

	private static void restart() {
		goHome();
		if (!finished) {
			finished = true;
		}

		if (backThread != null) {
			try {
				backThread.interrupt();
			} catch (Exception e) {
				// TODO: handle exception
			}
			backThread = null;
		}
		backThread = new Thread(buildSocket);
		backThread.start();
	}

	protected static void runCommand(AndroidActionProtocol request) {
		Log.e("AutoDriver", "RecvMessage:" + request.body);
		AndroidActionProtocol response = new AndroidActionProtocol();
		switch (request.actionCode) {
		case AndroidActionType.FIND:
			CommandFind commandFind = new CommandFind();
			response = commandFind.excute(request);
			break;
		case AndroidActionType.SEE:
			CommandSee seecommand = new CommandSee();
			response = seecommand.excute(request);
			break;
		case AndroidActionType.CLICK:
			CommandClick clickcommand = new CommandClick();
			response = clickcommand.excute(request);
			break;
		case AndroidActionType.SCREENSHOT:

			CommandScreenShot screenShotCommand = new CommandScreenShot();
			response = screenShotCommand.excute(request);
			break;
		case AndroidActionType.DEVICEINFO:

			CommandDeviceInfo deviceInfoCommand = new CommandDeviceInfo();
			response = deviceInfoCommand.excute(request);
			break;
		case AndroidActionType.PRESSKEY:

			CommandKey commandKey = new CommandKey();
			response = commandKey.excute(request);
			break;
		case AndroidActionType.WAITTODISAPPEAR:

			CommandWaitToDisappear commandWaitToDisappear = new CommandWaitToDisappear();
			response = commandWaitToDisappear.excute(request);
			break;
		case AndroidActionType.VIEWDUMP:
			CommandViewDump commandViewDump = new CommandViewDump();
			response = commandViewDump.excute(request);
			break;
		case AndroidActionType.FINISH:
			response = new AndroidActionProtocol();
			response.actionCode = request.actionCode;
			response.SeqNo = request.SeqNo;
			response.result = (byte) 0;
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("value", "success");
				response.json = jsonObject;
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		default:
			break;
		}
		if (response != null) {
			sendMessage(response);
		}
		if (response.actionCode == AndroidActionType.FINISH) {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void sendMessage(AndroidActionProtocol response) {
		byte[] dataBean = CommondProcoltolUtil.buileResponse(response);
		Log.e("AutoDriver", "SendMessage:" + response.body);
		OutputStream outputStream;
		try {
			outputStream = client.getOutputStream();
			outputStream.write(dataBean);
			outputStream.flush();
			dataBean = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
