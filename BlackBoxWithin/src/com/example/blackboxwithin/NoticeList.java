package com.example.blackboxwithin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/*
 * @공지사항@ - List
 */
public class NoticeList extends Activity {

	StringBuilder sBuilder;
	String json;
	JsonDownLoad task;
	ListViewAdapter adapter;
	ListView listView;

	ListView notice_listView;
	Notice_JsonDownLoad notice_task;
	NoticeListViewAdapter notice_adapter;

	Button connBtn, fin_connBtn, notice_connBtn;
	Intent id, i, intent;
	String strId;

	TabMain mainKey;
	long backKeyPressTime;
	Toast toast;
	String url;

	WakeLock wakeLock = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noticelist);

		url = "http://within2015.dothome.co.kr/core/api_app_test.php?method=getNotice";

		connectCheck("tab3", url);

		notice_listView = (ListView) findViewById(R.id.notice_list);
		notice_adapter = new NoticeListViewAdapter(this);

		notice_listView.setAdapter(notice_adapter);

		notice_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NoticeList.this, Notice_info.class);
				i.putExtra("pos", Integer.valueOf(position).toString());
				startActivity(i);

			}
		});

	}

	public void connectCheck(String page, String url) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			notice_task = new Notice_JsonDownLoad(this, page, url);
			notice_task.execute();

		} else
			Toast.makeText(this, "네트워크 상태를 확인하십시오", Toast.LENGTH_SHORT).show();
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent home = new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_DEFAULT);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		connectCheck("tab3", url);
		stopService(new Intent(getBaseContext(), BackGroundService.class));

		if (BackGroundService.count > 43200) {
			intent = new Intent(getBaseContext(), Login.class);
			startActivity(intent);
			finish();
		}
	}

}
