package com.example.blackboxwithin;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
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
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * @설치대기@ - 상세정보 - 카메라 버튼
 * 사진 추가 가능, 사진은 3개만
 */
public class CameraBtn extends Activity implements View.OnClickListener {

	static int CAMERA = 1;
	Button backBtn, shutBtn, delBtn, img1Btn, img2Btn, img3Btn;
	static ImageView imgView;
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
		setContentView(R.layout.camerabtn);
		Intent i = this.getIntent();
		str = i.getStringExtra("accept_num");

		title = (TextView) findViewById(R.id.title);
		title.setText("사    진  1");

		backBtn = (Button) findViewById(R.id.cambackBtn);
		shutBtn = (Button) findViewById(R.id.shutBtn);
		delBtn = (Button) findViewById(R.id.delBtn);

		img1Btn = (Button) findViewById(R.id.img1Btn);
		img2Btn = (Button) findViewById(R.id.img2Btn);
		img3Btn = (Button) findViewById(R.id.img3Btn);

		imgView = (ImageView) findViewById(R.id.imgView);
		
		sig = new ServerImg();
		sig.execute("http://within2015.dothome.co.kr/cont_img/" + str + "_" + pos + ".jpg");

		findViewById(R.id.cambackBtn).setOnClickListener(this);
		findViewById(R.id.shutBtn).setOnClickListener(this);
		findViewById(R.id.delBtn).setOnClickListener(this);

		findViewById(R.id.img1Btn).setOnClickListener(this);
		findViewById(R.id.img2Btn).setOnClickListener(this);
		findViewById(R.id.img3Btn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cambackBtn:
			finish();
			break;
		case R.id.shutBtn:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			startActivityForResult(intent, CAMERA);
			break;
		case R.id.delBtn:
			deletePhoto(pos, "deletePicture");
			imgView.setImageDrawable(null);
			imgView.invalidate();
			break;
		case R.id.img1Btn:
			pos = 0;
			title.setText("사    진  1");
			sig = new ServerImg();
			sig.execute("http://within2015.dothome.co.kr/cont_img/" + str + "_" + pos + ".jpg");

			System.out.println("1btn");
			break;
		case R.id.img2Btn:
			pos = 1;
			title.setText("사    진  2");
			sig = new ServerImg();
			sig.execute("http://within2015.dothome.co.kr/cont_img/" + str + "_" + pos + ".jpg");

			System.out.println("2btn");
			break;
		case R.id.img3Btn:
			pos = 2;
			title.setText("사    진  3");
			sig = new ServerImg();
			sig.execute("http://within2015.dothome.co.kr/cont_img/" + str + "_" + pos + ".jpg");

			System.out.println("3btn");
			break;

		}
	}

	private void uploadPhoto(final Bitmap bitmap, final int index, final String method) {

		Thread thread = new Thread(new Runnable() {

			public void run() {
				InputStream is = null;
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				byte[] ba = bao.toByteArray();

				String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("image", ba1));
				nameValuePairs.add(new BasicNameValuePair("id", str));
				nameValuePairs.add(new BasicNameValuePair("index", Integer.valueOf(index).toString()));

				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(
							"http://within2015.dothome.co.kr/core/api_app_test.php?method=uploadPicture");
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = client.execute(post);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();

					// convert response to string
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 100);
					StringBuilder stringBuilder = new StringBuilder();
					String line = null;

					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					is.close();

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();

	}

	// 사진 촬영 후 업로드 및 뷰 세팅
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CAMERA) {
			Bitmap bitmap = (Bitmap) data.getExtras().get("data");

			if (pos == 0) {
				img0str = bitmapToByteArray(bitmap);
				uploadPhoto(bitmap, pos, "uploadPicture");
				imgView.setImageBitmap(byteArraytoBitimap(img0str));
			} else if (pos == 1) {
				img1str = bitmapToByteArray(bitmap);
				uploadPhoto(bitmap, pos, "uploadPicture");
				imgView.setImageBitmap(byteArraytoBitimap(img1str));
			} else if (pos == 2) {
				img2str = bitmapToByteArray(bitmap);
				uploadPhoto(bitmap, pos, "uploadPicture");
				imgView.setImageBitmap(byteArraytoBitimap(img2str));
			}

		}
	}

	// 뷰 사진 삭제 및 요청
	private void deletePhoto(final int index, final String method) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				InputStream is = null;
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("id", str));
				nameValuePairs.add(new BasicNameValuePair("index", Integer.valueOf(index).toString()));
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(
							"http://within2015.dothome.co.kr/core/api_app_test.php?method=deletePicture");
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = client.execute(post);
					HttpEntity entity = response.getEntity();

					is = entity.getContent();

					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 100);
					StringBuilder stringBuilder = new StringBuilder();
					String line = null;

					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					is.close();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();

	}

	// 이미지 전송,수신 관련 클래스
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
			imgView.setImageBitmap(bitmap);
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

	// 백버튼 처리
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

	// 백그라운드 유지 시간 체크
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

// 이미지 요청
class CallImg extends AsyncTask<String, String, String> {
	LoadManager load;
	int pos;

	public CallImg() {
		load = new LoadManager("http://ionic.gtf.kr:8081/app1/core/test.php?method=downloadPictures&num="
				+ Integer.parseInt(CameraBtn.str));
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
				CameraBtn.img0str = json.getString("ord_picture_0").toString().getBytes();
				CameraBtn.img1str = json.getString("ord_picture_1").toString().getBytes();
				CameraBtn.img2str = json.getString("ord_picture_2").toString().getBytes();
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

// 이미지 전송
class SendImg extends AsyncTask<String, Void, Void> {
	@Override
	protected Void doInBackground(String... urls) {
		// TODO Auto-generated method stub
		BufferedReader reader = null;
		System.out.println("urls : " + urls[0]);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}