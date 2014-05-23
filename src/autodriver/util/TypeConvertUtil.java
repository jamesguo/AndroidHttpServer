package autodriver.util;

import java.text.SimpleDateFormat;

public class TypeConvertUtil {
	public static byte[] intToByte(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) (i >> 24 & 0xFF);
		result[1] = (byte) (i >> 16 & 0xFF);
		result[2] = (byte) (i >> 8 & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	public static int bytesToInt(byte[] bytes) {
		int i = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
		return i;
	}

	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	public static byte[] combineByteArr(byte[] arr1, byte[] arr2) {
		if ((arr1 == null || arr1.length <= 0) && (arr2 == null || arr2.length <= 0)) {
			return new byte[0];
		}
		if (arr1 == null || arr1.length <= 0) {
			return arr2;
		}
		if (arr2 == null || arr2.length <= 0) {
			return arr1;
		}
		byte[] submit = new byte[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, submit, 0, arr1.length);
		System.arraycopy(arr2, 0, submit, arr1.length, arr2.length);
		return submit;
	}

	public static String getFormatImageTime() {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis());
	}

	public static String getSimpleStr(String origon) {
		origon = origon.replace("\t", "");
		origon = origon.replace("\n", "");
		origon = origon.replace("\r", "");
		origon = origon.replace(" ", "");
		origon = origon.replace(" ", "");
		origon = origon.replace("¡¡", "");
		return origon;
	}
}
