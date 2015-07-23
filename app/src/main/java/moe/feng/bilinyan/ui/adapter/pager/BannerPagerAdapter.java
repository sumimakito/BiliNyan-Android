package moe.feng.bilinyan.ui.adapter.pager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

import moe.feng.bilinyan.model.HomeBanner;
import moe.feng.bilinyan.ui.fragment.HomeBannerFragment;

public class BannerPagerAdapter extends FragmentPagerAdapter {

	private HomeBannerFragment[] fragments;
	private ArrayList<HomeBanner> items;

	public BannerPagerAdapter(FragmentManager fm, ArrayList<HomeBanner> items) {
		super(fm);
		fragments = new HomeBannerFragment[items.size()];
		this.items = items;
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments[position] == null) {
			fragments[position] = HomeBannerFragment.newInstance(items.get(position));
		}
		return fragments[position];
	}

	@Override
	public int getCount() {
		return items.size();
	}

	public void setBannerImageTransitionY(float y) {
		for (HomeBannerFragment fragment : fragments) {
			if (fragment != null) {
				fragment.setImageTransitionY(y);
			}
		}
	}

}
