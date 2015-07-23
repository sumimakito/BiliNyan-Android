package moe.feng.bilinyan.ui;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.api.UrlHelper;
import moe.feng.bilinyan.api.VideoApi;
import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.VideoItemInfo;
import moe.feng.bilinyan.model.VideoViewInfo;
import moe.feng.bilinyan.ui.adapter.list.VideoPartListAdapter;
import moe.feng.bilinyan.ui.common.AbsActivity;
import moe.feng.bilinyan.util.AsyncTask;
import moe.feng.bilinyan.util.Utility;
import moe.feng.bilinyan.view.CircleProgressView;
import moe.feng.bilinyan.view.ObservableScrollView;
import moe.feng.bilinyan.view.UserTagView;
import moe.feng.material.statusbar.AppBarLayout;

public class VideoViewActivity extends AbsActivity implements ObservableScrollView.OnScrollChangeListener {

	private ObservableScrollView mScrollView;
	private ImageView mPreviewView;
	private AppBarLayout mAppBarBackground;
	private FrameLayout mAppBarContainer;
	private FloatingActionButton mFAB;
	private CircleProgressView mCircleProgress;
	private LinearLayout mContainer;
	private AppCompatTextView mTitleText, mPlayTimeText, mReviewCountText, mDescText, mCreatedAtText;
	private UserTagView mAuthorTagView;
	private ExpandableHeightListView mVideoPartList;

	private int av;
	private VideoItemInfo itemInfo;
	private VideoViewInfo viewInfo;
	private ArrayList<VideoViewInfo> parts;

	private boolean isPlayingFABAnimation = false;

	private int APP_BAR_HEIGHT, TOOLBAR_HEIGHT, STATUS_BAR_HEIGHT = 0, minHeight = 0;

	private static String EXTRA_ITEM_INFO = "extra_item_info", EXTRA_AV = "extra_av";

