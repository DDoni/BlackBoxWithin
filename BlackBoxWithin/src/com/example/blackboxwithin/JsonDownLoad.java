package com.example.blackboxwithin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
/*
 * @설치대기@ - JsonDown
 */
public class JsonDownLoad extends AsyncTask<String, String, String> {

	BeforeList tabFrame_context;
	Login login_context;
	ProgressDialog dialog;

	LoadManager load;
	String page;

	public JsonDownLoad(Context context, String page, String url) {

		// TODO Auto-generated constructor stub

		this.tabFrame_context = (BeforeList) context;
		load = new LoadManager(url);
		this.page = page;

	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		String data = load.request();
		return data;

	}

	protected void onPreExecute() {
		super.onPreExecute();

		dialog = new ProgressDialog(tabFrame_context);
		dialog.show();

	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		dialog.dismiss();

		ArrayList<ListViewItem> before_list = tabFrame_context.adapter.data;
		before_list.removeAll(before_list);

		try {

			JSONArray jArray = new JSONArray(result);
			JSONObject json = null;

			ListViewItem data = null;
			for (int i = 0; i < jArray.length(); i++) {
				json = jArray.getJSONObject(i);
				if (json != null) {
					data = new ListViewItem();
					data.setName(json.getString("ord_customer_name").toString());
					data.setNeeds(json.getString("ord_memo").toString());
					if (json.getString("ord_reserve_date").toString().equalsIgnoreCase("0") == true)
						data.setReserve("");
					else {
						data.setReserve(json.getString("ord_reserve_date"));
					}

					data.setAccept_date(json.getString("ord_register_date").toString());
					data.setAccept_num(json.getString("ord_num").toString());
					data.setField_pay(json.getString("ord_addcost_site").toString());
					data.setPhone2(json.getString("ord_phone_2").toString());
					data.setAddr(json.getString("ord_address").toString());
					data.setPhone1(json.getString("ord_phone_1").toString());
					data.setBlackbox(json.getString("ord_product_name").toString());
					data.setChannel(json.getString("ord_channel"));
					data.setCar(json.getString("ord_car_model").toString());
					before_list.add(data);
					tabFrame_context.listView.invalidateViews();

				}
			}
			tabFrame_context.listView.invalidate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
