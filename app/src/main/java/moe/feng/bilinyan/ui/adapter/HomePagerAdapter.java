package moe.feng.bilinyan.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.fragment.home.BaseHomeFragment;
import moe.feng.bilinyan.ui.fragment.home.PlaceholderFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

	private final String[] TITLES;
	private BaseHomeFragment[] fragments;

	int minimumY = 0;

	public HomePagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		TITLES = context.getResources().getStringArray(R.array.sections);
		fragments = new BaseHomeFragment[TITLES.length];
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments[position] == null) {
			switch (position) {
				case 0:
					fragments[position] = PlaceholderFragment.newInstance(minimumY);
					break;
				default:
					fragments[position] = PlaceholderFragment.newInstance(minimumY);
			}
		}
		return fragments[position];
	}

	@Override
	public int getCount() {
		return TITLES.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}

	public void setMinimumScrollY(int y) {
		this.minimumY = y;
		for (BaseHomeFragment fragment : fragments) {
			if (fragment != null) {
				fragment.setMinimumScrollY(y);
			}
		}
	}

	public int getScrollY(int pos) {
		return fragments[pos] != null ? fragments[pos].getNowScrollY() : 0;
	}

	public void scrollToTop(int pos) {
		if (fragments[pos] != null) {
			fragments[pos].scrollToTop();
		}
	}

	public void scrollToMinimumY(int pos) {
		if (fragments[pos] != null) {
			fragments[pos].scrollToMinimumY();
		}
	}

}
