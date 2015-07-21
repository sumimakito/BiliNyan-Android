package moe.feng.bilinyan.ui.adapter.list;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.model.VideoItemInfo;
import moe.feng.bilinyan.ui.common.AbsRecyclerViewAdapter;

public class VideoItemRecyclerAdapter extends AbsRecyclerViewAdapter {

	private List<VideoItemInfo> items;

	public VideoItemRecyclerAdapter(RecyclerView recyclerView, List<VideoItemInfo> items) {
		super(recyclerView);
		this.items = items;
	}

	@Override
	public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		bindContext(parent.getContext());
		return new ItemViewHolder(
				LayoutInflater.from(getContext())
						.inflate(R.layout.list_item_video_item, parent, false)
		);
	}

	@Override
	public void onBindViewHolder(ClickableViewHolder cvh, int pos) {
		super.onBindViewHolder(cvh, pos);
		if (cvh instanceof ItemViewHolder) {
			ItemViewHolder holder = (ItemViewHolder) cvh;
			holder.mTitleText.setText(items.get(pos).title);
			holder.mUperNameText.setText(
					String.format(
							getContext().getString(R.string.item_info_uper_format),
							items.get(pos).author
					)
			);
			holder.mNumber0Text.setText(items.get(pos).play);
			holder.mNumber1Text.setText(Integer.toString(items.get(pos).video_review));

			Picasso.with(getContext()).load(items.get(pos).pic).into(holder.mPreviewImage);
		}
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public class ItemViewHolder extends ClickableViewHolder {

		private ImageView mPreviewImage;
		private AppCompatTextView mTitleText, mNumber0Text, mNumber1Text, mUperNameText;
		private ImageView mNumber0Icon, mNumber1Icon;

		public ItemViewHolder(View itemView) {
			super(itemView);

			mPreviewImage = $(R.id.iv_preview);
			mTitleText = $(R.id.tv_title);
			mUperNameText = $(R.id.tv_uper);
			mNumber0Text = $(R.id.tv_number_0);
			mNumber1Text = $(R.id.tv_number_1);
			mNumber0Icon = $(R.id.icon_number_0);
			mNumber1Icon = $(R.id.icon_number_1);
		}

	}

}
