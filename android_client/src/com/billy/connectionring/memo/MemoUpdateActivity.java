package com.billy.connectionring.memo;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

public class MemoUpdateActivity extends Activity{

	WebsocketHelper mWebSocketHelper;

	TextView txt_title;
	TextView txt_content;
	ImageButton imv_complete;
	ImageButton imv_correct;
	Spinner spinner_dash;

	SqlLiteManager dbManager;

	ArrayList<Dash> dashes;

	int selectedItemIndex;

	Component component;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_memo_write);

		setLayout();


		spinner_dash.setOnItemSelectedListener(mOnItemSelectedListener);
	}



	private void setLayout() {

		mWebSocketHelper = WebsocketHelper.getInstance(this);
		dbManager = SqlLiteManager.getInstance(this);

		txt_title = (TextView)findViewById(R.id.editTitle);
		txt_content = (TextView)findViewById(R.id.editContent);
		imv_complete = (ImageButton)findViewById(R.id.btnMemoWriteComplete);
		imv_correct =  (ImageButton)findViewById(R.id.btnMemoWriteCorrection);
		spinner_dash = (Spinner)findViewById(R.id.spinner_MemoWrite);
		
		imv_correct.setOnClickListener(mOnclickListener);
		imv_complete.setOnClickListener(mOnclickListener);

		// load db
		Intent intent = getIntent();
		int cid = intent.getIntExtra("cid", 0);
		String query = SqlLiteQuery.get_SELECT_ITEM_COMPONENT(cid);
		component = dbManager.selectComponent(query);

		spinUpdate();
		
		txt_title.setText(component.getTitle());
		txt_content.setText(component.getContent());

		spinner_dash.setFocusable(false);
		txt_title.setFocusable(false);
		txt_content.setFocusable(false);

		imv_complete.setVisibility(View.GONE);
		imv_correct.setVisibility(View.VISIBLE);
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

		for(int i = 0; i < dashes.size(); i++)
		{
			if(component.getDid() == dashes.get(i).getDid())
			{
				spinner_dash.setSelection(i);
				selectedItemIndex = i;
			}
		}
	}

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

	private OnClickListener mOnclickListener =  new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch(v.getId())
			{
			case R.id.btnMemoWriteComplete :
				
				mWebSocketHelper.sendMessage(new Component(component.getCid(), component.getDid(), component.getTypec(),
						txt_title.getText().toString(), txt_content.getText().toString(), component.getX(), component.getY(),
						new Date(), component.getDate(), component.isVisible()), Constants.req_componentDelete);
				
				mWebSocketHelper.sendMessage(new Component(component.getCid(), dashes.get(selectedItemIndex).getDid(), component.getTypec(),
						txt_title.getText().toString(), txt_content.getText().toString(), component.getX(), component.getY(),
						new Date(), component.getDate(), component.isVisible()), Constants.req_componentNew);

				finish();
				break;
			case R.id.btnMemoWriteCorrection :
				txt_content.setFocusableInTouchMode(true);
				txt_title.setFocusableInTouchMode(true);
				spinner_dash.setFocusableInTouchMode(true);
				imv_complete.setVisibility(View.VISIBLE);
				imv_correct.setVisibility(View.GONE);
				break;
			default :
				break;

			}



		}
	};
}
