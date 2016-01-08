package com.example.blackboxwithin;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * @로그인@
 */
public class Login extends Activity implements View.OnClickListener {

	static EditText id_edit;
	static EditText pw_edit;
	static Boolean check = false;
	static Intent i;

	CheckBox checkBox;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	ProgressDialog dialog;
	static TabMain tm;

	long backKeyPressTime;
	Toast toast;
	String login_rs;
	String login_name;
	ImageView logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		logo = (ImageView) findViewById(R.id.logo);
		logo.setImageResource(R.drawable.logo);
		findViewById(R.id.loginBtn).setOnClickListener(this);
		i = new Intent(this, TabMain.class);
		id_edit = (EditText) findViewById(R.id.idTxt);
		pw_edit = (EditText) findViewById(R.id.pwTxt);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		prefs = getSharedPreferences("Pref_id", MODE_PRIVATE);

		checkBox.setChecked(prefs.getBoolean("check", false));
		if (checkBox.isChecked() == true) {

			if (prefs.getString("id", "").equalsIgnoreCase("null") == false
					|| prefs.getString("id", "").equalsIgnoreCase("") == false)
				id_edit.setText(prefs.getString("id", "").toString());
		}
	}

	public void onClick(View v) {
		CallLogin cl = new CallLogin(this);
		cl.execute();

	}

	class CallLogin extends AsyncTask<String, String, String> {
		LoadManager load;
		int pos;

		Login login;

		public CallLogin(Context context) {
			// TODO Auto-generated constructor stub
			login = (Login) context;

			load = new LoadManager("http://within2015.dothome.co.kr/core/api_app_test.php?" + "method=checkInfo&id="
					+ Login.id_edit.getText().toString() + "&pw=" + Login.pw_edit.getText().toString());
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String data = load.request();
			return data;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();

			try {

				JSONObject json = new JSONObject(result);

				if (json != null) {

					login_rs = json.getString("result").toString();
					login_name = json.getString("data").toString();

				}

				System.out.println("rs : " + login_rs);
				System.out.println("name : " + login_name);
				if (login_rs.equalsIgnoreCase("fail") == true) {
					Login.check = false;

					Toast.makeText(getApplicationContext(), "ID/PW를 확인하세요.", Toast.LENGTH_SHORT).show();

				} else {
					i.putExtra("id", Login.id_edit.getText().toString());
					i.putExtra("name", login_name);

					tm.strId = id_edit.getText().toString();

					prefs = getSharedPreferences("Pref_id", MODE_PRIVATE);
					editor = prefs.edit();
					editor.putBoolean("check", checkBox.isChecked());

					if (checkBox.isChecked() == true) {
						editor.putString("id", Login.id_edit.getText().toString());

					}
					editor.commit();
					startActivity(i);
					finish();
					//
					Login.check = true;
				}
				//
			} catch (

			Exception e)

			{
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(login);
			dialog.show();

		}

	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent home = new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_DEFAULT);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);
	}
}
