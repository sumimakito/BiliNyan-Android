package moe.feng.bilinyan.ui.fragment.home;

import moe.feng.bilinyan.model.Index;
import moe.feng.bilinyan.ui.common.LazyFragment;

public abstract class BaseHomeFragment extends LazyFragment {

	public abstract void scrollToTop();

	public abstract boolean canScrollVertically(int direction);

	public abstract void notifyIndexDataUpdate(Index data);

}
