package moe.feng.bilinyan.api;

import java.util.ArrayList;
import java.util.List;

import moe.feng.bilinyan.model.Bangumi;
import moe.feng.bilinyan.model.BasicMessage;

public class BangumiApi {

	public static final int BTYPE_ALL = -1, BTYPE_2D = 2, BTYPE_RETINA = 3;
	public static final int WD_ALL = -1, WD_SUN = 0, WD_MON = 1, WD_TUE = 2, WD_WED = 3, WD_THU = 4,
			WD_FRI = 5, WD_SAT = 6;

	public static BasicMessage<ArrayList<Bangumi>> getBangumi() {
		return getBangumi(BTYPE_ALL, WD_ALL);
	}

	public static BasicMessage<ArrayList<Bangumi>> getBangumi(int btype, int weekday) {
		String url = ApiHelper.getBangumiUrl(btype, weekday);
		BasicMessage<BangumiResult> result =
				ApiHelper.getSimpleUrlResult(url, BangumiResult.class);
		BasicMessage<ArrayList<Bangumi>> msg = new BasicMessage<>();
		if (result.getCode() == BasicMessage.CODE_SUCCEED) {
			msg.setObject(new ArrayList<>(result.getObject().list));
			msg.setCode(BasicMessage.CODE_SUCCEED);
		} else {
			msg.setCode(BasicMessage.CODE_ERROR);
		}
		return msg;
	}

	private class BangumiResult {

		public int results;
		public List<Bangumi> list;

	}

}
