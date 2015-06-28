package moe.feng.bilinyan.ui.common;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.util.Utility;

public abstract class AbsActivity extends AppCompatActivity {

	protected Toolbar mToolbar;
	protected ActionBar mActionBar;

	@SuppressLint("MissingSuperCall")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.onCreate(savedInstanceState, true);
	}

	protected void onCreate(Bundle savedInstanceState, boolean statusBarTranslucent) {
		if (statusBarTranslucent) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !Utility.isChrome()) {
				getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				getWindow().setStatusBarColor(Color.TRANSPARENT);
			}
		}

		super.onCreate(savedInstanceState);
	}

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
