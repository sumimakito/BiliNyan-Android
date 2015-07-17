package moe.feng.bilinyan.ui.adapter.pager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.fragment.home.BaseHomeFragment;
import moe.feng.bilinyan.ui.fragment.home.PlaceholderFragment;
import moe.feng.bilinyan.ui.fragment.home.RecommendFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

	private final String[] TITLES;
	private BaseHomeFragment[] fragments;

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
					fragments[position] = RecommendFragment.newInstance();
					break;
				default:
					fragments[position] = PlaceholderFragment.newInstance();
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

	public void scrollToTop(int pos) {
		if (fragments[pos] != null) {
			fragments[pos].scrollToTop();
		}
	}

}
