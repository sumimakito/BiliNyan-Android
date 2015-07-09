package moe.feng.bilinyan.ui.fragment.home;

import moe.feng.bilinyan.ui.common.LazyFragment;

public abstract class BaseHomeFragment extends LazyFragment {

	private int minimumScrollY = 0;

	protected BaseHomeFragment(int minimumScrollY) {
		this.minimumScrollY = minimumScrollY;
	}

	public void setMinimumScrollY(int y) {
		this.minimumScrollY = y;
		this.onMinimumScrollYSet(y);
	}

	public abstract void onMinimumScrollYSet(int y);

	public abstract void scrollToTop();

	public abstract void scrollToMinimumY();

	public int getMinimumScrollY() {
		return this.minimumScrollY;
	}

	public abstract int getNowScrollY();

}
