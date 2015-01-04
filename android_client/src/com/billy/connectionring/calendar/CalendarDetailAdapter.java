package com.billy.connectionring.calendar;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.connectionring.Constants;
import com.billy.connectionring.R;
import com.billy.connectionring.SlidingChildModel;
import com.billy.connectionring.model.Component;

public class CalendarDetailAdapter extends BaseAdapter{
	private Context m_Context = null;
	private List<Component> list_popup = null;

	public CalendarDetailAdapter(Context context, List<Component> list_popup) {
		this.m_Context = context;
		this.list_popup = list_popup;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_popup.size();
	}
	@Override
	public Component getItem(int position) {
		// TODO Auto-generated method stub
		return list_popup.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.m_Context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.calendar_detail_row, null);
		}


		
		
		
		String text = null;
		text = list_popup.get(position).getContent();
		
		// content
		TextView txtView = (TextView) convertView
				.findViewById(R.id.tv_calendar_row);
		txtView.setText(Integer.toString(position + 1) + ". " + text);


		return convertView ;
	}

	public void update(List<Component> slideList) {
		this.list_popup = slideList;
		this.notifyDataSetChanged();

	}
}