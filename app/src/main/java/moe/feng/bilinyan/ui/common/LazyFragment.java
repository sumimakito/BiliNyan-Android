package moe.feng.bilinyan.ui.common;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.feng.bilinyan.R;
import moe.feng.material.statusbar.AppBarLayout;
import moe.feng.material.statusbar.StatusBarHeaderView;

public abstract class LazyFragment extends Fragment {

	private View parentView;
	private AppCompatActivity activity;
	private LayoutInflater inflater;

	private StatusBarHeaderView mStatusBarHeaderView;
	private AppBarLayout mAppBarLayout;

	public abstract @LayoutRes int getLayoutResId();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		this.inflater = inflater;
		parentView = inflater.inflate(getLayoutResId(), container, false);
		activity = getSupportActivity();
		try {
			mStatusBarHeaderView = $(R.id.status_bar_header_view);
		} catch (Exception e) {

		}
		try {
			mAppBarLayout = $(R.id.appbar_layout);
		} catch (Exception e) {

		}
		finishCreateView(state);
		return parentView;
	}

	public abstract void finishCreateView(Bundle state);

	@Override
	public void onResume() {
		super.onResume();
		if (mStatusBarHeaderView != null) {
			mStatusBarHeaderView.invalidate();
		}
		if (mAppBarLayout != null) {
			mAppBarLayout.invalidate();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (AppCompatActivity) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.activity = null;
	}

	public AppCompatActivity getSupportActivity() {
		return (AppCompatActivity) super.getActivity();
	}

	public ActionBar getSupportActionBar() {
		return getSupportActivity().getSupportActionBar();
	}

	public Context getApplicationContext() {
		return this.activity == null ?
				(getActivity() == null ? null : getActivity().getApplicationContext()) :
				this.activity.getApplicationContext();
	}

	protected LayoutInflater getLayoutInflater() {
		return inflater;
	}

	protected View getParentView() {
		return parentView;
	}

	public <T extends View> T $(int id) {
		return (T) parentView.findViewById(id);
	}

}
