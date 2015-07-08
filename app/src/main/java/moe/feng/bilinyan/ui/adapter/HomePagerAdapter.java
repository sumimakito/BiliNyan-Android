package moe.feng.bilinyan.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.fragment.home.PlaceholderFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

	private final String[] TITLES;
	private Fragment[] fragments;

	public HomePagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		TITLES = context.getResources().getStringArray(R.array.sections);
		fragments = new Fragment[TITLES.length];
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments[position] == null) {
			switch (position) {
				case 0:
					fragments[position] = new PlaceholderFragment();
					break;
				default:
					fragments[position] = new PlaceholderFragment();
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

}
