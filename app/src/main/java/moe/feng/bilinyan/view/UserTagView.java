package moe.feng.bilinyan.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.UserInfoActivity;

public class UserTagView extends FrameLayout {

	private CardView cardView;
	private CircleImageView avatarView;
	private TextView userNameText;

	private OnClickListener onClickListener;

	private AppCompatActivity activity;
	private String name;
	private int mid = -1;
	private String avatarUrl;

	public UserTagView(Context context) {
		this(context, null);
	}

	public UserTagView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UserTagView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		cardView = (CardView) LayoutInflater.from(context).inflate(R.layout.widget_user_tag_view, null);
		avatarView = (CircleImageView) cardView.findViewById(R.id.user_avatar);
		userNameText = (TextView) cardView.findViewById(R.id.user_name);

		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				getResources().getDimensionPixelSize(R.dimen.user_tag_view_height)
		);
		this.addView(cardView, lp);

		cardView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mid != -1 && activity != null) {
					UserInfoActivity.launch(activity, name, mid, avatarUrl);
				} else if (onClickListener != null) {
					onClickListener.onClick(view);
				}
			}
		});
	}

	public void setAvatar(Bitmap bitmap) {
		avatarView.setImageBitmap(bitmap);
	}

	public void setAvatar(Drawable drawable) {
		avatarView.setImageDrawable(drawable);
	}

	public void setAvatar(@DrawableRes int id) {
		avatarView.setImageResource(id);
	}

	public CircleImageView getAvatarView() {
		return this.avatarView;
	}

	public void setUserName(String userName) {
		userNameText.setText(userName);
	}

	public TextView getUserNameText() {
		return this.userNameText;
	}

	public void setUpWithInfo(AppCompatActivity activity, String name, int mid, String avatarUrl) {
		this.activity = activity;
		this.name = name;
		this.mid = mid;
		this.avatarUrl = avatarUrl;
		this.setUserName(name);

		Picasso.with(getContext()).load(this.avatarUrl).into(avatarView);
	}

	@Override
	public void setOnClickListener(OnClickListener listener) {
		this.onClickListener = listener;
	}

}
