package com.example.blackboxwithin;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/*
 * @공지사항@ - ListViewAdapter
 */
public class NoticeListViewAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<NoticeListViewItem> data = new ArrayList<NoticeListViewItem>();

	public NoticeListViewAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (convertView == null) {
			view = inflater.inflate(R.layout.notice_row, parent, false);
		} else
			view = convertView;
		NoticeListViewItem item = (NoticeListViewItem) data.get(position);

		TextView index = (TextView) view.findViewById(R.id.index);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView uploader = (TextView) view.findViewById(R.id.uploader);
		TextView date = (TextView) view.findViewById(R.id.date);

		index.setText(Integer.toString((getCount() - position)));
		title.setText(item.getTitle());
		uploader.setText("관리자");
		date.setText(item.getDate());
		
		return view;
	}

}
