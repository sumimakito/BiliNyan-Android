package moe.feng.bilinyan.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 这个方法似乎只能在 Chrome 45 上实现，目前 Stable 还在 43
 *
 */
public class ChromeTabCompat {

	private String url;
	private Intent intent;
	private Bundle extra;

	static final String STABLE_PACKAGE = "com.android.chrome";
	static final String BETA_PACKAGE = "com.chrome.beta";
	static final String DEV_PACKAGE = "com.chrome.dev";
	static final String LOCAL_PACKAGE = "com.google.android.apps.chrome";

	private static String mPackageNameToUse;

	public static final String EXTRA_TITLE_VISIBILITY_STATE = "android.support.customtabs.extra.TITLE_VISIBILITY";
	private static final int NO_TITLE = 0;
	private static final int SHOW_PAGE_TITLE = 1;

	public static final String EXTRA_CLOSE_BUTTON_STYLE = "android.support.customtabs.extra.CLOSE_BUTTON_STYLE";
	public static final int CLOSE_BUTTON_CROSS = 0;
	public static final int CLOSE_BUTTON_ARROW = 1;

	private static final String EXTRA_CUSTOM_TABS_SESSION = "android.support.customtabs.extra.SESSION";
	private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
	private static final String KEY_CUSTOM_TABS_ICON = "android.support.customtabs.customaction.ICON";
	public static final String KEY_CUSTOM_TABS_PENDING_INTENT = "android.support.customtabs.customaction.PENDING_INTENT";
	public static final String EXTRA_CUSTOM_TABS_ACTION_BUTTON_BUNDLE = "android.support.customtabs.extra.ACTION_BUNDLE_BUTTON";
	public static final String KEY_CUSTOM_TABS_MENU_TITLE = "android.support.customtabs.customaction.MENU_ITEM_TITLE";
	public static final String EXTRA_CUSTOM_TABS_MENU_ITEMS = "android.support.customtabs.extra.MENU_ITEMS";
	public static final String EXTRA_CUSTOM_TABS_EXIT_ANIMATION_BUNDLE = "android.support.customtabs.extra.EXIT_ANIMATION_BUNDLE";

	public static final String ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService";

	private ChromeTabCompat(String url) {
		this.url = url;
		intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		extra = new Bundle();
	}

	public void start(Context context) {
		context.startActivity(intent);
	}

	public static boolean isAvailable(Context context) {
		return getPackageNameToUse(context) != null;
	}

	public static String getPackageNameToUse(Context context) {
		if (mPackageNameToUse != null) return mPackageNameToUse;

		PackageManager pm = context.getPackageManager();
		// Get default VIEW intent handler.
		Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));
		ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
		String defaultViewHandlerPackageName = null;
		if (defaultViewHandlerInfo != null) {
			defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
		}

		// Get all apps that can handle VIEW intents.
		List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
		List<String> packagesSupportingCustomTabs = new ArrayList<>();
		for (ResolveInfo info : resolvedActivityList) {
			Intent serviceIntent = new Intent();
			serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
			serviceIntent.setPackage(info.activityInfo.packageName);
			if (pm.resolveService(serviceIntent, 0) != null) {
				packagesSupportingCustomTabs.add(info.activityInfo.packageName);
			}
		}

		// Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
		// and service calls.
		if (packagesSupportingCustomTabs.isEmpty()) {
			mPackageNameToUse = null;
		} else if (packagesSupportingCustomTabs.size() == 1) {
			mPackageNameToUse = packagesSupportingCustomTabs.get(0);
		} else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
				&& !hasSpecializedHandlerIntents(context, activityIntent)
				&& packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
			mPackageNameToUse = defaultViewHandlerPackageName;
		} else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
			mPackageNameToUse = STABLE_PACKAGE;
		} else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
			mPackageNameToUse = BETA_PACKAGE;
		} else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
			mPackageNameToUse = DEV_PACKAGE;
		} else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
			mPackageNameToUse = LOCAL_PACKAGE;
		}
		return mPackageNameToUse;
	}

	private static boolean hasSpecializedHandlerIntents(Context context, Intent intent) {
		try {
			PackageManager pm = context.getPackageManager();
			List<ResolveInfo> handlers = pm.queryIntentActivities(
					intent,
					PackageManager.GET_RESOLVED_FILTER);
			if (handlers == null || handlers.size() == 0) {
				return false;
			}
			for (ResolveInfo resolveInfo : handlers) {
				IntentFilter filter = resolveInfo.filter;
				if (filter == null) continue;
				if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue;
				if (resolveInfo.activityInfo == null) continue;
				return true;
			}
		} catch (RuntimeException e) {
		}
		return false;
	}

	public static class Builder {

		private ChromeTabCompat tab;

		public Builder(String url) {
			this.tab = new ChromeTabCompat(url);
			if (Build.VERSION.SDK_INT >= 18) {
				this.tab.extra.putBinder(EXTRA_CUSTOM_TABS_SESSION, null);
			}
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
		public Builder sessionBinder(IBinder binder) {
			this.tab.extra.putBinder(EXTRA_CUSTOM_TABS_SESSION, binder);
			return this;
		}

		public Builder toolbarColor(int color) {
			this.tab.extra.putInt(EXTRA_CUSTOM_TABS_TOOLBAR_COLOR, color);
			return this;
		}

		public Builder setShowTitle(boolean showTitle) {
			int titleVisibilityState = showTitle ? SHOW_PAGE_TITLE : NO_TITLE;
			this.tab.extra.putInt(EXTRA_TITLE_VISIBILITY_STATE, titleVisibilityState);
			return this;
		}

		public Builder setCloseButtonStyle(int style) {
			if (style == CLOSE_BUTTON_CROSS || style == CLOSE_BUTTON_ARROW) {
				this.tab.extra.putInt(EXTRA_CLOSE_BUTTON_STYLE, style);
			}
			return this;
		}

		public ChromeTabCompat build() {
			this.tab.intent.putExtras(this.tab.extra);
			return this.tab;
		}

	}

}
