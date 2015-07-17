package moe.feng.bilinyan.ui.fragment.home;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.api.RecommendApi;
import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.RecommendList;
import moe.feng.bilinyan.model.VideoItemInfo;
import moe.feng.bilinyan.ui.fragment.SectionHomeFragment;
import moe.feng.bilinyan.util.AsyncTask;
import moe.feng.bilinyan.view.ObservableScrollView;

@SuppressLint("ValidFragment")
public class RecommendFragment extends BaseHomeFragment {

	private ObservableScrollView mScrollView;

	private List<FrameLayout> cards;

	public static RecommendFragment newInstance(int minimumY) {
		RecommendFragment fragment = new RecommendFragment(minimumY);
		return fragment;
	}

	private RecommendFragment(int y) {
		super(y);
	}

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_tab_recommend;
	}

	@Override
	public void finishCreateView(Bundle state) {
		mScrollView = $(R.id.scrollable);

		cards = new ArrayList<>();
		cards.add((FrameLayout) $(R.id.card_frame_0));
		cards.add((FrameLayout) $(R.id.card_frame_1));
		cards.add((FrameLayout) $(R.id.card_frame_2));
		cards.add((FrameLayout) $(R.id.card_frame_3));
		cards.add((FrameLayout) $(R.id.card_frame_4));
		cards.add((FrameLayout) $(R.id.card_frame_5));

		mScrollView.addOnScrollChangeListener(new ObservableScrollView.OnScrollChangeListener() {
			@Override
			public void onScrollChanged(ObservableScrollView view, int x, int y, int oldx, int oldy) {
				Fragment fragment = getParentFragment();
				if (fragment != null && fragment instanceof SectionHomeFragment) {
					((SectionHomeFragment) fragment).onScrollChanged(x, y, oldx, oldy);
				}
			}
		});
		mScrollView.scrollTo(mScrollView.getScrollX(), getMinimumScrollY());

		new GetRecommendTask().execute();
	}

	@Override
	public void onMinimumScrollYSet(int y) {
		if (mScrollView != null && mScrollView.getScrollY() <= y) {
			mScrollView.scrollTo(mScrollView.getScrollX(), y);
		}
	}

	@Override
	public void scrollToTop() {
		mScrollView.smoothScrollTo(mScrollView.getScrollX(), 0);
	}

	@Override
	public void scrollToMinimumY() {
		mScrollView.smoothScrollTo(mScrollView.getScrollX(), getMinimumScrollY());
	}

	@Override
	public int getNowScrollY() {
		return mScrollView != null ? mScrollView.getScrollY() : 0;
	}

	private class GetRecommendTask extends AsyncTask<Void, Void, BasicMessage<RecommendList>> {

		@Override
		protected BasicMessage<RecommendList> doInBackground(Void... params) {
			return RecommendApi.getList("20", null, "6", RecommendApi.ORDER_NONE);
		}

		@Override
		protected void onPostExecute(BasicMessage<RecommendList> result) {
			if (result != null) {
				if (result.getCode() == BasicMessage.CODE_SUCCEED) {
					for (int i = 0; i < 6; i++) {
						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
								FrameLayout.LayoutParams.MATCH_PARENT,
								FrameLayout.LayoutParams.MATCH_PARENT
						);

						cards.get(i).addView(createVideoCard(result.getObject().lists.get(i)), lp);
					}
				}
			}
		}

	}

	private CardView createVideoCard(VideoItemInfo info) {
		CardView view = (CardView) getLayoutInflater().inflate(R.layout.card_item_home_recommend, null);

		((TextView) view.findViewById(R.id.video_title)).setText(info.title);
		Picasso.with(getApplicationContext())
				.load(info.pic)
				.into((ImageView) view.findViewById(R.id.video_preview));

		return view;
	}

}
