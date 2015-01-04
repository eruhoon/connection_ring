package com.billy.connectionring.popup;

import java.util.List;

import com.billy.connectionring.Constants;
import com.billy.connectionring.R;
import com.billy.connectionring.SlidingChildModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class POPUPListAdapter extends BaseAdapter{
	private Context m_Context = null;
	private List<String> list_popup = null;
	
	public POPUPListAdapter(Context context, List<String> list_popup) {
		this.m_Context = context;
		this.list_popup = list_popup;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_popup.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.m_Context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.popup_row, null);
		}
		
		// icon
	    ImageView image = (ImageView)convertView.findViewById(R.id.iv_popup);
	    int imageResourceId = 0;
		
		if(position == 0) {
			imageResourceId = R.drawable.popup_bookmark;
		}else  if(position == 1) {
			imageResourceId = R.drawable.popup_garbage;
		}
		
		image.setImageResource(imageResourceId);
		
		// content
		TextView txtView = (TextView) convertView
				.findViewById(R.id.tv_popup);
		txtView.setText(list_popup.get(position));
		
		
		return convertView ;
	}
	

}