package moe.feng.bilinyan.ui;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.model.VideoItemInfo;
import moe.feng.bilinyan.model.VideoViewInfo;
import moe.feng.bilinyan.ui.common.AbsActivity;
import moe.feng.bilinyan.util.Utility;
import moe.feng.bilinyan.view.ObservableScrollView;
import moe.feng.material.statusbar.AppBarLayout;

public class VideoViewActivity extends AbsActivity implements ObservableScrollView.OnScrollChangeListener {

	private ObservableScrollView mScrollView;
	private ImageView mPreviewView;
	private AppBarLayout mAppBarBackground;
	private FrameLayout mAppBarContainer;
	private FloatingActionButton mFAB;

	private VideoItemInfo itemInfo;
	private VideoViewInfo viewInfo;

	private boolean isPlayingFABAnimation = false;

	private int APP_BAR_HEIGHT, TOOLBAR_HEIGHT, STATUS_BAR_HEIGHT = 0, minHeight = 0;

	private static String EXTRA_ITEM_INFO = "extra_item_info";

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
		itemInfo = VideoItemInfo.createFromJson(intent.getStringExtra(EXTRA_ITEM_INFO));

		setContentView(R.layout.activity_video_view);

		Picasso.with(this).load(itemInfo.pic).into(mPreviewView);
	}

	@Override
	protected void setUpViews() {
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle(itemInfo.title);

		mAppBarBackground = $(R.id.appbar_background);
		mAppBarContainer = $(R.id.appbar_container);
		mScrollView = $(R.id.scroll_view);
		mPreviewView = $(R.id.video_preview);
		mFAB = $(R.id.fab);

		mFAB.setTranslationY(-getResources().getDimension(R.dimen.floating_action_button_size_half));
		mScrollView.addOnScrollChangeListener(this);
	}

	public static void launch(AppCompatActivity activity, VideoItemInfo itemInfo) {
		Intent intent = new Intent(activity, VideoViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_ITEM_INFO, itemInfo.toJsonString());
		activity.startActivity(intent);
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
		} else if (alpha < 0.65f && !isPlayingFABAnimation) {
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

		mPreviewView.setTranslationY(target * 0.7f);
	}

}
