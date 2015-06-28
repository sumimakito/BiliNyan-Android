package moe.feng.bilinyan.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.MainActivity;
import moe.feng.bilinyan.ui.common.LazyFragment;

public class SectionHomeFragment extends LazyFragment {

	private Toolbar mToolbar;

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_section_home;
	}

	@Override
	public void finishCreateView(Bundle state) {
		mToolbar = $(R.id.toolbar);
		mToolbar.setTitle(R.string.app_name);
		if (getActivity() != null && getActivity() instanceof MainActivity) {
			((MainActivity) getActivity()).bindToolbar(mToolbar);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof MainActivity) {
			if (mToolbar != null) {
				((MainActivity) activity).bindToolbar(mToolbar);
			}
		}
	}

}
