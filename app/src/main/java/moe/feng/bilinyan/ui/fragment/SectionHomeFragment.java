package moe.feng.bilinyan.ui.fragment;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.adapter.BannerPagerAdapter;
import moe.feng.bilinyan.ui.adapter.HomePagerAdapter;
import moe.feng.bilinyan.ui.common.LazyFragment;
import moe.feng.bilinyan.util.Utility;
import moe.feng.bilinyan.view.CircleIndicator;
import moe.feng.bilinyan.view.SlidingTabLayout;

public class SectionHomeFragment extends LazyFragment implements ObservableScrollViewCallbacks {

	private TouchInterceptionFrameLayout mInterceptionLayout;
	private int mSlop;
	private boolean mScrolled;
	private ScrollState mLastScrollState;

	private ViewPager mBannerPager, mTabPager;
	private BannerPagerAdapter mBannerAdapter;
	private HomePagerAdapter mHomeAdapter;
	private CircleIndicator mBannerIndicator;
	private SlidingTabLayout mSlidingTab;

	private View mAppBarLayout, mAppBarBackground;

	private int APP_BAR_HEIGHT, TOOLBAR_HEIGHT, STATUS_BAR_HEIGHT = 0;

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

		mBannerPager = $(R.id.banner_pager);
		mTabPager = $(R.id.tab_pager);
		mBannerIndicator = $(R.id.pager_indicator);
		mSlidingTab = $(R.id.sliding_tabs);
		mAppBarLayout = $(R.id.appbar_layout);
		mAppBarBackground = $(R.id.appbar_background);
		mInterceptionLayout = $(R.id.container);

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

		ViewConfiguration vc = ViewConfiguration.get(getSupportActivity());
		mSlop = vc.getScaledTouchSlop();
		mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
	}

	@Override
	public void onDownMotionEvent() {
	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
	}

	private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
		@Override
		public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
			if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
				// Horizontal scroll is maybe handled by ViewPager
				return false;
			}

			Scrollable scrollable = getCurrentScrollable();
			if (scrollable == null) {
				mScrolled = false;
				return false;
			}

			// If interceptionLayout can move, it should intercept.
			// And once it begins to move, horizontal scroll shouldn't work any longer.
			int translationY = (int) ViewCompat.getTranslationY(mInterceptionLayout);
			boolean scrollingUp = 0 < diffY;
			boolean scrollingDown = diffY < 0;
			if (scrollingUp) {
				if (translationY < 0) {
					mScrolled = true;
					mLastScrollState = ScrollState.UP;
					return true;
				}
			} else if (scrollingDown) {
				if (-APP_BAR_HEIGHT + mSlidingTab.getHeight() + TOOLBAR_HEIGHT < translationY) {
					mScrolled = true;
					mLastScrollState = ScrollState.DOWN;
					return true;
				}
			}
			mScrolled = false;
			return false;
		}

		@Override
		public void onDownMotionEvent(MotionEvent ev) {
		}

		@Override
		public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
			float translationY = ScrollUtils.getFloat(
					ViewCompat.getTranslationY(mInterceptionLayout) + diffY,
					-APP_BAR_HEIGHT,
					0
			);

			ViewCompat.setTranslationY(mInterceptionLayout, translationY);
			ViewCompat.setTranslationY(mAppBarBackground, -translationY);
			mAppBarLayout.getLayoutParams().height = APP_BAR_HEIGHT - (int) translationY;
			mBannerAdapter.setBannerImageTransitionY(-translationY / 2);

			float alpha = Math.min(
					1,
					(float) (mAppBarLayout.getLayoutParams().height - (mSlidingTab.getHeight() + TOOLBAR_HEIGHT + STATUS_BAR_HEIGHT)) /
					(float) (APP_BAR_HEIGHT)
			) * 2 - 1;
			mAppBarBackground.setAlpha(alpha);

			if (translationY < 0) {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
				lp.height = (int) (-translationY + Utility.getScreenHeight(getApplicationContext()));
				mInterceptionLayout.requestLayout();
			}
		}

		@Override
		public void onUpOrCancelMotionEvent(MotionEvent ev) {
			mScrolled = false;
		}
	};

	private Scrollable getCurrentScrollable() {
		LazyFragment fragment = getCurrentFragment();
		if (fragment == null) {
			return null;
		}
		return fragment.$(R.id.scrollable);
	}

	private LazyFragment getCurrentFragment() {
		return (LazyFragment) mHomeAdapter.getItem(mTabPager.getCurrentItem());
	}

}
