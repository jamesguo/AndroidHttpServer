package android.httpserver.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

public class ViewPreview {
	public static InputStream getViewPreview(long id) {
		View view = ViewScanner.findViewByID(id);
		if (view != null) {
			Bitmap bitmap = convertViewToBitmap(view);
			if (bitmap != null) {
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 80, arrayOutputStream);
				byte[] bytearray = arrayOutputStream.toByteArray();
				ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytearray);
				bitmap.recycle();
				bitmap = null;
				return arrayInputStream;
			}
		}
		return null;
	}

	public static Bitmap convertViewToBitmap(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		view.draw(new Canvas(bitmap));
		Matrix matrix = new Matrix();
		matrix.postScale(0.5f, 0.5f);
		Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), matrix, true);
		bitmap.recycle();
		bitmap = null;
		return newbm;
	}
}
