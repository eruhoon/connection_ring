package com.billy.connectionring.quick;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.billy.connectionring.Constants;
import com.billy.connectionring.R;
import com.billy.connectionring.connection.WebsocketHelper;
import com.billy.connectionring.memo.MemoWriteActivity;
import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.Dash;
import com.billy.connectionring.sqllite.SqlLiteManager;
import com.billy.connectionring.sqllite.SqlLiteQuery;

@SuppressLint("NewApi")
public class QuickMemoService extends Service {

	private WebsocketHelper mWebSocketHelper;
	private SqlLiteManager dbManager;
	
	private WindowManager.LayoutParams mParams;		//layout params ��ü. ���� ��ġ �� ũ�⸦ �����ϴ� ��ü
	private WindowManager mWindowManager;			//������ �Ŵ���

	// layout
	private LayoutInflater inflater;
	private View convertView;

	private LinearLayout linear_quickempty;
	private LinearLayout linear_quickmemo;
	private EditText et_title;
	private EditText et_content;
	private ImageButton im_move;
	private ImageButton im_complete;
	private ImageButton im_maximum;
	private ImageButton im_exit;
	private ImageButton im_vertical;
	private ImageButton im_horizontal;
	private Spinner spinner_dash;
	
	//spinner
	ArrayList<Dash> dashes;
	int selectedItemIndex;
	
	private float width;
	private float height;

	// zoom
	boolean verticalSelected;
	boolean horizontalSelected;

	// move
	private float MOVE_START_X, MOVE_START_Y;							//�����̱� ���� ��ġ�� ���� ��
	private int PREV_X, PREV_Y;								//�����̱� ������ �䰡 ��ġ�� ��
	private int MAX_X = -1, MAX_Y = -1;					//���� ��ġ �ִ� ��

	private float MAX_WIDTH, MAX_HEIGHT;
	private float MIN_WIDTH, MIN_HEIGHT;

	// touch���� Ŭ�� ������ ���� timer
	long lastUpdateTime = 0;
	long currentTime = 0;

	boolean isEditMode;

	@Override
	public IBinder onBind(Intent arg0) { return null; }



	@Override
	public void onCreate() {
		super.onCreate();
		mWebSocketHelper = WebsocketHelper.getInstance(getApplicationContext());
		dbManager = SqlLiteManager.getInstance(getApplicationContext());
		
		setLayout();
	}

	private void setLayout() {
		if (inflater == null) 
			inflater = (LayoutInflater) this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.quickmemo, null);

		linear_quickempty  = (LinearLayout)convertView.findViewById(R.id.linear_quick_emptyspace);
		linear_quickmemo = (LinearLayout)convertView.findViewById(R.id.linear_quickmemo);
		et_title = (EditText)convertView.findViewById(R.id.edit_quick_title);
		et_content = (EditText)convertView.findViewById(R.id.edit_quick_content);
		im_move = (ImageButton)convertView.findViewById(R.id.btn_quick_move);
		im_complete  = (ImageButton)convertView.findViewById(R.id.btn_quick_complete);
		im_maximum = (ImageButton)convertView.findViewById(R.id.btn_quick_maximum);
		im_exit = (ImageButton)convertView.findViewById(R.id.btn_quick_exit);
		im_vertical = (ImageButton)convertView.findViewById(R.id.btn_quick_vertical);
		im_horizontal = (ImageButton)convertView.findViewById(R.id.btn_quick_horizontal);
		spinner_dash = (Spinner)convertView.findViewById(R.id.spinner_QuickMemo);
		
		linear_quickempty.setOnTouchListener(mViewTouchListener);
		im_move.setOnTouchListener(mViewTouchListener);
		im_vertical.setOnTouchListener(mViewTouchListener);
		im_horizontal.setOnTouchListener(mViewTouchListener);
		
		im_complete.setOnClickListener(mViewClickListener);
		im_maximum.setOnClickListener(mViewClickListener);
		im_exit.setOnClickListener(mViewClickListener);
		et_title.setOnClickListener(mViewClickListener);
		et_content.setOnClickListener(mViewClickListener);

		spinner_dash.setOnItemSelectedListener(new OnItemSelectedListener() {

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
		});

