package com.billy.connectionring.gcm;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.billy.connectionring.Constants;
import com.billy.connectionring.MainActivity;
import com.billy.connectionring.R;
import com.billy.connectionring.connection.User;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService
{

	public static MainActivity mainActivity;
	
	public GcmIntentService()
	{
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty())
		{ // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.*/

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
			{
				sendNotification("Send error: " + extras.toString());
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
			{
				sendNotification("Deleted messages on server: " + extras.toString());
				// If it's a regular GCM message, do some work.
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
			{
				String msg_t = extras.toString();
				
				if(mainActivity != null)
					mainActivity.synchronizeUpdate();
				
				// Post notification of received message.
				//            sendNotification("Received: " + extras.toString());
				sendNotification(msg_t);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg)
	{
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		msg = msg.substring(7, msg.length()-1);
		Log.d("GcmIntentService.java | onHandleIntent", msg);
		String id = "";
		String type = "";
		String typec ="";
		String msgs ="";
		JSONObject json_msg = null;
		try {
			json_msg = new JSONObject(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			id = json_msg.getString("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			type = json_msg.getString("type");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			typec = json_msg.getString("typec");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			msgs = json_msg.getString("msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		User user = User.getInstance();

		
		if(user.getId().equals(id))
			return;

		//dashupdate componentnew componentupdate componentdelete 
		if(type.equals("componentUpdate")) {
			if(msgs.equals("move")) {
				return; 
			}
			if(typec.equals(Constants.COMPONENT_ITEMTYPE_MEMO))
				msg = id + "님이 메모를 수정하였습니다.";
			else if(typec.equals(Constants.COMPONENT_ITEMTYPE_CALENDAR))
				msg = id + "님이 일정을 수정하였습니다.";
		}
		else if(type.equals("componentDelete")) {
			msg = id + "님이 컴포넌트를 삭제하였습니다.";
		}
		else if(type.equals("componentNew")) {
			if(typec.equals(Constants.COMPONENT_ITEMTYPE_MEMO))
				msg = id + "님이 새로운 메모를 생성하였습니다.";
			else if(typec.equals(Constants.COMPONENT_ITEMTYPE_CALENDAR))
				msg = id + "님이 새로운 일정을 생성하였습니다.";
		}else if(type.equals("dashUpdate")) {
			msg = id + "님이 새로운 대시보드를 생성하였습니다.";
		}else {
		}
		
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("msg", msg);



		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_connection)
				.setContentTitle("너와나의 연결고리 메시지 도착")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg)
				.setAutoCancel(true)
				.setVibrate(new long[] { 0, 500 });

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify((int)System.currentTimeMillis(), mBuilder.build());
	}
}