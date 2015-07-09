package moe.feng.bilinyan.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.common.LazyFragment;

public class HomeBannerFragment extends LazyFragment {

	private ImageView mImageView;
	private TextView mTitleText;

	private static final String ARG_BANNER_ID = "arg_banner_id";

	public static HomeBannerFragment newInstance(int id) {
		HomeBannerFragment fragment = new HomeBannerFragment();
		Bundle data = new Bundle();
		data.putInt(ARG_BANNER_ID, id);
		fragment.setArguments(data);
		return fragment;
	}

	private HomeBannerFragment() {

	}

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_home_banner;
	}

	@Override
	public void finishCreateView(Bundle state) {
		mImageView = $(R.id.banner_image);
		mTitleText = $(R.id.banner_title);

		$(R.id.banner_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		int paddingBottom = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
		paddingBottom += getResources().getDimensionPixelSize(R.dimen.circle_indicator_default_height);

		int paddingStart = mTitleText.getPaddingStart();
		int paddingEnd = mTitleText.getPaddingEnd();
		int paddingTop = mTitleText.getPaddingTop();

		mTitleText.setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom);

		switch (getArguments().getInt(ARG_BANNER_ID)) {
			case 0:
				mTitleText.setText("那就是声优！");
				mImageView.setImageResource(R.drawable.test_banner_0);
				break;
			case 1:
				mTitleText.setText("哔哩哔哩夏日鬼畜大赛");
				mImageView.setImageResource(R.drawable.test_banner_1);
				break;
			case 2:
				mTitleText.setText("VocaloidP主介绍 TOA");
				mImageView.setImageResource(R.drawable.test_banner_2);
				break;
		}
	}

	public void setImageTransitionY(float y) {
		mImageView.setTranslationY(y);
	}

}
