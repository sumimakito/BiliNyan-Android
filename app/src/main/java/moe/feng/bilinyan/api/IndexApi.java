package moe.feng.bilinyan.api;

import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.Index;

public class IndexApi {

	public static BasicMessage<Index> getIndex() {
		String url = ApiHelper.getIndexUrl();
		return ApiHelper.getSimpleUrlResult(url, Index.class);
	}

}
