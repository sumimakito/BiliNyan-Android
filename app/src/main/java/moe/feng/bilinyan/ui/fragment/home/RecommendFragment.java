package moe.feng.bilinyan.ui.fragment.home;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.api.RecommendApi;
import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.RecommendList;
import moe.feng.bilinyan.ui.adapter.list.RecommendRecyclerAdapter;
import moe.feng.bilinyan.ui.fragment.SectionHomeFragment;
import moe.feng.bilinyan.util.AsyncTask;
import moe.feng.bilinyan.view.ObservableRecyclerView;

@SuppressLint("ValidFragment")
public class RecommendFragment extends BaseHomeFragment {

	private ObservableRecyclerView mRecyclerView;
	private RecommendRecyclerAdapter mAdapter;

	public static RecommendFragment newInstance(int minimumY) {
		RecommendFragment fragment = new RecommendFragment(minimumY);
		return fragment;
	}

	private RecommendFragment(int y) {
		super(y);
	}

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_tab_recycler;
	}

	@Override
	public void finishCreateView(Bundle state) {
		mRecyclerView = $(R.id.scrollable);

		mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.addOnScrollChangeListener(new ObservableRecyclerView.OnScrollChangeListener() {
			@Override
			public void onScrollChanged(ObservableRecyclerView view, int x, int y, int oldx, int oldy) {
				Fragment fragment = getParentFragment();
				if (fragment != null && fragment instanceof SectionHomeFragment) {
					((SectionHomeFragment) fragment).onScrollChanged(x, y, oldx, oldy);
				}
			}
		});

		mAdapter = new RecommendRecyclerAdapter(mRecyclerView, null);
		setRecyclerAdapter(mAdapter);

		new GetRecommendTask().execute();
	}

	private void setRecyclerAdapter(RecommendRecyclerAdapter adapter) {
		mRecyclerView.setAdapter(adapter);
		// mRecyclerView.scrollTo(mRecyclerView.getScrollX(), getMinimumScrollY());
	}

	@Override
	public void onMinimumScrollYSet(int y) {
		if (mRecyclerView != null && mRecyclerView.getScrollY() <= y) {
			// mRecyclerView.scrollTo(mRecyclerView.getScrollX(), y);
		}
	}

	@Override
	public void scrollToTop() {
		mRecyclerView.smoothScrollToPosition(0);
	}

	@Override
	public void scrollToMinimumY() {
		mRecyclerView.smoothScrollToPosition(1);
	}

	@Override
	public int getNowScrollY() {
		return mRecyclerView != null ? mRecyclerView.getScrollY() : 0;
	}

	private class GetRecommendTask extends AsyncTask<Void, Void, BasicMessage<RecommendList>> {

		@Override
		protected BasicMessage<RecommendList> doInBackground(Void... params) {
			return RecommendApi.getList("20", null, null, RecommendApi.ORDER_NONE);
		}

		@Override
		protected void onPostExecute(BasicMessage<RecommendList> result) {
			if (result != null) {
				if (result.getCode() == BasicMessage.CODE_SUCCEED) {
					mAdapter = new RecommendRecyclerAdapter(mRecyclerView, result.getObject());
					setRecyclerAdapter(mAdapter);
				}
			}
		}

	}

}
