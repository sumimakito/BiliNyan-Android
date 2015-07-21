package moe.feng.bilinyan.api;

import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.model.VideoSrc;

public class VideoSrcApi {

	public static BasicMessage<VideoSrc> getVideoSrc(int av) {
		String url = ApiHelper.getHTML5Url(String.valueOf(av));
		return ApiHelper.getSimpleUrlResult(url, VideoSrc.class);
	}

}