		//�ֻ��� �����쿡 �ֱ� ���� ����
		mParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_FULLSCREEN ,
				PixelFormat.TRANSLUCENT);
		mParams.gravity = Gravity.LEFT | Gravity.TOP;
		//mParams.softInputMode = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);	//������ �Ŵ��� �ҷ���.
		mWindowManager.addView(convertView, mParams);


		linear_quickmemo.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setSize(linear_quickmemo.getWidth(), linear_quickmemo.getHeight());
			}
		});

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
	}

	private OnClickListener mViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.btn_quick_complete :
				Toast.makeText(getApplicationContext(), "메모를 저장합니다.", Toast.LENGTH_LONG).show();
				mWebSocketHelper.sendMessage(new Component(0, dashes.get(selectedItemIndex).getDid(), Constants.COMPONENT_ITEMTYPE_MEMO,
						et_title.getText().toString(), et_content.getText().toString(), new Date(), new Date(), true), Constants.req_componentNew);
				// Todo
				// ������ �����
				stopSelf();
				break;
			case R.id.btn_quick_maximum :
				Intent intent = new Intent(getApplicationContext(), MemoWriteActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("title", et_title.getText().toString());
				intent.putExtra("content", et_content.getText().toString());
				startActivity(intent);
				stopSelf();
				break;
			case R.id.btn_quick_exit :
				stopSelf();
				break;
			case R.id.edit_quick_title :
				setEditMode(true);
				break;
			case R.id.edit_quick_content :
				setEditMode(true);
				break;
			default :
				break;
			}
		}
	};


	private OnTouchListener mViewTouchListener = new OnTouchListener() {
		@Override public boolean onTouch(View v, MotionEvent event) {


			switch(event.getAction()) {

			case MotionEvent.ACTION_DOWN:	
				if(v == linear_quickempty)
				{
					setEditMode(false);
					return false;
				}else if (v== im_move || v ==  im_horizontal || v == im_vertical) {
					if(MAX_X == -1)
						setMaxPosition();
					MOVE_START_X = event.getRawX();					
					MOVE_START_Y = event.getRawY();				
					PREV_X = mParams.x;						
					PREV_Y = mParams.y;

					if(v == im_vertical) {
						verticalSelected= true;
					}else if(v == im_horizontal) {
						horizontalSelected = true;
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:

				if(v == im_horizontal) {
					int currentX = (int)(event.getRawX());
					int deltaX = currentX - (int)MOVE_START_X;
					MOVE_START_X = currentX;

					if(width+deltaX >= MIN_WIDTH && width+deltaX <= MAX_WIDTH) width = width+deltaX; 
					else return true; 

					linear_quickmemo.setLayoutParams(new LinearLayout.LayoutParams((int)width, (int)height));
				}else if(v == im_vertical) {
					int currentY = (int)(event.getRawY());
					int deltaY = currentY - (int)MOVE_START_Y;
					MOVE_START_Y = currentY;

					if(height + deltaY >= MIN_HEIGHT && height + deltaY <= MAX_HEIGHT) height = height+deltaY;
					else return true;

					linear_quickmemo.setLayoutParams(new LinearLayout.LayoutParams((int)width, (int)height));
				}else if(v == im_move){
					int x = (int)(event.getRawX() - MOVE_START_X);	
					int y = (int)(event.getRawY() - MOVE_START_Y);
					//��ġ�ؼ� �̵��� ��ŭ �̵� ��Ų��
					mParams.x = PREV_X + x;
					mParams.y = PREV_Y + y;
					optimizePosition();		//���� ��ġ ����ȭ
					mWindowManager.updateViewLayout(convertView, mParams);	//�� ������Ʈ
				}
				break;
			case MotionEvent.ACTION_UP :
				if(v == im_vertical) {
					verticalSelected= false;
				}else if(v == im_horizontal) {
					horizontalSelected= false;
				}else {

				}
				break;
			}
			return true;
		}
	};


	private void setEditMode(boolean flag) {
		int x = mParams.x;
		int y = mParams.y;
		if(flag == true)
		{

			isEditMode = true;
			mParams = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_FULLSCREEN ,
					PixelFormat.TRANSLUCENT);
			mParams.gravity = Gravity.LEFT | Gravity.TOP;
			mParams.x = x;
			mParams.y = y;
		}else {

			isEditMode = false;
			mParams = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSLUCENT);
			mParams.gravity = Gravity.LEFT | Gravity.TOP;
			mParams.x = x;
			mParams.y = y;
		}

		mWindowManager.updateViewLayout(convertView, mParams);

	}

	public void setSize(float Width,float Height){
		this.MIN_WIDTH =Width;
		this.MIN_HEIGHT=Height;

		this.width = Width;
		this.height = Height;
	}

	/**
	 * ���� ��ġ�� ȭ�� �ȿ� �ְ� �ִ밪�� �����Ѵ�
	 */
	private void setMaxPosition() {
		DisplayMetrics matrix = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(matrix);		//ȭ�� ������ �����ͼ�

		MAX_X = matrix.widthPixels - convertView.getWidth();			//x �ִ밪 ����
		MAX_Y = matrix.heightPixels - convertView.getHeight();			//y �ִ밪 ����

		MAX_WIDTH = matrix.widthPixels;
		MAX_HEIGHT = matrix.heightPixels;
	}

	/**
	 * ���� ��ġ�� ȭ�� �ȿ� �ְ� �ϱ� ���ؼ� �˻��ϰ� �����Ѵ�.
	 */
	private void optimizePosition() {
		//�ִ밪 �Ѿ�� �ʰ� ����
		if(mParams.x > MAX_X) mParams.x = MAX_X;
		if(mParams.y > MAX_Y) mParams.y = MAX_Y;
		if(mParams.x < 0) mParams.x = 0;
		if(mParams.y < 0) mParams.y = 0;
	}


	/**
	 * ���� / ���� ��� ���� �� �ִ밪 �ٽ� ������ �־�� ��.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		setMaxPosition();		//�ִ밪 �ٽ� ����
		optimizePosition();		//�� ��ġ ����ȭ
	}


	@Override
	public void onDestroy() {
		if(mWindowManager != null) {		//���� ����� �� ����. *�߿� : �並 �� ���� �ؾ���.
			if(convertView != null) mWindowManager.removeView(convertView);
			//if(mSeekBar != null) mWindowManager.removeView(mSeekBar);
		}
		super.onDestroy();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}
}