package com.billy.connectionring;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.billy.connectionring.connection.WebsocketHelper;
import com.billy.connectionring.login.LoginActivity;
import com.billy.connectionring.sharedpreference.SharedPreferenceSystem;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class UserActivity extends Activity {

	WebsocketHelper mWebSocketHelper;
	
	EditText et_id;
	EditText et_oldpw;
	EditText et_pw;
	EditText et_pw2;
	EditText et_name;

	ImageButton imv_correct;
	ImageButton imv_cancel;
	ImageButton imv_logout;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		et_id = (EditText)findViewById(R.id.et_user_id);
		et_oldpw = (EditText)findViewById(R.id.et_user_old_pw);
		et_pw = (EditText)findViewById(R.id.et_user_pw);
		et_pw2 = (EditText)findViewById(R.id.et_user_pw2);
		et_name = (EditText)findViewById(R.id.et_user_name);
		
		et_id.setText(SharedPreferenceSystem.getPreferences(this, "id"));
		et_name.setText(SharedPreferenceSystem.getPreferences(this, "name"));
		
		imv_correct = (ImageButton)findViewById(R.id.btn_user_correct);
		imv_cancel = (ImageButton)findViewById(R.id.btn_user_cancel);
		imv_logout = (ImageButton)findViewById(R.id.btn_user_logout);
		
		imv_correct.setOnClickListener(mOnClickListener);
		imv_cancel.setOnClickListener(mOnClickListener);
		imv_logout.setOnClickListener(mOnClickListener);
		
		mWebSocketHelper = WebsocketHelper.getInstance(getApplicationContext());
	};

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.btn_user_correct :
				if(register() == true) {
					Toast.makeText(getApplicationContext(), "변경 성공",  Toast.LENGTH_SHORT).show();
					finish();
					break;
				}else {
					Toast.makeText(getApplicationContext(), "변경 실패",  Toast.LENGTH_SHORT).show();
					break;
				}
			case R.id.btn_user_cancel  :
				finish();
				break;
			case R.id.btn_user_logout :
				try {
					SharedPreferenceSystem.savePreferences(getApplicationContext(), "autologin",  "false");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mWebSocketHelper.sendMessage(null, Constants.req_logout);
				//mWebSocketHelper.closeWebSocket();
				
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				break;
			default : 
				break;
			}
		}

	};
	
	
	private boolean register()
	{
		String id = et_id.getText().toString();
		String oldpw = et_oldpw.getText().toString();
		String pw = et_pw.getText().toString();
		String pw2 = et_pw2.getText().toString();
		String name = et_name.getText().toString();

		et_oldpw.setText("");
		et_pw.setText("");
		et_pw2.setText("");
		
		if(pw.equals(pw2) == false) {
			return false;
		}
		System.out.println("httptest" + id + oldpw + pw + name);
		HttpPost request = makeHttpPost(id , oldpw, pw, name,  "http://210.118.74.153/ring/index.php/user_module/edit/" ) ; 
		
		HttpClient client = new DefaultHttpClient() ; 
		ResponseHandler<String> reshandler = new BasicResponseHandler() ;
		try {
			String result = client.execute( request, reshandler ) ;
			System.out.println("httptest" + result);
			
			JSONObject object;
			try {
				object = new JSONObject(result);

				if(!object.getBoolean("result")) {
					return false;	
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		} 
		
		try {
			SharedPreferenceSystem.savePreferences(this,  "id", id);
			SharedPreferenceSystem.savePreferences(this,  "password", pw);
			SharedPreferenceSystem.savePreferences(this,  "name", name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	

	//Post 방식일경우
	private HttpPost makeHttpPost(String id, String oldpw, String pw, String name, String url) { 
		// TODO Auto-generated method stub 
		HttpPost request = new HttpPost( url ) ; 
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;
		nameValue.add( new BasicNameValuePair( "id", id) ) ;
		nameValue.add( new BasicNameValuePair( "oldpw", oldpw) ) ;
		nameValue.add( new BasicNameValuePair( "newpw", pw ) ) ; 
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
	
}
