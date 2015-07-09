package moe.feng.bilinyan.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.adapter.BannerPagerAdapter;
import moe.feng.bilinyan.ui.adapter.HomePagerAdapter;
import moe.feng.bilinyan.ui.common.LazyFragment;
import moe.feng.bilinyan.util.Utility;
import moe.feng.bilinyan.view.CircleIndicator;
import moe.feng.bilinyan.view.SlidingTabLayout;

public class SectionHomeFragment extends LazyFragment {

	private ViewPager mBannerPager, mTabPager;
	private BannerPagerAdapter mBannerAdapter;
	private HomePagerAdapter mHomeAdapter;
	private CircleIndicator mBannerIndicator;
	private SlidingTabLayout mSlidingTab;

	private View mAppBarLayout, mAppBarBackground;

	private int APP_BAR_HEIGHT, TOOLBAR_HEIGHT, STATUS_BAR_HEIGHT = 0;
	private int minHeight = 0;

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_section_home;
	}

	@Override
	public void finishCreateView(Bundle state) {
		APP_BAR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.appbar_parallax_max_height);
		TOOLBAR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
		if (Build.VERSION.SDK_INT >= 19) {
			STATUS_BAR_HEIGHT = Utility.getStatusBarHeight(getApplicationContext());
		}
		minHeight = APP_BAR_HEIGHT - TOOLBAR_HEIGHT * 2 - STATUS_BAR_HEIGHT;

		mBannerPager = $(R.id.banner_pager);
		mTabPager = $(R.id.tab_pager);
		mBannerIndicator = $(R.id.pager_indicator);
		mSlidingTab = $(R.id.sliding_tabs);
		mAppBarLayout = $(R.id.appbar_layout);
		mAppBarBackground = $(R.id.appbar_background);

		ViewCompat.setElevation(mAppBarLayout, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));

		mBannerAdapter = new BannerPagerAdapter(getChildFragmentManager(), 3);
		mBannerPager.setAdapter(mBannerAdapter);
		mBannerIndicator.setViewPager(mBannerPager);

		mHomeAdapter = new HomePagerAdapter(getChildFragmentManager(), getApplicationContext());
		mTabPager.setAdapter(mHomeAdapter);
		mSlidingTab.setViewPager(mTabPager);
		mSlidingTab.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
		mSlidingTab.setSelectedIndicatorColors(getResources().getColor(android.R.color.white));
		mSlidingTab.setDistributeEvenly(true);
		mSlidingTab.setOnTabItemClickListener(new SlidingTabLayout.OnTabItemClickListener() {
			@Override
			public void onTabItemClick(int pos) {
				if (mHomeAdapter.getScrollY(pos) > minHeight) {
					mHomeAdapter.scrollToMinimumY(pos);
				} else {
					mHomeAdapter.scrollToTop(pos);
				}
			}
		});
	}

	public void onScrollChanged(int x, int y, int oldx, int oldy) {
		setViewsTranslation(Math.min(minHeight, y));
	}

	private void setViewsTranslation(int target) {
		mAppBarLayout.setTranslationY(-target);
		mAppBarBackground.setTranslationY(target);
		float alpha = Math.min(1, -mAppBarLayout.getTranslationY() / (float) minHeight);
		mAppBarBackground.setAlpha(alpha);

		mBannerAdapter.setBannerImageTransitionY(target / 2);

		mHomeAdapter.setMinimumScrollY(target);
	}

}
