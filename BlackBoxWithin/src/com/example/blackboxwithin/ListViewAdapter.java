package com.example.blackboxwithin;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * @설치대기@ - ListViewAdapter
 */
public class ListViewAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<ListViewItem> data = new ArrayList<ListViewItem>();
	Context context;

	public ListViewAdapter(Context context) {
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
			//
			view = inflater.inflate(R.layout.list_row, parent, false);
		} else
			view = convertView;

		TextView name = (TextView) view.findViewById(R.id.name);
		TextView needs = (TextView) view.findViewById(R.id.needs);
		TextView reserve = (TextView) view.findViewById(R.id.reserve);
		TextView addr = (TextView) view.findViewById(R.id.address);
		TextView car = (TextView) view.findViewById(R.id.car);
		TextView blackbox = (TextView) view.findViewById(R.id.blackbox);
		TextView channel = (TextView) view.findViewById(R.id.channel);
		LinearLayout stateColor = (LinearLayout) view.findViewById(R.id.color_zone);
		LinearLayout stateColor1 = (LinearLayout) view.findViewById(R.id.color_zone1);
		ImageView state = (ImageView) view.findViewById(R.id.state);

		ListViewItem item = (ListViewItem) data.get(position);

		name.setText(item.getName());

		if (item.getNeeds().toString().equalsIgnoreCase("") == true
				|| item.getNeeds().toString().equalsIgnoreCase(null) == true
				|| item.getNeeds().toString().equalsIgnoreCase("NULL") == true) {
			needs.setText("");

		} else {
			needs.setText("V");
			needs.setTextColor(Color.RED);

		}
		if (item.getReserve().equalsIgnoreCase("") == true || item.getReserve().equalsIgnoreCase("null") == true
				|| item.getReserve().equalsIgnoreCase("0") == true) {
			stateColor.setBackgroundColor(Color.rgb(90, 190, 255));
			stateColor1.setBackgroundColor(Color.rgb(90, 190, 255));

			name.setTextColor(Color.WHITE);

			reserve.setText("");
			state.setImageResource(NO_SELECTION);
		} else {

			stateColor.setBackgroundColor(Color.rgb(255, 195, 30));
			stateColor1.setBackgroundColor(Color.rgb(255, 195, 30));

			name.setTextColor(Color.BLACK);

			reserve.setTextColor(Color.BLACK);
			reserve.setText("설치 예정일 : " + item.getReserve().substring(0, 4) + "." + item.getReserve().substring(4, 6)
					+ "." + item.getReserve().substring(6, 8) + " / " + item.getReserve().substring(8, 10) + ":"
					+ item.getReserve().substring(10, 12) + "  ");

		}
		if (item.getState().equalsIgnoreCase("dday") == true) {
			state.setImageResource(R.drawable.state_dday);

		} else if (item.getState().equalsIgnoreCase("hurry") == true) {

			state.setImageResource(R.drawable.state_hurry);
		} else if (item.getState().equalsIgnoreCase("good") == true) {
			state.setImageResource(R.drawable.state_good);

		} else if (item.getState().equalsIgnoreCase("nope") == true)
			state.setImageResource(NO_SELECTION);
		addr.setText(item.getAddr());
		car.setText(item.getCar());
		blackbox.setText(item.getBlackbox());
		channel.setText(item.getChannel());
		return view;
	}

}
