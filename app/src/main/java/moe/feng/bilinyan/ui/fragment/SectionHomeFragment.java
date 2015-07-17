package moe.feng.bilinyan.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.adapter.pager.HomePagerAdapter;
import moe.feng.bilinyan.ui.common.LazyFragment;
import moe.feng.bilinyan.view.SlidingTabLayout;
import moe.feng.material.statusbar.AppBarLayout;

public class SectionHomeFragment extends LazyFragment {

	private ViewPager mPager;
	private HomePagerAdapter mHomeAdapter;
	private SlidingTabLayout mSlidingTab;

	private AppBarLayout mAppBarLayout;

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_section_home;
	}

	@Override
	public void finishCreateView(Bundle state) {
		mPager = $(R.id.pager);
		mSlidingTab = $(R.id.sliding_tabs);
		mAppBarLayout = $(R.id.appbar_layout);

		ViewCompat.setElevation(mAppBarLayout, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));

		mHomeAdapter = new HomePagerAdapter(getChildFragmentManager(), getApplicationContext());
		mPager.setAdapter(mHomeAdapter);
		mSlidingTab.setViewPager(mPager);
		mSlidingTab.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
		mSlidingTab.setSelectedIndicatorColors(getResources().getColor(android.R.color.white));
		mSlidingTab.setDistributeEvenly(true);
		mSlidingTab.setOnTabItemClickListener(new SlidingTabLayout.OnTabItemClickListener() {
			@Override
			public void onTabItemClick(int pos) {
				if (pos != mPager.getCurrentItem()) return;
				mHomeAdapter.scrollToTop(pos);
			}
		});
	}

}
