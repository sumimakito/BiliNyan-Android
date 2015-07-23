package moe.feng.bilinyan.api;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.List;
import moe.feng.bilinyan.model.UserInfo;

public class UserInfoApi {

	public static final String TAG = UserInfoApi.class.getSimpleName();

	public static BasicMessage<UserInfo> getUserInfoById(int uid) {
		String url = ApiHelper.getUserInfoUrl(uid);
		BasicMessage<UserInfo> msg = ApiHelper.getSimpleUrlResult(url, UserInfo.class);
		if (msg.getCode() == BasicMessage.CODE_SUCCEED && msg.getObject().code == UserInfo.CODE_NOT_EXIST) {
			msg.setCode(UserInfo.CODE_NOT_EXIST);
		}
		return msg;
	}

	public static BasicMessage<UserInfo> getUserInfoByName(String user) {
		String url = ApiHelper.getUserInfoUrl(user);
		BasicMessage<UserInfo> msg = ApiHelper.getSimpleUrlResult(url, UserInfo.class);
		if (msg.getCode() == BasicMessage.CODE_SUCCEED && msg.getObject().code == UserInfo.CODE_NOT_EXIST) {
			msg.setCode(UserInfo.CODE_NOT_EXIST);
		}
		return msg;
	}

	public static BasicMessage<List> getUserVideoList(int uid, int page) {
		String url = ApiHelper.getUserVideoListUrl(uid, page);

		Request request = new Request.Builder()
				.url(url)
				.header("User-Agent", ApiHelper.COMMON_UA_STR)
				.build();
		Log.i(TAG, "Set up the request" + request.toString());

		BasicMessage<List> msg = new BasicMessage<>();
		try {
			Response response = new OkHttpClient().newCall(request).execute();
			Log.i(TAG, "Get response:" + response.code());
			String result = response.body().string();
			Log.i(TAG, result);
			msg.setObject(List.createFromJson(result));
			msg.setCode(BasicMessage.CODE_SUCCEED);
		} catch (IOException e) {
			e.printStackTrace();
			msg.setCode(BasicMessage.CODE_ERROR);
		}

		return msg;
	}

}
