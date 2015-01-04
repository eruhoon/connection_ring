package com.billy.connectionring.connection;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.billy.connectionring.Constants;
import com.billy.connectionring.sharedpreference.SharedPreferenceSystem;
import com.billy.connectionring.sqllite.SqlLiteManager;

public class WebsocketHelper {

	private static WebsocketHelper instance;

	private WebSocketClient mWebSocket;

	private Context context;
	
	private SqlLiteManager dbManager;
	
	private JSONHelper jsonHelper;
	
	private boolean isConnection = false;
	private boolean isLogin = false;
	
	public static synchronized WebsocketHelper getInstance(Context context) {
		if(instance == null) {
			Log.d("kkk", "websocket new create");
			instance = new WebsocketHelper(context);
		}
		
		return instance;
	}
	private WebsocketHelper(Context context) {
		this.context = context;
		this.dbManager = SqlLiteManager.getInstance(this.context);
		jsonHelper = JSONHelper.getInstance(this.context);
		
		connectWebSocket();
		
	}

	public synchronized void sendMessage(Object object, int action_index) {
		//while(true) {
		//if(isConnection() == false)
			//connectWebSocket();
				//break;
		//}
		
		if(action_index == Constants.req_login && isLogin() == true) {
			Log.d("kkk", "중복 로그인 시도");
			closeWebSocket();
			return;
		}
			
			
		String message_json = jsonHelper.ToJSON(object, action_index);
		Log.d("kkk", message_json);
		
		try {
			mWebSocket.send(message_json);
		}catch (Exception e) {
			Toast.makeText(context, "서버와 연결에 문제가 있습니다.", Toast.LENGTH_SHORT);
		}
	}

	public boolean isConnection() {
		return isConnection;
	}

	public boolean isLogin() {
		return isLogin;
	}
	
	public void setLogin(boolean input) {
		isLogin = input;
	}
	
	public void closeWebSocket() {
		if(isConnection() == true) {
			isConnection = false;
			setLogin(false);
			mWebSocket.close();
			mWebSocket = null;
		}
	}
	

	public void connectWebSocket() {
		URI uri;
		try {
			uri = new URI("ws://210.118.74.153:8000");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}

		mWebSocket = new WebSocketClient(uri) {
			@Override
			public void onOpen(ServerHandshake serverHandshake) {
				isConnection = true;
				String isAutoLogin = SharedPreferenceSystem.getPreferences(context,  "autologin");
				if(isAutoLogin.equals("true"))
					AutoLogin();
				
			}

			
			@Override
			public void onMessage(String message) {
				
				try {
					jsonHelper.FromJSON(message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onClose(int i, String s, boolean b) {
				Log.d("kkk", "closesocket call");				
				System.out.println("WebSocketError : " + s);
				if(isConnection) {
					closeWebSocket();
					return;
				}
				connectWebSocket();
				//Log.d("hh", "Closed " + s);
			}

			@Override
			public void onError(Exception e) {
				/*Log.d("kkk", "errorsocket call : " + e.getMessage());
				if(isConnection) {
					closeWebSocket();
					return;
				}
				connectWebSocket();*/
				//Log.d("hh", "Error " + e.getMessage());
			}
		};
		mWebSocket.connect();
	}
	
	private void AutoLogin()
	{
		String saved_id = SharedPreferenceSystem.getPreferences(context,  "id");
		String saved_password = SharedPreferenceSystem.getPreferences(context,  "password");
		User user = User.getInstance();
		user.setId(saved_id);
		user.setPw(saved_password);
		sendMessage(null, Constants.req_login);
	}

}






