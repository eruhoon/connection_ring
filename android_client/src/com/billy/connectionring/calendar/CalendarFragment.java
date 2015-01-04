package com.billy.connectionring.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.billy.connectionring.R;
import com.billy.connectionring.connection.JSONHelper;
import com.billy.connectionring.connection.WebsocketHelper;
import com.billy.connectionring.model.Component;
import com.billy.connectionring.sqllite.SqlLiteConverter;
import com.billy.connectionring.sqllite.SqlLiteManager;
import com.billy.connectionring.sqllite.SqlLiteQuery;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CaldroidListener;


public class CalendarFragment extends CaldroidFragment implements Observer{

	WebsocketHelper webSocketHelper;
	SqlLiteManager dbManager;
	
	HashMap<String, ArrayList<Component>> hash_calendar;
	
	CalendarAdapter calendarAdapter;
	
	boolean initComplete = false;
	
	public CalendarFragment() {
		// TODO Auto-generated constructor stub
		super();

		webSocketHelper = WebsocketHelper.getInstance(getActivity());
		dbManager = SqlLiteManager.getInstance(getActivity());
		
		hash_calendar = new HashMap<String, ArrayList<Component>>();
		
		
		setCaldroidListener(listener);
		initComplete = true;
	}
	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		calendarAdapter =new CalendarAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
		UpdateDates();
		
		return calendarAdapter;
	}


	public synchronized void UpdateDates() {
		
		
		String query = SqlLiteQuery.get_SELECT_LIST_COMPONENT_DAY_ORDERBYDAY();
		ArrayList<Component> components = dbManager.selectComponentList(query);
		
		if(components == null)
			return;
		
		ArrayList<String> list_parent = new ArrayList<String>();

		// list_parent
		for(int i = 0; i < components.size(); i++) {
			Date date = components.get(i).getDate();

			String str_day = SqlLiteConverter.Date2String(date);
			str_day = str_day.substring(0, 10) + " 00:00:00";

			if(list_parent.contains(str_day) == false)
				list_parent.add(str_day);
		}

		hash_calendar = new HashMap<String, ArrayList<Component>>();
		
		for(int i = 0; i <list_parent.size(); i++)
		{
			String str_date = list_parent.get(i);
			
			
			query = SqlLiteQuery.get_SELECT_LIST_COMPONENT_CALENDAR_ORDERBYDAY(str_date);
			components = dbManager.selectComponentList(query);
			
			
			if(components == null)
				continue;
			
			if(components.size() == 0)
				continue;
			
			hash_calendar.put(str_date, components);
			
			Date date = SqlLiteConverter.String2Date(str_date);
			

			setBackgroundResourceForDate(R.color.green,
					date);
			setTextColorForDate(R.color.white, date);
		}

		calendarAdapter.updateCalendar(list_parent, hash_calendar);
		// Reset calendar
		clearDisableDates();
		clearSelectedDates();
		setMinDate(null);
		setMaxDate(null);
		setShowNavigationArrows(true);
		refreshView();
		
	}

	
	// Setup listener
	final CaldroidListener listener = new CaldroidListener() {

		@Override
		public void onSelectDate(Date date, View view) {
			
			String str_date = SqlLiteConverter.Date2String(date);
			str_date = str_date.substring(0,  10) + " 00:00:00";
			
			ArrayList<Component> detailList = hash_calendar.get(str_date);
			
			if(detailList == null) {
				Toast.makeText(getActivity(), "등록된 일정이 없습니다",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(detailList.size() == 0) {
				Toast.makeText(getActivity(), "등록된 일정이 없습니다",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			AlertDialog dialog = create_newCalendarDetailDialog(date);
			Context context = getActivity();
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.calendar_detail_dialog, (ViewGroup)getActivity().findViewById(R.id.calendar_detail_root));
			
			ListView list = (ListView)layout.findViewById(R.id.list_calendar);
			
			CalendarDetailAdapter listAdapter = new CalendarDetailAdapter(getActivity(), detailList);
			list.setAdapter(listAdapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View arg1, int position,
						long arg3) {
				}});
			
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			dialog.setView(layout);

			
			// Dialog 사이즈 조절 하기
			LayoutParams params = dialog.getWindow().getAttributes();
		    params.width = 500;
		    params.height = 500;
		    dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		    
			dialog.show();
		}

		private AlertDialog create_newCalendarDetailDialog(final Date date) {
			final String str_date = SqlLiteConverter.Date2String(date);
			String year = str_date.substring(0, 4);
			String month = str_date.substring(5, 7);
			String day = str_date.substring(8, 10);
			
			AlertDialog dialogBox = new AlertDialog.Builder(getActivity())
			.setTitle("Calendar Detail")
			.setMessage(year+"년 " + month + "월 " + day + "일 일정 목록")
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
			
			return dialogBox;
		}
		
		@Override
		public void onChangeMonth(int month, int year) {
		}

		@Override
		public void onLongClickDate(Date date, View view) {
		}

		@Override
		public void onCaldroidViewCreated() {
			if (getLeftArrowButton() != null) {
				Toast.makeText(getActivity(),
						"Caldroid view is created", Toast.LENGTH_SHORT)
						.show();
			}
		}

	};
	


	
	@Override
	public void update(Observable observable, final Object data) {
		// TODO Auto-generated method stub

		Log.d("ffdfddffdfdd", "ffdfddffdfdd");
		
		if (observable instanceof JSONHelper) {
			String input = (String)data;
			if(input.equals("SynchronizeComponent") || input.equals("UpdateComponent"))
				UpdateDates();
			
		}
	}
}