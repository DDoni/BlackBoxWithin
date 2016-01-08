package com.example.blackboxwithin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/*
 * @설치대기@ - 상세정보
 */
public class List_info extends Activity implements View.OnClickListener {
	static int mHour;
	static int mMinute;
	Button save_btn;
	Button back_btn;

	Button map_btn;
	Button sms1_btn;
	Button sms2_btn;
	Button call1_btn;
	Button call2_btn;
	Button finish_btn;
	Button cut_btn;
	Button pass_btn;
	Button return_btn;
	Button etc_btn;
	Button camera_btn;
	static Button reserve_date;
	static Button reserve_time;

	static TextView accept_date;
	static TextView accept_num;
	static TextView blackbox;
	static EditText car;
	static TextView name;
	static EditText addr;
	static TextView phone1;
	static TextView phone2;

	static TextView state;
	static TextView business_pay;
	static TextView field_pay;
	static TextView total_state;
	static TextView channel;

	static int dYear, dMonth, dDay;
	static long n, d, r;
	static int nYear, nMonth, nDay;

	static Double lat, lng;
	Uri uri;
	Intent i, intent;

	static EditText needs;
	static int year, month, day, hour, minute;
	static int reserve_hour, reserve_minute;
	TabMain adapter;
	Context context;

	static String pos;
	int position;
	CallData cd;
	static String id;
	static long reserve_fix;
	static int choice = 6;

