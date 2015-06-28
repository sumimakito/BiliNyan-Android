package moe.feng.bilinyan.ui.common;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class LazyFragment extends Fragment {

	private View parentView;
	private Activity activity;

	public abstract @LayoutRes int getLayoutResId();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		parentView = inflater.inflate(getLayoutResId(), container, false);
		activity = getActivity();
		finishCreateView(state);
		return parentView;
	}

	public abstract void finishCreateView(Bundle state);

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.activity = null;
	}

	public Context getApplicationContext() {
		return this.activity == null ?
				(getActivity() == null ? null : getActivity().getApplicationContext()) :
				this.activity.getApplicationContext();
	}

	public <T extends View> T $(int id) {
		return (T) parentView.findViewById(id);
	}

}
