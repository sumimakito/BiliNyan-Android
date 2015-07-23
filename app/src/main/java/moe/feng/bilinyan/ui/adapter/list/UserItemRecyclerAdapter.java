package moe.feng.bilinyan.ui.adapter.list;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.api.UrlHelper;
import moe.feng.bilinyan.model.UserInfo;
import moe.feng.bilinyan.ui.common.AbsRecyclerViewAdapter;

public class UserItemRecyclerAdapter extends AbsRecyclerViewAdapter {

	private List<UserInfo> items;

	public UserItemRecyclerAdapter(RecyclerView recyclerView, List<UserInfo> items) {
		super(recyclerView);
		this.items = items;
	}

	@Override
	public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		bindContext(parent.getContext());
		return new ItemViewHolder(
				LayoutInflater.from(getContext())
						.inflate(R.layout.list_item_user, parent, false)
		);
	}

	@Override
	public void onBindViewHolder(ClickableViewHolder cvh, int pos) {
		super.onBindViewHolder(cvh, pos);
		if (cvh instanceof ItemViewHolder) {
			ItemViewHolder holder = (ItemViewHolder) cvh;
			String temp;
			holder.mUserNameText.setText(
					!TextUtils.isEmpty(temp = items.get(pos).name) ?
							temp : !TextUtils.isEmpty(temp = items.get(pos).uname) ?
							temp : items.get(pos).userid
			);

			Picasso.with(getContext())
					.load(UrlHelper.getFaceUrl(items.get(pos)))
					.placeholder(R.drawable.avatar_placeholder_empty)
					.into(holder.mAvatarImage);
		}
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public class ItemViewHolder extends ClickableViewHolder {

		private ImageView mAvatarImage;
		private AppCompatTextView mUserNameText;

		public ItemViewHolder(View itemView) {
			super(itemView);

			mAvatarImage = $(R.id.user_avatar_view);
			mUserNameText = $(R.id.user_name);
		}

	}

}
