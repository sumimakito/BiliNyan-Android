package moe.feng.bilinyan.api;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

class Api {

	private static Gson gson = new Gson();
	private static OkHttpClient client = new OkHttpClient();

	protected static Gson getGson() {
		return gson;
	}

	protected static OkHttpClient getHttpClient() {
		return client;
	}

}
