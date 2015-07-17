package moe.feng.bilinyan.ui.common;

import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import moe.feng.bilinyan.R;
import moe.feng.material.statusbar.TranslucentSBActivity;

public abstract class AbsActivity extends TranslucentSBActivity {

	protected Toolbar mToolbar;
	protected ActionBar mActionBar;

	protected abstract void setUpViews();

	@Override
	public void setContentView(@LayoutRes int layoutResId) {
		super.setContentView(layoutResId);

		try {
			mToolbar = $(R.id.toolbar);
			setSupportActionBar(mToolbar);
		} catch (Exception e) {
			Log.e("setContentView", "Cannot find toolbar.");
		}
		mActionBar = getSupportActionBar();

		setUpViews();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected <T extends View> T $(int id) {
		return (T) findViewById(id);
	}

}
