package com.example.blackboxwithin;

import android.app.ActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.TabHost;

/*
 * �� ����
 */

public class TabMain extends TabActivity {

	Intent id, i;
	static String strId;
	static String strname;
	ActivityManager am;

	TabHost th;
	TabHost.TabSpec spec;

	// ������� ����
	WakeLock wakeLock = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		
		if (getIntent() != null) {
			id = getIntent();
			strId = id.getStringExtra("id").toString();
			strname = id.getStringExtra("name").toString();
		}

		th = getTabHost();

		i = new Intent(this, BeforeList.class);
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = th.newTabSpec("tab1").setIndicator("��ġ���").setContent(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

		th.addTab(spec);

		i = new Intent(this, FinList.class);
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = th.newTabSpec("tab2").setIndicator("��ġ�Ϸ�").setContent(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
		th.addTab(spec);

		i = new Intent(this, NoticeList.class);
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = th.newTabSpec("tab3").setIndicator("��������").setContent(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
		th.addTab(spec);

		th.setCurrentTab(0);

	}

	// ������� ����
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
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
