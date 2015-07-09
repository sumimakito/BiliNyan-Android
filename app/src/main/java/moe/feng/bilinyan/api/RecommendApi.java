package moe.feng.bilinyan.api;

import android.util.Log;

import com.squareup.okhttp.Request;

import java.io.IOException;

import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.RecommendList;

public class RecommendApi extends Api {

	private static final String TAG = RecommendApi.class.getSimpleName();

	public static final int ORDER_DEFAULT = 0, ORDER_NEW = 1, ORDER_REVIEW = 2,
			ORDER_HOT = 3, ORDER_DAMKU = 4, ORDER_COMMENT = 5, ORDER_PROMOTE = 6, ORDER_NONE = 0;

	private static final String[] ORDER_VALUE = new String[]{
			ApiHelper.RecommendOrder.DEFAULT,
			ApiHelper.RecommendOrder.NEW,
			ApiHelper.RecommendOrder.REVIEW,
			ApiHelper.RecommendOrder.HOT,
			ApiHelper.RecommendOrder.DAMKU,
			ApiHelper.RecommendOrder.COMMENT,
			ApiHelper.RecommendOrder.PROMOTE,
			null
	};

	public static BasicMessage<RecommendList> getList(String tid, String pagenum,
	                                                  String pagesize, int order) {
		String url = ApiHelper.getRecommendUrl(tid, pagenum, pagesize, ORDER_VALUE[order]);

		Log.i(TAG, url);

		Request request = new Request.Builder().url(url).build();

		BasicMessage<RecommendList> msg = new BasicMessage<>();
		try {
			String result = getHttpClient().newCall(request).execute().body().string();
			Log.i(TAG, result);
			msg.setObject(getGson().fromJson(result, RecommendList.class));
			msg.setCode(BasicMessage.CODE_SUCCEED);
		} catch (IOException e) {
			e.printStackTrace();
			msg.setCode(BasicMessage.CODE_ERROR);
		}

		return msg;
	}

}
