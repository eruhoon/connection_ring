package com.billy.connectionring.quick;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.connectionring.Constants;
import com.billy.connectionring.MainActivity;
import com.billy.connectionring.R;
import com.billy.connectionring.SlidingChildModel;
import com.billy.connectionring.memo.MemoUpdateActivity;
import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.Dash;
import com.billy.connectionring.sqllite.SqlLiteManager;
import com.billy.connectionring.sqllite.SqlLiteQuery;

public class QuickAccessService extends Service implements OnClickListener{

	private SqlLiteManager dbManager;

	private WindowManager.LayoutParams mParams;		
	private WindowManager.LayoutParams mQuickListParams;		
	private WindowManager.LayoutParams mQuickMouseParams;
	private WindowManager.LayoutParams mQuickPopupParams;
	private WindowManager mWindowManager;			//������ �Ŵ���

	// layout
	private LayoutInflater inflater;
	private View QuickAccessView;
	private LinearLayout quickPage;
	private LinearLayout quickMouse;
	private LinearLayout quickPopup;
	private LinearLayout quickCancelPopup;

	// quickaccessbutton
	private ImageButton im_quickAccess;

	// quickaccesspage
	private ImageButton im_quickAccess_newmemo;
	private ImageButton im_quickAccess_connectionring;
	private TextView [] txt_quickAccess_item;
	private SlidingChildModel[] childModel;
	private boolean[] isChildEmpty;

	// quickpopup
	private ListView list_quick_pop;
	private Button btn_quick_pop;
	QuickPopupAdapter listAdapter;
	private List<SlidingChildModel> slideList;

	// quickcancelpopup
	private Button btn_cancel_confirm;
	private Button btn_cancel_cancel;
	private int deleteItemNumber;

	// quickmouse
	private ImageView imv_quickMouse;

	// quickpage button collider data
	private Rect collider_newmemo;
	private Rect collider_connectionring;
	private Rect collider_item_1;
	private Rect collider_item_2;
	private Rect collider_item_3;
	private Rect collider_item_4;
	private Rect collider_item_5;

	private boolean isQuickActive;
	private boolean isLongQuickActive;


	Animation rotate_animation;

	private int currentSelectItemIndex = 0;

	
	@Override
	public IBinder onBind(Intent arg0) {return null;}

	@Override
	public void onCreate() {
		super.onCreate();
		dbManager = SqlLiteManager.getInstance(this);

		isQuickActive = false;
		isLongQuickActive = false;
		rotate_animation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate); 

		childModel = new SlidingChildModel[5];
		isChildEmpty = new boolean[5];
		for(int i = 0; i < isChildEmpty.length; i++)
			isChildEmpty[i] = true;

