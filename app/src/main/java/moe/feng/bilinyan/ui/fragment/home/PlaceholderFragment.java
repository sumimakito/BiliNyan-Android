package moe.feng.bilinyan.ui.fragment.home;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.common.LazyFragment;

public class PlaceholderFragment extends LazyFragment {

	private ObservableScrollView mScrollView;

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_tab_placeholder;
	}

	@Override
	public void finishCreateView(Bundle state) {
		mScrollView = $(R.id.scrollable);

		Fragment parentFragment = getParentFragment();
		ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
		if (viewGroup != null) {
			mScrollView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
			if (parentFragment instanceof ObservableScrollViewCallbacks) {
				mScrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
			}
		}
	}

}
