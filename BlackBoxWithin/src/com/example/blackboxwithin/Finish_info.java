package com.example.blackboxwithin;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * @설치완료@ - 상세정보
 */
public class Finish_info extends Activity implements View.OnClickListener {

	Button save_btn;
	Button back_btn;

	Button map_btn;
	Button sms1_btn;
	Button sms2_btn;
	Button call1_btn;
	Button call2_btn;
	Button camera_btn;

	static TextView accept_date;
	static TextView accept_num;
	static TextView blackbox;
	static TextView car;
	static TextView name;
	static TextView addr;
	static TextView phone1;
	static TextView phone2;
	static TextView finish_Date;
	static TextView state;
	static TextView business_pay;
	static TextView field_pay;
	static TextView total_state;
	static TextView channel;

	static int dYear, dMonth, dDay;
	static long n, d, r;
	static int nYear, nMonth, nDay;

	Uri uri;
	Intent i;

	static TextView needs;
	static int year, month, day, hour, minute;

	TabMain adapter;
	Context context;

	static String pos;
	int position;
	Fin_CallData cd;
	static String id;
	static long reserve_fix;
	static int choice = 6;
	Intent intent;
	WakeLock wakeLock = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish_info);
		Intent i = this.getIntent();

		pos = i.getStringExtra("key");
		id = i.getStringExtra("id");
		position = Integer.parseInt(pos);

		cd = new Fin_CallData();
		cd.setPos(position);

		cd.execute();

		findViewById(R.id.save_btn).setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.map_btn).setOnClickListener(this);
		findViewById(R.id.sms1_btn).setOnClickListener(this);
		findViewById(R.id.sms2_btn).setOnClickListener(this);
		findViewById(R.id.call1_btn).setOnClickListener(this);
		findViewById(R.id.call2_btn).setOnClickListener(this);

		findViewById(R.id.camera_btn).setOnClickListener(this);

		channel = (TextView) findViewById(R.id.channel);
		accept_date = (TextView) findViewById(R.id.accept_date);
		accept_num = (TextView) findViewById(R.id.accept_num);
		blackbox = (TextView) findViewById(R.id.blackbox);
		car = (TextView) findViewById(R.id.car);
		name = (TextView) findViewById(R.id.name);
		addr = (TextView) findViewById(R.id.address);
		phone1 = (TextView) findViewById(R.id.phone1);
		phone2 = (TextView) findViewById(R.id.phone2);
		state = (TextView) findViewById(R.id.state);
		business_pay = (TextView) findViewById(R.id.business_pay);
		field_pay = (TextView) findViewById(R.id.field_pay);
		total_state = (TextView) findViewById(R.id.total_state);
		finish_Date = (TextView) findViewById(R.id.finish_date);
		needs = (TextView) findViewById(R.id.needs);

		GregorianCalendar calender = new GregorianCalendar();

		year = calender.get(Calendar.YEAR);
		month = calender.get(Calendar.MONTH);
		day = calender.get(Calendar.DAY_OF_MONTH);
		hour = calender.get(Calendar.HOUR_OF_DAY);
		minute = calender.get(Calendar.MINUTE);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.map_btn:
			uri = Uri.parse("http://map.google.com/maps?f=d&ssaddr=" + addr.getText().toString() + "&daddr="
					+ addr.getText().toString() + "hl=ko");
			i = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(i);
			break;
		case R.id.sms1_btn:
			uri = Uri.parse("smsto:" + phone1.getText().toString());
			i = new Intent(Intent.ACTION_SENDTO, uri);
			i.putExtra("sms_body", "-With 人을 이용해주셔서 감사합니다.-\n");
			startActivity(i);
			break;
		case R.id.sms2_btn:
			uri = Uri.parse("smsto:" + phone2.getText().toString());
			i = new Intent(Intent.ACTION_SENDTO, uri);
			i.putExtra("sms_body", "-With 人을 이용해주셔서 감사합니다.-\n");
			startActivity(i);
			break;
		case R.id.call1_btn:
			uri = Uri.parse("tel:" + phone1.getText().toString());
			i = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(i);
			break;
		case R.id.call2_btn:
			uri = Uri.parse("tel:" + phone2.getText().toString());
			i = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(i);
			break;
		case R.id.camera_btn:
			i = new Intent(Finish_info.this, Fin_CameraBtn.class);
			i.putExtra("accept_num", Finish_info.accept_num.getText().toString());
			startActivity(i);
			break;
		}
	}

	// 설치 완료 목록 요청
	class Fin_CallData extends AsyncTask<String, String, String> {
		LoadManager load;
		int pos;

		public Fin_CallData() {
			// TODO Auto-generated constructor stub
			load = new LoadManager(
					"http://within2015.dothome.co.kr/core/api_app_test.php?method=getFinishList&id=" + Finish_info.id);
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
				System.out.println(result);
				JSONArray jArray = new JSONArray(result);
				JSONObject json = null;

				json = jArray.getJSONObject(pos);
				if (json != null) {

					Finish_info.name.setText(json.getString("ord_customer_name").toString());
					Finish_info.accept_date.setText(json.getString("ord_register_date").toString());
					Finish_info.accept_num.setText(json.getString("ord_num").toString());

					if (json.getString("ord_addcost_site").toString().equalsIgnoreCase("") == true
							|| json.getString("ord_addcost_site").toString().equalsIgnoreCase("null") == true)
						Finish_info.field_pay.setText("");
					else
						Finish_info.field_pay.setText(json.getString("ord_addcost_site").toString());

					if (json.getString("ord_phone_1").toString().equalsIgnoreCase("") == true
							|| json.getString("ord_phone_1").toString().equalsIgnoreCase("null") == true)
						Finish_info.phone1.setText("");
					else
						Finish_info.phone1.setText(json.getString("ord_phone_1").toString());

					if (json.getString("ord_phone_2").toString().equalsIgnoreCase("") == true
							|| json.getString("ord_phone_2").toString().equalsIgnoreCase("null") == true)
						Finish_info.phone2.setText("");
					else
						Finish_info.phone2.setText(json.getString("ord_phone_2").toString());

					if (json.getString("ord_memo").toString().equals("null") == true)
						Finish_info.needs.setText("");
					else
						Finish_info.needs.setText(json.getString("ord_memo").toString());

					Finish_info.blackbox.setText(json.getString("ord_product_name").toString());
					Finish_info.channel.setText(json.getString("ord_channel"));
					Finish_info.addr.setText(json.getString("ord_address").toString());
					Finish_info.car.setText(json.getString("ord_car_model"));
					Finish_info.finish_Date.setText(json.getString("ord_finish_date").substring(0, 4) + "."
							+ json.getString("ord_finish_date").substring(4, 6) + "."
							+ json.getString("ord_finish_date").substring(6, 8) + " / "
							+ json.getString("ord_finish_date").substring(8, 10) + ":"
							+ json.getString("ord_finish_date").substring(10, 12));
					Finish_info.total_state.setText(json.getString("ord_state").toString());
					Finish_info.total_state.setText("설치완료");
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

	// 슬립모드 유지
	public void changeToWakeMode() {
		if (wakeLock == null) {
			PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wakelock");
			wakeLock.acquire();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}
		super.onStop();
		startService(new Intent(getBaseContext(), BackGroundService.class));
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
