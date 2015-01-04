package com.billy.connectionring.calendar;

import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.connectionring.R;
import com.billy.connectionring.model.Component;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;



public class CalendarAdapter extends CaldroidGridAdapter {

	ArrayList<String> parent;
	HashMap<String, ArrayList<Component>> hash_calendar;
	
	public CalendarAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData) {
		super(context, month, year, caldroidData, extraData);
	}

	public void updateCalendar(ArrayList<String> parent, HashMap<String, ArrayList<Component>> hash_calendar)
	{
		if(this.parent != null)
			this.parent.clear();
		if(this.hash_calendar!=null)
			this.hash_calendar.clear();
		this.parent = parent;
		this.hash_calendar = hash_calendar;
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent_v) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		
		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.calendar_cell, null);
		}

		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
		TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);

		tv1.setTextColor(Color.BLACK);

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		
		Resources resources = context.getResources();

		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			tv1.setTextColor(resources
					.getColor(com.caldroid.R.color.caldroid_darker_gray));
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

			tv1.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
			} else {
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
			}

		} else {
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
			} else {
				cellView.setBackgroundColor(resources
						.getColor(com.caldroid.R.color.caldroid_sky_blue));
			}

			tv1.setTextColor(CaldroidFragment.selectedTextColor);

		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
			} else {
				cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
			}
		}
		

		tv1.setText("" + dateTime.getDay());
		tv2.setText("");
		/*
		String str_date = dateTime.format("YYYY-MM-DD").toString() + " 00:00:00";

		if(this.parent.contains(str_date)) {
			ArrayList<Component> components = hash_calendar.get(str_date);

			Log.d("hiff", str_date);
			if(components == null) {
				tv2.setText("");
			} else {
				String content = "";
				for(int i = 0; i < components.size(); i++)
				{
					String componentText = components.get(i).getContent();
					if(componentText.length() > 2)
						componentText = componentText.substring(0, 3);
					
					if(i == components.size()-1)
						content = componentText + "..";
					else
						content = componentText + "..\n";
				}
				tv2.setText(content);
			}
		}
		else 
			tv2.setText("");
			*/
		

		// Somehow after setBackgroundResource, the padding collapse.
		// This is to recover the padding
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);

		// Set custom color if required
		setCustomResources(dateTime, cellView, tv1);

		return cellView;
	}

	
	
}
