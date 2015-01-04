package com.billy.connectionring.memo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.billy.connectionring.Constants;
import com.billy.connectionring.R;
import com.billy.connectionring.connection.JSONHelper;
import com.billy.connectionring.connection.WebsocketHelper;
import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.Dash;
import com.billy.connectionring.popup.POPUPListAdapter;
import com.billy.connectionring.sqllite.SqlLiteConverter;
import com.billy.connectionring.sqllite.SqlLiteManager;
import com.billy.connectionring.sqllite.SqlLiteQuery;



public class MemoFragment extends Fragment implements Observer {


	MemoListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> list_parent;
	HashMap<String, List<Component>> list_child;

	WebsocketHelper mWebsocketHelper;
	SqlLiteManager dbManager;
	
	Button imv_dashname;

	int selectedDid;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mWebsocketHelper = WebsocketHelper.getInstance(getActivity());
		dbManager = SqlLiteManager.getInstance(getActivity());

		View v = inflater.inflate(R.layout.inflate_memo, container, false);

		imv_dashname = (Button)v.findViewById(R.id.txt_memo_dashname);
		// get the listview
		expListView = (ExpandableListView) v.findViewById(R.id.lvExp);

		// preparing list data
		loadListData(-1, null);

		listAdapter = new MemoListAdapter(getActivity(), list_parent, list_child);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		// Listview Group click listener
		expListView.setOnGroupClickListener(mOnGroupClickListener);

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(mOnGroupExpandListener);

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(mOnGroupCollapseListener);

		// Listview on child click listener
		expListView.setOnChildClickListener(mOnChildClickListener);

		expListView.setOnItemLongClickListener(mOnItemLongClickListener);

		imv_dashname.setOnClickListener(mOnClickListener);

