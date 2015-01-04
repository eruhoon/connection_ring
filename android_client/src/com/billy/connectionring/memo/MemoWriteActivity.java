package com.billy.connectionring.memo;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.billy.connectionring.Constants;
import com.billy.connectionring.R;
import com.billy.connectionring.connection.WebsocketHelper;
import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.Dash;
import com.billy.connectionring.sqllite.SqlLiteManager;
import com.billy.connectionring.sqllite.SqlLiteQuery;

public class MemoWriteActivity extends Activity{

	WebsocketHelper mWebSocketHelper;
	
	TextView txt_title;
	TextView txt_content;
	ImageButton imv_complete;
	Spinner spinner_dash;
	
	SqlLiteManager dbManager;
	
	ArrayList<Dash> dashes;
	
	int selectedItemIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_memo_write);
		
		mWebSocketHelper = WebsocketHelper.getInstance(this);
		dbManager = SqlLiteManager.getInstance(this);
		
		setLayout();
		
		imv_complete.setOnClickListener(mOnclickListener);
		
		spinner_dash.setOnItemSelectedListener(mOnItemSelectedListener);
		
		
	}
	
	private void setLayout() {
		txt_title = (TextView)findViewById(R.id.editTitle);
		txt_content = (TextView)findViewById(R.id.editContent);
		imv_complete = (ImageButton)findViewById(R.id.btnMemoWriteComplete);
		spinner_dash = (Spinner)findViewById(R.id.spinner_MemoWrite);
		
		
		Intent intent = getIntent();
		
		txt_title.setText(intent.getStringExtra("title"));
		txt_content.setText(intent.getStringExtra("content"));
		
		spinUpdate();
	}
	
	private void spinUpdate()
	{
		String query = SqlLiteQuery.get_SELECT_LIST_DASH();
		dashes = dbManager.selectDashList(query);
		String[] str_dashes = new String[dashes.size()];
		for(int i = 0; i < dashes.size(); i++)
			str_dashes[i] = dashes.get(i).getDashname();
		
		ArrayAdapter<String> adspin = new ArrayAdapter<String>(this, R.layout.spinner_item, str_dashes);
		
		adspin.setDropDownViewResource(android.R.layout.simple_list_item_checked);
		spinner_dash.setAdapter(adspin);
		
		selectedItemIndex = 0;
	}
	
	private OnClickListener mOnclickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mWebSocketHelper.sendMessage(new Component(0, dashes.get(selectedItemIndex).getDid(), Constants.COMPONENT_ITEMTYPE_MEMO,
					txt_title.getText().toString(), txt_content.getText().toString(), new Date(), new Date(), true), Constants.req_componentNew);
			
			finish();
		}
	};
	
	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			// TODO Auto-generated method stub
			selectedItemIndex = position;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
}
