package moe.feng.bilinyan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.api.FriendApi;
import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.UserInfo;
import moe.feng.bilinyan.ui.adapter.list.UserItemRecyclerAdapter;
import moe.feng.bilinyan.ui.common.AbsActivity;
import moe.feng.bilinyan.ui.common.AbsRecyclerViewAdapter;
import moe.feng.bilinyan.util.AsyncTask;
import moe.feng.bilinyan.view.CircleProgressView;

public class UserListActivity extends AbsActivity {

	private RecyclerView mUserList;
	private CircleProgressView mCircleProgress;

	private ArrayList<Integer> uids;
	private ArrayList<UserInfo> users;

	private static final String EXTRA_UIDS = "extra_users_list";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		uids = intent.getIntegerArrayListExtra(EXTRA_UIDS);

		setContentView(R.layout.activity_user_list);

		startGetListTask();
	}

	@Override
	protected void setUpViews() {
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mCircleProgress = $(R.id.circle_progress);
		mUserList = $(R.id.user_list);

		mUserList.setLayoutManager(new LinearLayoutManager(this));
		mUserList.setHasFixedSize(true);
	}

	public static void launch(AppCompatActivity activity, ArrayList<Integer> uids) {
		Intent intent = new Intent(activity, UserListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_UIDS, uids);
		activity.startActivity(intent);
	}

	private void startGetListTask() {
		mUserList.setVisibility(View.GONE);
		mCircleProgress.setVisibility(View.VISIBLE);
		mCircleProgress.spin();

		new GetListTask().execute();
	}

	private void finishGetListTask() {
		mUserList.setVisibility(View.VISIBLE);
		mCircleProgress.setVisibility(View.GONE);
		mCircleProgress.stopSpinning();
	}

	private class GetListTask extends AsyncTask<Void, Void, BasicMessage<ArrayList<UserInfo>>> {

		@Override
		protected BasicMessage<ArrayList<UserInfo>> doInBackground(Void... params) {
			return FriendApi.getUserList(uids);
		}

		@Override
		protected void onPostExecute(BasicMessage<ArrayList<UserInfo>> msg) {
			if (msg != null && msg.getCode() == BasicMessage.CODE_SUCCEED) {
				users = msg.getObject();
				UserItemRecyclerAdapter adapter = new UserItemRecyclerAdapter(mUserList, users);
				mUserList.setAdapter(adapter);
				adapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(int pos, AbsRecyclerViewAdapter.ClickableViewHolder holder) {
						UserInfo info = users.get(pos);
						String temp;
						temp = !TextUtils.isEmpty(temp = info.name) ?
										temp : !TextUtils.isEmpty(temp = info.uname) ?
										temp : info.userid;
						UserInfoActivity.launch(UserListActivity.this, temp, info.mid, info.face);
					}
				});
			} else {

			}
			finishGetListTask();
		}

	}

}
