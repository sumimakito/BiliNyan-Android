package moe.feng.bilinyan.api;

import java.util.ArrayList;
import java.util.HashMap;

public class ApiHelper {

	public static final String API_HOST = "http://api.bilibili.cn";
	public static final String BILIBILI_SITE = "http://www.bilibili.com";

	public static String getHTML5Url(String aid) {
		return new UrlBuilder(API_HOST + "/" + ApiUrl.HTML5).addParams("aid", aid).toString();
	}

	public static String getRecommendUrl(String tid, String pagenum, String pagesize, String order) {
		UrlBuilder builder = new UrlBuilder(API_HOST + "/" + ApiUrl.RECOMMEND);

		if (tid != null) builder.addParams("tid", tid);
		if (pagenum != null) builder.addParams("page", pagenum);
		if (pagesize != null) builder.addParams("pagesize", pagesize);
		if (order != null) builder.addParams("order", order);

		return builder.toString();
	}

	public static String getVideoInfoUrl(String av, String page, boolean needFav) {
		UrlBuilder builder = new UrlBuilder(API_HOST + "/" + ApiUrl.VIEW);

		builder.addParams("id", av);
		builder.addParams("page", page);
		builder.addParams("fav", needFav ? "1" : "0");

		return builder.toString();
	}

	private static class UrlBuilder {

		String urlRoot;
		ArrayList<HashMap<String, String>> params;

		public UrlBuilder(String urlRoot) {
			this.urlRoot = urlRoot;
			this.params = new ArrayList<>();
		}

		public UrlBuilder addParams(String key, String value) {
			HashMap<String, String> map = new HashMap<>();
			map.put("key", key);
			map.put("value", value);
			this.params.add(map);
			return this;
		}

		public UrlBuilder removeParams(String key) {
			for (HashMap<String, String> map : params) {
				if (map.get("key").equals(key)) {
					params.remove(map);
					return this;
				}
			}
			return this;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer(urlRoot);
			for (int i = 0; i < params.size(); i++) {
				sb.append(i == 0 ? "?" : "&")
						.append(params.get(i).get("key"))
						.append("=")
						.append(params.get(i).get("value"));
			}
			return sb.toString();
		}

	}

	private static class ApiUrl {

		static final String HTML5 = "/m/html5";
		static final String RECOMMEND = "/recommend";
		static final String VIEW = "/view";

	}

	static class RecommendOrder {
		public static final String DEFAULT = "default", NEW = "new", REVIEW = "review",
				HOT = "hot", DAMKU = "damku", COMMENT = "comment", PROMOTE = "promote";
	}

}
