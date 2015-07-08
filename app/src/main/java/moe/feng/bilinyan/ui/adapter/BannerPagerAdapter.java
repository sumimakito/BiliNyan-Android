package moe.feng.bilinyan.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import moe.feng.bilinyan.ui.fragment.HomeBannerFragment;

public class BannerPagerAdapter extends FragmentPagerAdapter {

	private int count;
	private HomeBannerFragment[] fragments;

	public BannerPagerAdapter(FragmentManager fm, int count) {
		super(fm);
		this.count = count;
		fragments = new HomeBannerFragment[count];
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments[position] == null) {
			fragments[position] = HomeBannerFragment.newInstance(position);
		}
		return fragments[position];
	}

	@Override
	public int getCount() {
		return count;
	}

	public void setBannerImageTransitionY(float y) {
		for (HomeBannerFragment fragment : fragments) {
			if (fragment != null) {
				fragment.setImageTransitionY(y);
			}
		}
	}

}