	WakeLock wakeLock = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_info);
		Intent i = this.getIntent();

		pos = i.getStringExtra("key");
		id = i.getStringExtra("id");
		position = Integer.parseInt(pos);

		cd = new CallData();
		cd.setPos(position);

		cd.execute();

		findViewById(R.id.save_btn).setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.map_btn).setOnClickListener(this);
		findViewById(R.id.sms1_btn).setOnClickListener(this);
		findViewById(R.id.sms2_btn).setOnClickListener(this);
		findViewById(R.id.call1_btn).setOnClickListener(this);
		findViewById(R.id.call2_btn).setOnClickListener(this);
		findViewById(R.id.finish_btn).setOnClickListener(this);
		findViewById(R.id.cut_btn).setOnClickListener(this);
		findViewById(R.id.pass_btn).setOnClickListener(this);
		findViewById(R.id.return_btn).setOnClickListener(this);
		findViewById(R.id.etc_btn).setOnClickListener(this);
		findViewById(R.id.camera_btn).setOnClickListener(this);
		findViewById(R.id.reserve_time).setOnClickListener(this);
		findViewById(R.id.reserve_date).setOnClickListener(this);

		reserve_date = (Button) findViewById(R.id.reserve_date);
		reserve_time = (Button) findViewById(R.id.reserve_time);
		channel = (TextView) findViewById(R.id.channel);
		accept_date = (TextView) findViewById(R.id.accept_date);
		accept_num = (TextView) findViewById(R.id.accept_num);
		blackbox = (TextView) findViewById(R.id.blackbox);
		car = (EditText) findViewById(R.id.car);
		name = (TextView) findViewById(R.id.name);
		addr = (EditText) findViewById(R.id.address);
		phone1 = (TextView) findViewById(R.id.phone1);
		phone2 = (TextView) findViewById(R.id.phone2);
		state = (TextView) findViewById(R.id.state);
		business_pay = (TextView) findViewById(R.id.business_pay);
		field_pay = (TextView) findViewById(R.id.field_pay);
		total_state = (TextView) findViewById(R.id.total_state);
		finish_btn = (Button) findViewById(R.id.finish_btn);

		finish_btn.setBackgroundColor(Color.rgb(90, 190, 255));
		needs = (EditText) findViewById(R.id.needs);

		GregorianCalendar calender = new GregorianCalendar();

		year = calender.get(Calendar.YEAR);
		month = calender.get(Calendar.MONTH);
		day = calender.get(Calendar.DAY_OF_MONTH);
		hour = calender.get(Calendar.HOUR_OF_DAY);
		minute = calender.get(Calendar.MINUTE);

		if (reserve_date.getText().toString().equalsIgnoreCase("일자 설정") == true) {
			reserve_time.setClickable(false);
		} else
			reserve_time.setClickable(true);

	}

	public void onClick(View v) {
		SendData send;
		send = new SendData();
		Calendar tmp = Calendar.getInstance();

		Long finDate = tmp.get(Calendar.YEAR) * 100000000L + (tmp.get(Calendar.MONTH) + 1) * 1000000L
				+ tmp.get(Calendar.DAY_OF_MONTH) * 10000L + tmp.get(Calendar.HOUR_OF_DAY) * 100L
				+ tmp.get(Calendar.MINUTE) * 1L;

		switch (v.getId()) {
		case R.id.save_btn:
			String url;
			if (reserve_date.getText().toString().equalsIgnoreCase("일자 설정") == false
					&& reserve_time.getText().toString().equalsIgnoreCase("시간 설정") == true) {
				Toast.makeText(this, "예약 시간을 설정 해야 합니다.", Toast.LENGTH_SHORT).show();

			} else {
				if (choice == 4) {
					url = "http://within2015.dothome.co.kr/core/api_app_test.php?method=setFinish&num="
							+ Integer.parseInt(List_info.accept_num.getText().toString()) + "&finDate=" + finDate;

					send.execute(url);
				} else {
					if (reserve_date.getText().toString().equalsIgnoreCase("일자 설정") == false
							&& reserve_time.getText().toString().equalsIgnoreCase("시간 설정") == false)
						List_info.reserve_fix = dYear * 100000000L + dMonth * 1000000L + dDay * 10000L
								+ reserve_hour * 100L + reserve_minute * 1L;

					String str = null;
					String strEncode = null;
					str = List_info.needs.getText().toString();
					if (choice >= 0 && choice < 4)
						str = "[담당기사 ID : " + TabMain.strname + "]" + "\n" + str;

					str = str.replace("\n", "<br>");
					try {
						strEncode = URLEncoder.encode(str, "utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(str);
					url = "http://within2015.dothome.co.kr/core/api_app_test.php?method=setDetailInfo&num="
							+ List_info.accept_num.getText().toString() + "&memo=" + strEncode + "&resDate="
							+ Long.toString(List_info.reserve_fix) + "&finDate=" + "" + "&choice="
							+ Integer.toString(List_info.choice);
					System.out.println("url : " + url);
					send.execute(url);

				}
				i = new Intent(List_info.this, TabMain.class);
				i.putExtra("id", id);
				startActivity(i);
				finish();
			}
			break;
		case R.id.back_btn:
			i = new Intent(List_info.this, TabMain.class);
			i.putExtra("id", id);
			startActivity(i);
			finish();
			break;
		case R.id.map_btn:
			try {
				url = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=ko&address="
						+ URLEncoder.encode(addr.getText().toString(), "utf-8");
				send.execute(url);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
				uri = Uri.parse("geo:" + List_info.lat + "," + List_info.lng);

				i = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case R.id.sms1_btn:
			uri = Uri.parse("smsto:" + phone1.getText().toString());
			i = new Intent(Intent.ACTION_SENDTO, uri);
			i.putExtra("sms_body", name.getText() + " 고객님. 블랙박스 출장장착 설치기사입니다.연락 부탁드립니다. 감사합니다.");
			startActivity(i);
			break;
		case R.id.sms2_btn:
			uri = Uri.parse("smsto:" + phone1.getText().toString());
			i = new Intent(Intent.ACTION_SENDTO, uri);
			i.putExtra("sms_body", name.getText() + " 고객님의 블랙박스 설치 예약일은" + reserve_date.getText() + " / "
					+ reserve_time.getText() + "입니다. 감사합니다.");
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
		case R.id.finish_btn:
			choice = 4;
			total_state.setText("설치완료");
			total_state.setTextColor(Color.RED);
			state.setText("완      료");

			break;
		case R.id.cut_btn:
			choice = 0;
			total_state.setText("회송(연락두절)");
			reserve_date.setText("일자 설정");
			reserve_time.setText("시간 설정");
			state.setText("");
			total_state.setTextColor(Color.BLACK);

			break;
		case R.id.pass_btn:
			choice = 1;
			total_state.setText("회송(지역이관)");
			reserve_date.setText("일자 설정");
			reserve_time.setText("시간 설정");
			state.setText("");
			total_state.setTextColor(Color.BLACK);

			break;
		case R.id.return_btn:
			choice = 2;
			total_state.setText("회송(반품)");
			reserve_date.setText("일자 설정");
			reserve_time.setText("시간 설정");
			state.setText("");
			total_state.setTextColor(Color.BLACK);
			break;
		case R.id.etc_btn:
			choice = 3;
			total_state.setText("회송(기타)");
			reserve_date.setText("일자 설정");
			reserve_time.setText("시간 설정");
			state.setText("");
			total_state.setTextColor(Color.BLACK);
			break;
		case R.id.camera_btn:
			i = new Intent(List_info.this, CameraBtn.class);
			i.putExtra("accept_num", List_info.accept_num.getText().toString());
			startActivity(i);
			break;

		case R.id.reserve_date:
			new DatePickerDialog(List_info.this, dateSetListener, year, month, day).show();

			break;
		case R.id.reserve_time:

			new TimePickerDialog(List_info.this, timeSetListener, hour, minute, false).show();

			break;
		}

	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			reserve_date.setText(year + "." + (monthOfYear + 1) + "." + dayOfMonth);

			Calendar dCal = Calendar.getInstance();
			Calendar nCal = Calendar.getInstance();

			nYear = nCal.get(Calendar.YEAR);
			nMonth = nCal.get(Calendar.MONTH);
			nDay = nCal.get(Calendar.DAY_OF_MONTH);

			dCal.set(year, monthOfYear + 1, dayOfMonth);
			nCal.set(nYear, nMonth + 1, nDay);

			dYear = year;
			dMonth = monthOfYear + 1;
			dDay = dayOfMonth;
			d = dCal.getTimeInMillis() / (24 * 60 * 60 * 1000);
			n = nCal.getTimeInMillis() / (24 * 60 * 60 * 1000);

			r = (d - n);

			if ((int) r > 5)
				state.setText("여      유");
			else if ((int) r < 6 && (int) r > 2)
				state.setText("보      통");
			else if ((int) r < 3 && (int) r > 0)
				state.setText("임      박");
			else if ((int) r == 0)
				state.setText("당      일");
			else if (r < 0)
				state.setText("망      함");

			choice = 5;

			if (reserve_date.getText().toString().equalsIgnoreCase("일자 설정") == true) {
				reserve_time.setClickable(false);
			} else
				reserve_time.setClickable(true);
		}
	};

	// 설치 예약 시간 설정
	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			List_info.reserve_hour = hourOfDay;
			List_info.reserve_minute = minute;
			reserve_time.setText(hourOfDay + ":" + minute);
			choice = 5;
			total_state.setText("예약 완료");

		}
	};

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

