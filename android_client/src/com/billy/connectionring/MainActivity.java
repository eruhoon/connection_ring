package com.billy.connectionring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.billy.connectionring.calendar.CalendarFragment;
import com.billy.connectionring.connection.JSONHelper;
import com.billy.connectionring.connection.WebsocketHelper;
import com.billy.connectionring.gcm.GcmIntentService;
import com.billy.connectionring.gcm.PreferenceUtil;
import com.billy.connectionring.memo.MemoFragment;
import com.billy.connectionring.memo.MemoWriteActivity;
import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.Dash;
import com.billy.connectionring.quick.QuickAccessService;
import com.billy.connectionring.sharedpreference.SharedPreferenceSystem;
import com.billy.connectionring.sqllite.SqlLiteConverter;
import com.billy.connectionring.sqllite.SqlLiteManager;
import com.billy.connectionring.sqllite.SqlLiteQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;


@SuppressWarnings({ "deprecation", "deprecation" })
public class MainActivity extends FragmentActivity implements OnClickListener{

	public static final String TAG = "MainActivity";

	private WebsocketHelper mWebSocketHelper;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	JSONHelper jsonHelper;

	private ViewPager mViewPager;
	private MemoFragment memoFragment;
	private CalendarFragment calendarFragment;

	private ImageButton btn_Memo;
	private ImageButton btn_Calender;

	//DB Manager
	private SqlLiteManager dbManager;

	private EditText et_newdash;
	private EditText et_newcalendar;
	private Spinner spinner_newcalendar;

	private CaldroidFragment dialogCaldroidFragment;
	ArrayList<Dash> dashes;

	int selectedItemIndex;

	//action btn group
	private LinearLayout actionBtnGroup;
	private ImageButton btn_action_expand;
	private ImageButton btn_action_memo;
	private ImageButton btn_action_calendar;
	private ImageButton btn_action_dash;

	private boolean isActionGruopExpand;


	//sliding menu group
	private DrawerLayout mDrawerLayout;
	@SuppressWarnings("deprecation")
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;


	SlidingListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> list_sliding_parent;
	HashMap<String, List<SlidingChildModel>> list_sliding_child;

	boolean isExpand_DashBoard = true;
	boolean isExpand_Bookmark = true;

	private Bundle state;

	//GCM
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String SENDER_ID = "55614632324";

	private GoogleCloudMessaging _gcm;
	private String _regId;

	private ProgressDialog pDialog;

	private boolean isFirstCall = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("");
		setContentView(R.layout.activity_main);
		/*setTimer();
		showPDialog();*/
		
		
		state = savedInstanceState;

		setLayout();

		setViewPager();

		mWebSocketHelper = WebsocketHelper.getInstance(this);
		dbManager = SqlLiteManager.getInstance(this);
		jsonHelper = JSONHelper.getInstance(this);
		jsonHelper.setActivity(this);

		startService(new Intent(this, QuickAccessService.class));

		setSlidingMenu();

		initGCM();

		GcmIntentService.mainActivity = this;
		
