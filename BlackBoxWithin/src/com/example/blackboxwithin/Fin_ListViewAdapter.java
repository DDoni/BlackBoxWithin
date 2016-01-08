package com.example.blackboxwithin;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/*
 * @설치완료@ - ListViewAdapter set
 */
public class Fin_ListViewAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<Fin_ListViewItem> data = new ArrayList<Fin_ListViewItem>();
	Context context;

	public Fin_ListViewAdapter(Context context) {
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
			view = inflater.inflate(R.layout.finish_row, parent, false);
		} else
			view = convertView;

		TextView index = (TextView) view.findViewById(R.id.index);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView needs = (TextView) view.findViewById(R.id.needs);
		TextView reserve = (TextView) view.findViewById(R.id.reserve);
		TextView addr = (TextView) view.findViewById(R.id.address);
		TextView phone = (TextView) view.findViewById(R.id.phone);
		TextView blackbox = (TextView) view.findViewById(R.id.blackbox);
		TextView channel = (TextView) view.findViewById(R.id.channel);
		TextView state = (TextView) view.findViewById(R.id.state);

		Fin_ListViewItem item = (Fin_ListViewItem) data.get(position);

		index.setText(Integer.toString((getCount() - position)));

		name.setText(item.getName());
		reserve.setText("완료일 : " + item.getReserve().substring(0, 4) + "." + item.getReserve().substring(4, 6) + "."
				+ item.getReserve().substring(6, 8) + " / " + item.getReserve().substring(8, 10) + ":"
				+ item.getReserve().substring(10, 12));
		addr.setText(item.getAddr());
		phone.setText(item.getPhone1());
		blackbox.setText(item.getBlackbox());
		channel.setText(item.getChannel());
		state.setText(item.getState());
		return view;
	}
}
