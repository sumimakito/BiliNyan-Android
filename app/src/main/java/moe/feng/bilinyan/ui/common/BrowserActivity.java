package moe.feng.bilinyan.ui.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nispok.snackbar.Snackbar;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.api.UrlHelper;
import moe.feng.bilinyan.ui.VideoViewActivity;

public class BrowserActivity extends AbsActivity {

	private WebView mWebView;

	private String defaultUrl;
	private String title;

	private static final String EXTRA_URL = "extra_url", EXTRA_TITLE = "extra_title";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		defaultUrl = intent.getStringExtra(EXTRA_URL);
		title = intent.getStringExtra(EXTRA_TITLE);

		setContentView(R.layout.activity_simple_browser);
	}

	@Override
	protected void setUpViews() {
		mActionBar.setDisplayHomeAsUpEnabled(true);
		if (title != null) {
			mActionBar.setTitle(title);
		}

		mWebView = $(R.id.web_view);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (UrlHelper.isVideoUrl(url)) {
					VideoViewActivity.launch(BrowserActivity.this, UrlHelper.getAVfromVideoUrl(url));
					return true;
				}
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap bitmap) {
				mActionBar.setTitle(url.equals(defaultUrl) ? title : url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mActionBar.setTitle(view.getTitle());
				invalidateOptionsMenu();
			}

		});

		mWebView.loadUrl(defaultUrl);
	}

	public static void launch(AppCompatActivity activity, String defaultUrl, String title) {
		Intent intent = new Intent(activity, BrowserActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_URL, defaultUrl);
		intent.putExtra(EXTRA_TITLE, title);
		activity.startActivity(intent);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.menu_simple_browser, menu);

		menu.findItem(R.id.action_forward).setEnabled(mWebView.canGoForward());

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_forward) {
			mWebView.goForward();
			return true;
		} else if (id == R.id.action_reload) {
			mWebView.reload();
			return true;
		} else if (id == R.id.action_open_outside) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl()));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_copy_link) {
			setClipboardString(mWebView.getUrl());
			Snackbar.with(this)
					.text(R.string.tips_copy_link_okay)
					.show(this);
			return true;
		} else if (id == R.id.action_share) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT, mWebView.getTitle() + " " + mWebView.getUrl());
			intent.setType("text/plain");
			startActivity(Intent.createChooser(intent, mActionBar.getTitle()));
			return true;
		} else if (id == android.R.id.home) {
			super.onBackPressed();
			return true;
		}

		return false;
	}

	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	private void setClipboardString(String text) {
		ClipboardManager clipMan = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		clipMan.setPrimaryClip(ClipData.newPlainText(null, text));
	}

}
