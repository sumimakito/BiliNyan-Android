package moe.feng.bilinyan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.api.UrlHelper;
import moe.feng.bilinyan.api.UserInfoApi;
import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.List;
import moe.feng.bilinyan.model.UserInfo;
import moe.feng.bilinyan.model.VideoItemInfo;
import moe.feng.bilinyan.ui.adapter.list.VideoItemRecyclerAdapter;
import moe.feng.bilinyan.ui.common.AbsActivity;
import moe.feng.bilinyan.ui.common.AbsRecyclerViewAdapter;
import moe.feng.bilinyan.util.AsyncTask;
import moe.feng.bilinyan.view.CircleImageView;

public class UserInfoActivity extends AbsActivity {

	private CircleImageView mAvatarImage;
	private AppCompatTextView mUserNameText, mDescriptionText, mFollowNumText, mFansNumText;
	private RecyclerView mVideoList;
	private SwipeRefreshLayout mRefreshLayout;
	private VideoItemRecyclerAdapter mAdapter;

	private String name = "";
	private int mid;
	private String avatar_url;

	private UserInfo userInfo;
	private List userVideos;

	private ArrayList<VideoItemInfo> list;

	private int nowPage = 0;

	private static final String EXTRA_USER_NAME = "extra_user_name", EXTRA_MID = "extra_mid",
			EXTRA_AVATAR_URL = "extra_avatar_url";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		name = intent.getStringExtra(EXTRA_USER_NAME);
		mid = intent.getIntExtra(EXTRA_MID, -1);
		avatar_url = intent.getStringExtra(EXTRA_AVATAR_URL);

		setContentView(R.layout.activity_user_info);

		new UserBasicInfoTask().execute();
	}

	@Override
	protected void setUpViews() {
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mAvatarImage = $(R.id.user_avatar_view);
		mUserNameText = $(R.id.user_name);
		mDescriptionText = $(R.id.user_desc);
		mFollowNumText = $(R.id.tv_follow_users);
		mFansNumText = $(R.id.tv_fans);
		mVideoList = $(R.id.user_video_list);
		mRefreshLayout = $(R.id.refresh_layout);

		mRefreshLayout.setColorSchemeResources(
				R.color.deep_purple_500, R.color.pink_500, R.color.orange_500, R.color.brown_500,
				R.color.indigo_500, R.color.blue_500, R.color.teal_500, R.color.green_500
		);
		mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (!mRefreshLayout.isRefreshing()) {
					mRefreshLayout.setRefreshing(true);
				}
				new UserVideoGetTask().execute(nowPage = 0);
			}
		});

		mFollowNumText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				UserListActivity.launch(UserInfoActivity.this, new ArrayList<>(userInfo.attentions));
			}
		});
		mFansNumText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		if (name != null) {
			mUserNameText.setText(name);
		}
		if (avatar_url != null) {
			Picasso.with(this).load(avatar_url)
					.placeholder(R.drawable.avatar_placeholder_empty)
					.into(mAvatarImage);
		}

		list = new ArrayList<>();
		mAdapter = new VideoItemRecyclerAdapter(mVideoList, list);
		mVideoList.setHasFixedSize(true);
		mVideoList.setLayoutManager(new LinearLayoutManager(this));
		mVideoList.setAdapter(mAdapter);
		setUpAdapter();
	}

	public void finishBasicInfoGetTask() {
		$(R.id.number_bar).setVisibility(View.VISIBLE);

		mUserNameText.setText(userInfo.name);
		mDescriptionText.setText(userInfo.sign);
		mFollowNumText.setText(String.format(getString(R.string.info_following_format), userInfo.attention));
		mFansNumText.setText(String.format(getString(R.string.info_fans_format), userInfo.fans));

		Picasso.with(this).load(UrlHelper.getFaceUrl(userInfo))
				.placeholder(R.drawable.avatar_placeholder_empty)
				.into(mAvatarImage);

		mRefreshLayout.setRefreshing(true);
		new UserVideoGetTask().execute(nowPage = 0);
	}

	private void setUpAdapter() {
		mAdapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder) {
				VideoViewActivity.launch(UserInfoActivity.this, list.get(position));
			}
		});
	}

	public static void launch(AppCompatActivity activity, String name, int mid, String avatar_url) {
		Intent intent = new Intent(activity, UserInfoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_USER_NAME, name);
		intent.putExtra(EXTRA_MID, mid);
		intent.putExtra(EXTRA_AVATAR_URL, avatar_url);
		activity.startActivity(intent);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.menu_user_info, menu);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_follow) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public class UserBasicInfoTask extends AsyncTask<Void, Void, BasicMessage<UserInfo>> {

		@Override
		protected BasicMessage<UserInfo> doInBackground(Void... params) {
			return UserInfoApi.getUserInfoByName(name);
		}

		@Override
		protected void onPostExecute(BasicMessage<UserInfo> msg) {
			if (msg != null) {
				if (msg.getCode() == BasicMessage.CODE_SUCCEED) {
					userInfo = msg.getObject();
					finishBasicInfoGetTask();
				} else if (msg.getCode() == UserInfo.CODE_NOT_EXIST) {

				}
			}
		}

	}

	public class UserVideoGetTask extends AsyncTask<Integer, Void, BasicMessage<List>> {

		@Override
		protected BasicMessage<List> doInBackground(Integer... params) {
			return UserInfoApi.getUserVideoList(mid, params[0]);
		}

		@Override
		protected void onPostExecute(BasicMessage<List> msg) {
			mRefreshLayout.setRefreshing(false);
			if (msg != null) {
				if (msg.getCode() == BasicMessage.CODE_SUCCEED) {
					userVideos = msg.getObject();
					list.addAll(userVideos.lists);
					mAdapter.notifyDataSetChanged();
				} else {

				}
			}
		}

	}

}
