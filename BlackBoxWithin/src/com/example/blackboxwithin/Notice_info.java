package com.example.blackboxwithin;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/*
 * @공지사항@ - 상세정보
 */
public class Notice_info extends Activity implements View.OnClickListener {

	Button back_btn;
	static TextView title;
	static TextView uploader;
	static TextView date;
	static TextView notice_body;
	Intent intent;
	NoticeCallData ncd;

	int position;
	String pos;
	WakeLock wakeLock = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_info);
		Intent i = this.getIntent();

		pos = i.getStringExtra("pos");
		position = Integer.parseInt(pos);
		ncd = new NoticeCallData();
		ncd.setPos(position);
		ncd.execute();

		findViewById(R.id.back_btn).setOnClickListener(this);

		title = (TextView) findViewById(R.id.title);
		uploader = (TextView) findViewById(R.id.uploader);
		date = (TextView) findViewById(R.id.date);
		notice_body = (TextView) findViewById(R.id.notice_body);

	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.back_btn:
			finish();
			break;

		}

	}

	// 슬립모드 유지
	public void changeToWakeMode() {
		if (wakeLock == null) {
			PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wakelock");
			wakeLock.acquire();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(wakeLock != null)
		{
			wakeLock.release();
			wakeLock = null;
		}
		super.onStop();
		startService(new Intent (getBaseContext(), BackGroundService.class));
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		stopService(new Intent(getBaseContext(), BackGroundService.class));

		if (BackGroundService.count > 43200) {
			intent = new Intent(getBaseContext(), Login.class);
			startActivity(intent);
			finish();
		}
	}
}

// 공지사항 데이터 호출
class NoticeCallData extends AsyncTask<String, String, String> {
	LoadManager load;
	int pos;

	public NoticeCallData() {

		// TODO Auto-generated constructor stub
		load = new LoadManager("http://within2015.dothome.co.kr/core/api_app_test.php?method=getNotice");
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String data = load.request();
		return data;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		try {

			JSONArray jArray = new JSONArray(result); // 여기에서 값이
														// 안들어감@@@@@@@

			JSONObject json = null;
			Notice_info.title.setText("");
			Notice_info.date.setText("");
			Notice_info.notice_body.setText("");

			json = jArray.getJSONObject(pos);
			if (json != null) {

				Notice_info.title.setText(json.getString("not_title").toString());
				Notice_info.date.setText(json.getString("not_register_date").toString().substring(0, 4) + "."
						+ json.getString("not_register_date").toString().substring(4, 6) + "."
						+ json.getString("not_register_date").toString().substring(6, 8));
				Notice_info.notice_body.setText(Html.fromHtml(json.getString("not_contents")));

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	void setPos(int pos) {
		this.pos = pos;
	}

}