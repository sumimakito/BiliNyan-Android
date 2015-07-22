package moe.feng.bilinyan.api;

import java.util.ArrayList;

import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.UserInfo;

public class FriendApi {

	public static BasicMessage<ArrayList<UserInfo>> getUserList(ArrayList<Integer> uids) {
		BasicMessage<ArrayList<UserInfo>> msg = new BasicMessage<>();
		ArrayList<UserInfo> list = new ArrayList<>();

		for (int i : uids) {
			BasicMessage<UserInfo> result = UserInfoApi.getUserInfoById(i);
			if (result != null && result.getCode() == BasicMessage.CODE_SUCCEED) {
				list.add(result.getObject());
			}
		}

		msg.setObject(list);
		msg.setCode(BasicMessage.CODE_SUCCEED);

		return msg;
	}

}
