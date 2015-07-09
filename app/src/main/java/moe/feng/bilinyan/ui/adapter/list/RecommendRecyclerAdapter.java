package moe.feng.bilinyan.ui.adapter.list;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.model.RecommendList;
import moe.feng.bilinyan.model.VideoItemInfo;
import moe.feng.bilinyan.ui.common.AbsRecyclerViewAdapter;

public class RecommendRecyclerAdapter extends AbsRecyclerViewAdapter {

	private RecommendList mList;

	private static final int TYPE_HEADER = 0, TYPE_CONTENT = 1;

	public RecommendRecyclerAdapter(RecyclerView recyclerView, RecommendList list) {
		super(recyclerView);
		this.mList = list;
	}

	@Override
	public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		bindContext(parent.getContext());
		View v = LayoutInflater.from(getContext())
				.inflate(
						viewType == TYPE_HEADER ?
								R.layout.list_item_home_header :
								R.layout.card_item_home_recommend,
						parent,
						false
				);
		return viewType == TYPE_HEADER ? new ClickableViewHolder(v) : new CardHolder(v);
	}

	@Override
	public void onBindViewHolder(ClickableViewHolder cvh, int position) {
		super.onBindViewHolder(cvh, position);
		if (cvh instanceof CardHolder) {
			CardHolder holder = (CardHolder) cvh;

			holder.mTitleView.setText(getVideoItem(position).title);

			Picasso.with(getContext())
					.load(Uri.parse(getVideoItem(position).pic))
					.centerInside()
					.into(holder.mPreviewImage);
		}
	}

	public VideoItemInfo getVideoItem(int pos) {
		return mList.lists.get(pos - 1);
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? TYPE_HEADER : TYPE_CONTENT;
	}

	@Override
	public int getItemCount() {
		return 1 + (mList != null ? mList.lists.size() : 0);
	}

	public class CardHolder extends ClickableViewHolder {

		public ImageView mPreviewImage;
		public TextView mTitleView;

		public CardHolder(View itemView) {
			super(itemView);
			mPreviewImage = $(R.id.video_preview);
			mTitleView = $(R.id.video_title);
		}

	}

}
