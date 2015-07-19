package moe.feng.bilinyan.api;

import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.VideoViewInfo;

public class VideoApi {

	public static BasicMessage<VideoViewInfo> getVideoViewInfo(int av, int page,
	                                                           boolean needReadFav) {
		String url = ApiHelper.getVideoInfoUrl(av, page, needReadFav);
		return ApiHelper.getSimpleUrlResult(url, VideoViewInfo.class);
	}

}
