package com.example.blackboxwithin;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/*
 * 로그인 유지 기능
 */

public class BackGroundService extends Service implements Runnable {

	public static final String TAG = "BackGroundService";

	static int count = 0;
	Context context;
	ActivityManager am;
	static Thread mThread;
	static boolean stopFlag = true;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		stopFlag = true;
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (stopFlag) {
			try {
				Log.i(TAG, "BackGroundService called #" + count);
				count++;
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		count = 0;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopFlag = false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