		synchronizeUpdate();
	}


	public void synchronizeUpdate()
	{
		mWebSocketHelper.sendMessage(null, Constants.req_getDashList);
		mWebSocketHelper.sendMessage(null, Constants.req_getDashComponentList);
	}

	private void initGCM() {
		// google play service가 사용가능한가
		if (checkPlayServices())
		{
			_gcm = GoogleCloudMessaging.getInstance(this);
			_regId = getRegistrationId();

			//if (TextUtils.isEmpty(_regId))
			registerInBackground();
		}
		else
		{
			Log.i("MainActivity.java | onCreate", "|No valid Google Play Services APK found.|");
		}

		// display received msg
		Log.d("gcmt", _regId);
		mWebSocketHelper.sendMessage(_regId, Constants.registGCM);

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		//updateSlidingList();
	}

	public void updateSlidingList() {
		prepareListData();
		listAdapter.update(list_sliding_parent, list_sliding_child);
		if(memoFragment != null) {
			Intent intent = getIntent();
			int did = intent.getIntExtra("did", 0);
			if(did != 0 && isFirstCall == true) {
				String dashname = intent.getStringExtra("dashname");
				memoFragment.updateSlidingList(did, dashname);
				isFirstCall = false;
			}else
				memoFragment.updateSlidingList();
		}if(calendarFragment != null)
			calendarFragment.UpdateDates();
	}

	private void sampleDBInsert() {
		String query = null;

		// sample dash create
		/*query = SqlLiteQuery.get_INSERT_DASH_QUERY(new Dash(1, "창의"));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_DASH_QUERY(new Dash(2, "영걸"));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_DASH_QUERY(new Dash(3, "헤이 빌리"));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_DASH_QUERY(new Dash(4, "빌리버블"));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_DASH_QUERY(new Dash(5, "언빌리버블"));
		dbManager.insertData(query);*/

		// sample component create
		/*query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(1, 1, "MEMO", "1번", "1번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(2, 1, "MEMO", "2번", "2번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(3, 2, "MEMO", "3번", "3번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(4, 2, "MEMO", "4번", "4번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(5, 3, "MEMO", "5번", "5번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(6, 3, "MEMO", "6번", "6번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(7, 4, "MEMO", "7번", "7번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(8, 4, "MEMO", "8번", "8번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(9, 5, "MEMO", "9번", "9번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(10, 5, "MEMO", "10번", "10번", new Date(), new Date(), true));
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(11, 5, "MEMO", "11번", "11번", new Date(), new Date(), true));
		dbManager.insertData(query);*/


		// sample bookmark create
		/*query = SqlLiteQuery.get_INSERT_Bookmark_QUERY(1, Constants.ITEM_MEMO);
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_Bookmark_QUERY(2, Constants.ITEM_MEMOGROUP);
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_Bookmark_QUERY(3, Constants.ITEM_MEMO);
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_Bookmark_QUERY(4, Constants.ITEM_MEMOGROUP);
		dbManager.insertData(query);

		query = SqlLiteQuery.get_INSERT_Bookmark_QUERY(5, Constants.ITEM_MEMO);
		dbManager.insertData(query);*/
	}

	private void setLayout(){
		btn_Memo = (ImageButton) findViewById(R.id.btnMemo);
		btn_Calender = (ImageButton) findViewById(R.id.btnCalender);

		btn_Memo.setOnClickListener(this);
		btn_Calender.setOnClickListener(this);

		setActionLayout();
	}

	private void setActionLayout() {
		LayoutInflater inflater = (LayoutInflater)getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		actionBtnGroup = (LinearLayout)inflater.inflate(R.layout.action_button_gruop, null);

		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		addContentView(actionBtnGroup, paramlinear);

		btn_action_expand = (ImageButton)findViewById(R.id.btnActionExpand);
		btn_action_memo = (ImageButton)findViewById(R.id.btnActionMemo);
		btn_action_calendar = (ImageButton)findViewById(R.id.btnActionCalendar);
		btn_action_dash = (ImageButton)findViewById(R.id.btnActionDash);
		btn_action_expand.setOnClickListener(this);
		btn_action_memo.setOnClickListener(this);
		btn_action_calendar.setOnClickListener(this);
		btn_action_dash.setOnClickListener(this);

		collapseActionGroup(0);
	}

	private void setViewPager()
	{
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				setSelectedTabButton(arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		setSelectedTabButton(0);
	}



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	/**
	 * 
	 * SlidingMenu
	 */


	private void setSlidingMenu()
	{
		setSlidingMenuList();

		mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		/*mDrawerList.setOnItemClickListener(new DrawerItemClickListener());*/

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                    
				mDrawerLayout,           
				R.drawable.ic_drawer,    
				R.string.drawer_open,    
				R.string.drawer_close    
				) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				//getActionBar().setTitle(mDrawerTitle);
				updateSlidingList();
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}


	private void setSlidingMenuList()
	{

		expListView = (ExpandableListView) findViewById(R.id.list_slide);

		prepareListData();

		listAdapter = new SlidingListAdapter(this, list_sliding_parent, list_sliding_child);

		expListView.setAdapter(listAdapter);

		expandAll();


		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(mOnGroupExpandListener);

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(mOnGroupCollapseListener);

		// Listview Group click listener
		expListView.setOnGroupClickListener(mOnGroupClickListener);

		// Listview on child click listener
		expListView.setOnChildClickListener(mOnChildClickListener);


		findViewById(R.id.btn_slide_synchronize).setOnClickListener(mOnClickListener);
	}

	private OnGroupExpandListener mOnGroupExpandListener = new OnGroupExpandListener() {
		@Override
		public void onGroupExpand(int groupPosition) {
		}
	};

	private OnGroupCollapseListener mOnGroupCollapseListener = new OnGroupCollapseListener() {
		@Override
		public void onGroupCollapse(int groupPosition) {
		}
	} ;


	private OnGroupClickListener mOnGroupClickListener = new OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			// user
			if(groupPosition == 0)
			{
				mDrawerLayout.closeDrawers();
				Intent intent = new Intent(getApplicationContext(), UserActivity.class);
				startActivity(intent);
			}
			// all note
			else if (groupPosition == 1)
			{
				memoFragment.updateSlidingList();
				mDrawerLayout.closeDrawers();
			}
			// 설정
			else if(groupPosition == 4)
			{
				mDrawerLayout.closeDrawers();
			}

			return false;
		}
	};

	private OnChildClickListener mOnChildClickListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub

			mDrawerLayout.closeDrawers();
			SlidingChildModel item = list_sliding_child.get(list_sliding_parent.get(groupPosition)).get(childPosition);

			if(item.getType() == Constants.ITEM_MEMO) {
				int cid = item.getCid();
				memoFragment.memoCorrect(cid);
			}else if (item.getType() == Constants.ITEM_MEMOGROUP) {
				int did = item.getDid();
				String dashname = item.getDashname();
				memoFragment.updateSlidingList(did, dashname);
			}

			return false;
		}
	};

	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			synchronizeUpdate();
			updateSlidingList();
			Toast.makeText(getApplicationContext(), "동기화를 시작합니다.", Toast.LENGTH_SHORT).show();
			mDrawerLayout.closeDrawers();
		}
	};

	public void expandAll(){
		int groupCnt = getGroupCount();
		if(groupCnt == 0){
			return;
		}

		expListView.expandGroup(Constants.SLIDE_DASHBOARD);
		expListView.expandGroup(Constants.SLIDE_BOOKMARK);

	}

	public int getGroupCount() {
		int cnt = 0;

		cnt = expListView.getCount();
		return cnt;
	}


	/*
	 * Preparing the list data
	 */
	private void prepareListData() {

		// todo
		// slide list data prepare


		list_sliding_parent = new ArrayList<String>();
		list_sliding_child = new HashMap<String, List<SlidingChildModel>>();
		List<SlidingChildModel> dash_slideList = new ArrayList<SlidingChildModel>();
		List<SlidingChildModel> bookmark_slideList = new ArrayList<SlidingChildModel>();

		// Adding child data
		list_sliding_parent.add(SharedPreferenceSystem.getPreferences(this,  "id"));
		list_sliding_parent.add("모든 노트");
		list_sliding_parent.add("DashBoard");
		list_sliding_parent.add("Bookmark");
		list_sliding_parent.add("Settings");

		// Dash
		String query = SqlLiteQuery.get_SELECT_LIST_DASH();
		ArrayList<Dash> dash_dashes = dbManager.selectDashList(query);
		if(dash_dashes == null)
			return;

		for(int i = 0; i < dash_dashes.size(); i++) {
			Dash dash = dash_dashes.get(i);

			dash_slideList.add(new SlidingChildModel(dash));
		}
		list_sliding_child.put(list_sliding_parent.get(Constants.SLIDE_DASHBOARD), dash_slideList); // Header, Child data

		// bookmark
		// bookmark-dash
		query = SqlLiteQuery.get_SELECT_LIST_DASH_BOOKMARK();
		ArrayList<Dash> bookmark_dashes = dbManager.selectDashList(query);
		for(int i = 0; i < bookmark_dashes.size(); i++) {
			Dash dash = bookmark_dashes.get(i);

			bookmark_slideList.add(new SlidingChildModel(dash));
		}

		// bookmark-component
		query = SqlLiteQuery.get_SELECT_LIST_COMPONENT_BOOKMARK();
		ArrayList<Component> components = dbManager.selectComponentList(query);

		for(int i = 0; i < components.size(); i++) {
			Component component = components.get(i);

			bookmark_slideList.add(new SlidingChildModel(component));
		}
		list_sliding_child.put(list_sliding_parent.get(Constants.SLIDE_BOOKMARK), bookmark_slideList); // Header, Child data
	}



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	/**
	 * 
	 * ActionGroup
	 */

	// cmd == 0 collapse // cmd == 1 expand
	private void collapseActionGroup(int cmd)
	{
		btn_action_expand.setVisibility(View.GONE);
		btn_action_memo.setVisibility(View.GONE);
		btn_action_calendar.setVisibility(View.GONE);
		btn_action_dash.setVisibility(View.GONE);
		if(cmd == 0) {
			btn_action_expand.setBackgroundResource(R.drawable.action_expand_btn);
			btn_action_expand.setVisibility(View.VISIBLE);
			isActionGruopExpand = false;
		}else {
			btn_action_expand.setBackgroundResource(R.drawable.action_collapse_btn);
			btn_action_calendar.setVisibility(View.VISIBLE);
			btn_action_expand.setVisibility(View.VISIBLE);
			btn_action_memo.setVisibility(View.VISIBLE);
			btn_action_dash.setVisibility(View.VISIBLE);
			isActionGruopExpand = true;
		}
	}



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * callback listener
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMemo:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.btnCalender:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.btnActionExpand:
			setSelectedActionButton(v.getId());
			break;
		case R.id.btnActionMemo:
			setSelectedActionButton(v.getId());
			break;
		case R.id.btnActionCalendar:
			setSelectedActionButton(v.getId());
			break;
		case R.id.btnActionDash:
			setSelectedActionButton(v.getId());
			break;
		default :
			break;


		}
	}


	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}*/


	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		/*boolean drawerOpen = mDrawerLayout.isDrawerOpen(expListView);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);*/
		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {
		/*case R.id.action_search:
			// SearchActivity
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);

			return true;*/
		default:
			return super.onOptionsItemSelected(item);
		}
	}





	@SuppressWarnings("deprecation")
	private void setSelectedTabButton(int index) {
		btn_Memo.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_memo_nonclick));
		btn_Calender.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_calendar_nonclick));

		switch (index) {
		case 0:
			btn_Memo.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_memo_click));
			break;
		case 1:
			btn_Calender.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_calendar_click));
			break;
		default:
			break;
		}
	}



	private void setSelectedActionButton(int id) {
		String query;
		ArrayList<Dash> dashes;

		switch (id) {
		case R.id.btnActionExpand:
			if(isActionGruopExpand == true) {
				collapseActionGroup(0);
			}else {
				collapseActionGroup(1);
			}
			break;
		case R.id.btnActionMemo:
			// New Memo
			query = SqlLiteQuery.get_SELECT_LIST_DASH();
			dashes = dbManager.selectDashList(query);

			if(dashes.size() != 0) {
				Intent intent = new Intent(this, MemoWriteActivity.class);
				startActivity(intent);
			}else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);     // ���⼭ this�� Activity�� this

				// ���⼭ ���ʹ� �˸�â�� �Ӽ� ����
				builder.setTitle("경고")        // ���� ����
				.setMessage("Dashboard를 먼저 생성하세요")        // �޼��� ����
				.setCancelable(false)
				.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
					// Ȯ�� ��ư Ŭ���� ����
					public void onClick(DialogInterface dialog, int whichButton){
						dialog.cancel();
					}
				}); 

				AlertDialog dialog = builder.create();    // �˸�â ��ü ����
				dialog.show();    // �˸�â ����
			}
			collapseActionGroup(0);
			break;
		case R.id.btnActionCalendar:
			// New Calendar
			query = SqlLiteQuery.get_SELECT_LIST_DASH();
			dashes = dbManager.selectDashList(query);
			if(dashes.size() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);     // ���⼭ this�� Activity�� this

				// ���⼭ ���ʹ� �˸�â�� �Ӽ� ����
				builder.setTitle("경고")        // ���� ����
				.setMessage("Dashboard를 먼저 생성하세요")        // �޼��� ����
				.setCancelable(false)
				.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
					// Ȯ�� ��ư Ŭ���� ����
					public void onClick(DialogInterface dialog, int whichButton){
						dialog.cancel();
					}
				}); 

				AlertDialog dialog = builder.create();    // �˸�â ��ü ����
				dialog.show();    // �˸�â ����
				collapseActionGroup(0);
				return;
			}

			dialogCaldroidFragment = new CaldroidFragment();
			dialogCaldroidFragment.setCaldroidListener(listener);

			// If activity is recovered from rotation
			final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
			if (state != null) {
				dialogCaldroidFragment.restoreDialogStatesFromKey(
						getSupportFragmentManager(), state,
						"DIALOG_CALDROID_SAVED_STATE", dialogTag);
				Bundle args = dialogCaldroidFragment.getArguments();
				if (args == null) {
					args = new Bundle();
					dialogCaldroidFragment.setArguments(args);
				}
				args.putString(CaldroidFragment.DIALOG_TITLE,
						"날짜를 선택하세요.");
			} else {
				// Setup arguments
				Bundle bundle = new Bundle();
				// Setup dialogTitle
				bundle.putString(CaldroidFragment.DIALOG_TITLE,
						"날짜를 선택하세요.");
				dialogCaldroidFragment.setArguments(bundle);
			}


			dialogCaldroidFragment.show(getSupportFragmentManager(),
					dialogTag);
			collapseActionGroup(0);
			break;
		case R.id.btnActionDash :
			AlertDialog dialog = create_newDashDialog();
			Context context = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.new_dash_dialog, (ViewGroup)findViewById(R.id.popup_root));
			et_newdash = (EditText)layout.findViewById(R.id.newdash);
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			dialog.setView(layout);
			dialog.show();
			collapseActionGroup(0);
			break;
		default:
			break;
		}
	}

	// Setup listener
	final CaldroidListener listener = new CaldroidListener() {

		@Override
		public void onSelectDate(Date date, View view) {
			AlertDialog dialog = create_newCalendarDialog(date);
			Context context = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.new_calendar_dialog, (ViewGroup)findViewById(R.id.newcalendar_root));
			et_newcalendar = (EditText)layout.findViewById(R.id.et_newcalendar);
			spinner_newcalendar = (Spinner)layout.findViewById(R.id.spinner_newcalendar);
			spinner_newcalendar.setOnItemSelectedListener(mOnItemSelectedListener);
			spinUpdate();
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			dialog.setView(layout);
			dialog.show();
			dialogCaldroidFragment.dismiss();
		}

		@Override
		public void onChangeMonth(int month, int year) {
		}

		@Override
		public void onLongClickDate(Date date, View view) {
		}

		@Override
		public void onCaldroidViewCreated() {
			if (dialogCaldroidFragment.getLeftArrowButton() != null) {
			}
		}

	};


	private AlertDialog create_newCalendarDialog(final Date date) {
		final String str_date = SqlLiteConverter.Date2String(date);
		String year = str_date.substring(0, 4);
		String month = str_date.substring(5, 7);
		String day = str_date.substring(8, 10);

		AlertDialog dialogBox = new AlertDialog.Builder(this)
		.setTitle("New Calendar")
		.setMessage(year+"년 " + month + "월 " + day + "일 일정 등록")
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mWebSocketHelper.sendMessage(new Component(0, dashes.get(selectedItemIndex).getDid(), Constants.COMPONENT_ITEMTYPE_CALENDAR,
						"빈제목", et_newcalendar.getText().toString(), new Date(), date, false), Constants.req_componentNew);

				calendarFragment.UpdateDates();

			}
		})
		.setNeutralButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 아니오 버튼 눌렀을때 액션 구현
			}
		}).create();
		return dialogBox;
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
		spinner_newcalendar.setAdapter(adspin);

		selectedItemIndex = 0;
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
	private AlertDialog create_newDashDialog() {
		AlertDialog dialogBox = new AlertDialog.Builder(this)
		.setTitle("New Dash Board")
		.setMessage("사용할 대시보드 이름을 입력하세요")
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mWebSocketHelper.sendMessage(new Dash(0, et_newdash.getText().toString()), Constants.req_dashNew);
				updateSlidingList();

			}
		})
		.setNeutralButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 아니오 버튼 눌렀을때 액션 구현
			}
		}).create();
		return dialogBox;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new MemoFragment();
				memoFragment = (MemoFragment) fragment;
				jsonHelper.addObserver(memoFragment);
				break;
			case 1:
				//fragment = new MemoFragment();
				//memoFragment = (MemoFragment) fragment;
				fragment = new CalendarFragment();
				calendarFragment = (CalendarFragment)fragment;
				jsonHelper.addObserver(calendarFragment);
				break;
			default:
				break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			default:
				return "";
			}
		}
	}


	// GCM
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);

		// display received msg
		String msg = intent.getStringExtra("msg");
		Log.i("MainActivity.java | onNewIntent", "|" + msg + "|");
		if (!TextUtils.isEmpty(msg))
			Log.d("GCMTEST", "1 : " + msg);
	}

	// google play service가 사용가능한가
	private boolean checkPlayServices()
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS)
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
			{
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			}
			else
			{
				Log.i("MainActivity.java | checkPlayService", "|This device is not supported.|");
				// _textStatus.append("\n This device is not supported.\n");
				finish();
			}
			return false;
		}
		return true;
	}

	// registration  id를 가져온다.
	private String getRegistrationId()
	{
		String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
		if (TextUtils.isEmpty(registrationId))
		{
			Log.i("MainActivity.java | getRegistrationId", "|Registration not found.|");
			//_textStatus.append("\n Registration not found.\n");
			return "";
		}
		int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
		int currentVersion = getAppVersion();
		if (registeredVersion != currentVersion)
		{
			Log.i("MainActivity.java | getRegistrationId", "|App version changed.|");
			// _textStatus.append("\n App version changed.\n");
			return "";
		}
		return registrationId;
	}

	// app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
	private int getAppVersion()
	{
		try
		{
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			return packageInfo.versionCode;
		}
		catch (NameNotFoundException e)
		{
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	// gcm 서버에 접속해서 registration id를 발급받는다.
	private void registerInBackground()
	{
		new AsyncTask<Void, Void, String>()
		{
			@Override
			protected String doInBackground(Void... params)
			{
				String msg = "";
				try
				{
					if (_gcm == null)
					{
						_gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					}

					_regId = _gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + _regId;

					// For this demo: we don't need to send it because the device
					// will send upstream messages to a server that echo back the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(_regId);
				}
				catch (IOException ex)
				{
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}

				return msg;
			}

			@Override
			protected void onPostExecute(String msg)
			{
				Log.i("MainActivity.java | onPostExecute", "|" + msg + "|");
				Log.d("GCMTEST", "2 : "+msg);
				//_textStatus.append(msg);
			}
		}.execute(null, null, null);
	}

	// registraion id를 preference에 저장한다.
	private void storeRegistrationId(String regId)
	{
		int appVersion = getAppVersion();
		Log.i("MainActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
		PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
		PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
	}

	/*private static Timer mTimer;
	private static TimerTask mTask;
	
	public void setTimer() {
		mTimer = new Timer();
		mTask = new TimerTask() {
	        @Override
	        public void run() {
	        	hidePDialog();
	        }
	    };
	}*/
	/*
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
	*/

	
/*
	public void showPDialog() {
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.show();
			mTimer.schedule(mTask, 3000);
			
		}
	}
*/


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		jsonHelper.deleteObserver(memoFragment);
		jsonHelper.deleteObserver(calendarFragment);
	}


}
