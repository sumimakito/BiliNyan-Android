package moe.feng.bilinyan.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;

public class ObservableRecyclerView extends RecyclerView {

	private ArrayList<OnScrollChangeListener> listeners = new ArrayList<>();

	public ObservableRecyclerView(Context context) {
		super(context);
	}

	public ObservableRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObservableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
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

		void onScrollChanged(ObservableRecyclerView view, int x, int y, int oldx, int oldy);

	}

}
