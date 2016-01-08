package com.example.blackboxwithin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * @설치완료@ - 상세정보 - 카메라 버튼
 * 촬영 기능 불가, 사진은 3개
 */
public class Fin_CameraBtn extends Activity implements View.OnClickListener {

	static int CAMERA = 1;
	Button fin_backBtn, fin_img1Btn, fin_img2Btn, fin_img3Btn;
	static ImageView fin_imgView;

	Bitmap bitmap;
	WakeLock wakeLock = null;
	String upUrl, downUrl;
	TextView title;
	int pos = 0;
	static String str;
	static byte[] img0str, img1str, img2str = null;

	static String EXTERNAL_STORAGE_PATH = "";
	ServerImg sig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fin_camerabtn);
		Intent i = this.getIntent();
		str = i.getStringExtra("accept_num");

		title = (TextView) findViewById(R.id.fin_title);
		title.setText("사    진  1");

		fin_backBtn = (Button) findViewById(R.id.fin_cambackBtn);

		fin_img1Btn = (Button) findViewById(R.id.fin_img1Btn);
		fin_img2Btn = (Button) findViewById(R.id.fin_img2Btn);
		fin_img3Btn = (Button) findViewById(R.id.fin_img3Btn);

		fin_imgView = (ImageView) findViewById(R.id.fin_imgView);
		sig = new ServerImg();
		sig.execute("http://within2015.dothome.co.kr/cont_img/" + str + "_" + pos + ".jpg");
		findViewById(R.id.fin_cambackBtn).setOnClickListener(this);

		findViewById(R.id.fin_img1Btn).setOnClickListener(this);
		findViewById(R.id.fin_img2Btn).setOnClickListener(this);
		findViewById(R.id.fin_img3Btn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fin_cambackBtn:
			finish();
			break;

		case R.id.fin_img1Btn:
			pos = 0;
			title.setText("사    진  1");
			sig = new ServerImg();
			sig.execute("http://within2015.dothome.co.kr/cont_img/" + str + "_" + pos + ".jpg");

			System.out.println("1btn");
			break;
		case R.id.fin_img2Btn:
			pos = 1;
			title.setText("사    진  2");
			sig = new ServerImg();
			sig.execute("http://within2015.dothome.co.kr/cont_img/" + str + "_" + pos + ".jpg");

			System.out.println("2btn");
			break;
		case R.id.fin_img3Btn:
			pos = 2;
			title.setText("사    진  3");
			sig = new ServerImg();
			sig.execute("http://within2015.dothome.co.kr/cont_img/" + str + "_" + pos + ".jpg");

			System.out.println("3btn");
			break;

		}
	}

	private class ServerImg extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... urls) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(urls[0]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.connect();

				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap img) {
			fin_imgView.setImageBitmap(bitmap);
		}

	}

	public static byte[] bitmapToByteArray(Bitmap bitmap) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		return byteArray;
	}

	public static Bitmap byteArraytoBitimap(byte[] $byteArray) {
		Bitmap bitmap = BitmapFactory.decodeByteArray($byteArray, 0, $byteArray.length);
		return bitmap;

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_DEFAULT);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		stopService(new Intent(getBaseContext(), BackGroundService.class));
		if (BackGroundService.count > 43200) {
			Intent intent = new Intent(getBaseContext(), Login.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}

// 이미지 호출
class Fin_CallImg extends AsyncTask<String, String, String> {
	LoadManager load;
	int pos;

	public Fin_CallImg() {
		load = new LoadManager("http://ionic.gtf.kr:8081/app1/core/test.php?method=downloadPictures&num="
				+ Integer.parseInt(Fin_CameraBtn.str));
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

			JSONArray jArray = new JSONArray(result);
			JSONObject json = null;

			json = jArray.getJSONObject(pos);
			if (json != null) {
				Fin_CameraBtn.img0str = json.getString("ord_picture_0").toString().getBytes();
				Fin_CameraBtn.img1str = json.getString("ord_picture_1").toString().getBytes();
				Fin_CameraBtn.img2str = json.getString("ord_picture_2").toString().getBytes();
			}
		} catch (Exception e)

		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	void setPos(int pos) {
		this.pos = pos;
	}
}