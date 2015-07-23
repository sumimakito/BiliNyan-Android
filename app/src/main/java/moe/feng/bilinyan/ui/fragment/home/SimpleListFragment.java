package moe.feng.bilinyan.ui.fragment.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.model.Index;
import moe.feng.bilinyan.model.VideoItemInfo;
import moe.feng.bilinyan.ui.VideoViewActivity;
import moe.feng.bilinyan.ui.adapter.list.RecommendRecyclerAdapter;
import moe.feng.bilinyan.ui.common.AbsRecyclerViewAdapter;
import moe.feng.bilinyan.ui.fragment.SectionHomeFragment;

public class SimpleListFragment extends BaseHomeFragment {

	private RecyclerView mRecyclerView;
	private RecommendRecyclerAdapter mAdapter;

	private Index mIndexData;
	private int indexType;
	private ArrayList<VideoItemInfo> items;

	public static final int TYPE_CARTOON = 1, TYPE_MUSIC = 3, TYPE_GAME = 4, TYPE_ENTERTAINMENT = 5,
			TYPE_TV_SERIES = 11, TYPE_ANIME = 13, TYPE_MOVIE = 23, TYPE_TECHNOLOGY = 36,
			TYPE_DANCE = 129, TYPE_FUNNY = 119;

	private static final String ARG_TYPE = "arg_type";

	public static SimpleListFragment newInstance(int type) {
		SimpleListFragment fragment = new SimpleListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_TYPE, type);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_tab_recycler;
	}

	@Override
	public void finishCreateView(Bundle state) {
		indexType = getArguments().getInt(ARG_TYPE);

		mRecyclerView = $(R.id.recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

		if (getParentFragment() != null && getParentFragment() instanceof SectionHomeFragment) {
			mIndexData = ((SectionHomeFragment) getParentFragment()).getIndexData();
			makeUpItems();
		}

		mAdapter = new RecommendRecyclerAdapter(mRecyclerView, items);
		setUpRecyclerAdapter(mAdapter);
	}

	private void makeUpItems() {
		if (mIndexData != null) {
			switch (indexType) {
				case TYPE_ANIME:
					items = mIndexData.typeAnime.list();
					break;
				case TYPE_CARTOON:
					items = mIndexData.typeCartoon.list();
					break;
				case TYPE_DANCE:
					items = mIndexData.typeDance.list();
					break;
				case TYPE_ENTERTAINMENT:
					items = mIndexData.typeEntertainment.list();
					break;
				case TYPE_FUNNY:
					items = mIndexData.typeFunny.list();
					break;
				case TYPE_GAME:
					items = mIndexData.typeGame.list();
					break;
				case TYPE_MOVIE:
					items = mIndexData.typeMovie.list();
					break;
				case TYPE_MUSIC:
					items = mIndexData.typeMusic.list();
					break;
				case TYPE_TECHNOLOGY:
					items = mIndexData.typeTechnology.list();
					break;
				case TYPE_TV_SERIES:
					items = mIndexData.typeTvSeries.list();
					break;
			}
		} else {
			items = new ArrayList<>();
		}
	}

	private void setUpRecyclerAdapter(RecommendRecyclerAdapter adapter) {
		adapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder) {
				VideoViewActivity.launch(getSupportActivity(), items.get(position));
			}
		});
		mRecyclerView.setAdapter(adapter);
	}

	@Override
	public void scrollToTop() {
		mRecyclerView.smoothScrollToPosition(0);
	}

	@Override
	public boolean canScrollVertically(int direction) {
		return mRecyclerView != null && mRecyclerView.canScrollVertically(direction);
	}

	@Override
	public void notifyIndexDataUpdate(Index data) {
		if (data == null) return;
		mIndexData = data;
		makeUpItems();
		mAdapter = new RecommendRecyclerAdapter(mRecyclerView, items);
		setUpRecyclerAdapter(mAdapter);
	}

}
