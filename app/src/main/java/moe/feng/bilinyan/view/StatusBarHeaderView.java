package moe.feng.bilinyan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.util.Utility;

public class StatusBarHeaderView extends View {

	public StatusBarHeaderView(Context context) {
		this(context, null);
	}

	public StatusBarHeaderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StatusBarHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusBarHeaderView, defStyleAttr, R.style.Widget_FengMoe_StatusBarHeaderView);
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			this.setBackgroundColor(a.getColor(R.styleable.StatusBarHeaderView_colorNormal, Color.TRANSPARENT));
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			this.setBackgroundColor(a.getColor(R.styleable.StatusBarHeaderView_colorDarker, Color.TRANSPARENT));
		}
		this.setVisibility(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? VISIBLE : GONE);
		a.recycle();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		recalculateHeight();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		recalculateHeight();
	}

	private void recalculateHeight() {
		ViewGroup.LayoutParams params = getLayoutParams();
		params.height = Utility.getStatusBarHeight(getContext());
	}

}
