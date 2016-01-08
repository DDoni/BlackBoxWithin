package com.example.blackboxwithin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/*
 * @설치완료@ - JsonDown 관련
 */
public class Fin_JsonDownLoad extends AsyncTask<String, String, String> {

	FinList tabFrame_context;
	Login login_context;
	ProgressDialog dialog;

	LoadManager load;
	String page;

	public Fin_JsonDownLoad(Context context, String page, String url) {
		// TODO Auto-generated constructor stub
		this.tabFrame_context = (FinList) context;
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

		ArrayList<Fin_ListViewItem> fin_list = tabFrame_context.finish_adapter.data;
		fin_list.removeAll(fin_list);

		try {

			JSONArray jArray = new JSONArray(result);
			JSONObject json = null;

			Fin_ListViewItem data = null;
			for (int i = 0; i < jArray.length(); i++) {
				json = jArray.getJSONObject(i);
				if (json != null) {
					data = new Fin_ListViewItem();
					data.setName(json.getString("ord_customer_name").toString());
					data.setAccept_date(json.getString("ord_register_date").toString());
					data.setAccept_num(json.getString("ord_num").toString());
					data.setField_pay(json.getString("ord_addcost_site").toString());
					data.setPhone2(json.getString("ord_phone_2").toString());
					data.setAddr(json.getString("ord_address").toString());
					data.setPhone1(json.getString("ord_phone_1").toString());
					data.setBlackbox(json.getString("ord_product_name").toString());
					data.setChannel(json.getString("ord_channel"));
					data.setReserve(json.getString("ord_finish_date"));
					
					fin_list.add(data);
					tabFrame_context.finish_listView.invalidateViews();
				}
			}
			tabFrame_context.finish_listView.invalidate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
