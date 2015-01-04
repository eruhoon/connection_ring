package com.billy.connectionring.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.billy.connectionring.Constants;
import com.billy.connectionring.MainActivity;
import com.billy.connectionring.R;
import com.billy.connectionring.connection.JSONHelper;
import com.billy.connectionring.connection.User;
import com.billy.connectionring.connection.WebsocketHelper;
import com.billy.connectionring.sharedpreference.SharedPreferenceSystem;

public class LoginActivity extends Activity implements Observer{

	JSONHelper jsonHelper;
	WebsocketHelper mWebSocketHelper;
	User user;

	final private int SELECT_MAIN = 0;
	final private int SELECT_LOGIN = 1;
	final private int SELECT_REGISTER = 2;

	private Animation out_animation;
	private Animation in_animation;

	private LinearLayout layout_main;
	private LinearLayout layout_login;
	private LinearLayout layout_register;

	private ImageButton imv_main_register;
	private ImageButton imv_main_login;
	private ImageButton imv_login_login;
	private ImageButton imv_login_cancel;
	private ImageButton imv_register_register;
	private ImageButton imv_register_cancel;

	private EditText et_login_id;
	private EditText et_login_pw;
	private EditText et_register_id;
	private EditText et_register_pw;
	private EditText et_register_pw2;
	private EditText et_register_name;

	private CheckBox check_auto;

	int currentLayout;
	int beforeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		//setTimer();

		mWebSocketHelper = WebsocketHelper.getInstance(this); 


		setAnimation();

		setLayout();

		jsonHelper = JSONHelper.getInstance(this);
		jsonHelper.addObserver(this);

		String isAutoLogin = SharedPreferenceSystem.getPreferences(this,  "autologin");


		if(isAutoLogin.equals("true")) {
			AutoLogin();
		}

