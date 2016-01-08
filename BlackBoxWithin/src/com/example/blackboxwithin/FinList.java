package com.example.blackboxwithin;

import java.util.ArrayList;

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
 * @설치완료@ - 리스트
 */
public class FinList extends Activity {

	ArrayList<ListViewItem> finish_data = new ArrayList<ListViewItem>();

	StringBuilder sBuilder;
	String json;

	JsonDownLoad task;
	ListViewAdapter adapter;
	ListView listView;

	ListView finish_listView;
	Fin_JsonDownLoad fin_task;
	Fin_ListViewAdapter finish_adapter;

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
		setContentView(R.layout.finlist);

		url = "http://within2015.dothome.co.kr/core/api_app_test.php?method=getFinishList&id=" + mainKey.strId;
		connectCheck("tab2", url);
		finish_listView = (ListView) findViewById(R.id.finish_list);

		finish_adapter = new Fin_ListViewAdapter(this);
		finish_listView.setAdapter(finish_adapter);
		finish_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FinList.this, Finish_info.class);
				i.putExtra("key", Integer.valueOf(position).toString());
				i.putExtra("id", mainKey.strId);
				startActivity(i);

			}
		});

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

	public void connectCheck(String page, String url) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			fin_task = new Fin_JsonDownLoad(this, page, url);
			fin_task.execute();

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
		connectCheck("tab2", url);
		stopService(new Intent(getBaseContext(), BackGroundService.class));

		if (BackGroundService.count > 43200) {
			intent = new Intent(getBaseContext(), Login.class);
			startActivity(intent);
			finish();
		}
	}

}