		selectedDid = -1;
		return v;
	}

	
	AlertDialog dashdialog;
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.txt_memo_dashname : 
				if(selectedDid != -1)
				{
					dashdialog = DashOptionDialog();
					Context context = getActivity();
					LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
					View layout = inflater.inflate(R.layout.dash_option, (ViewGroup)getActivity().findViewById(R.id.popup_dash));

					Button btn_correct = (Button)layout.findViewById(R.id.dashoption_correction);
					Button btn_bookmark = (Button)layout.findViewById(R.id.dashoption_bookmark);
					Button btn_delete = (Button)layout.findViewById(R.id.dashoption_delete);
					btn_correct.setOnClickListener(mDialogClickListener);
					btn_bookmark.setOnClickListener(mDialogClickListener);
					btn_delete.setOnClickListener(mDialogClickListener);
					dashdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					dashdialog.setView(layout);
					dashdialog.show();

				}
				break;
			default : 
				break;
			}
		}

	};

	
	EditText et_newdash;
	private OnClickListener mDialogClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.dashoption_correction :
				AlertDialog dialog = create_newDashDialog();
				Context context = getActivity();
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.new_dash_dialog, (ViewGroup)getActivity().findViewById(R.id.popup_root));
				et_newdash = (EditText)layout.findViewById(R.id.newdash);
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				dialog.setView(layout);
				dialog.show();

				break;
			case R.id.dashoption_bookmark :
				Toast.makeText(getActivity(), "Bookmark에 추가되었습니다.", Toast.LENGTH_SHORT);
				String query = SqlLiteQuery.get_INSERT_Bookmark_QUERY(selectedDid, Constants.ITEM_MEMOGROUP);
				dbManager.insertData(query);
				
				break;
			case R.id.dashoption_delete :
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
				alt_bld.setMessage("Dashboard를 삭제하면 모든 하위 컴포넌트가 삭제됩니다. 그래도 삭제하시겠습니까?").setCancelable(
						false).setPositiveButton("확인",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mWebsocketHelper.sendMessage(new Dash(selectedDid, ""), Constants.req_dashDelete);
								updateSlidingList();
							}
						}).setNegativeButton("취소",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								dialog.cancel();
							}
						});
				AlertDialog alert = alt_bld.create();
				alert.setTitle("경고");
				alert.show();
				break;
			default : 
				break;
				
			}
			dashdialog.dismiss();
		}
	};

	private AlertDialog DashOptionDialog() {
		AlertDialog dialogBox = new AlertDialog.Builder(getActivity())
		.setTitle("Dash Option").create();
		return dialogBox;
	}


	private AlertDialog create_newDashDialog() {
		AlertDialog dialogBox = new AlertDialog.Builder(getActivity())
		.setTitle("Update Dash Board")
		.setMessage("수정할 대시보드 이름을 입력하세요")
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mWebsocketHelper.sendMessage(new Dash(selectedDid, et_newdash.getText().toString()), Constants.req_dashUpdate);
				updateSlidingList(selectedDid, et_newdash.getText().toString());
			}
		})
		.setNeutralButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 아니오 버튼 눌렀을때 액션 구현
			}
		}).create();
		return dialogBox;
	}

	private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
				int groupPosition = ExpandableListView.getPackedPositionGroup(id);
				int childPosition = ExpandableListView.getPackedPositionChild(id);
				final Component component = list_child.get( list_parent.get(groupPosition)).get(childPosition);

				List<String> list_popup = new ArrayList<String>();
				list_popup.add(new String("Bookmark에 추가"));
				list_popup.add(new String("삭 제"));
				POPUPListAdapter listAdapter = new POPUPListAdapter(getActivity(), list_popup);

				AlertDialog.Builder popupDlg = new AlertDialog.Builder(getActivity());
				popupDlg.setTitle("Bookmark");
				popupDlg.setAdapter(listAdapter, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(which == 0) {
							String query = SqlLiteQuery.get_INSERT_Bookmark_QUERY(component.getCid(), Constants.ITEM_MEMO);
							dbManager.insertData(query);
						}else {
							mWebsocketHelper.sendMessage(component, Constants.req_componentDelete);
							//updateSlidingList();
							
						}
					}
				});

				popupDlg.setCancelable(true);
				popupDlg.show();
				return true;
			}

			return false;
		}
	};

	OnGroupClickListener mOnGroupClickListener = new OnGroupClickListener() {

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			return true;
		}
	};

	OnGroupExpandListener mOnGroupExpandListener = new OnGroupExpandListener() {

		@Override
		public void onGroupExpand(int groupPosition) {
		}
	};

	OnGroupCollapseListener mOnGroupCollapseListener = new OnGroupCollapseListener() {

		@Override
		public void onGroupCollapse(int groupPosition) {
		}
	};

	OnChildClickListener mOnChildClickListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			// TODO 
			
			int cid = list_child.get( list_parent.get(groupPosition)).get(childPosition).getCid();
			memoCorrect(cid);
			
			
			return false;
		}
	};

	public void memoCorrect(int cid)
	{
		Intent intent = new Intent(getActivity(), MemoUpdateActivity.class);

		intent.putExtra("cid", cid);

		startActivity(intent);
	}




	public void expandAll(){
		int groupCnt = getParentCount();
		Log.d("MainActivity", Integer.toString(groupCnt));
		if(groupCnt == 0){
			return;
		}
		
		for(int i = 0; i < groupCnt; i++){
			if(getChildCount(i) > 0)
				expListView.expandGroup(i);
		}
		expListView.setSelection(0);
	}


	public int getParentCount() {
		int cnt = 0;
		if(list_parent != null)
			cnt = this.list_parent.size();
		return cnt;
	}
	
	public int getChildCount(int parentid) {
		int cnt = 0;
		if(list_child != null && list_child.containsKey(this.list_parent.get(parentid))) 
			cnt = list_child.get(this.list_parent.get(parentid)).size();
		return cnt;
	}

	/*
	 * Preparing the list data
	 */
	@SuppressWarnings("deprecation")
	private void loadListData(int did, String dasnname) {

		String query = null;
		selectedDid = did;
		if(did == -1)
		{
			query = SqlLiteQuery.get_SELECT_LIST_COMPONENT_MEMO_ORDERBYDAY();
			
			if(!imv_dashname.getText().toString().equals("ALLNOTE"))
				imv_dashname.setText("ALL NOTE");
		}else {
			query = SqlLiteQuery.get_SELECT_LIST_DASHOFCOMPONENT_MEMO_ORDERBYDAY(did);
			if(!imv_dashname.getText().toString().equals(dasnname))
				imv_dashname.setText(dasnname);
		}

		Log.d("abf", "2");
		ArrayList<Component> components = dbManager.selectComponentList(query);

		Log.d("abf", "3");
		if(components == null) {
			return;
		}

		
		list_parent = new ArrayList<String>();
		list_child = new HashMap<String, List<Component>>();


		// list_parent
		for(int i = 0; i < components.size(); i++) {
			Date date = components.get(i).getRegdate();

			String str_day = SqlLiteConverter.Date2String(date);
			str_day = str_day.substring(0, 10);

			if(list_parent.contains(str_day) == false)
				list_parent.add(str_day);
		}

		if(did == -1)
			list_parent.add(Constants.TUTORIAL_TEXT);
		
		// list_child
		for(int i = 0; i < list_parent.size(); i++) {
			List<Component> memoList = new ArrayList<Component>();

			if(i == list_parent.size()-1 && did == -1)
				break;
			
			for(int j = 0; j < components.size(); j++)
			{
				
				Date date = components.get(j).getRegdate();

				String str_day = SqlLiteConverter.Date2String(date);
				str_day = str_day.substring(0, 10);

				// �׷��� ���ϵ��� ��¥�� ���� ���
				if(list_parent.get(i).substring(0, 10).equals(str_day))
				{
					memoList.add(components.get(j));
				}
			}

			list_child.put(list_parent.get(i), memoList); // Header, Child data
		}

	}


	public void updateSlidingList(int did, String dasnname) {

		loadListData(did, dasnname);
		listAdapter.update(list_parent, list_child);
		expandAll();
	}

	public void updateSlidingList() {
		loadListData(-1, null);
		listAdapter.update(list_parent, list_child);
		expandAll();
	}

	@Override
	public void onResume() {
		super.onResume();
		updateSlidingList();
	}



	@Override
	public void onPause() {
		super.onResume();
	}

	

	@Override
	public void update(Observable observable, final Object data) {
		// TODO Auto-generated method stub

		if (observable instanceof JSONHelper) {

			Log.d("hiff", "update call");
			String input = (String)data;
			if(input.equals("SynchronizeComponent") || input.equals("UpdateComponent")) {
				
				updateSlidingList();
			}
		}
	}
	
}