		//createTutorial();
	}
	/*
	private void createTutorial()
	{
		String tutorialIsPlayed = SharedPreferenceSystem.getPreferences(this, "tutorial");

		if(tutorialIsPlayed == "")
			try {
				SharedPreferenceSystem.savePreferences(this,  "tutorial",  "false");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		tutorialIsPlayed = SharedPreferenceSystem.getPreferences(this, "tutorial");

		if(tutorialIsPlayed == "false")
		{
			// 튜토리얼 삽입
			String query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component( 
					10000, ));

			try {
				SharedPreferenceSystem.savePreferences(this,  "tutorial",  "true");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	 */

	private void setLayout() {
		layout_main = (LinearLayout)findViewById(R.id.login_main_layout);
		layout_login = (LinearLayout)findViewById(R.id.login_login_layout);
		layout_register = (LinearLayout)findViewById(R.id.login_register_layout);

		imv_main_register = (ImageButton)findViewById(R.id.login_main_register);
		imv_main_login = (ImageButton)findViewById(R.id.login_main_login);
		imv_login_login = (ImageButton)findViewById(R.id.login_login_login);
		imv_login_cancel = (ImageButton)findViewById(R.id.login_login_cancel);
		imv_register_register = (ImageButton)findViewById(R.id.login_register_register);
		imv_register_cancel = (ImageButton)findViewById(R.id.login_register_cancel);

		et_login_id = (EditText)findViewById(R.id.login_login_edit_id);
		et_login_pw = (EditText)findViewById(R.id.login_login_edit_pw);
		et_register_id = (EditText)findViewById(R.id.login_register_edit_id);
		et_register_pw = (EditText)findViewById(R.id.login_register_edit_pw);
		et_register_pw2 = (EditText)findViewById(R.id.login_register_edit_pw_2);
		et_register_name = (EditText)findViewById(R.id.login_register_edit_name);

		check_auto = (CheckBox)findViewById(R.id.login_login_check);

		imv_main_register.setOnClickListener(mOnClickListener);
		imv_main_login.setOnClickListener(mOnClickListener);
		imv_login_login.setOnClickListener(mOnClickListener);
		imv_login_cancel.setOnClickListener(mOnClickListener);
		imv_register_register.setOnClickListener(mOnClickListener);
		imv_register_cancel.setOnClickListener(mOnClickListener);

		check_auto.setOnCheckedChangeListener(mOnCheckedListener);
		currentLayout = SELECT_MAIN;
		beforeLayout = SELECT_MAIN;
	}

	private void setAnimation() {

		in_animation = AnimationUtils.loadAnimation(this, R.anim.in_animation); 

		out_animation = AnimationUtils.loadAnimation(this, R.anim.out_animation);
		out_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if(currentLayout == SELECT_REGISTER && beforeLayout == SELECT_MAIN)
				{
					layout_main.setVisibility(View.GONE);
					layout_register.setVisibility(View.VISIBLE);
					layout_register.startAnimation(in_animation);

				}else if(currentLayout == SELECT_LOGIN && beforeLayout == SELECT_MAIN)
				{
					layout_main.setVisibility(View.GONE);
					layout_login.setVisibility(View.VISIBLE);
					layout_login.startAnimation(in_animation);
				}

				else if(currentLayout == SELECT_MAIN && beforeLayout == SELECT_REGISTER)
				{
					layout_register.setVisibility(View.GONE);
					layout_main.setVisibility(View.VISIBLE);
					layout_main.startAnimation(in_animation);
				}

				else if(currentLayout == SELECT_MAIN && beforeLayout == SELECT_LOGIN)
				{
					layout_login.setVisibility(View.GONE);
					layout_main.setVisibility(View.VISIBLE);
					layout_main.startAnimation(in_animation);
				}
			}
		});

	}



	private OnCheckedChangeListener mOnCheckedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked)
			{
				try {
					SharedPreferenceSystem.savePreferences(getApplicationContext(), "autologin",  "true");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				try {
					SharedPreferenceSystem.savePreferences(getApplicationContext(), "autologin",  "false");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	};

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.login_main_register : 
				layout_main.startAnimation(out_animation);
				currentLayout = SELECT_REGISTER;
				beforeLayout = SELECT_MAIN;
				break;
			case R.id.login_main_login : 
				layout_main.startAnimation(out_animation);
				currentLayout = SELECT_LOGIN;
				beforeLayout = SELECT_MAIN;
				break;
			case R.id.login_login_login : 
				login();
				break;
			case R.id.login_login_cancel :
				layout_login.startAnimation(out_animation);
				currentLayout = SELECT_MAIN;
				beforeLayout = SELECT_LOGIN;
				break;
			case R.id.login_register_register :
				register();
				layout_register.startAnimation(out_animation);
				currentLayout = SELECT_MAIN;
				beforeLayout = SELECT_REGISTER;
				break;
			case R.id.login_register_cancel :
				layout_register.startAnimation(out_animation);
				currentLayout = SELECT_MAIN;
				beforeLayout = SELECT_REGISTER;
				break;
			default : 
				break;
			}
		}
	};


	private void register()
	{
		String id = et_register_id.getText().toString();
		String pw = et_register_pw.getText().toString();
		String pw2 = et_register_pw2.getText().toString();
		String name = et_register_name.getText().toString();

		try {
			SharedPreferenceSystem.savePreferences(this,  "name", name);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(registerCheck(id, pw, pw2, name) == false)
		{
			return;
		}

		et_register_id.setText("");
		et_register_pw.setText("");
		et_register_pw2.setText("");
		et_register_name.setText("");

		HttpPost request = makeHttpPost(id , pw, name,  "http://210.118.74.153/ring/index.php/user_module/join/" ) ; 

		HttpClient client = new DefaultHttpClient() ; 
		ResponseHandler<String> reshandler = new BasicResponseHandler() ;
		try {
			String result = client.execute( request, reshandler ) ;

			System.out.println("httptest" + result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	//Post 방식일경우
	private HttpPost makeHttpPost(String id, String pw, String name, String url) { 
		// TODO Auto-generated method stub 
		HttpPost request = new HttpPost( url ) ; 
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;
		nameValue.add( new BasicNameValuePair( "id", id) ) ; 
		nameValue.add( new BasicNameValuePair( "pw", pw ) ) ; 
		nameValue.add( new BasicNameValuePair( "name", name) ) ; 
		request.setEntity( makeEntity(nameValue) ) ; 
		return request ; 
	} 

	private HttpEntity makeEntity( Vector<NameValuePair> nameValue ) { 
		HttpEntity result = null ; 
		try { 
			result = new UrlEncodedFormEntity( nameValue, HTTP.UTF_8) ; 
		} catch (UnsupportedEncodingException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 
		return result ; 
	} 

	private boolean registerCheck(String id, String pw, String pw2, String name)
	{
		boolean res = true;

		if(id.length() < 1) {
			Toast.makeText(this,  "id가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(pw.length() < 2) {
			Toast.makeText(this,  "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(name.length() < 2) {
			Toast.makeText(this,  "이름이 너무 짧습니다.", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(pw.equals(pw2) == false) {
			Toast.makeText(this,  "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
			return false;
		}


		return res;
	}

	private boolean loginCheck(String id, String pw)
	{
		if(id.length() < 1) {
			Toast.makeText(this,  "id가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
		}
		else if(pw.length() < 2) {
			Toast.makeText(this,  "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	private void AutoLogin()
	{
		String saved_id = SharedPreferenceSystem.getPreferences(this,  "id");
		String saved_password = SharedPreferenceSystem.getPreferences(this,  "password");
		user = User.getInstance();
		user.setId(saved_id);
		user.setPw(saved_password);
		loginCallback(true, true);
		//showPDialog();
		//mWebSocketHelper.sendMessage(null, Constants.req_login);
	}

	private void login() 
	{

		String id = et_login_id.getText().toString();
		String pw = et_login_pw.getText().toString();

		if(loginCheck(id, pw) == false)
		{
			return;
		}
		pw = toMD5(pw);
		//pw = "098f6bcd4621d373cade4e832627b4f6";
		user = User.getInstance();
		user.setId(id);
		user.setPw(pw);
		mWebSocketHelper.sendMessage(null, Constants.req_login);

		//showPDialog();
	}

	public void showLoginText(final String str)
	{
		this.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void loginCallback(boolean isLoginOK, boolean isAuto)
	{
		if(isLoginOK)
		{

			//hidePDialog();
			if(mWebSocketHelper.isConnection() == true) {
					showLoginText("로그인 성공");
			}else {
				showLoginText("서버와의 연결 또는 멤버십 와이파이연결여부를 확인해 주세요");
				return;
			}

			try {
				SharedPreferenceSystem.savePreferences(this,  "id", user.getId());
				SharedPreferenceSystem.savePreferences(this,  "password", user.getPw());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);

		}else {
			Toast.makeText(getApplicationContext(), "로그인 오류", Toast.LENGTH_SHORT).show();
		}
	}

	public String toMD5(String str){
		String MD5 = ""; 
		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes()); 
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();

		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			MD5 = null; 
		}
		return MD5;
	}

	@Override
	public void update(Observable observable, final Object data) {
		// TODO Auto-generated method stub

		if (observable instanceof JSONHelper) {

			String input = (String)data;
			Log.d("kkk", input);
			if(input.equals("LoginOK")) 
				loginCallback(true, false);
			else
				loginCallback(false, false);
		}
	}

/*
	private static Timer mTimer;
	private static TimerTask mTask;
	private ProgressDialog pDialog;

	public void setTimer() {
		mTimer = new Timer();
		mTask = new TimerTask() {
			@Override
			public void run() {
				hidePDialog();
				showLoginText("연결에 실패하였습니다.");
			}
		};
	}

	public void hidePDialog()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {    
				if (pDialog != null) {
					pDialog.dismiss();
					pDialog = null;
				}
			}
		}).start();
	}




	public void showPDialog() {
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
			pDialog.setIndeterminate(false);
			pDialog.setMessage("Loading...");
			pDialog.show();
			mTimer.schedule(mTask, 10000);
		}
	}
*/

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		jsonHelper.deleteObserver(this);
	}

}