class SendData extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... urls) {
		// TODO Auto-generated method stub
		BufferedReader reader = null;

		System.out.println("send");
		DefaultHttpClient client = new DefaultHttpClient();
		String line;
		String rs = "";
		// 데이터 전송
		try {

			HttpPost post = new HttpPost(urls[0]);
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);

			HttpResponse response = client.execute(post);

			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(), "utf-8"));

			while ((line = bufreader.readLine()) != null)
				rs += line;

			if (List_info.choice != 4) {
				String str = null;
				str = URLEncoder.encode(List_info.car.getText().toString(), "utf-8");
				String url = "http://within2015.dothome.co.kr/core/api_app_test.php?method=updateCarModel&num="
						+ List_info.accept_num.getText().toString() + "&model=" + str;
				post = new HttpPost(url);
				params = client.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 3000);
				HttpConnectionParams.setSoTimeout(params, 3000);

				response = client.execute(post);

				bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

				while ((line = bufreader.readLine()) != null)
					rs += line;

			}

			JSONObject json = new JSONObject(rs);
			JSONArray resultArray = new JSONArray(json.getString("results"));

			for (int i = 0; i < resultArray.length(); i++) {
				JSONObject resultJson = resultArray.getJSONObject(i);
				JSONObject geoJson = new JSONObject(resultJson.getString("geometry"));
				JSONObject locaJson = new JSONObject(geoJson.getString("location"));

				if (geoJson != null) {
					System.out.println("pointJson : " + geoJson);
					System.out.println("lat : " + locaJson.getDouble("lat"));

					System.out.println("lng : " + locaJson.getDouble("lng"));

					List_info.lat = locaJson.getDouble("lat");
					List_info.lng = locaJson.getDouble("lng");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

}

// 데이터 호출
class CallData extends AsyncTask<String, String, String> {
	LoadManager load;
	int pos;

	private String pic0, pic1, pic2;

	public CallData() {

		// TODO Auto-generated constructor stub
		load = new LoadManager(
				"http://within2015.dothome.co.kr/core/api_app_test.php?method=getOrderList&id=" + List_info.id);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String data = load.request();
		return data;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		long n, d;
		int nYear, nMonth, nDay, r;
		try {

			JSONArray jArray = new JSONArray(result);
			JSONObject json = null;

			json = jArray.getJSONObject(pos);
			if (json != null) {

				List_info.name.setText(json.getString("ord_customer_name").toString());

				List_info.accept_date.setText(json.getString("ord_register_date").toString());
				List_info.accept_num.setText(json.getString("ord_num").toString());

				if (json.getString("ord_addcost_site").toString().equalsIgnoreCase("") == true
						|| json.getString("ord_addcost_site").toString().equalsIgnoreCase("null") == true)
					List_info.field_pay.setText("");
				else
					List_info.field_pay.setText(json.getString("ord_addcost_site").toString());

				if (json.getString("ord_phone_1").toString().equalsIgnoreCase("") == true
						|| json.getString("ord_phone_1").toString().equalsIgnoreCase("null") == true)
					List_info.phone1.setText("");
				else
					List_info.phone1.setText(json.getString("ord_phone_1").toString());

				if (json.getString("ord_phone_2").toString().equalsIgnoreCase("") == true
						|| json.getString("ord_phone_2").toString().equalsIgnoreCase("null") == true)
					List_info.phone2.setText("");
				else
					List_info.phone2.setText(json.getString("ord_phone_2").toString());

				if (json.getString("ord_memo").toString().equals("null") == true)
					List_info.needs.setText("");
				else {
					List_info.needs.setText(json.getString("ord_memo").toString().replace("<br>", "\n"));

				}
				List_info.blackbox.setText(json.getString("ord_product_name").toString());
				List_info.channel.setText(json.getString("ord_channel"));
				List_info.addr.setText(json.getString("ord_address").toString());
				List_info.car.setText(json.getString("ord_car_model").toString());

				List_info.total_state.setText(json.getString("ord_state").toString());

				if (List_info.total_state.getText().equals("회송(연락두절)") == true)
					List_info.choice = 0;
				else if (List_info.total_state.getText().equals("회송(지역이관)") == true)
					List_info.choice = 1;
				else if (List_info.total_state.getText().equals("회송(반품)") == true)
					List_info.choice = 2;
				else if (List_info.total_state.getText().equals("회송(기타)") == true)
					List_info.choice = 3;
				else if (List_info.total_state.getText().equals("설치완료") == true)
					List_info.choice = 4;
				else if (List_info.total_state.getText().equals("예약완료") == true)
					List_info.choice = 5;
				else if (List_info.total_state.getText().equals("배정완료") == true)
					List_info.choice = 6;

				if (json.getString("ord_reserve_date").toString().equalsIgnoreCase("") == true
						|| json.getString("ord_reserve_date").toString().equalsIgnoreCase("null") == true
						|| json.getString("ord_reserve_date").toString().equalsIgnoreCase("0") == true) {

					List_info.reserve_date.setText("일자 설정");
				} else {
					List_info.reserve_date.setText(json.getString("ord_reserve_date").toString().substring(0, 4) + "."
							+ json.getString("ord_reserve_date").toString().substring(4, 6) + "."
							+ json.getString("ord_reserve_date").toString().substring(6, 8));
					List_info.reserve_time.setText(json.getString("ord_reserve_date").toString().substring(8, 10) + ":"
							+ json.getString("ord_reserve_date").toString().substring(10, 12));
					List_info.total_state.setText("예약 완료");
					Calendar dCal = Calendar.getInstance();
					Calendar nCal = Calendar.getInstance();
					List_info.mHour = nCal.get(Calendar.HOUR_OF_DAY);
					List_info.mMinute = nCal.get(Calendar.MINUTE);

					nYear = nCal.get(Calendar.YEAR);
					nMonth = nCal.get(Calendar.MONTH) + 1;
					nDay = nCal.get(Calendar.DAY_OF_MONTH);

					nCal.set(nYear, nMonth, nDay);

					n = nCal.getTimeInMillis();

					dCal.set(Integer.parseInt(json.getString("ord_reserve_date").toString().substring(0, 4)),
							Integer.parseInt(json.getString("ord_reserve_date").toString().substring(4, 6)),
							Integer.parseInt(json.getString("ord_reserve_date").toString().substring(6, 8)));
					nCal.set(nYear, nMonth, nDay);

					d = dCal.getTimeInMillis() / (24 * 60 * 60 * 1000);
					n = nCal.getTimeInMillis() / (24 * 60 * 60 * 1000);

					r = (int) (d - n);

					if (r > 5)
						List_info.state.setText("여유");
					else if (r < 6 && r > 2)
						List_info.state.setText("보통");
					else if (r < 3 && r > 0)
						List_info.state.setText("임박");
					else if (r == 0)
						List_info.state.setText("당일");
					else if (r < 0)
						List_info.state.setText("망함");
				}

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