	public static String TAG = VideoViewActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		APP_BAR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.appbar_parallax_max_height);
		TOOLBAR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
		if (Build.VERSION.SDK_INT >= 19) {
			STATUS_BAR_HEIGHT = Utility.getStatusBarHeight(getApplicationContext());
		}
		minHeight = APP_BAR_HEIGHT - TOOLBAR_HEIGHT - STATUS_BAR_HEIGHT;

		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_ITEM_INFO)) {
			itemInfo = VideoItemInfo.createFromJson(intent.getStringExtra(EXTRA_ITEM_INFO));
		} else if (intent.hasExtra(EXTRA_AV)) {
			av = intent.getIntExtra(EXTRA_AV, -1);
		}

		setContentView(R.layout.activity_video_view);

		if (itemInfo != null) {
			Picasso.with(this).load(UrlHelper.getClearVideoPreviewUrl(itemInfo.pic)).into(mPreviewView);
		}

		startGetTask();
	}

	@Override
	protected void setUpViews() {
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("");

		mAppBarBackground = $(R.id.appbar_background);
		mAppBarContainer = $(R.id.appbar_container);
		mScrollView = $(R.id.scroll_view);
		mPreviewView = $(R.id.video_preview);
		mFAB = $(R.id.fab);
		mCircleProgress = $(R.id.circle_progress);
		mContainer = $(R.id.container_view);
		mTitleText = $(R.id.tv_title);
		mPlayTimeText = $(R.id.tv_play_time);
		mReviewCountText = $(R.id.tv_review_count);
		mDescText = $(R.id.tv_description);
		mCreatedAtText = $(R.id.tv_created_at);
		mAuthorTagView = $(R.id.author_tag);
		mVideoPartList = $(R.id.video_part_list);

		mFAB.setTranslationY(-getResources().getDimension(R.dimen.floating_action_button_size_half));
		mScrollView.addOnScrollChangeListener(this);
	}

	public static void launch(AppCompatActivity activity, VideoItemInfo itemInfo) {
		Intent intent = new Intent(activity, VideoViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_ITEM_INFO, itemInfo.toJsonString());
		activity.startActivity(intent);
	}

	public static void launch(AppCompatActivity activity, int aid) {
		Intent intent = new Intent(activity, VideoViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_AV, aid);
		activity.startActivity(intent);
	}

	private void startGetTask() {
		mCircleProgress.setVisibility(View.VISIBLE);
		mCircleProgress.spin();
		mContainer.setVisibility(View.GONE);

		new ViewInfoGetTask().execute();
	}

	private void finishGetTask() {
		mCircleProgress.setVisibility(View.GONE);
		mCircleProgress.stopSpinning();
		mContainer.setVisibility(View.VISIBLE);

		// 加载分P，当页码只有一时直接获取当前信息
		if (viewInfo.pages.equals("1")) {
			parts = new ArrayList<>();
			parts.add(viewInfo);
			finishPartsGetTask();
		} else {
			new PartsGetTask().execute();
		}

		// 当没有传入 VideoItemInfo 时需要加载出信息后再读取图片
		if (itemInfo == null) {
			Picasso.with(this).load(UrlHelper.getClearVideoPreviewUrl(viewInfo.pic)).into(mPreviewView);
		}

		// 加载信息
		mTitleText.setText(viewInfo.title);
		mPlayTimeText.setText(String.format(getString(R.string.info_play_times_format), viewInfo.play));
		mReviewCountText.setText(String.format(getString(R.string.info_reviews_format), viewInfo.review));
		mDescText.setText(viewInfo.description);
		mCreatedAtText.setText(viewInfo.created_at);
		mAuthorTagView.setUpWithInfo(this, viewInfo.author, viewInfo.mid, viewInfo.face);
	}

	private void finishPartsGetTask() {
		mVideoPartList.setAdapter(new VideoPartListAdapter(getApplicationContext(), parts));
		mVideoPartList.setExpanded(true);
		mVideoPartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			}
		});
	}

	@Override
	public void onScrollChanged(ObservableScrollView view, int x, int y, int oldx, int oldy) {
		setViewsTranslation(Math.min(minHeight, y));
	}

	private void setViewsTranslation(int target) {
		mAppBarContainer.setTranslationY(-target);
		mAppBarBackground.setTranslationY(target);
		float alpha = Math.min(1, -mAppBarContainer.getTranslationY() / (float) minHeight);
		mAppBarBackground.setAlpha(alpha);

		mFAB.setTranslationY(-getResources().getDimension(R.dimen.floating_action_button_size_half)-target);
		if (alpha > 0.8f && !isPlayingFABAnimation) {
			hideFAB();
		} else if (alpha < 0.65f && !isPlayingFABAnimation) {
			showFAB();
		}

		mPreviewView.setTranslationY(target * 0.7f);
	}

	private void showFAB() {
		mFAB.animate().scaleX(1f).scaleY(1f)
				.setInterpolator(new OvershootInterpolator())
				.setListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
						isPlayingFABAnimation = true;
					}

					@Override
					public void onAnimationEnd(Animator animator) {
						isPlayingFABAnimation = false;
						if (mAppBarBackground.getAlpha() > 0.8f) {
							hideFAB();
						}
					}

					@Override
					public void onAnimationCancel(Animator animator) {
						isPlayingFABAnimation = false;
					}

					@Override
					public void onAnimationRepeat(Animator animator) {

					}
				})
				.start();
	}

	private void hideFAB() {
		mFAB.animate().scaleX(0f).scaleY(0f)
				.setInterpolator(new AccelerateInterpolator())
				.setListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
						isPlayingFABAnimation = true;
					}

					@Override
					public void onAnimationEnd(Animator animator) {
						isPlayingFABAnimation = false;
						if (mAppBarBackground.getAlpha() < 0.65f) {
							showFAB();
						}
					}

					@Override
					public void onAnimationCancel(Animator animator) {
						isPlayingFABAnimation = false;
					}

					@Override
					public void onAnimationRepeat(Animator animator) {

					}
				})
				.start();
	}

	public class ViewInfoGetTask extends AsyncTask<Void, Void, BasicMessage<VideoViewInfo>> {

		@Override
		protected BasicMessage<VideoViewInfo> doInBackground(Void... params) {
			return VideoApi.getVideoViewInfo(itemInfo != null ? itemInfo.aid : av, 0, false);
		}

		@Override
		protected void onPostExecute(BasicMessage<VideoViewInfo> msg) {
			if (msg != null && msg.getCode() == BasicMessage.CODE_SUCCEED) {
				viewInfo = msg.getObject();
				finishGetTask();
			} else {

			}
		}

	}

	public class PartsGetTask extends AsyncTask<Void, Void, BasicMessage> {

		@Override
		protected BasicMessage doInBackground(Void... params) {
			BasicMessage msg = new BasicMessage();

			parts = new ArrayList<>();
			if (viewInfo != null) {
				for (int i = 0; i < Integer.valueOf(viewInfo.pages); i++) {
					BasicMessage<VideoViewInfo> b = VideoApi.getVideoViewInfo(
							itemInfo != null ? itemInfo.aid : av,
							i,
							false
					);
					if (b != null && b.getCode() == BasicMessage.CODE_SUCCEED) {
						parts.add(b.getObject());
					}
				}
				msg.setCode(BasicMessage.CODE_SUCCEED);
			} else {
				msg.setCode(BasicMessage.CODE_ERROR);
			}

			return msg;
		}

		@Override
		protected void onPostExecute(BasicMessage msg) {
			if (msg.getCode() == BasicMessage.CODE_SUCCEED) {
				finishPartsGetTask();
			}
		}

	}

}
