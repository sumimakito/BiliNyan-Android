package moe.feng.bilinyan.ui;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.common.AbsActivity;
import moe.feng.bilinyan.ui.fragment.SectionDiscoverFragment;
import moe.feng.bilinyan.ui.fragment.SectionHomeFragment;
import moe.feng.bilinyan.view.NavigationView;

public class MainActivity extends AbsActivity implements NavigationView.OnNavigationItemSelectedListener {

	private DrawerLayout mDrawerLayout;
	private NavigationView mNavigationView;
	private ActionBarDrawerToggle mDrawerToggle;

	private Fragment[] fragments;

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fragments = new Fragment[]{
				new SectionHomeFragment(),
				new SectionDiscoverFragment(),
				new SectionDiscoverFragment(),
				new SectionDiscoverFragment()
		};

		setShowingFragment(fragments[0]);
	}

	@Override
	protected void setUpViews() {
		mDrawerLayout = $(R.id.drawer_layout);
		mNavigationView = $(R.id.navigation_view);
		mDrawerLayout.setDrawerListener(new DrawerListener());

		mNavigationView.setNavigationItemSelectedListener(this);

		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayUseLogoEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);

		mActionBar.setLogo(R.drawable.ic_bili_logo_white);

		mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout,
				mToolbar,
				R.string.abc_action_bar_home_description,
				R.string.abc_action_bar_home_description
		);

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		switch (item.getItemId()) {
			case R.id.item_home:
				setShowingFragment(fragments[0]);
				item.setChecked(true);
				return true;
			case R.id.item_following:
				setShowingFragment(fragments[1]);
				item.setChecked(true);
				return true;
			case R.id.item_discover:
				setShowingFragment(fragments[2]);
				item.setChecked(true);
				return true;
			case R.id.item_download:
				setShowingFragment(fragments[3]);
				item.setChecked(true);
				return true;
			case R.id.item_favourite:
				return true;
			case R.id.item_history:
				return true;
		}
		return false;
	}

	private void setShowingFragment(Fragment fragment) {
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
	}

	private class DrawerListener implements DrawerLayout.DrawerListener {

		@Override
		public void onDrawerOpened(View drawerView) {
			if (mDrawerToggle != null) {
				mDrawerToggle.onDrawerOpened(drawerView);
			}
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			if (mDrawerToggle != null) {
				mDrawerToggle.onDrawerClosed(drawerView);
			}
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			if (mDrawerToggle != null) {
				mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
			}
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			if (mDrawerToggle != null) {
				mDrawerToggle.onDrawerStateChanged(newState);
			}
		}

	}

	private class ActionBarDrawerToggle extends android.support.v7.app.ActionBarDrawerToggle {

		public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar,
		                             int openDrawerContentDescRes, int closeDrawerContentDescRes) {
			super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			super.onDrawerClosed(drawerView);
			invalidateOptionsMenu();
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			super.onDrawerOpened(drawerView);
			invalidateOptionsMenu();
		}

	}

}
