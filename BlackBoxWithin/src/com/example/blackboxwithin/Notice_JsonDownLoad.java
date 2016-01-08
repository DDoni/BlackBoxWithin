package com.example.blackboxwithin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/*
 * @공지사항@ - JsonDown
 */
public class Notice_JsonDownLoad extends AsyncTask<String, String, String> {

	NoticeList tabFrame_context;
	Login login_context;
	ProgressDialog dialog;

	LoadManager load;
	String page;

	public Notice_JsonDownLoad(Context context, String page, String url) {

		// TODO Auto-generated constructor stub
		if (page.equalsIgnoreCase("tab3") == true) {
			this.tabFrame_context = (NoticeList) context;
			load = new LoadManager(url);
			this.page = page;
		}
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

		ArrayList<NoticeListViewItem> notice_list = tabFrame_context.notice_adapter.data;
		notice_list.removeAll(notice_list);

		try {

			JSONArray jArray = new JSONArray(result);
			JSONObject json = null;

			NoticeListViewItem data = null;
			for (int i = 0; i < jArray.length(); i++) {
				json = jArray.getJSONObject(i);
				if (json != null) {
					data = new NoticeListViewItem();
					data.setDate(json.getString("not_register_date").toString().substring(0, 4) + "."
							+ json.getString("not_register_date").toString().substring(4, 6) + "."
							+ json.getString("not_register_date").toString().substring(6, 8));
					data.setTitle(json.getString("not_title").toString());
					data.setNotice_body(json.getString("not_contents").toString());
					notice_list.add(data);
					tabFrame_context.notice_listView.invalidateViews();

				}
			}
			tabFrame_context.notice_listView.invalidate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