		setQuickAccess();
		setQuickPage();
		setQuickMouse();
		setQuickPopup();
		hideQuickPage();
		hideQuickMouse();
		hideQuickPopup();
		hideQuickCancelPopup();

	}

	private void updateItem() {

		for(int i = 0; i < 5; i++) {
			txt_quickAccess_item[i].setText("Quick Item을 추가하세요.");
			txt_quickAccess_item[i].setGravity(Gravity.CENTER);
			txt_quickAccess_item[i].setTextSize(16);
		}

		// bookmark
		// bookmark-dash
		String  query = SqlLiteQuery.get_SELECT_LIST_DASH_QUICK();
		ArrayList<Dash> dashes = dbManager.selectDashList(query);


		if(dashes == null)
			return;

		for(int i = 0; i < isChildEmpty.length; i++)
			isChildEmpty[i] = true;

		for(int i = 0; i < dashes.size(); i++) {
			if(i>=5)
				return;
			txt_quickAccess_item[i].setGravity(Gravity.CENTER_VERTICAL);
			txt_quickAccess_item[i].setTextSize(12);
			Dash dash = dashes.get(i);
			childModel[i] = new SlidingChildModel(dash);
			isChildEmpty[i] = false;
			txt_quickAccess_item[i].setText("'" + childModel[i].getDashname() + "' DashBoard로 이동");
		}

		// sample
		query = SqlLiteQuery.get_SELECT_LIST_COMPONENT_QUICK();

		ArrayList<Component> components = dbManager.selectComponentList(query);

		if(components == null)
			return;


		for(int i = dashes.size(); i < components.size() + dashes.size(); i++) {
			if(i>=5)
				return;
			txt_quickAccess_item[i].setGravity(Gravity.CENTER_VERTICAL );
			txt_quickAccess_item[i].setTextSize(12);
			Component memo = components.get(i - dashes.size() );
			childModel[i] = new SlidingChildModel(memo);
			isChildEmpty[i] = false;
			String title = childModel[i].getTitle();
			String content = childModel[i].getContent();


			String hours = Integer.toString(childModel[i].getRegdate().getHours());
			String minutes = Integer.toString(childModel[i].getRegdate().getMinutes());

			// time
			String timeText;
			if(childModel[i].isAM(childModel[i].getRegdate())) timeText = "오전 " + hours + "시 : " + minutes + "분";
			else timeText = "오후 " + hours + "시 : " + minutes + "분";


			txt_quickAccess_item[i].setText(timeText + " " + title + "\n" + content);

			Spannable span = new SpannableString(txt_quickAccess_item[i].getText().toString());
			span.setSpan(new ForegroundColorSpan(Color.GREEN), 0, timeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			txt_quickAccess_item[i].setText(span);
		}

	}


	private void setQuickAccess() {
		if (inflater == null) 
			inflater = (LayoutInflater) this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (QuickAccessView == null)
			QuickAccessView = inflater.inflate(R.layout.quickaccess, null);

		im_quickAccess = (ImageButton)QuickAccessView.findViewById(R.id.btn_quick_access);
		im_quickAccess.setOnTouchListener(mViewTouchListener);


		//�ֻ��� �����쿡 �ֱ� ���� ����
		mParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
				PixelFormat.TRANSLUCENT);
		mParams.gravity = Gravity.RIGHT | Gravity.TOP;
		//mParams.softInputMode = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);	//������ �Ŵ��� �ҷ���.
		mWindowManager.addView(QuickAccessView, mParams);

	}

	private void setQuickPopup() {
		LayoutInflater inflater = (LayoutInflater)getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		quickPopup = (LinearLayout)inflater.inflate(R.layout.quickaccess_pop, null);
		quickCancelPopup = (LinearLayout)inflater.inflate(R.layout.quickaccess_cancelpop, null);

		list_quick_pop = (ListView)quickPopup.findViewById(R.id.list_quick_popup);
		btn_quick_pop = (Button)quickPopup.findViewById(R.id.btn_quick_popup);
		btn_cancel_cancel = (Button)quickCancelPopup.findViewById(R.id.btn_quickcancel_cancel);
		btn_cancel_confirm= (Button)quickCancelPopup.findViewById(R.id.btn_quickcancel_confim);

		btn_cancel_cancel.setOnClickListener(this);
		btn_cancel_confirm.setOnClickListener(this);

		mQuickPopupParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_FULLSCREEN ,
				PixelFormat.TRANSLUCENT);
		mQuickPopupParams.gravity = Gravity.LEFT | Gravity.TOP;
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);	//������ �Ŵ��� �ҷ���.
		mWindowManager.addView(quickPopup, mQuickPopupParams);
		mWindowManager.addView(quickCancelPopup, mQuickPopupParams);

		slideList = new ArrayList<SlidingChildModel>();
		listAdapter = new QuickPopupAdapter(getApplicationContext(), slideList);
		list_quick_pop.setAdapter(listAdapter);
		list_quick_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long arg3) {
				SlidingChildModel child = (SlidingChildModel) adapter.getAdapter().getItem(position);

				String query = null;
				if(child.getType() == Constants.ITEM_MEMO) 
					query = SqlLiteQuery.get_INSERT_Quick_QUERY(child.getCid(), Constants.ITEM_MEMO);
				else
					query = SqlLiteQuery.get_INSERT_Quick_QUERY(child.getDid(), Constants.ITEM_MEMOGROUP);
				dbManager.insertData(query);

				hideQuickPopup();
			}});
		btn_quick_pop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideQuickPopup();
			}
		});

		updatePopupList();
	}

	private void updatePopupList() {

		slideList = new ArrayList<SlidingChildModel>();
		// bookmark-dash
		String query = SqlLiteQuery.get_SELECT_LIST_DASH_EXCEPT_QUICK();
		ArrayList<Dash> bookmark_dashes = dbManager.selectDashList(query);
		if(bookmark_dashes != null) {
			for(int i = 0; i < bookmark_dashes.size(); i++) {
				Dash dash = bookmark_dashes.get(i);
				slideList.add(new SlidingChildModel(dash));
			}
		}
		// bookmark-component
		query = SqlLiteQuery.get_SELECT_LIST_COMPONENT_EXCEPT_QUICK();
		ArrayList<Component> components = dbManager.selectComponentList(query);
		if(components != null) {
			for(int i = 0; i < components.size(); i++) {
				Component component = components.get(i);
				slideList.add(new SlidingChildModel(component));
			}
		}
		listAdapter.update(slideList);
		list_quick_pop.setSelection(0);
	}

	private void setQuickPage() {
		LayoutInflater inflater = (LayoutInflater)getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		quickPage = (LinearLayout)inflater.inflate(R.layout.quickaccesspage, null);

		im_quickAccess_newmemo = (ImageButton)quickPage.findViewById(R.id.btn_quickaccess_newmemo);
		im_quickAccess_connectionring = (ImageButton)quickPage.findViewById(R.id.btn_quickaccess_connectionring);
		txt_quickAccess_item = new TextView[5];
		txt_quickAccess_item[0] = (TextView)quickPage.findViewById(R.id.btn_quickaccess_item_1);
		txt_quickAccess_item[1] = (TextView)quickPage.findViewById(R.id.btn_quickaccess_item_2);
		txt_quickAccess_item[2] = (TextView)quickPage.findViewById(R.id.btn_quickaccess_item_3);
		txt_quickAccess_item[3] = (TextView)quickPage.findViewById(R.id.btn_quickaccess_item_4);
		txt_quickAccess_item[4] = (TextView)quickPage.findViewById(R.id.btn_quickaccess_item_5);

		im_quickAccess_newmemo.setOnTouchListener(mViewTouchListener);
		im_quickAccess_connectionring.setOnTouchListener(mViewTouchListener);


		//�ֻ��� �����쿡 �ֱ� ���� ����
		mQuickListParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
				PixelFormat.TRANSLUCENT);
		mQuickListParams.gravity = Gravity.LEFT | Gravity.TOP;
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);	//������ �Ŵ��� �ҷ���.
		mWindowManager.addView(quickPage, mQuickListParams);

		updateItem();
		// �浹���� ����
		setCollider();
	}

	@SuppressLint("NewApi")
	private void setCollider() {
		collider_newmemo = new Rect();
		collider_connectionring = new Rect();
		collider_item_1 = new Rect();
		collider_item_2 = new Rect();
		collider_item_3 = new Rect();
		collider_item_4 = new Rect();
		collider_item_5 = new Rect();;

		im_quickAccess_newmemo.post(new Runnable() {
			@Override
			public void run() {  im_quickAccess_newmemo.getGlobalVisibleRect(collider_newmemo);	}}
				);

		im_quickAccess_connectionring.post(new Runnable() {
			@Override
			public void run() {  im_quickAccess_connectionring.getGlobalVisibleRect(collider_connectionring);	}}
				);

		txt_quickAccess_item[0].post(new Runnable() {
			@Override
			public void run() {  txt_quickAccess_item[0].getGlobalVisibleRect(collider_item_1);	}}
				);

		txt_quickAccess_item[1].post(new Runnable() {
			@Override
			public void run() {  txt_quickAccess_item[1].getGlobalVisibleRect(collider_item_2);	}}
				);

		txt_quickAccess_item[2].post(new Runnable() {
			@Override
			public void run() {  txt_quickAccess_item[2].getGlobalVisibleRect(collider_item_3);	}}
				);

		txt_quickAccess_item[3].post(new Runnable() {
			@Override
			public void run() {  txt_quickAccess_item[3].getGlobalVisibleRect(collider_item_4);	}}
				);

		txt_quickAccess_item[4].post(new Runnable() {
			@Override
			public void run() {  txt_quickAccess_item[4].getGlobalVisibleRect(collider_item_5);	}}
				);
	}

	private void removeQuickPage() {
		mWindowManager.removeView(quickPage);
	}

	private void showQuickPage() {
		updateItem();
		updatePopupList();
		quickPage.setVisibility(View.VISIBLE);
	}
	private void hideQuickPage() {
		quickPage.setVisibility(View.GONE);
	}

	private void showQuickMouse() {
		quickMouse.setVisibility(View.VISIBLE);
	}
	private void hideQuickMouse() {
		quickMouse.setVisibility(View.GONE);
	}
	private void showQuickPopup() {

		quickPopup.setVisibility(View.VISIBLE);
	}
	private void hideQuickPopup() {
		quickPopup.setVisibility(View.GONE);
	}
	private void showQuickCancelPopup() {

		quickCancelPopup.setVisibility(View.VISIBLE);
	}
	private void hideQuickCancelPopup() {
		quickCancelPopup.setVisibility(View.GONE);
	}

	private void setQuickMouse() {
		LayoutInflater inflater = (LayoutInflater)getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		quickMouse = (LinearLayout)inflater.inflate(R.layout.quickmouse, null);

		//�ֻ��� �����쿡 �ֱ� ���� ����
		mQuickMouseParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
				PixelFormat.TRANSLUCENT);
		mQuickMouseParams.gravity = Gravity.LEFT | Gravity.TOP;
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);	//������ �Ŵ��� �ҷ���.
		mWindowManager.addView(quickMouse, mParams);

		imv_quickMouse = (ImageView)quickMouse.findViewById(R.id.imv_quick_mouse);


		imv_quickMouse.startAnimation(rotate_animation);

	}


	private void removeQuickMouse() {
		mWindowManager.removeView(quickMouse);
	}

	private void moveQuickMouse(float _x, float _y) {
		int x = (int) (_x - imv_quickMouse.getWidth()/2);
		int y = (int) (_y - imv_quickMouse.getHeight()/2);
		mQuickMouseParams.x = x;
		mQuickMouseParams.y = y;
		mWindowManager.updateViewLayout(quickMouse, mQuickMouseParams);
	}




	public long getTime() {
		return System.nanoTime() / 1000000;
	}

	long lasttime = 0;
	long currenttime = 0;
	View beforeFocus_v;
	private synchronized void setFocusButton(float x, float y) {
		View v = getColliderView(x, y);

		if(v == null) {
			beforeFocus_v = v;
			setUnFocusButton();
			return;
		}
		
		// focus 유지상태
		if(beforeFocus_v == v && v != im_quickAccess_newmemo && v != im_quickAccess_connectionring && isChildEmpty[currentSelectItemIndex-2] == false)
		{
			if(lasttime == 0) lasttime = getTime();
			currenttime = getTime();
			long delta = (int) ( (currenttime - lasttime)/1000);
			if(delta > 1)
			{				
				// 제거 관련 팝업
				isLongQuickActive = true;
				setUnFocusButton();
				v.setBackgroundColor(Color.parseColor("#00dd99"));
			}

			return;
		}
		// focus를 옮김
		isLongQuickActive = false;
		lasttime = getTime();

		beforeFocus_v = v;
		setUnFocusButton();
		v.setBackgroundColor(Color.parseColor("#ffdedede"));
	}

	private synchronized void setUnFocusButton() {
		im_quickAccess_newmemo.setBackgroundResource(R.drawable.quick_background);
		im_quickAccess_connectionring.setBackgroundResource(R.drawable.quick_background);
		txt_quickAccess_item[0].setBackgroundResource(R.drawable.quick_background);
		txt_quickAccess_item[1].setBackgroundResource(R.drawable.quick_background);
		txt_quickAccess_item[2].setBackgroundResource(R.drawable.quick_background);
		txt_quickAccess_item[3].setBackgroundResource(R.drawable.quick_background);
		txt_quickAccess_item[4].setBackgroundResource(R.drawable.quick_background);
	}
	

	private View getColliderView(float x, float y) {
		Rect [] rect = 
			{collider_newmemo, collider_connectionring, collider_item_1, collider_item_2, collider_item_3, collider_item_4, collider_item_5};

		View [] v =
			{im_quickAccess_newmemo, im_quickAccess_connectionring, txt_quickAccess_item[0], txt_quickAccess_item[1], txt_quickAccess_item[2], txt_quickAccess_item[3], txt_quickAccess_item[4]};

		for(int i = 0; i < 7; i++) {
			if((x >= rect[i].left && x <= rect[i].right) && 
					(y >= rect[i].top && y <= rect[i].bottom))
			{
				
				if(currentSelectItemIndex == i) {
					return v[i];
				}
				currentSelectItemIndex = i;
				
				return v[i];
			}
		}

		return null;
	}




	private void setSelectButton(float x, float y, boolean isLongClick) {

		int selectNum = 10;
		Rect [] rect = 
			{collider_newmemo, collider_connectionring, collider_item_1, collider_item_2, collider_item_3, collider_item_4, collider_item_5};

		View [] v =
			{im_quickAccess_newmemo, im_quickAccess_connectionring, txt_quickAccess_item[0], txt_quickAccess_item[1], txt_quickAccess_item[2], txt_quickAccess_item[3], txt_quickAccess_item[4]};

		for(int i = 0; i < 7; i++) {
			if((x >= rect[i].left && x <= rect[i].right) && 
					(y >= rect[i].top && y <= rect[i].bottom))
			{
				selectNum = i;
			}
		}

		if(selectNum == 10)
			return;

		if(selectNum == 0) {
			String query = SqlLiteQuery.get_SELECT_LIST_DASH();
			ArrayList<Dash> dashes = dbManager.selectDashList(query);

			if(dashes.size() != 0) {
				startService(new Intent(this, QuickMemoService.class));
			}else {
				Toast.makeText(getApplicationContext(), "Dashboard를 먼저 생성하세요.", Toast.LENGTH_LONG).show();
			}
		}
		if(selectNum == 1)  {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		if(selectNum == 2 || selectNum == 3 || selectNum == 4 || selectNum == 5 || selectNum == 6) {
			selectNum = selectNum-2;
			if(isChildEmpty[selectNum]) {

				setNewItem();
				return;
			}
			if(isLongClick == true)
			{
				deleteItem(selectNum);
				return;
			}

			if(childModel[selectNum].getType() == Constants.ITEM_MEMO)
			{
				Intent intent = new Intent(getApplicationContext(), MemoUpdateActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("cid", 
						childModel[selectNum].getCid());
				startActivity(intent);
			}else {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				intent.putExtra("did", 
						childModel[selectNum].getDid());
				intent.putExtra("dashname", 
						childModel[selectNum].getDashname());
				startActivity(intent);
			}
		}
	}


	private void setNewItem() {
		showQuickPopup();
	}

	private void deleteItem(int index) {
		deleteItemNumber = index;
		showQuickCancelPopup();
	}


	private OnTouchListener mViewTouchListener = new OnTouchListener() {
		@Override public boolean onTouch(View v, MotionEvent event) {

			float x = event.getRawX();
			float y = event.getRawY();
			System.out.println("cancel");
			switch(event.getAction()) {

			case MotionEvent.ACTION_DOWN:	
				if(v == im_quickAccess)
				{
					isQuickActive = true;
					showQuickPage();
					showQuickMouse();
					moveQuickMouse(x, y);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(isQuickActive == true) {
					moveQuickMouse(x, y);
					setFocusButton(x, y);
				}
				break;
			case MotionEvent.ACTION_UP :
				if(isLongQuickActive == true)
				{
					isLongQuickActive = false;
					isQuickActive = false;
					setSelectButton(x, y, true);
					//setUnFocusButton();
					hideQuickPage();
					hideQuickMouse();
				}
				else if(isQuickActive == true)
				{
					isLongQuickActive = false;
					isQuickActive = false;
					//setUnFocusButton();
					setSelectButton(x, y, false);
					hideQuickPage();
					hideQuickMouse();
				}

				break;
			}
			return true;
		}
	};

	@Override
	public void onDestroy() {
		if(mWindowManager != null) {		//���� ����� �� ����. *�߿� : �並 �� ���� �ؾ���.
			if(QuickAccessView != null) mWindowManager.removeView(QuickAccessView);
			//if(mSeekBar != null) mWindowManager.removeView(mSeekBar);
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Notification notification = new Notification(R.drawable.ic_connection, "너와나의 연결고리", System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), "Screen Service", "Quick Access 서비스를 실행 중", null);
		startForeground(1, notification);

		return Service.START_NOT_STICKY;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_quickcancel_cancel :
			hideQuickCancelPopup();
			break;
		case R.id.btn_quickcancel_confim :

			SlidingChildModel child = childModel[deleteItemNumber];

			String query = null;
			System.out.println("confirm" + child.getType());
			if( child.getType() == Constants.ITEM_MEMO)
			{
				query = SqlLiteQuery.get_DELETE_COMPONENT_QUICK_QUERY(child.getCid());
			}else if( child.getType() == Constants.ITEM_MEMOGROUP)
			{
				query = SqlLiteQuery.get_DELETE_DASH_QUICK_QUERY(child.getDid());
			}
			dbManager.removeData(query);

			hideQuickCancelPopup();
			break;
		default : 
			break;
		}
	}





}
