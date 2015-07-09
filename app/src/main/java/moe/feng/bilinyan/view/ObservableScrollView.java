package moe.feng.bilinyan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

public class ObservableScrollView extends ScrollView {

	private ArrayList<OnScrollChangeListener> listeners = new ArrayList<>();

	public ObservableScrollView(Context context) {
		super(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void addOnScrollChangeListener(OnScrollChangeListener listener) {
		this.listeners.add(listener);
	}

	public boolean removeOnScrollChangeListener(OnScrollChangeListener listener) {
		return this.listeners.remove(listener);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		for (OnScrollChangeListener listener : listeners) {
			listener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	public interface OnScrollChangeListener {

		void onScrollChanged(ObservableScrollView view, int x, int y, int oldx, int oldy);

	}

}
