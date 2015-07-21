package moe.feng.bilinyan.ui.adapter.list;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.model.VideoViewInfo;

public class VideoPartListAdapter extends BaseAdapter {

	private ArrayList<VideoViewInfo> parts;
	private LayoutInflater mInflater;

	public VideoPartListAdapter(Context context, ArrayList<VideoViewInfo> parts) {
		mInflater = LayoutInflater.from(context);
		this.parts = parts;
	}

	@Override
	public int getCount() {
		return parts.size();
	}

	@Override
	public VideoViewInfo getItem(int i) {
		return parts.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder vh;
		if (view == null) {
			view = mInflater.inflate(R.layout.list_item_video_part_item, null);
			vh = new ViewHolder(view);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}

		if (TextUtils.isEmpty(getItem(i).partname)) {
			vh.mPartNameText.setText("P" + i);
		} else {
			vh.mPartNameText.setText(getItem(i).partname);
		}

		return view;
	}

	private class ViewHolder {

		TextView mPartNameText;

		public ViewHolder(View parentView) {
			mPartNameText = (TextView) parentView.findViewById(R.id.part_name_text);
		}

	}

}
