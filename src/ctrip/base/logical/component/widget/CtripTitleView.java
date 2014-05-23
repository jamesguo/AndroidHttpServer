package ctrip.base.logical.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>
 * 封装Title控件，具体显示效果如下：
 * <p>
 * -----------------------------------------
 * </p>
 * <p>
 * LeftButton Title RightButton
 * </p>
 * <p>
 * -----------------------------------------
 * </p>
 * <p>
 * Left Button - title左侧按钮
 * </p>
 * <p>
 * Title - 显示的title文字
 * </p>
 * <p>
 * Right Button - title右侧按钮
 * </p>
 */
public class CtripTitleView extends android.widget.RelativeLayout implements View.OnClickListener {

	public CtripTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}